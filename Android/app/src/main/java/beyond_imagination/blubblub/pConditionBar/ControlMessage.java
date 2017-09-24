package beyond_imagination.blubblub.pConditionBar;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.pWebConnection.ControlRequest;
import beyond_imagination.blubblub.pWebConnection.HttpClient;
import beyond_imagination.blubblub.pWebConnection.SendToBowl;

/**
 * Created by cru65 on 2017-09-18.
 */

public class ControlMessage extends LinearLayout {
    /*** Variable ***/
    TextView textView;
    Button button_ok;
    Button button_cancel;

    MainActivity mainActivity;

    String type;

    /*** Function ***/
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

                Log.d("asdfasdf", type);
                if(type.equals("먹이")){
                    //ControlRequest controlRequest = new ControlRequest("http://163.152.219.170:8000/feeding/", FirebaseInstanceId.getInstance().getToken());
                    //ControlRequest controlRequest = new ControlRequest("163.152.219.170", 8000,"token="+FirebaseInstanceId.getInstance().getToken() , new ContentValues());
                    //controlRequest.start();
                    mainActivity.sendRequestToBowl("먹이");
                    setVisibility(INVISIBLE);
                } else if (type.equals("더움")) {
                    setVisibility(INVISIBLE);
                }else if (type.equals("추움")) {
                    setVisibility(INVISIBLE);
                }else if (type.equals("어두움")) {
                    setVisibility(INVISIBLE);
                }else if (type.equals("탁함")) {
                    setVisibility(INVISIBLE);
                }
            }
        });

        button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onUpdate(String type, String body) {
        textView.setText(body);
        this.type = type;
        Log.d("asdfasdf", type);
        Log.d("asdfasdf", body);
    }
}
