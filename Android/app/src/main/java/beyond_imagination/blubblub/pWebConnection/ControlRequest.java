package beyond_imagination.blubblub.pWebConnection;

import android.content.ContentValues;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

/**
 * Created by cru65 on 2017-09-18.
 */

public class ControlRequest extends Thread {
    /*** Variable ***/
    String address;
    int port;

    HttpClient.Builder http;
    HttpClient post;

    String data;

    Socket socket;

    BufferedWriter outputStream;

    ContentValues params;

    /*** Function ***/
    public ControlRequest(String address, String data) {
        this.address = address;
        this.data = data;
        http = new HttpClient.Builder("POST",address);
        post = http.create();
    }

    public ControlRequest(String address, int port, String data) {
        this.address = address;
        this.port = port;
        this.data = data;
    }
    public ControlRequest(String address, int port, String data, ContentValues params) {
        this.address = address;
        this.port = port;
        this.data = data;
        this.params = params;
    }
/*
    @Override
    public void run()
    {
        post.access();
    }
*/
    @Override
    public void run() {

        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();

        params.put("token", FirebaseInstanceId.getInstance().getToken());

        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : params.valueSet()){
                    key = parameter.getKey();
                    value = parameter.getValue().toString();

                    // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                    if (isAnd)
                        sbParams.append("&");

                    sbParams.append(key).append("=").append(value);

                    Log.d("asdfasdf", "aaaaaaaaaaaaaaaaa" + sbParams.toString());


                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (params.size() >= 2)
                        isAnd = true;
            }
        }
        try {
            URL url = new URL("http://163.152.219.170:8000/feeding/");
            Log.d("asdfasdf", "aaaaaaaaaaaaaaaaa" + sbParams.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                Log.d("asdfasdf", "aaaaaaaaaaaaaaaaa" + sbParams.toString());
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                //outputStream.write(sbParams.toString().getBytes("UTF-8"));
                String asdf = "token=HelloWorld";
                outputStream.writeBytes(data);

                int rescode = conn.getResponseCode();

                Log.d("asdfasdf", "bbbbbbbbbbbbbbbbbb");
                outputStream.flush();
                outputStream.close();

                conn.disconnect();
            }
        } catch (Exception e) {

        }
    }
}
