package com.dailyislam;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GetNearBY extends AsyncTask<Object, String, String> {
    private static int v=0;
    Context context;
    GetNearBY(Context context){
        this.context=context;
    }
    private GoogleMap googleMap;
    private String url;
    private InputStream is;
    private BufferedReader br;
    private StringBuilder stringBuilder;
    private  String data;


    @Override
    protected String doInBackground(Object... objects) {
        googleMap=(GoogleMap) objects[0];
        url=(String) objects[1];

        try {
            URL myUrl=new URL(url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)myUrl.openConnection();
            httpURLConnection.connect();
            is=httpURLConnection.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));
            String line="";
            stringBuilder=new StringBuilder();
            while((line=br.readLine())!=null){
                stringBuilder.append(line);
            }
            data=stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parentObject=new JSONObject(s);
            JSONArray resultArray=parentObject.getJSONArray("results");
            v=resultArray.length();
            for (int i=0;i<resultArray.length();i++) {

                JSONObject jsonObject=resultArray.getJSONObject(i);
                JSONObject locationObj=jsonObject.getJSONObject("geometry").getJSONObject("location");
                String lat=locationObj.getString("lat");
                String lng=locationObj.getString("lng");
                JSONObject nameobj=resultArray.getJSONObject(i);
                String name_res=nameobj.getString("name");
                String vicinity=nameobj.getString("vicinity");
                String icon=nameobj.getString("icon");

                LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,13);
                googleMap.animateCamera(update);

                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.title(name_res+" , "+vicinity);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);

                // Toast.makeText(context, name_res+vicinity, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}