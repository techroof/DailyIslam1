package com.dailyislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

public class DailyRoutine extends AppCompatActivity {
    private CheckBox[] checkBoxes = new CheckBox[9];
    private String SaveString, matchString, formattedDate;
    private int chkBoxId[] = new int[9];
    private Calendar calendar;
    private Date currentDate;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_routine);

        toolbar=findViewById(R.id.daily_routine_toolbar);
        checkBoxes[0] = findViewById(R.id.fajar_chkbox);
        checkBoxes[1] = findViewById(R.id.duhar_chkbox);
        checkBoxes[2] = findViewById(R.id.asr_chkbox);
        checkBoxes[3] = findViewById(R.id.maghrib_chkbox);
        checkBoxes[4] = findViewById(R.id.isha_chkbox);
        checkBoxes[5] = findViewById(R.id.quran_chkbox);
        checkBoxes[6] = findViewById(R.id.tasbeeh_one_chkbox);
        checkBoxes[7] = findViewById(R.id.tasbeeh_two_chkbox);
        checkBoxes[8] = findViewById(R.id.tasbeeh_three_chkbox);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily Routine");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = sdf.format(currentDate);

        Paper.init(this);
        for (int i = 0; i < 9; i++) {
            try {
                matchString = Paper.book().read(String.valueOf(checkBoxes[i].getId()));
                if (matchString.equals("Yes" + formattedDate)) {
                    checkBoxes[i].setChecked(true);
                } else {
                    Paper.book().destroy();
                }
            } catch (Exception e) {
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void chkBox(View v) {
        int checkBoxTag = Integer.parseInt(v.getTag().toString());
        CheckBox checkBox = (CheckBox) v;
        chkBoxId[checkBoxTag] = checkBox.getId();

        if (checkBox.isChecked()) {
            SaveString = "Yes";
            Paper.book().write(String.valueOf(chkBoxId[checkBoxTag]), SaveString + formattedDate);

        } else {
            SaveString = "No";
            Paper.book().write(String.valueOf(chkBoxId[checkBoxTag]), SaveString + formattedDate);
        }

    }
}
