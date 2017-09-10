package beyond_imagination.blubblub.pWebConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.StringTokenizer;

import beyond_imagination.blubblub.MainActivity;

/**
 * Created by cru65 on 2017-08-21.
 */

public class NetworkTask extends AsyncTask<MainActivity, String, String> {
    /*** Variable ***/
    MainActivity mainActivity;

    /*** Fungtion ***/
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("Http_result", s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        mainActivity.onConditionUpdate(values[0], values[1],values[2]);
    }

    @Override
    protected String doInBackground(MainActivity... params) {
        mainActivity = params[0];

        String temperature;
        String illumination;
        String turbidity;

        // Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://163.152.219.170:8000/sensor/");

        // Http 요청 전송 - request 한 번 하고나면 연결이 disconnect되니 data를 받아오려고 할 때에 계속 실행시켜준다.
        HttpClient post = http.create();
        post.request();

        int statusCode = post.getHttpStatusCode();

        String body = post.getBody();

        // 공백까지 처리하는 split
        String[] values = body.split("<p>|</p>");

        temperature = values[1];
        illumination = values[3];
        turbidity = values[5];

        for(int i = 1; i<values.length; i+=2)
        {
            Log.d("Http", values[i]);
        }

        // 데이터 전송 - AsyncTask의 progressupdate 접근. Main Thread임.
        publishProgress(temperature, illumination, turbidity);

        // tokenizer을 사용하니, <p>로 구분하는게 아니라 <,p,>로 구분하는 결과로 나옴.
        /*
        StringTokenizer values = new StringTokenizer(body, "<p>|</p>");

        for(int i = 0;values.hasMoreTokens(); i++)
        {
            // STringTokenizer의 trim은 공백을 제거하여 처리.
            Log.d("Http", values.nextToken().trim());
        }
        */

        return body;
    }
}
