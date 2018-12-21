package com.example.ex_3_weatherforecast;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import okhttp3.Call;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
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
import android.view.Menu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    TextView tv1,tv2;//结果显示
    cityC cityc=new cityC();
    private String[] day={"1","2","3","4","5"};
    private String city_code="101010100";//默认是北京

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        //默认显示北京
        sendRequestWithHttpURLConnection();

        Button bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText(day[0]);
            }
        });
        Button bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText(day[1]);
            }
        });
        Button bt3 = findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText(day[2]);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //选项菜单
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.que:
                //新增单词
                queCity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //获取输入的城市，并转换为相应的代码
    private void queCity(){
        final EditText et = new EditText(this);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("请输入需要查询的城市:");
        ad1.setView(et);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                try {

                    String city_name = et.getText().toString();
                    city_code = cityc.nameToCode(city_name);
                    //city_code=et.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                }
                sendRequestWithHttpURLConnection();

            }

        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();
    }
//连接天气网站，并获取json天气
    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://t.weather.sojson.com/api/weather/city/"+city_code).build();
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

    //解析json形式的天气数据,并调用显示函数
private void parseJSONWithJSONObject(String jsonData){
        try {

            JSONObject jsonObject = new JSONObject(jsonData);

            JSONObject ctiy1 = new JSONObject(jsonObject.getString("cityInfo"));
            String city = ctiy1.getString("city");

            String date= jsonObject.getString("date");

            JSONObject data1 = new JSONObject(jsonObject.getString("data"));
            String shidu = data1.getString("shidu");
            String quality = data1.getString("quality");
            String wendu = data1.getString("wendu");

            try {
                JSONArray jsonArray = new JSONArray(data1.getString("forecast"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    day[i]="";
                    JSONObject j1 = jsonArray.getJSONObject(i);
                    String type = j1.getString("type");
                    String high = j1.getString("high");
                    String low = j1.getString("low");
                    String fl = j1.getString("fl");
                    day[i]+="天气"+type+"\n"+
                            "温度："+low+"~"+high+"\n"+
                            "风级"+fl;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            showResponse("当前城市："+city+"\n"+
                    "日期："+date+"\n"+
                    "湿度："+shidu+"\n"+
                    "空气质量："+quality+"\n"+
                    "温度："+wendu
            );

        }catch (Exception e){
            e.printStackTrace();
        }
}
    //显示天气
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv1.setText(response);

            }
        });
    }

}
