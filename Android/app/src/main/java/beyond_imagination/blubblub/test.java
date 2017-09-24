package beyond_imagination.blubblub;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cru65 on 2017-09-21.
 */

public class test extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String postData = "";

        HttpURLConnection httpURLConnection= null;
        try {

            httpURLConnection= (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            DataOutputStream outputStream= new DataOutputStream(httpURLConnection.getOutputStream());
            outputStream.writeBytes( params[1]);
            outputStream.flush();
            outputStream.close();

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char currentData = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                postData += currentData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection!= null) {
                httpURLConnection.disconnect();
            }
        }
        return postData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
