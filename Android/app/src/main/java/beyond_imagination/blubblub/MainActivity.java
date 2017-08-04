package beyond_imagination.blubblub;

import android.content.Intent;
import android.service.notification.Condition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import beyond_imagination.blubblub.pChatting.ChattingLayout;
import beyond_imagination.blubblub.pConditionBar.ConditionBar;
import beyond_imagination.blubblub.pSetting.SettingButton;
import beyond_imagination.blubblub.pWebView.MainWebView;

public class MainActivity extends AppCompatActivity {

    /*** Variable ***/
    private MainWebView mainWebView;
    private ChattingLayout chattingLayout;
    private ConditionBar conditionBar;

    // Setting
    private Setting setting;
    private SettingButton settingBtn;

    // Gesture
    private GestureDetector gestureDetector;

    // Animation
    private AnimationManager animationManager;

    // Code value
    private final static int SETTING_CALL = 1000;

    /*** Function ***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = new Setting();

        mainWebView = (MainWebView) findViewById(R.id.webView);
        chattingLayout = (ChattingLayout) findViewById(R.id.chatting);
        conditionBar = (ConditionBar) findViewById(R.id.conditionbar);
        settingBtn = (SettingButton) findViewById(R.id.button_setting);

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
                    if (xFirst > xLast) {
                        chattingLayout.startAnimation(animationManager.chattinglayout_open);
                        chattingLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (xFirst < xLast) {
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
        switch (resultCode) {
            case RESULT_CANCELED:
                break;

            case RESULT_OK: {
                switch (requestCode) {
                    case SETTING_CALL:
                        setting = data.getExtras().getParcelable("setting");
                        break;
                }
            }
        }
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

}
