package co.edureka.session6;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BookIntentService extends IntentService {

    StringBuffer response;

    public BookIntentService() {
        super("BookIntentService");
    }

    // Background code goes here
    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            response = new StringBuffer();

            URL url = new URL("http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Requesting the server

            InputStream inputStream = connection.getInputStream(); // Response from the server

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                response.append(line);
            }

            Intent i = new Intent("co.edureka.books.retrieved");
            i.putExtra("responseKey",response.toString());
            sendBroadcast(i);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
