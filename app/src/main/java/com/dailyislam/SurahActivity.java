package com.dailyislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.ViewDebug;
import android.view.textclassifier.TextLinks;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SurahActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Surah> surahArrayList;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private String url = "http://api.alquran.cloud/v1/surah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah);

        toolbar = findViewById(R.id.surah_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Surahs List");

        recyclerView=findViewById(R.id.surah_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        surahArrayList=new ArrayList<>();
        requestQueue=Volley.newRequestQueue(this);

        ParseJson();

    }

    private void ParseJson() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j = null;
                            String name=null;
                            String number=null;
                            String nameArabic=null;
                            try {
                                j = jsonArray.getJSONObject(i);
                                name = j.getString("englishName");
                                number = j.getString("number");
                                nameArabic=j.getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            surahArrayList.add(new Surah(name, Integer.parseInt(number),nameArabic));

                            adapter = new RecyclerViewAdapter(SurahActivity.this, surahArrayList);
                            recyclerView.setAdapter(adapter);
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

