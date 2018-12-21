package com.example.ex_3_weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv1;//结果显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = findViewById(R.id.send_request);
        tv1=findViewById(R.id.res_text);
        sendRequest.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        if(v.getId()==R.id.send_request){
            sendRequestWithHttpURLConnection();

        }
    }

    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://www.weather.com.cn/data/cityinfo/101070801.html").build();
                Response response=null;
                try {
                     response= client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String responseData=null;
                try {
                    responseData = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                parseJSONWithJSONObject(responseData);
                //showResponse(responseData);

            }

        }).start();
    }
private void parseJSONWithJSONObject(String jsonData){
        try{

            JSONObject jsonObject1 = new JSONObject(jsonData);
            String weatherinfo=jsonObject1.getString("weatherinfo");
            int n =1;
            JSONArray jsonArray = new JSONArray("["+weatherinfo+"]");
            n++;
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("city");
                String temp1 = jsonObject.getString("temp1");
                String temp2 = jsonObject.getString("temp2");
                String weather = jsonObject.getString("weather");
                String ptime = jsonObject.getString("ptime");
                showResponse(id+temp1+temp2+weather+ptime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
}
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv1.setText(response);

            }
        });
    }

}
