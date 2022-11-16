package com.example.gp;

import static com.example.gp.Activity_song.context;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Readapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<String> title;
    private ArrayList<String> singer;
    private ArrayList<String> img;

    ArrayList<String> storetitle, storesinger;

    public Readapter(ArrayList<String> ttitle, ArrayList<String> tsinger){
        title = new ArrayList<>();
        singer = new ArrayList<>();
        img = new ArrayList<>();

        storetitle = ttitle;
        storesinger = tsinger;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.song_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(context, view, storetitle, storesinger);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        System.out.println(img.get(position));
        holder.title.setText(title.get(position));
        holder.singer.setText(singer.get(position));
        Glide.with(context).load(img.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount(){
        return title.size();
    }

    public void setArrayData(String strData, String strData2, String strData3){
        title.add(strData);
        singer.add(strData2);
        img.add(strData3);
    }

}
