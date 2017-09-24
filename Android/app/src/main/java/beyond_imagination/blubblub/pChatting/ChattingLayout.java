package beyond_imagination.blubblub.pChatting;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.pWebConnection.SendToChatbot;

/**
 * Created by cru65 on 2017-07-27.
 */

public class ChattingLayout extends LinearLayout{
    /*** Variable ***/
    MainActivity mainActivity;

    EditText editText;
    Button button;
    TextView send;
    TextView receive;

    /*** Function ***/
    public ChattingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mainActivity = (MainActivity)context;

        init();
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_chatting, this, false);
        addView(v);

        editText = (EditText) findViewById(R.id.chatting_input);
        button = (Button) findViewById(R.id.chatting_btn_send);
        send = (TextView) findViewById(R.id.chatting_sendText);
        receive = (TextView) findViewById(R.id.chatting_receiveText);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainActivity.sendMessageToChatbot(mainActivity.getDataHandler().sendMessage(editText.getText().toString()));

                //mainActivity.onControlMessage("대화보냄",editText.getText().toString());

                mainActivity.getSecretaryService().getScheduleData();
            }
        });
    }

    public void sendMessage(String message)
    {
        send.setText(message);
        editText.setText("");
    }

    public void receiveMessage(String message) {
        receive.setText(message);
    }

    ////
    // Getter and Setter
    ////

    public EditText getEditText() {
        return editText;
    }
}
