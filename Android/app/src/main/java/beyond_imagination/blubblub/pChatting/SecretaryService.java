package beyond_imagination.blubblub.pChatting;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import beyond_imagination.blubblub.MainActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by cru65 on 2017-09-24.
 */

public class SecretaryService implements EasyPermissions.PermissionCallbacks {
    /*** Variable ***/
    MainActivity mainActivity;

    EditText ouputText;

    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1006;
    static final int REQUEST_AUTHORIZATION = 1007;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1008;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1009;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    /*** Function ***/
    public SecretaryService(Context context, EditText editText) {
        mainActivity = (MainActivity) context;
        ouputText = editText;

        mCredential = GoogleAccountCredential.usingOAuth2(mainActivity, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
    }

    public void getScheduleData() {
        getResultsFromApi(null);
    }

    public void setScheduleData(String message) {
        getResultsFromApi(message);
    }

    private void getResultsFromApi(String message) {
        Log.d("asdfasdf", "asdfasd");

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            Log.d("asdfasdf", "1");
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(message);
            Log.d("asdfasdf", "2");
        } else if (!isDeviceOnline()) {
            Toast.makeText(mainActivity, "No network connection available", Toast.LENGTH_SHORT).show();
            Log.d("asdfasdf", "3");
        } else {
            if(message == null)
                new MakeRequestTask(mCredential).execute();
            else
                new InputDataTask(message).execute();
            Log.d("asdfasdf", "4");
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount(String message) {
        if (EasyPermissions.hasPermissions(mainActivity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = mainActivity.getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName == null)
                Log.d("asdfasdf", "ㅁㄴㅇㄹ" + accountName);

            Log.d("asdfasdf", "ㅁㄴㅇㄹ");

            if (accountName != null) {
                Log.d("asdfasdf", accountName);
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi(message);
            } else {
                Log.d("asdfasdf", "ㅁㄴㅇㄹ");
                mainActivity.startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(mainActivity, "This app needs to access your Google account(via Contacts)", REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    // 네트워크와 연결되었는지 확인.
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    // GooglePlay service APK가 설치되어있는지, 최신인지 확인해줌.
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // 유저 dialog를 통해서 missing, out of date(유효기간), isInvalid 등을 확인해줌. 한마디로 에러 확인.
    private void acquireGooglePlayServices() {

        Log.d("asdfasdf", "asdfasd");

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    // 구글 플레이 서비스 이용에 에러가 있을 시, 보여주는 Dialog
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(mainActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential).setApplicationName("Google Calendar API Android Quickstart").build();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            try {
                Log.d("asdfadsf", "캘린더 작업중이당~~");
                return getDataFromApi();
            } catch (IOException e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        // 이전 캘린더에서 10개의 이벤트를 가져온다. 이벤트 string list 반환.
        private List<String> getDataFromApi() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary").setMaxResults(5).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
            List<Event> items = events.getItems();

            Log.d("asdfadsf", "캘린더 작업중이당~~");

            DateTime nowDate;
            String priDate = null;
            String temp = null;
            String[] token;

            /*
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                eventStrings.add(String.format("%s (%s)", event.getSummary(), start));
            }
            */

            if (items.size() == 0) {
                eventStrings.add("     등록된 일정이 없습니다...ㅠㅠ");
            } else {
                for (Event event : items) {
                    nowDate = event.getStart().getDateTime();
                    if (nowDate == null) {
                        nowDate = event.getStart().getDate();
                    }

                    Log.d("asdfasdf", nowDate.toString());
                    token = nowDate.toString().split("T");
                    Log.d("asdfasdf", token[0]);

                    if (token.length != 1)
                        temp = token[1];
                    // 날짜 출력
                    if (priDate == null || priDate.toString().equals(token[0]) == false) {
                        if (priDate != null) {
                            eventStrings.add("\n");
                        }
                        priDate = token[0];
                        token = token[0].split("-");
                        Log.d("asdfasdf", token[0]);
                        eventStrings.add(String.format(" <%s년 %s월 %s일>", token[0], token[1], token[2]));
                    }
                    // 일정 출력
                    if (temp == null) {
                        eventStrings.add(String.format("  오늘 %s", event.getSummary()));
                    } else {
                        token = temp.split(":");
                        eventStrings.add(String.format("  %s시 %s", token[0], event.getSummary()));
                    }
                }
            }
            return eventStrings;
        }

        protected void onPreExecute() {
            mainActivity.onControlMessage("대화", "뻐끔뻐끔(일정 확인 중 입니다~)");
        }

        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
                Toast.makeText(mainActivity, "No results returnd", Toast.LENGTH_SHORT).show();
            } else {
                //output.add(0, "Data retrieved using the Google Calendar API");
                output.add(0, "----------일  정----------");
                output.add("--------------------");
                Toast.makeText(mainActivity, "Data retrieved using the Google Calendar API", Toast.LENGTH_SHORT).show();

                String result = (TextUtils.join("\n", output));

                mainActivity.onControlMessage("대화", (TextUtils.join("\n", output)));
            }
        }

        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    Log.e("asdfasdf", "여기여기");
                    mainActivity.startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(mainActivity, "The following error occurred:\n" + mLastError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mainActivity, "Request cancelled", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private class InputDataTask extends AsyncTask<Void, Void, String> {
        HttpTransport transport = null;
        JsonFactory jsonFactory = null;
        com.google.api.services.calendar.Calendar service = null;

        String message;
        String yearData;
        String monthData;
        String dayData;
        String startTimeData;
        String endTimeData;
        String duringTimeData;
        String summaryData;

        public InputDataTask(String message) {
            int permissionCheck = ContextCompat.checkSelfPermission(mainActivity,
                    Manifest.permission.WRITE_CALENDAR);
            Log.d("asdfadsf", "permissioni : " + permissionCheck);

            this.message = message;
        }

        @Override
        protected String doInBackground(Void... params) {
            transport = AndroidHttp.newCompatibleTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
            service = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("R_D_Location Callendar")
                    .build();

            // 데이터 파싱
            String[] token;
            String[] token2;
            String temp;
            int index;
            //// 내용 파싱
            token = message.split("\"");
            summaryData = token[1];

            if(token.length == 3)
                temp = token[0] + token[2];
            else
                temp = token[0];

            token = temp.split(" ");

            for(int i = 0; i<token.length;i++) {
                if(token[i].contains("일정등록") || token[i].contains("일정") || token[i].contains("등록")){
                    token[i] = "";
                }
                else if (token[i].contains("년")) {
                    token2 = token[i].split("년");
                    yearData = token2[0];
                }
                else if (token[i].contains("월")) {
                    token2 = token[i].split("월");
                    monthData = token2[0];
                    if(Integer.valueOf(monthData) < 10)
                        monthData = "0"+monthData;
                }
                else if (token[i].contains("일")) {
                    token2 = token[i].split("일");
                    dayData = token2[0];

                    if(Integer.valueOf(dayData) < 10)
                        dayData = "0"+dayData;
                }
                else if (token[i].contains("시부터")) {
                    token2 = token[i].split("시");
                    startTimeData = token2[0];
                    if(Integer.valueOf(startTimeData) < 10)
                        startTimeData = "0" + startTimeData;
                }
                else if (token[i].contains("시간")) {
                    token2 = token[i].split("시");
                    duringTimeData = token2[0];

                    int endtime = Integer.valueOf(startTimeData) + Integer.valueOf(duringTimeData);

                    if(endtime < 10)
                        endTimeData = "0" + endtime;
                    else
                        endTimeData = ""+endtime;
                }
            }

            Log.d("asdfasdf", " " + yearData +" " + monthData +" " + dayData +" " + startTimeData +" " + duringTimeData +" " + endTimeData+" " + summaryData);


            // 이벤트 정보 등록
            //Event event = new Event().setSummary("Google I/O 2015").setLocation("800 Howard St., San Francisco, CA 94103").setDescription("A chance to hear more about Google's developer products.");

            Event event = new Event().setSummary(summaryData);

            DateTime startDateTime = new DateTime(String.format("%s-%s-%sT%s:00:00+09:00", yearData, monthData, dayData, startTimeData));
            EventDateTime start = new EventDateTime().setDateTime(startDateTime);
            event.setStart(start);

            DateTime endDateTime = new DateTime(String.format("%s-%s-%sT%s:00:00+09:00", yearData, monthData, dayData, endTimeData));
            EventDateTime end = new EventDateTime().setDateTime(endDateTime);
            event.setEnd(end);

            /*
            DateTime startDateTime = new DateTime("2017-09-28T09:00:00-07:00");
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setStart(start);

            DateTime endDateTime = new DateTime("2017-09-28T17:00:00-07:00");
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setEnd(end);
            */
            String calendarId = "primary";
            try {
                event = service.events().insert(calendarId, event).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("asdfasdf", "EventCreate : " + event.getHtmlLink());

            return event.getHtmlLink();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                mainActivity.onControlMessage("대화", "일정 정보가 부족합니다.\\n년, 월, 일, 시작시간, 끝나는시간, \"내용\"을 말해주세요");
            } else {
                mainActivity.onControlMessage("대화", "일정이 등록되었습니다~ 굿굿!");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    ////
    // Getter and Setter
    ////
    public GoogleAccountCredential getmCredential() {
        return mCredential;
    }
}
