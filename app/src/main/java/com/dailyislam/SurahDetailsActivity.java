package com.dailyislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurahDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SurahDetailsAdapter adapter;
    private ArrayList<SurahDetails> surahArrayList;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private String url = null, ayatNumber = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_details);

        toolbar = findViewById(R.id.surah_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Surah");

        recyclerView = findViewById(R.id.surah_details_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        surahArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        ayatNumber = getIntent().getStringExtra("ayat_number");


        url = "http://api.alquran.cloud/v1/surah/" + ayatNumber + "/ar.alafasy";

        ParseJson();
    }

    private void ParseJson() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject("data").getJSONArray("ayahs");

                            String v = jsonArray.getJSONObject(0).get("text").toString();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject j = null;
                                String ayat = null;
                                String number = null;
                                String audioLink=null;
                                try {
                                    j = jsonArray.getJSONObject(i);
                                    audioLink = j.getString("audio");
                                    number = j.getString("number");
                                    ayat = j.getString("text");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                surahArrayList.add(new SurahDetails(ayat,audioLink, Integer.parseInt(number)));

                                adapter = new SurahDetailsAdapter(SurahDetailsActivity.this,
                                        surahArrayList);
                                recyclerView.setAdapter(adapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}



