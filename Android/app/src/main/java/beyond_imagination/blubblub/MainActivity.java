package beyond_imagination.blubblub;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.iid.FirebaseInstanceId;

import beyond_imagination.blubblub.pChatting.ChattingLayout;
import beyond_imagination.blubblub.pCondition.ConditionBar;
import beyond_imagination.blubblub.pCondition.ControlMessage;
import beyond_imagination.blubblub.pSetting.SettingButton;
import beyond_imagination.blubblub.pWebConnection.AutoService;
import beyond_imagination.blubblub.pWebConnection.GetConditionData;
import beyond_imagination.blubblub.pWebConnection.SendToBowl;
import beyond_imagination.blubblub.pWebConnection.SendToChatbot;
import beyond_imagination.blubblub.pWebView.MainWebView;

/**
 * @author Yehun Park
 * @file MainActivity.java
 * @breif This class is core of this application.
 * Almost class connected with MainActivity and worked by MainActivity.
 */
public class MainActivity extends AppCompatActivity {
    /****************/
    /*** Variable ***/
    /****************/
    private MainWebView mainWebView;
    private ChattingLayout chattingLayout;
    private ConditionBar conditionBar;

    // 알림 축적
    int accumulateCount;

    // Identity Data
    private DataHandler dataHandler;

    // Setting
    private Setting setting;
    private SettingButton settingBtn;

    // Gesture
    private GestureDetector gestureDetector;

    // Animation
    private AnimationManager animationManager;

    // Code value
    private final static int SETTING_CALL = 1000;
    private final static int UPDATE_CONDITION = 1001;
    private final static int FCM_PROBLEM = 1002;
    private final static int SEND_MESSAGE = 1003;
    private final static int RECEIVE_MESSAGE = 1004;
    private final static int GET_CALENDAR_DATA = 1005;
    static final int REQUEST_ACCOUNT_PICKER = 1006;
    static final int REQUEST_AUTHORIZATION = 1007;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1008;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1009;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    // WebConnection
    private GetConditionData getConditionData;

    // ControlMessage
    private ControlMessage controlMessage;

    // FCM에서 접근할 변수
    private static MainActivity mainActivity;

    public static Context getInstance() {
        return mainActivity;
    }

    // Handler
    private FCMHandler fcmHandler;

    /**
     * @breif Just main Thread access to UI. So, in sub Thread, for access to UI, using Handler and access to main Thread
     * Type : Update condition, FCM problem, send message, receive message
     */
    class FCMHandler extends Handler {
        Bundle bundle;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            bundle = msg.getData();

            Log.d("MainActivity", "Handler - " + msg.what);

