package com.photo.weaterapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final String WEATER_URL="https://samples.openweathermap.org/data/2.5/weather?q=%s,uk&appid=b6907d289e10d714a6e88b30761fae22";
    private EditText editTextCity;
    private TextView textViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCity=findViewById(R.id.editTextCity);
        textViewWeather=findViewById(R.id.textWiewWother);
    }

    public void onClickShowWeater(View view) {
        String city=editTextCity.getText().toString().trim();
        if (!city.isEmpty()){
            DownloadWeaterTask task=new DownloadWeaterTask();
            String url=String.format(WEATER_URL,city);
            task.execute(url);
        }
    }

    private class DownloadWeaterTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url=null;
            HttpURLConnection urlConnection=null;
            StringBuilder result=new StringBuilder();

            try {
                url=new URL(strings[0]);
                InputStream inputStream=urlConnection.getInputStream();
                urlConnection=(HttpURLConnection)url.openConnection();
InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
BufferedReader reader=new BufferedReader(inputStreamReader);
String line =reader.readLine();
while (line!=null){
    result.append(line);
    line=reader.readLine();
}
return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }finally {
if (urlConnection!=null){
    urlConnection.disconnect();
}
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String city=jsonObject.getString("name");
                String temp=jsonObject.getJSONObject("main").getString("temp");
                String descreption=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                String weather=String.format("%s\n Temeratura:&s\nHa Ulice:%s",city,temp,descreption);
                textViewWeather.setText(weather);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
