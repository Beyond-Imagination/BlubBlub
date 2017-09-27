package beyond_imagination.blubblub.pWebConnection;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Created by cru65 on 2017-08-21.
 */

public class HttpClient {

    /*** Variable ***/
    private static final String WWW_FORM = "application/x-www-form-urlencoded";

    private int httpStatusCode;
    private String body;

    private Builder builder;

    /*** Function ***/
    public int getHttpStatusCode(){
        return httpStatusCode;
    }

    public String getBody() {
        return body;
    }

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public void request(){
        HttpURLConnection conn = getConnection();
        if(conn == null)
            Log.d("asdfasdf", "request null 입니다.");
        setHeader(conn);
        setBody(conn);
        httpStatusCode = getStatusCode(conn);
        Log.d("asdfasdf-code", Integer.toString(httpStatusCode));
        body = readStream(conn);
        conn.disconnect();
    }

    private HttpURLConnection getConnection() {
        try {
            URL url = new URL(builder.getUrl());
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setHeader(HttpURLConnection connection) {
        setContentType(connection);
        setRequestMethod(connection);

        connection.setConnectTimeout(5000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private void setContentType(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", WWW_FORM);
    }

    private void setRequestMethod(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(builder.getMethod());
            Log.d("asdf-Method", builder.getMethod());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    private void setBody(HttpURLConnection connection) {
        String parameter = builder.getParameters();

        if(parameter != null && parameter.length() > 0)
        {
            OutputStream outputStream = null;

            try {
                outputStream = connection.getOutputStream();
                outputStream.write(parameter.getBytes("UTF-8"));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getStatusCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -10;
    }

    private String readStream(HttpURLConnection connection) {
        String result = "";
        BufferedReader reader = null;

        Log.d("asdfasdf", "readStream");

        try {
            int statuscode = connection.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Log.d("asdfasdf-result", result);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("asdfasdf", "Error");
        } finally{
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("asdfasdf", result);
        return result;
    }

    /*** Inner Class ***/
    public static class Builder{
        private Map<String, String> parameters;
        private String method;
        private String url;

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public Builder(String method, String url) {
            if (method == null) {
                method = "GET";
            }
            this.method = method;
            this.url = url;
            this.parameters = new HashMap<String, String>();
        }

        public void addOrReplace(String key, String value) {
            this.parameters.put(key, value);
        }

        public void addAllParameters(Map<String, String> parameters) {
            this.parameters.putAll(parameters);
        }

        public String getParameters() {
            return generateParameters();
        }

        public String getParameter(String key) {
            return this.parameters.get(key);
        }

        private String generateParameters() {
            StringBuffer parameters = new StringBuffer();

            Iterator<String> keys = getKeys();

            String key = "";

            while (keys.hasNext()) {
                key = keys.next();
                parameters.append(String.format("%s = %s", key, this.parameters.get(key)));
                parameters.append("&");
            }

            String params = parameters.toString();

            if (params.length() > 0) {
                params = params.substring(0, params.length() - 1);
            }

            return params;
        }

        private Iterator<String> getKeys() {
            return this.parameters.keySet().iterator();
        }

        public HttpClient create(){
            HttpClient client = new HttpClient();
            client.setBuilder(this);
            return client;
        }
    }
}
