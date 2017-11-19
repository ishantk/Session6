package co.edureka.session6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter; // Pre-defined adapter API. You can customize it.

    BookAdapter bookAdapter;

    StringBuffer response;
    String responseString;

    ArrayList<Book> bookList;

    //BookReceiver bookReceiver;

    void initViews(){
        listView = (ListView)findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        response = new StringBuffer();

        bookList = new ArrayList<>();

        // Static Data...
        /*adapter.add("Book1\nINR200");
        adapter.add("Book2\nINR300");
        adapter.add("Book3\nINR400");

        listView.setAdapter(adapter);*/

        if(isInternetConnected()){

            //new RetrieveBooksThread().start(); // start a java thread parllely to the MainActivity Thread
            //new RetrieveTask().execute();

            //Intent intent = new Intent(MainActivity.this,BookIntentService.class);
            //startService(intent);

            requestWebService();

        }else {
            Toast.makeText(this,"Please turn on your internet",Toast.LENGTH_LONG).show();
        }

    }

    boolean isInternetConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        IntentFilter filter = new IntentFilter();
        filter.addAction("co.edureka.books.retrieved");


        //bookReceiver = new BookReceiver();
        //registerReceiver(bookReceiver,filter);

    }

    void requestWebService(){

        // Volley Code Here.
        // No need of Thread, AsyncTask or IntentService...

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        // take 4 inputs
        // 1. HTTP Method
        // 2. URL
        // 3. ResponseListener
        // 4. ErrorListener
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseString = response;
                        parseJSONResponse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Some Error Occured..!!",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );


        requestQueue.add(stringRequest); // execute the request

    }

    /*class BookReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("co.edureka.books.retrieved")){

                responseString = intent.getStringExtra("responseKey");
                parseJSONResponse();

            }

        }
    }*/


    // Java Thread...
    // This thread to retrieve the data from URL
    // We cannot update the UI from background thread. We shall send a message to the handler and Handler will update the UI.
    /*class RetrieveBooksThread extends Thread{

        // Background task goes in run method
        @Override
        public void run() {

            Looper.prepare();

            try {
                URL url = new URL("http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Requesting the server

                InputStream inputStream = connection.getInputStream(); // Response from the server

                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

                handler.sendEmptyMessage(101); // 101 can be any integer of your choice.

            }catch (Exception e){
                e.printStackTrace();
            }

            Looper.loop();
        }
    }

    // Handler belong to Activity i.e. UI Thread and can update the UI
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 101){

                // Code here to update the UI
                parseJSONResponse();
            }

        }
    };*/


    // Thread of Android
    /*class RetrieveTask extends AsyncTask{

        //UI Thread
        @Override
        protected void onPreExecute() {
            // some initializations
        }

        // NON UI Thread
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL("http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Requesting the server

                InputStream inputStream = connection.getInputStream(); // Response from the server

                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    response.append(line);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        //UI Thread
        @Override
        protected void onPostExecute(Object o) {
            parseJSONResponse();
        }
    }*/

    void parseJSONResponse(){

        try {

            // From the response you create JSONObject
            //JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("bookstore");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObj = jsonArray.getJSONObject(i);

                Book book = new Book();
                book.setPrice(jObj.getString("price"));
                book.setName(jObj.getString("name"));
                book.setAuthor(jObj.getString("author"));

                bookList.add(book); // If you wish to customize the adapter

                //adapter.add(jObj.getString("name")+" | "+jObj.getString("price")+"\n"+jObj.getString("author"));
            }


            bookAdapter = new BookAdapter(this,R.layout.list_item,bookList);
            listView.setAdapter(bookAdapter);

            //listView.setAdapter(adapter); // Dynamic Data on ListView


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
