package com.dailyislam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TallyCounterActivity extends AppCompatActivity {
    private TextView counterText;
    private Button resetBtn,countBtn;
    private int counter=0;
    private MediaPlayer mediaPlayer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tally_counter);

        counterText=findViewById(R.id.counter_text);
        resetBtn=findViewById(R.id.reset_btn);
        countBtn=findViewById(R.id.count_btn);
        toolbar=findViewById(R.id.tally_counter_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tally Counter");

        countBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter=counter+1;
                mediaPlayer= MediaPlayer.create(TallyCounterActivity.this,R.raw.count);
                mediaPlayer.start();
                counterText.setText(""+counter);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter=0;
                mediaPlayer= MediaPlayer.create(TallyCounterActivity.this,R.raw.reset);
                mediaPlayer.start();
                counterText.setText(""+counter);
            }
        });
    }
}
