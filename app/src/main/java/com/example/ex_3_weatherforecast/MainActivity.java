package com.example.ex_3_weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    private void sendRequestWithHttpURLConnection() {
        //发送网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    //设定url
                    URL url = null;

                    try {
                        url = new URL("http://www.baidu.com");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    try {
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //获取数据GET
                    try {
                        conn.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    //设定连接超时，读取超时
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    //输入流
                    InputStream in = null;
                    try {
                        in = conn.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //读取输入流

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;

                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {

                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
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
