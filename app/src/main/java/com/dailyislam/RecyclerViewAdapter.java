package com.dailyislam;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Surah> surahLst;


    public RecyclerViewAdapter(Context context, ArrayList<Surah> surahLst) {
        this.context = context;
        this.surahLst = surahLst;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.surah_list_view,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Surah currentItem=surahLst.get(position);
        String name=currentItem.getEngName();
        String nameArabic=currentItem.getNameArabic();
        int number=currentItem.getNumber();
        holder.surahNum.setText(String.valueOf(number));
        holder.surahName.setText(name);
        holder.surahNameArabic.setText(nameArabic);

        holder.surahNameArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent surahDetails=new Intent(context,SurahDetailsActivity.class);
                surahDetails.putExtra("ayat_number",String.valueOf(number));
                context.startActivity(surahDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return surahLst.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView surahName,surahNum,surahNameArabic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahName=itemView.findViewById(R.id.surah_name);
            surahNum=itemView.findViewById(R.id.surah_num);
            surahNameArabic=itemView.findViewById(R.id.surah_name_arabic);
        }
    }
}
