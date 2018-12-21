package com.example.ex_3_weatherforecast;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStreamReader;

public class cityC {
    private String name;
    private String city_code;

    public String getCity_code() {
        return city_code;
    }

    public String getName() {
        return name;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String nameToCode(String name) {

        InputStream is = cityC.this.getClass().getClassLoader().getResourceAsStream("assets/" + "city1.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray infArray = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < infArray.length(); i++) {
                JSONObject inf_Array = infArray.getJSONObject(i);
                if(name.equals(inf_Array.getString("city_name")) ){
                        return inf_Array.getString("city_code");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        try {
            JSONArray jsonArray = new JSONArray(name);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String n1 = jsonObject.getString("city_name");
                if(n1==name){
                    String n2 = jsonObject.getString("city_code");
                    return n2;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return null;
    }
}
