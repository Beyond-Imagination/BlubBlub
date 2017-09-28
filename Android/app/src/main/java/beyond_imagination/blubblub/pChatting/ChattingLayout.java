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
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.pWebConnection.SendToChatbot;

/**
 * Created by cru65 on 2017-07-27.
 */

/**
 * @file ChattingLayout.java
 * @breif
 * Class include all of things about chatting
 * Connect with chatbot server and Google Calendar API
 * @author Yehun Park
 */
public class ChattingLayout extends LinearLayout{
    /****************/
    /*** Variable ***/
    /****************/
    // For access to Mainactivity
    private MainActivity mainActivity;

    // Views
    private TextView title;
    private EditText editText;
    private Button button;
    private TextView chatting;
    private ScrollView scrollView;

    // Chatting Text
    private StringBuilder textBuilder;

    // Connect with Google Calendar API
    private SecretaryService secretaryService;
    /****************/
    /*** Function ***/
    /****************/

    /**
     * @brief
     * Constructor
     * @param context
     * @param attrs
     */
    public ChattingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("ChattingLayout", "Constructor execute");
        mainActivity = (MainActivity)context;
        init();
    }

    /**
     * @brief
     * initialize class
     * Inflate layout and initialize variables
     */
    private void init() {
        // inflate layout
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_chatting, this, false);
        addView(v);

        // initialize variables
        textBuilder = new StringBuilder();

        title = (TextView) findViewById(R.id.chatting_title);
        editText = (EditText) findViewById(R.id.chatting_input);
        button = (Button) findViewById(R.id.chatting_btn_send);
        chatting = (TextView) findViewById(R.id.chatting_text);
        chatting.setMovementMethod(new ScrollingMovementMethod());
        scrollView = (ScrollView) findViewById(R.id.chatting_text_scroll);
        secretaryService = new SecretaryService(mainActivity, editText);

        title.setText("< 물고기에게 말을 걸어보세요~ >");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메시지 확인
                checkCommand(editText.getText().toString());
            }
        });

        Log.d("ChattingLayout", "init()-success");
    }

    /**
     * @brief
     * Get message and check what user want to do
     * Check which command is contains in message
     * TYPE : 명령어, 일정확인, 일정등록
     * @param message
     */
    public void checkCommand(String message) {
        // initialize variables
        String result = null;
        String temp[] = message.split(" ");

        // output my message
        mainActivity.onControlMessage("대화보냄",message);
        Log.d("ChattingLayout", "checkCommand-message : " + message);

        // in normal case, send message to Chatbot server
        if (temp[0].contains("명령어")) {
            // show which command user can use
            Log.d("ChattingLayout", "checkCommand-명령어");

            result = message+"\n----------\n< 명령어 모음 >\n    명령어-명령어 모음을 볼 수 있습니다.\n    일정확인-최근 3일간의 일정을 확인할 수 있습니다.\n    일정등록-일정을 등록할 수 있습니다.\n(*????년 ?월 ?일 ?시부터 ?시간 \"내용\" 입력 필수!)\n----------";
            mainActivity.onControlMessage("대화", result);

        } else if(message.toString().contains("일정확인") || message.contains("일정 확인")) {
            // show my schedule contained in Google Calendar
            Log.d("ChattingLayout", "checkCommand-일정확인");

            secretaryService.getScheduleData();

        } else if(message.toString().contains("일정등록") || message.contains("일정 등록")) {
            // register my schedule in Google Calendar
            Log.d("ChattingLayout", "checkCommand-일정등록");

            secretaryService.setScheduleData(message);

        } else {
            // communication with chatbot server
            Log.d("ChattingLayout", "checkCommand-대화");

            result = message;
            mainActivity.sendMessageToChatbot(mainActivity.getDataHandler().sendMessage(result));

        }
    }

    /**
     * @brief
     * Show which message i send
     * @param message
     */
    public void sendMessage(String message){
        Log.d("ChattingLayout", "sendMessage-message : " + message);
        textBuilder.append("\n" + message);
        chatting.setText(textBuilder.toString());
        editText.setText("");

        // scroll down chatting text
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, chatting.getHeight());
            }
        });
    }

    /**
     * @brief
     * Show which message i send
     * @param message
     */
    public void receiveMessage(String message) {
        Log.d("ChattingLayout", "receciveMessage-message : " + message);
        textBuilder.append("\n" + message);
        chatting.setText(textBuilder.toString());

        // scroll down chatting text
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, chatting.getHeight());
            }
        });
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
