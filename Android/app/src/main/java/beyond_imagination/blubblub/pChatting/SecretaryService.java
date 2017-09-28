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

/**
 * @author Yehun Park
 * @file SecretaryService.java
 * @breif Class connect with Google Service and use GoogleCalendar API
 * Check permission and can use internet for identify user at Google Service
 * Get user schedule and register user schedule
 */
public class SecretaryService implements EasyPermissions.PermissionCallbacks {
    /****************/
    /*** Variable ***/
    /****************/
    // For access to Mainactivity
    MainActivity mainActivity;

    // Views
    EditText ouputText;

    // Identity
    GoogleAccountCredential mCredential;

    // REQUEST_CODES
    static final int REQUEST_ACCOUNT_PICKER = 1006;
    static final int REQUEST_AUTHORIZATION = 1007;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1008;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1009;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    /****************/
    /*** Function ***/
    /****************/

    /**
     * @param context
     * @param editText
     * @brief Constructor
     */
    public SecretaryService(Context context, EditText editText) {
        Log.d("SecretaryService", "Constructor execute");
        mainActivity = (MainActivity) context;
        ouputText = editText;

        mCredential = GoogleAccountCredential.usingOAuth2(mainActivity, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
    }

    /**
     * @brief Start get schedule data
     * Getting schedule don't need parameter
     */
    public void getScheduleData() {
        Log.d("SecretaryService", "get schedule");

        getResultsFromApi(null);
    }

    /**
     * @param message
     * @brief Start register schedule data
     * Registering schedule need Info of user schedule
     */
    public void setScheduleData(String message) {
        Log.d("SecretaryService", "set schedule, message : " + message);

        getResultsFromApi(message);
    }

    /**
     * @param message
     * @brief Start Identify for using Google Service first, internet second
     * If you can use Google Service and internet, access to Google Calender API
     * If getResultsFromApi's parameter is null, get schedule data
     * If getResultsFromApi's parameter have message, register schedule data
     */
    private void getResultsFromApi(String message) {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            Log.d("SecretaryService", "acquireGooglePlayServices");
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount(message);
            Log.d("SecretaryService", "chooseAccount");
        } else if (!isDeviceOnline()) {
            Toast.makeText(mainActivity, "No network connection available", Toast.LENGTH_SHORT).show();
            Log.d("SecretaryService", "No network connection available");
        } else {
            if (message == null)
                new MakeRequestTask(mCredential).start();
            else
                new InputDataTask(message).start();
            Log.d("SecretaryService", "start Access to Google Calendar");
        }
    }

