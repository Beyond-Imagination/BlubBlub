package beyond_imagination.blubblub.pCondition;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;

/**
 * Created by cru65 on 2017-09-18.
 */
/**
 * @file ControlMessage.java
 * @breif
 * Class show which problem happened in your bowl.
 * If there are some problem in your bowl, you receive FCM message
 * And ask, do you wanna control request to you bowl?
 * @author Yehun Park
 */
public class ControlMessage extends LinearLayout {
    /****************/
    /*** Variable ***/
    /****************/
    TextView textView;
    Button button_ok;
    Button button_cancel;

    MainActivity mainActivity;

    String type;

    /****************/
    /*** Function ***/
    /****************/
    public ControlMessage(Context context) {
        super(context);
        mainActivity = (MainActivity)context;
        init();
    }

    public ControlMessage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity)context;
        init();
    }

    public ControlMessage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainActivity = (MainActivity)context;
        init();
    }

    public void init() {
        final String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_controlmessage, this, false);
        addView(v);

        type = "먹이";

        textView = (TextView) findViewById(R.id.controlmessage);
        button_ok = (Button) findViewById(R.id.controlbtn);
        button_cancel = (Button) findViewById(R.id.controlbtncancel);

        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ControlMessage", "ControlMessage-type:" + type);
                sendMesssage();
                setVisibility(INVISIBLE);
            }
        });

        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.INVISIBLE);
            }
        });

        Log.d("ControlMessage", "init()-success");
    }

    private void sendMesssage()
    {
        if(type.equals("먹이")){
            mainActivity.sendRequestToBowl("먹이");
        } else if (type.equals("더움")) {
            // 온도 조절 추가 예정
        }else if (type.equals("추움")) {
            // 온도 조절 추가 예정
        }else if (type.equals("어두움")) {
            mainActivity.sendRequestToBowl("어두움");
        } else if (type.equals("밝음")) {
            mainActivity.sendRequestToBowl("밝음");
        } else if (type.equals("탁함")) {
            // 탁도 조절 추가 예정
        }
    }

    public void onUpdate(String type, String body) {
        Log.d("ControlMessage", "onUpdate");
        textView.setText(body);
        this.type = type;
    }
}