            switch (msg.what) {
                case UPDATE_CONDITION:
                    conditionBar.onConditionUpdate(bundle.getString("feedtime"), bundle.getString("temperature"), bundle.getString("illumination"), bundle.getString("turbidity"));
                    break;
                case FCM_PROBLEM:
                    controlMessage.onUpdate(bundle.getString("type"), bundle.getString("body"));
                    controlMessage.setVisibility(View.VISIBLE);
                    break;
                case SEND_MESSAGE:
                    chattingLayout.sendMessage("나 : " + bundle.getString("body"));
                    break;
                case RECEIVE_MESSAGE:
                    if (bundle.getString("type").equals("날씨")) {
                        chattingLayout.receiveWeather(bundle.getString("body"));
                    } else {
                        chattingLayout.receiveMessage("붕어 : " + bundle.getString("body"));
                    }
                    break;
            }
        }
    }

    /****************/
    /*** Function ***/
    /****************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.setDatabase(this);

        accumulateCount = 0;

        mainWebView = (MainWebView) findViewById(R.id.webView);
        chattingLayout = (ChattingLayout) findViewById(R.id.chatting);
        conditionBar = (ConditionBar) findViewById(R.id.conditionbar);
        settingBtn = (SettingButton) findViewById(R.id.button_setting);
        controlMessage = (ControlMessage) findViewById(R.id.controlview);

        dataHandler = new DataHandler(FirebaseInstanceId.getInstance().getToken(), "Beyond_Imagination");

        // Read Setting data from Database
        setting = Database.readFromDatabase();
        sendMessageToChatbot(dataHandler.sendSetting(setting));

        IdentityApplication();

        mainActivity = this;
        fcmHandler = new FCMHandler();

        getConditionData = new GetConditionData();
        getConditionData.execute(this);

        animationManager = new AnimationManager(this);

        /**
         * @breif
         * GestureDetector for visible, invisible chatting layout.
         * If you drag from right to left, open chatting layout
         * If you drag from left to right, close chatting layout.
         */
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            float xFirst;
            float xLast;

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                xFirst = e1.getX();
                xLast = e2.getX();
                if (chattingLayout.getVisibility() == View.INVISIBLE) {
                    if (xFirst - 100 > xLast) {
                        chattingLayout.startAnimation(animationManager.chattinglayout_open);
                        chattingLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (xFirst + 100 < xLast) {
                        chattingLayout.startAnimation(animationManager.chattinglayout_close);
                        chattingLayout.setVisibility(View.INVISIBLE);
                    }
                }

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        Log.d("MainActivity", "onCreate() - success");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "onActivityResult");

        Log.d("MainActivity", "onActivityResult - requestCode : " + requestCode);
        switch (requestCode) {
            case SETTING_CALL:
                if (resultCode == RESULT_OK) {
                    setting = data.getExtras().getParcelable("setting");
                    Database.updateRecord(setting);
                    sendMessageToChatbot(dataHandler.sendSetting(setting));

                    if (setting.getAuto() == true) {
                        Intent intent = new Intent(this, AutoService.class);
                        intent.putExtra("setting", setting);
                        intent.putExtra("token", dataHandler.getToken());
                        intent.putExtra("secret", dataHandler.getSecret());
                        startService(intent);
                    } else {
                        Intent intent = new Intent(this, AutoService.class);
                        stopService(intent);
                    }
                }
                break;

            // Google Calendar 관련.
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "이 앱은 구글 플레이서비스를 이용할 수가 없으니 까시오.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        chattingLayout.getSecretaryService().getmCredential().setSelectedAccountName(accountName);
                        chattingLayout.getSecretaryService().getScheduleData();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    chattingLayout.getSecretaryService().getScheduleData();
                }
                break;
        }
    }

    /**
     * @param feedtime
     * @param temperature
     * @param illumination
     * @param turbidity
     * @breif Get the bowl contidion data and using handler for update UI
     */
    public void onConditionUpdate(String feedtime, String temperature, String illumination, String turbidity) {
        Log.d("MainActivity", "onConditionUpdate");

        Message msg = fcmHandler.obtainMessage();
        Bundle bundle = new Bundle();

        bundle.putString("feedtime", feedtime);
        bundle.putString("temperature", temperature);
        bundle.putString("illumination", illumination);
        bundle.putString("turbidity", turbidity);

        msg.setData(bundle);

        msg.what = UPDATE_CONDITION;

        fcmHandler.sendMessage(msg);
    }

    /**
     * @param type
     * @param body
     * @breif According to message type, decide which handler is worked
     */
    public void onControlMessage(String type, String body) {
        Log.d("MainActivity", "onControlMessage");

        Message msg = fcmHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("body", body);

        if (type.equals("대화보냄")) {
            msg.what = SEND_MESSAGE;
        } else if (type.equals("대화") || type.equals("날씨")) {
            msg.what = RECEIVE_MESSAGE;
        } else if (type.equals("먹이") || type.equals("더움") || type.equals("추움") || type.equals("어두움") || type.equals("밝음") || type.equals("탁함")) {
            msg.what = FCM_PROBLEM;
        }
        msg.setData(bundle);

        fcmHandler.sendMessage(msg);
    }

    /**
     * @brief For using BlubBlub application, user must Identify Application.
     * For Identitiy, send our secret string.
     */
    public void IdentityApplication() {
        Log.d("MainActivity", "IdentityApplicaton");

        // To chatbot server
        sendMessageToChatbot(dataHandler.sendIdentity("chatbot"));

        // To bowl
        sendRequestToBowl("인증");
    }

    /**
     * @param type
     * @brief send message to Bowl for control Bowl
     */
    public void sendRequestToBowl(String type) {
        Log.d("MainActivity", "sendRequestToBowl");

        if (type.equals("인증")) {
            new SendToBowl(type, dataHandler.sendIdentity("bowl"));
        } else {
            new SendToBowl(type, dataHandler.SendToken("bowl"));

            if (type.equals("먹이")) {
                conditionBar.controllFeedBtn(false);
                countInitialize();
            } else if (type.equals("어두움")) {
                conditionBar.controllIllumSwitch(true);
            } else if (type.equals("밝음")) {
                conditionBar.controllIllumSwitch(false);
            }
        }
    }

    /**
     * @param message
     * @brief Send user message to chatbot server for receive response message.
     */
    public void sendMessageToChatbot(String message) {
        Log.d("MainActivity", "sendMessageToChatbot");

        new SendToChatbot("163.152.219.171", 8002, message);
    }

    /**
     * @brief If you receive FCM message in background state, for receive message data, you must use getIntent().
     * If you touch FCM message in background state, application restarted. So you can user onResume() for receive FCM message data.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume()");

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String type = intent.getExtras().getString("type");
            String body = intent.getExtras().getString("body");

            Log.d("MainActivity", "FCM Data - type : " + type + ", body : " + body);

            onControlMessage(type, body);
        }
    }

    public void countAccumulate() {
        accumulateCount++;
    }

    public void countInitialize() {
        accumulateCount = 0;
    }

    ////
    // Getter and Setter
    ////
    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public ControlMessage getControlMessage() {
        return controlMessage;
    }

    public FCMHandler getFcmHandler() {
        return fcmHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }
}
