package beyond_imagination.blubblub.pWebConnection;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

/**
 * Created by cru65 on 2017-09-22.
 **/
/**
 * @file SendToChatbot.java
 * @breif
 * Class send message to Chatbot server
 * @author Yehun Park
 */
public class SendToChatbot extends Thread {
    /****************/
    /*** Variable ***/
    /****************/
    String address;
    String data;
    int port;

    Socket socket;

    DataOutputStream dataOutputStream;

    /****************/
    /*** Function ***/
    /****************/
    public SendToChatbot(String address, int port, String data) {
        this.address = address;
        this.port = port;
        this.data = data;

        start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.write(data.getBytes("UTF-8"));
            Log.d("SendToChatbot", "DATA : " + data);
            dataOutputStream.flush();
            dataOutputStream.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
