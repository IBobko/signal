package ru.innopolis.messagino;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class SendMessage {
    public static void sendMessage(String message) {
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");

        try {

            JSONObject jsonObject = new JSONObject();

            JSONObject data = new JSONObject();
            data.put("score",message);

            jsonObject.accumulate("data", data);
            jsonObject.accumulate("to", FirebaseInstanceId.getInstance().getToken());

            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "key=AIzaSyBMs-zMxqJ5JIB6O-Adl-ikRWWWODokw8g");

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        HttpResponse httpResponse = httpclient.execute(httpPost);

                        InputStream inputStream = httpResponse.getEntity().getContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
