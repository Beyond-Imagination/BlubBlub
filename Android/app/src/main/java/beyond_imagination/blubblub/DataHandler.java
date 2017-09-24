package beyond_imagination.blubblub;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cru65 on 2017-09-22.
 */

public class DataHandler {
    /*** Variable ***/
    String token;
    String secret;

    /*** Function ***/
    public DataHandler() {
        token = null;
        secret = null;
    }

    public DataHandler(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String sendMessage(String message) {
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

    public String SendToken(String where) {
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

    public String sendIdentity(String where) {
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

    // Chatbot server
    public String sendSetting(Setting setting) {
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
