package beyond_imagination.blubblub.pWebConnection;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by cru65 on 2017-09-22.
 */
/**
 * @file SendToBowl.java
 * @breif
 * Class send control message and token to Bowl(Raspberry PI server)
 * There are 4 type control message(인증, 먹이, 어두움, 밝음)
 * @author Yehun Park
 */
public class SendToBowl extends Thread {
    /****************/
    /*** Variable ***/
    /****************/
    String type;
    String data;

    /****************/
    /*** Function ***/
    /****************/
    public SendToBowl(String type, String data) {
        this.type = type;
        this.data = data;

        start();
    }

    @Override
    public void run() {
        Log.d("SendToBowl", "type-" + type);
        if(type.equals("인증")) {
            getConnection("http://163.152.219.170:8000/register/");
        } else if (type.equals("먹이")) {
            Log.d("asdfadsf", "sendtobowl");
            getConnection("http://163.152.219.170:8000/feeding/");
        } else if (type.equals("어두움")) {
            getConnection("163.152.219.170:8000/ledOn");
        } else if (type.equals("밝음")) {
            getConnection("163.152.219.170:8000/ledOff");
        }
    }

    /**
     * @brief
     * Get Url and connection with Url.
     * Use HttpURLConnection and just use output stream for control message
     * Write data that you want to send and execute .getResponseCode().
     * @param url
     */
    public void getConnection(String url) {
        try {
            URL url_t = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) url_t.openConnection();

            Log.d("SendToBowl", "getConnection execute");
            if (conn != null) {
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                Log.d("SendToBowl", "Data : " + data);
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                outputStream.writeBytes(data);

                int rescode = conn.getResponseCode();
                Log.d("SendToBowl", "responseCode" + rescode);

                outputStream.flush();
                outputStream.close();

                conn.disconnect();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