    /**
     * @param message
     * @brief Choose account for using Google Service
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount(String message) {
        if (EasyPermissions.hasPermissions(mainActivity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = mainActivity.getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);

            if (accountName != null) {
                Log.d("SecretaryService", "accountName-" + accountName);
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi(message);
            } else {
                Log.d("SecretaryService", "Request account picker");
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

    /**
     * @return
     * @brief Check connection with internet
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * @return
     * @brief Check installation GooglePlay service APK or Up to date.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * @brief Check missing, out of date, isInvalid and so on.
     * Check error
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(mainActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    /**
     * @author Yehun Park
     * @file SecretaryService.MakeRequestTask.java
     * @breif Class connect with Google Calendar API and get schedule data
     * If get schedule data, format data according to app style
     */
    private class MakeRequestTask extends Thread {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            Log.d("MakeRequestTask", "Constructor execute");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential).setApplicationName("Google Calendar API Android Quickstart").build();
        }

        @Override
        public void run() {
            Log.d("MakeRequestTask", "run");

            mainActivity.onControlMessage("대화", "뻐끔뻐끔(일정 확인 중 입니다~)");

            try {
                getDataFromApi();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @return
         * @throws IOException
         * @brief Get 3 events after now from Google Calendar
         * Contents : Date, Time, To do
         */
        private void getDataFromApi() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary").setMaxResults(3).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
            List<Event> items = events.getItems();

            DateTime nowDate;
            String priDate = null;
            String temp = null;
            String[] token;

            if (items.size() == 0) {
                eventStrings.add("     등록된 일정이 없습니다...ㅠㅠ");
            } else {
                for (Event event : items) {
                    nowDate = event.getStart().getDateTime();
                    if (nowDate == null) {
                        nowDate = event.getStart().getDate();
                    }
                    token = nowDate.toString().split("T");

                    if (token.length != 1)
                        temp = token[1];
                    // Output Date
                    if (priDate == null || priDate.toString().equals(token[0]) == false) {
                        if (priDate != null) {
                            eventStrings.add("\n");
                        }
                        priDate = token[0];
                        token = token[0].split("-");
                        eventStrings.add(String.format(" <%s년 %s월 %s일>", token[0], token[1], token[2]));
                    }
                    // Output Time and To do
                    if (temp == null) {
                        eventStrings.add(String.format("  오늘 %s", event.getSummary()));
                    } else {
                        token = temp.split(":");
                        eventStrings.add(String.format("  %s시 %s", token[0], event.getSummary()));
                    }
                }
            }

            // show result
            if (eventStrings == null || eventStrings.size() == 0) {
            } else {
                eventStrings.add(0, "\n----------일  정----------");
                eventStrings.add("--------------------");

                String result = (TextUtils.join("\n", eventStrings));

                mainActivity.onControlMessage("대화", (TextUtils.join("\n", eventStrings)));
            }
            Log.d("MakeRequestTask", "Finish");
        }
    }

    /**
     * @author Yehun Park
     * @file SecretaryService.InputDataTask.java
     * @breif Class connect with Google Calendar API and register schedule data
     * For register schedule data, format data according to app style
     */
    private class InputDataTask extends Thread {
        HttpTransport transport = null;
        JsonFactory jsonFactory = null;
        com.google.api.services.calendar.Calendar service = null;

        String message = null;
        String yearData = null;
        String monthData = null;
        String dayData = null;
        String startTimeData = null;
        String endTimeData = null;
        String duringTimeData = null;
        String summaryData = null;

        String result = null;

        public InputDataTask(String message) {
            Log.d("InputDataTask", "Constructor execute");
            int permissionCheck = ContextCompat.checkSelfPermission(mainActivity,
                    Manifest.permission.WRITE_CALENDAR);
            Log.d("InputDataTask", "permissioni : " + permissionCheck);

            this.message = message;
        }

        @Override
        public void run() {
            Log.d("InputDataTask", "doInBackground start");

            transport = AndroidHttp.newCompatibleTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
            service = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("R_D_Location Callendar")
                    .build();


            if (messagePassing(message) == true) {

                // Register Event Info
                Event event = new Event().setSummary(summaryData);

                DateTime startDateTime = new DateTime(String.format("%s-%s-%sT%s:00:00+09:00", yearData, monthData, dayData, startTimeData));
                EventDateTime start = new EventDateTime().setDateTime(startDateTime);
                event.setStart(start);

                DateTime endDateTime = new DateTime(String.format("%s-%s-%sT%s:00:00+09:00", yearData, monthData, dayData, endTimeData));
                EventDateTime end = new EventDateTime().setDateTime(endDateTime);
                event.setEnd(end);

                String calendarId = "primary";
                try {
                    event = service.events().insert(calendarId, event).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                result = event.getHtmlLink();

                Log.d("InputDataTask", "EventCreate : " + result);
            }

            if (result == null) {
                mainActivity.onControlMessage("대화", "일정 정보가 부족합니다.\\n년, 월, 일, 시작시간, 끝나는시간, \"내용\"을 말해주세요");
            } else {
                mainActivity.onControlMessage("대화", "일정이 등록되었습니다~ 굿굿!");
            }
        }

        /**
         * @param message
         * @brief Message format according to our app style
         */
        private boolean messagePassing(String message) {
            String[] token = null;
            String[] token2 = null;
            String temp;

            if (message.contains("\""))
                token = message.split("\"");

            if (token == null)
                return false;

            if (token.length == 1)
                return false;

            summaryData = token[1];

            if (token.length == 3)
                temp = token[0] + token[2];
            else
                temp = token[0];

            token = temp.split(" ");

            for (int i = 0; i < token.length; i++) {
                if (token[i].contains("일정등록") || token[i].contains("일정") || token[i].contains("등록")) {
                    token[i] = "";
                } else if (token[i].contains("년")) {
                    token2 = token[i].split("년");
                    yearData = token2[0];
                } else if (token[i].contains("월")) {
                    token2 = token[i].split("월");
                    monthData = token2[0];
                    if (Integer.valueOf(monthData) < 10)
                        monthData = "0" + monthData;
                } else if (token[i].contains("일")) {
                    token2 = token[i].split("일");
                    dayData = token2[0];

                    if (Integer.valueOf(dayData) < 10)
                        dayData = "0" + dayData;
                } else if (token[i].contains("시부터")) {
                    token2 = token[i].split("시");
                    startTimeData = token2[0];
                    if (Integer.valueOf(startTimeData) < 10)
                        startTimeData = "0" + startTimeData;
                } else if (token[i].contains("시간")) {
                    token2 = token[i].split("시");
                    duringTimeData = token2[0];

                    int endtime = Integer.valueOf(startTimeData) + Integer.valueOf(duringTimeData);

                    if (endtime < 10)
                        endTimeData = "0" + endtime;
                    else
                        endTimeData = "" + endtime;
                }
            }
            if (yearData == null || monthData == null || dayData == null || startTimeData == null || duringTimeData == null || endTimeData == null)
                return false;

            return true;
        }
    }

    ////
    // Getter and Setter
    ////
    public GoogleAccountCredential getmCredential() {
        return mCredential;
    }
}
