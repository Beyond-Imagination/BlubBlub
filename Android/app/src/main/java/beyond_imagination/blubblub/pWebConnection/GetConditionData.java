package beyond_imagination.blubblub.pWebConnection;

import android.content.Context;
import android.util.Log;

import beyond_imagination.blubblub.MainActivity;

/**
 * Created by cru65 on 2017-09-24.
 */

public class GetConditionData extends Thread {
    /*** Variable ***/
    MainActivity mainActivity;
    boolean isRunning = false;

    /*** Fungtion ***/
    public GetConditionData(Context context)
    {
        mainActivity = (MainActivity)context;
        isRunning = true;
    }

    @Override
    public void run() {
        long time = 0;
        long temp;

        String temperature;
        String illumination;
        String turbidity;
        String feedtime;

        String data = new String();

        // Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://163.152.219.170:8000/sensor/");

        // Http 요청 전송 - request 한 번 하고나면 연결이 disconnect되니 data를 받아오려고 할 때에 계속 실행시켜준다.
        HttpClient post = http.create();

        while (isRunning) {
            temp = System.currentTimeMillis();

            if ((temp - time) > 10000) {
                post.request();
                Log.d("asdfasdf", "실행중입니다");
                int statusCode = post.getHttpStatusCode();

                // 반응이 있을 때만, 텍스트 편집
                if (statusCode == 200) {
                    data = post.getBody();

                    // 공백까지 처리하는 split
                    String[] values = data.split("<p>|</p>");

                    temperature = values[1];
                    illumination = values[3];
                    turbidity = values[5];
                    feedtime = values[7];

                    for (int i = 1; i < values.length; i += 2) {
                        Log.d("Http", values[i]);
                    }

                    // 데이터 전송 - AsyncTask의 progressupdate 접근. Main Thread임.
                    mainActivity.onConditionUpdate(feedtime, temperature, illumination, turbidity);
                }

                time = temp;
            }
        }
    }
}
