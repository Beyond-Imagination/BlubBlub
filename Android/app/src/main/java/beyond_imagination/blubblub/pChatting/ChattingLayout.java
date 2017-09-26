package beyond_imagination.blubblub.pChatting;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.pWebConnection.SendToChatbot;

/**
 * Created by cru65 on 2017-07-27.
 */

public class ChattingLayout extends LinearLayout{
    /*** Variable ***/
    MainActivity mainActivity;

    TextView title;
    EditText editText;
    Button button;
    TextView chatting;

    StringBuilder textBuilder;

    SecretaryService secretaryService;

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

        textBuilder = new StringBuilder();

        title = (TextView) findViewById(R.id.chatting_title);
        editText = (EditText) findViewById(R.id.chatting_input);
        button = (Button) findViewById(R.id.chatting_btn_send);
        chatting = (TextView) findViewById(R.id.chatting_text);
        chatting.setMovementMethod(new ScrollingMovementMethod());

        secretaryService = new SecretaryService(mainActivity, editText);

        title.setText("< 물고기에게 말을 걸어보세요~ >");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 메시지 확인
                checkCommand(editText.getText().toString());



                // Google Calendar
                //mainActivity.getSecretaryService().getScheduleData();
            }
        });
    }

    public void checkCommand(String message) {
        String result = null;
        String temp[] = message.split(" ");

        mainActivity.onControlMessage("대화보냄",message);

        // 일반적인 대화일 경우만 Chatbot Server로 Message 전달
        if (temp[0].contains("명령어")) {
            result = message+"\n----------\n< 명령어 모음 >\n    명령어-명령어 모음을 볼 수 있습니다.\n    일정확인-최근 3일간의 일정을 확인할 수 있습니다.\n    일정등록-일정을 등록할 수 있습니다.\n----------";
            mainActivity.onControlMessage("대화", result);
        } else if(message.toString().contains("일정확인") || message.contains("일정 확인")) {
            secretaryService.getScheduleData();
        } else if(message.toString().contains("일정등록") || message.contains("일정 등록")) {
            secretaryService.setScheduleData(message);
        } else {
            result = message;
            mainActivity.sendMessageToChatbot(mainActivity.getDataHandler().sendMessage(result));
        }
    }

    public void sendMessage(String message){
        textBuilder.append("\n" + message);
        chatting.setText(textBuilder.toString());
        editText.setText("");
    }

    public void receiveMessage(String message) {
        textBuilder.append("\n" + message);
        chatting.setText(textBuilder.toString());
    }

    ////
    // Getter and Setter
    ////

    public EditText getEditText() {
        return editText;
    }

    public SecretaryService getSecretaryService() {
        return secretaryService;
    }
}
