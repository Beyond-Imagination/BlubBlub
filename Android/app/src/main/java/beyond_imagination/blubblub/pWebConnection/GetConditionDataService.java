package beyond_imagination.blubblub.pWebConnection;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

import beyond_imagination.blubblub.DataHandler;
import beyond_imagination.blubblub.Setting;

/**
 * Created by cru65 on 2017-10-25.
 */

/**
 * @author Yehun Park
 * @file GetConditionDataService.java
 * @breif
 * Connect website(bowl server) and Crawling HTML code.
 * Get condition data from HTML code and check condition data.
 * If there are some problems, automatically send request to bowl.
 * This work execute per every 1 minute.
 */
public class GetConditionDataService extends Thread {
    /****************/
    /*** Variable ***/
    /****************/
    private Setting setting;

    private boolean isRunning;

    private DataHandler dataHandler;

    /****************/
    /*** Function ***/
    /****************/
    public GetConditionDataService() {
        dataHandler = new DataHandler(null, null);
    }

    @Override
    public synchronized void start() {
        isRunning = true;
        super.start();
    }

    @Override
    public void run() {
        Log.d("GetConditionDataService", "getCondition()");

        String temperature;
        String illumination;
        String turbidity;
        String feedtime;

        String data = new String();

        long time = 0;
        long temp;

        // Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://163.152.219.170:8000/sensor/");

        // Http 요청 전송 - request 한 번 하고나면 연결이 disconnect되니 data를 받아오려고 할 때에 계속 실행시켜준다.
        HttpClient post = http.create();

        while (isRunning) {
            temp = System.currentTimeMillis();

            // 1분마다 웹서버에서 어항 상태 데이터 수신.
            if ((temp - time) > 60000) {
                Log.d("GetConditionDataService", "Get condition Data");

                post.request();

                int statusCode = post.getHttpStatusCode();

                // 반응이 있을 때만, 텍스트 편집
                if(statusCode == 200) {
                    data = post.getBody();

                    // 공백까지 처리하는 split
                    String[] values = data.split("<p>|</p>");

                    temperature = values[1];
                    illumination = values[3];
                    turbidity = values[5];
                    feedtime = values[7];

                    for (int i = 1; i < values.length; i += 2) {
                        Log.d("GetConditionDataService", values[i]);
                    }

                    // 데이터 전송 - AsyncTask의 progressupdate 접근. Main Thread임.
                    checkCondition(feedtime, temperature, illumination, turbidity);
                }
                time = temp;
            }
        }
    }

    /**
     * Check bowl condition, is there any problem(Feeding, light etc).
     * @param feedtime
     * @param temperature
     * @param illumination
     * @param turbidity
     */
    public void checkCondition(String feedtime, String temperature, String illumination, String turbidity) {
        Log.d("GetConditionDataService", "checkCondition()");
        // time after feeding (min)
        Calendar calendar = Calendar.getInstance(Locale.KOREAN);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        int time = (hour * 60) + min;
        // 먹이 주기가 되면 먹이를 준다.
        if ((time - Integer.valueOf(feedtime)) > (setting.getFeed_cycle() * 60)) {
            sendRequestToBowl("먹이");
        }

        // illumination
        if (Integer.valueOf(illumination) > setting.getIllum_max()) {
            sendRequestToBowl("밝음");
        } else if (Integer.valueOf(illumination) < setting.getIllum_min()) {
            sendRequestToBowl("어두움");
        }
    }

    private void sendRequestToBowl(String type) {
        Log.d("GetConditionDataService", "sendRequestToBowl");

        new SendToBowl(type, dataHandler.SendToken("bowl"));
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

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }
}
