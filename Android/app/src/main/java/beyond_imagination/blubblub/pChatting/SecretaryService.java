package beyond_imagination.blubblub.pChatting;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.google.api.services.calendar.model.Events;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beyond_imagination.blubblub.MainActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by cru65 on 2017-09-24.
 */

public class SecretaryService implements EasyPermissions.PermissionCallbacks{
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
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    /*** Function ***/
    public SecretaryService(Context context, EditText editText) {
        mainActivity = (MainActivity)context;
        ouputText = editText;

        mCredential = GoogleAccountCredential.usingOAuth2(mainActivity, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
    }

    public void getScheduleData()
    {
        getResultsFromApi();
    }

    private void getResultsFromApi() {
        Log.d("asdfasdf", "asdfasd");

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            Log.d("asdfasdf", "1");
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
            Log.d("asdfasdf", "2");
        } else if (!isDeviceOnline()) {
            Toast.makeText(mainActivity, "No network connection available", Toast.LENGTH_SHORT).show();
            Log.d("asdfasdf", "3");
        } else {
            new MakeRequestTask(mCredential).execute();
            //new MakeRequestTask1(mCredential).start();
            Log.d("asdfasdf", "4");
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(mainActivity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = mainActivity.getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if(accountName == null)
                Log.d("asdfasdf", "ㅁㄴㅇㄹ"+accountName);

            Log.d("asdfasdf", "ㅁㄴㅇㄹ");

            if (accountName != null) {
                Log.d("asdfasdf", accountName);
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                Log.d("asdfasdf", "ㅁㄴㅇㄹ");
                mainActivity.startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(mainActivity, "This app needs to access your Google account(via Contacts)",REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
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
        Dialog dialog = apiAvailability.getErrorDialog(mainActivity,  connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
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
            Events events = mService.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
            List<Event> items = events.getItems();

            Log.d("asdfadsf", "캘린더 작업중이당~~");

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                eventStrings.add(String.format("%s (%s)", event.getSummary(), start));
            }
            return eventStrings;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
                Toast.makeText(mainActivity, "No results returnd", Toast.LENGTH_SHORT).show();
            } else {
                output.add(0, "Data retrieved using the Google Calendar API");
                Toast.makeText(mainActivity, "Data retrieved using the Google Calendar API", Toast.LENGTH_SHORT).show();
                mainActivity.onControlMessage("대화",(TextUtils.join("\n", output)));
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

    private class MakeRequestTask1 extends Thread{
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask1(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential).setApplicationName("Google Calendar API Android Quickstart").build();
        }

        @Override
        public void run() {
            try {
                Log.d("asdfadsf", "캘린더 작업중이당~~");
                getDataFromApi();
            } catch (IOException e) {
                e.printStackTrace();
                cancel();
            }
        }

        private void getDataFromApi() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> output = new ArrayList<String>();
            Events events = mService.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
            List<Event> items = events.getItems();

            Log.d("asdfadsf", "캘린더 작업중이당~~");

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                output.add(String.format("%s (%s)", event.getSummary(), start));
            }

            if (output == null || output.size() == 0) {
                Toast.makeText(mainActivity, "No results returnd", Toast.LENGTH_SHORT).show();
            } else {
                output.add(0, "Data retrieved using the Google Calendar API");
                //Toast.makeText(mainActivity, "Data retrieved using the Google Calendar API", Toast.LENGTH_SHORT).show();
                //ouputText.setText((TextUtils.join("\n", output)));
                mainActivity.onControlMessage("대화", (TextUtils.join("\n", output)));
            }
        }

        private void cancel(){
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    mainActivity.startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    //ouputText.setText("The following error occurred:\n" + mLastError.getMessage());
                    mainActivity.onControlMessage("대화","The following error occurred:\n" + mLastError.getMessage());
                }
            } else {
                //ouputText.setText("Request cancelled");
                mainActivity.onControlMessage("대화", "Request cancelled");
            }
        }
    }

    ////
    // Getter and Setter
    ////

    public GoogleAccountCredential getmCredential() {
        return mCredential;
    }
}
