package com.dailyislam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.IslamicCalendar;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private String url = "https://muslimsalat.com/islamabad.json?key=98f4b6e50d167581103542ea378fdb49";
    private String tag_json_obj = "json_obj_req", nextPrayer;
    private String prayerNames[] = new String[5];
    private Notification notification;

    private long silentTotalTime=900000,silentStartTime=0,silentEndTime=0;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private ListView listView;
    private TextView fajrTimeTv, duharTimeTv, asrTimeTv, maghribTimeTv, ishaTimeTv;
    private static final long START_TIME = 60000;
    private TextView prayerTimerTv;
    private CountDownTimer countDownTimer,silentTimer;
    private boolean timerRunning;
    String currentTime, currentDatePrayer;
    private long timeLeft, currentTimeInMilliseconds, fajrTimeMS, dhuhrTimeMS, asrTimeMS,
            magribTimeMS, ishaTimeMS;
    private CircleImageView prayerIcon[] = new CircleImageView[5];
    private Date prayerDate, currentDate;
    private int i = 0;
    private final String channelId="id";
    private final int notificationId=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fajrTimeTv = findViewById(R.id.fajr_time_tv);
        duharTimeTv = findViewById(R.id.zuhr_time_tv);
        asrTimeTv = findViewById(R.id.asar_time_tv);
        maghribTimeTv = findViewById(R.id.magrib_time_tv);
        ishaTimeTv = findViewById(R.id.isha_time_tv);
        prayerTimerTv = findViewById(R.id.prayer_timer_text);

        mToolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerIndicatorEnabled(true);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle("Daily Islam");

        setNavigationViewListener();
        notification=new Notification(this);

        prayerIcon[0] = findViewById(R.id.fajr_red);
        prayerIcon[1] = findViewById(R.id.zuhr_red);
        prayerIcon[2] = findViewById(R.id.asr_red);
        prayerIcon[3] = findViewById(R.id.magrib_red);
        prayerIcon[4] = findViewById(R.id.isha_red);

        prayerTimer();
    }

    private void prayerTimer() {

        if (i <= 4) {
           // Toast.makeText(this, "sjdksj: " + i, Toast.LENGTH_SHORT).show();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                currentDatePrayer = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();
                                prayerNames[0] = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                                prayerNames[1] = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                                prayerNames[2] = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                                prayerNames[3] = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                                prayerNames[4] = response.getJSONArray("items").getJSONObject(0).get("isha").toString();
                            }catch (Exception e){

                            }

                            fajrTimeTv.setText(prayerNames[0]);
                            duharTimeTv.setText(prayerNames[1]);
                            asrTimeTv.setText(prayerNames[2]);
                            maghribTimeTv.setText(prayerNames[3]);
                            ishaTimeTv.setText(prayerNames[4]);

                            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                            Date date = null;
                            try {
                                date = parseFormat.parse(prayerNames[i]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            ///////------------ Current Time -----------///////////
                            Calendar c = Calendar.getInstance();
                            String formattedDate = dateFormat.format(c.getTime());

                            ///////------------ Prayer Time -----------///////////
                            String strDate = currentDatePrayer + " " + displayFormat.format(date);

                            try {
                                prayerDate = dateFormat.parse(strDate);
                                currentDate = dateFormat.parse(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            long difference = prayerDate.getTime() - currentDate.getTime();
                            if(i==0) {
                                prayerIcon[i].setBackgroundResource(R.drawable.red_icon);
                                prayerIcon[i].setVisibility(View.VISIBLE);
                            }else if (i==1){
                                prayerIcon[i - 1].setBackgroundResource(R.drawable.red_icon);
                                prayerIcon[i - 1].setVisibility(View.VISIBLE);
                            }else{
                                prayerIcon[i - 2].setBackgroundResource(R.drawable.red_icon);
                                prayerIcon[i - 2].setVisibility(View.VISIBLE);
                            }
                            if(prayerDate.getTime()==currentDate.getTime() ||
                                prayerDate.getTime()-60000==currentDate.getTime() ||
                                prayerDate.getTime()+60000==currentDate.getTime()){
                                //silentStartTime=prayerDate.getTime();
                                //silentEndTime=silentStartTime+silentTotalTime;
                                silentTimer();
                            }
                            if (difference < 0) {
                                i = i + 1;
                             //   Toast.makeText(MainActivity.this, "d: " +
                               //         difference, Toast.LENGTH_SHORT).show();
                                prayerTimer();
                            } else {
                                timeLeft = difference;
                                startTimer();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            //timeLeft =currentTimeInMilliseconds;

            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } else {
            i = 0;
            prayerTimer();
        }
    }


    private void setNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.qibla_direction:
                Intent qibla = new Intent(MainActivity.this, QiblaDirectionActivity.class);
                startActivity(qibla);
                break;
            case R.id.nearby_mosque:
                Intent nearbyMosque = new Intent(MainActivity.this, NearbyMosqueActivity.class);
                startActivity(nearbyMosque);
                break;
            case R.id.islamic_calender:
                Intent islCal = new Intent(MainActivity.this, IslamicCalender.class);
                startActivity(islCal);
                break;
            case R.id.tally_counter:
                Intent tallyCounter = new Intent(MainActivity.this, TallyCounterActivity.class);
                startActivity(tallyCounter);
                break;
            case R.id.quran_verses:
                Intent hadith = new Intent(MainActivity.this, DailyHadithActivity.class);
                startActivity(hadith);
                break;

            case R.id.umri_cal:
                Intent umriCal = new Intent(MainActivity.this, QazaNamazCalActivity.class);
                startActivity(umriCal);
                break;

            case R.id.daily_routine:
                Intent dailyRoutine = new Intent(MainActivity.this, DailyRoutine.class);
                startActivity(dailyRoutine);
                break;

            case R.id.quran_surahs:
                Intent surahs = new Intent(MainActivity.this, SurahActivity.class);
                startActivity(surahs);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /////////---------- PRAYER TIMER -----------///////////////

    private void startTimer() {
        if (i <= 4) {
            if (i==0) {
                prayerIcon[i].setBackgroundResource(R.drawable.red_icon);
                prayerIcon[i].setVisibility(View.VISIBLE);
            }
            else{
            prayerIcon[i-1].setBackgroundResource(R.drawable.green_icon);
            prayerIcon[i-1].setVisibility(View.VISIBLE);
            }
        }
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCounterTimer();
            }

            @Override
            public void onFinish() {
                if (i==0) {
                    prayerIcon[i].setBackgroundResource(R.drawable.red_icon);
                    prayerIcon[i].setVisibility(View.VISIBLE);
                }else if (i==1){
                    prayerIcon[i-1].setBackgroundResource(R.drawable.red_icon);
                    prayerIcon[i-1].setVisibility(View.VISIBLE);
                }else{
                    prayerIcon[i-2].setBackgroundResource(R.drawable.red_icon);
                    prayerIcon[i-2].setVisibility(View.VISIBLE);
                    prayerIcon[i].setVisibility(View.INVISIBLE);
                }
                i = i + 1;
                if (i <= 4) {
                    prayerTimer();
                } else {
                    i = 0;
                    prayerTimer();
                }

            }
        }.start();
    }

    private void updateCounterTimer() {
        int days = (int) (timeLeft / (1000 * 60 * 60 * 24));
        int hours = (int) ((timeLeft - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (timeLeft - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int seconds = (int) (timeLeft / 1000) % 60;
        hours = (hours < 0 ? -hours : hours);
        String timeLeftFormatted = String
                .format(Locale.getDefault(),
                        "%02d Hrs: %02d Min: %02d Sec", hours, min
                        , seconds);
        prayerTimerTv.setText("Next Prayer in: " + timeLeftFormatted);
        sendOnChannel2(hours,min,seconds);
    }

    //////////-------- Auto silent ---------/////////
    private void silentTimer(){
        Toast.makeText(this, "Prayer time. Phone silent for 15 minutes", Toast.LENGTH_SHORT).show();
              AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

        silentTimer=new CountDownTimer(silentTotalTime,1000) {
            @Override
            public void onTick(long l) {
                silentTotalTime=l;
             }

            @Override
            public void onFinish() {
                AudioManager am;
                am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(MainActivity.this, "Prayer time ended", Toast.LENGTH_SHORT).show();

            }
        }.start();
    }

    ///////////////------- NOTIFICATION ---------///////////////
    public void sendOnChannel2(int hours,int min,int sec) {
        String time=hours+" Hour(s): "+min+" Minute(s): "+sec+" Second(s)";

        RemoteViews notificationView=new RemoteViews(getPackageName(),R.layout.notification_layout);
        notificationView.setTextViewText(R.id.text_view_collapsed_1,time);
        android.app.Notification.Builder builder=notification.getChannelNotification
                (notificationView);
                //("Next Prayer in:",time);
        builder.setOnlyAlertOnce(true);
        notification.getManager().notify(0,builder.build());
    }

}