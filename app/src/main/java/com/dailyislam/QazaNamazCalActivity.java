package com.dailyislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QazaNamazCalActivity extends AppCompatActivity {
    private TextView qazaUrmiDaysText,totalUrmiQaza;
    private EditText ageET,startNamazET,balighET,dobEt;
    private Button calBtn;
    private String dob,baligh;
    private int umriYears,umriDays;
    private Toolbar mToolbar;
    private String dateStr=null;
    private long elapsedDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaza_namaz_cal);

        qazaUrmiDaysText=findViewById(R.id.qaza_umri_days_tv);
        dobEt=findViewById(R.id.dob_et);
        balighET=findViewById(R.id.baligh_age_et);
        calBtn=findViewById(R.id.calculate_umri_qaza_btn);
        totalUrmiQaza=findViewById(R.id.total_qaza_umri_days_tv);

        mToolbar = findViewById(R.id.qaza_umri_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Umri Qaza Calculator");

        /////////////////---------- DOB PICKER ----------///////////////

        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                int month=monthOfYear+1;
                dateStr=dayOfMonth+"/"+month+"/"+year;


                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate = null;
                try {
                    startDate = myFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date endDate = null;
                endDate =Calendar.getInstance().getTime();

                long different = endDate.getTime() - startDate.getTime();
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;
                dobEt.setText(dateStr);

            }

        };

        dobEt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(QazaNamazCalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baligh=balighET.getText().toString();
                dob=dobEt.getText().toString();
                long totalQaza=elapsedDays*5;

                if (!TextUtils.isEmpty(dob) &&
                        !TextUtils.isEmpty(baligh)){

                    qazaUrmiDaysText.setText(elapsedDays+" Days");
                    totalUrmiQaza.setText(totalQaza+"");
                    Toast.makeText(QazaNamazCalActivity.this,
                            ""+dateStr, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
