package beyond_imagination.blubblub;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
import beyond_imagination.blubblub.pChatting.SecretaryService;
import beyond_imagination.blubblub.pConditionBar.ConditionBar;
import beyond_imagination.blubblub.pConditionBar.ControlMessage;
import beyond_imagination.blubblub.pService.FCMMessagingService;
import beyond_imagination.blubblub.pSetting.SettingButton;
import beyond_imagination.blubblub.pWebConnection.ControlRequest;
import beyond_imagination.blubblub.pWebConnection.GetConditionData;
import beyond_imagination.blubblub.pWebConnection.NetworkTask;
import beyond_imagination.blubblub.pWebConnection.SendToBowl;
import beyond_imagination.blubblub.pWebConnection.SendToChatbot;
import beyond_imagination.blubblub.pWebView.MainWebView;

public class MainActivity extends AppCompatActivity {

    /*** Variable ***/
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
    private NetworkTask networkTask;
    private GetConditionData getConditionData;

    // ControlMessage
    private ControlMessage controlMessage;

    // Secretary Service
    //private SecretaryService secretaryService;

    // FCM에서 접근할 변수
    private static MainActivity mainActivity;

    public static Context getInstance() {
        return mainActivity;
    }

    // Handler
    private FCMHandler fcmHandler;

    class FCMHandler extends Handler {
        Bundle bundle;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            bundle = msg.getData();
            //Log.d("qqqqqqqq", bundle.getString("body"));
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
                    Log.d("qqqqqqqq", bundle.getString("body"));
                    break;
                case RECEIVE_MESSAGE:
                    chattingLayout.receiveMessage("붕어 : " +bundle.getString("body"));
                    Log.d("qqqqqqqq", bundle.getString("body"));
                    break;

                case GET_CALENDAR_DATA:
                    break;
            }
        }
    }

    /*** Function ***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accumulateCount = 0;

        mainWebView = (MainWebView) findViewById(R.id.webView);
        chattingLayout = (ChattingLayout) findViewById(R.id.chatting);
        conditionBar = (ConditionBar) findViewById(R.id.conditionbar);
        settingBtn = (SettingButton) findViewById(R.id.button_setting);
        controlMessage = (ControlMessage) findViewById(R.id.controlview);

        setting = new Setting();

        //secretaryService = new SecretaryService(this, chattingLayout.getEditText());

        dataHandler = new DataHandler(FirebaseInstanceId.getInstance().getToken(), "Beyond_Imagination");

        IdentityApplication();

        mainActivity = this;
        fcmHandler = new FCMHandler();

        networkTask = new NetworkTask();
        networkTask.execute(this);
        //getConditionData = new GetConditionData(this);
        //getConditionData.start();

        animationManager = new AnimationManager(this);

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("asdfasdf", "akakakakakak");
        switch (requestCode) {
            case SETTING_CALL:
                if (resultCode == RESULT_OK) {
                    setting = data.getExtras().getParcelable("setting");

                    sendMessageToChatbot(dataHandler.sendSetting(setting));
                    //SendToChatbot sendToChatbot = new SendToChatbot("163.152.219.171", 8002, dataHandler.sendSetting(setting));
                    //sendToChatbot.start();

                }
                break;

            // Google Calendar 관련.
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "이 앱은 구글 플레이서비스를 이용할 수가 없으니 까시오.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                Log.d("asdfasdf", "akakakakakak");
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    Log.d("asdfasdf", "akakakakakak");
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

    // Condition Update
    public void onConditionUpdate( String feedtime, String temperature, String illumination, String turbidity) {
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

    // Control Message
    public void onControlMessage(String type, String body) {
        Message msg = fcmHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("body", body);

        if (type.equals("대화보냄")) {
            msg.what = SEND_MESSAGE;
        } else if (type.equals("대화")) {
            msg.what = RECEIVE_MESSAGE;
        } else if (type.equals("먹이") || type.equals("더움") || type.equals("추움") || type.equals("어두움") || type.equals("탁함")) {
            msg.what = FCM_PROBLEM;
        }
        msg.setData(bundle);

        fcmHandler.sendMessage(msg);
    }

    public void IdentityApplication() {
        // To chatbot server
        sendMessageToChatbot(dataHandler.sendIdentity("chatbot"));

        // To bowl
        sendRequestToBowl("인증");
    }

    public void sendRequestToBowl(String method) {
        if (method.equals("인증")) {
            new SendToBowl(method, dataHandler.sendIdentity("bowl"));
        } else  {
            new SendToBowl(method, dataHandler.SendToken("bowl"));

            if (method.equals("먹이")) {
                conditionBar.controllFeedBtn(false);
                countInitialize();
            }
        }
    }

    public void sendMessageToChatbot(String message) {
            new SendToChatbot("163.152.219.171", 8002, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Log.d("asdfasdf", "정보 받았당~" + intent.getExtras());
            String type = intent.getExtras().getString("type");
            Log.d("asdfasdf", type);
            String body = intent.getExtras().getString("body");
            Log.d("asdfasdf", body);

            onControlMessage(type, body);
        }
    }

    public void countAccumulate()
    {
        accumulateCount++;
    }

    public void countInitialize(){
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
