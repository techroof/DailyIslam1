package com.dailyislam;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class SurahDetailsAdapter extends RecyclerView.Adapter<SurahDetailsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SurahDetails> surahDetailsList;
    private int isPLAYING;
    MediaPlayer mp;
    int lenght = 0;
    int pid, currentAudio;
    private ProgressDialog pd;

    public SurahDetailsAdapter(Context context, ArrayList<SurahDetails> surahDetailsList) {
        this.context = context;
        this.surahDetailsList = surahDetailsList;
    }

    @NonNull
    @Override
    public SurahDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.surah_details_layout, parent, false);
        pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        SurahDetailsAdapter.ViewHolder viewHolder = new SurahDetailsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurahDetailsAdapter.ViewHolder holder, int position) {
        SurahDetails currentItem = surahDetailsList.get(position);
        String ayat = currentItem.getAyat();
        String ayatAudio = currentItem.getAyatAudioLink();
        holder.surahAyat.setText(String.valueOf(ayat));
        isPLAYING = 0;

        holder.ayatPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                if (isPLAYING==0){
                    try {
                        isPLAYING = 1;
                        mp = new MediaPlayer();
                        mp.setDataSource(ayatAudio);
                        mp.prepare();
                        if (surahDetailsList.get(position).getNumber() != pid) {
                            lenght = 0;
                        }
                        mp.seekTo(lenght);
                        pid = surahDetailsList.get(position).getNumber();
                        mp.start();
                        pd.dismiss();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                lenght = 0;
                                isPLAYING = 0;
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }else {
                    pd.dismiss();
                    isPLAYING = 0;
                    if (surahDetailsList.get(position).getNumber() == pid) {
                        lenght = mp.getCurrentPosition();
                        mp.pause();
                    } else {
                        mp.reset();
                        onClick(view);
                    }

                }
            }
        });
        }

    @Override
    public int getItemCount() {
        return surahDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView surahAyat;
        public ImageView ayatPlayBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahAyat = itemView.findViewById(R.id.surah_ayat);
            ayatPlayBtn = itemView.findViewById(R.id.ayat_play_btn);
        }
    }
}
