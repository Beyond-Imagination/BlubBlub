package beyond_imagination.blubblub;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cru65 on 2017-09-22.
 */
/**
 * @file DataHandler.java
 * @breif
 * Class format message according to situations.
 * Just send message, identify user, send token, send setting Info...
 * @author Yehun Park
 */
public class DataHandler {
    /****************/
    /*** Variable ***/
    /****************/
    String token;
    String secret;

    /****************/
    /*** Function ***/
    /****************/
    public DataHandler() {
        token = null;
        secret = null;
    }

    public DataHandler(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    /**
     * @breif
     * Just send message by JSON type.
     * @param message
     * @return
     */
    public String sendMessage(String message) {
        Log.d("DataHandler", "sendMessage");

        JSONObject data = new JSONObject();

        try {
            data.put("type", "message");
            data.put("message", message);
            data.put("token", token);

            return data.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @breif
     * Send token data by JSON type.
     * @param where
     * @return
     */
    public String SendToken(String where) {
        Log.d("DataHandler", "sendToken");

        if(where.equals("chatbot")) {
            JSONObject data = new JSONObject();

            try {
                data.put("token", token);

                return data.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            String data = "token="+token;
            return data;
        }

        return null;
    }

    /**
     * @breif
     * Send Identity data to chatbot and bowl server at the first using time.
     * By JSON type
     * @param where
     * @return
     */
    public String sendIdentity(String where) {
        Log.d("DataHandler", "sendIdentity");

        String data = new String();
        if (where.equals("chatbot")) {
            try {
                JSONObject data_json = new JSONObject();

                data_json.put("type", "token");
                data_json.put("token", token);
                data_json.put("secret", secret);

                data = data_json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (where.equals("bowl")) {
            data = "token="+token+"&secret="+secret;
        }
        return data;
    }

    /**
     * @breif
     * Send changed setting data to Chatbot server.
     * By JSON type.
     * @param setting
     * @return
     */
    public String sendSetting(Setting setting) {
        Log.d("DataHandler", "sendSettingData");

        JSONObject data = new JSONObject();

        try {
            data.put("type", "setting");
            data.put("feedcycle", setting.getFeed_cycle());
            data.put("maxtemp", setting.getTmp_max());
            data.put("mintemp", setting.getTmp_min());
            data.put("minillum", setting.getIllum_min());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    ////
    // Getter and Setter
    ////
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
