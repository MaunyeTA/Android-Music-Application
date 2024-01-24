package com.example.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>{

    ArrayList<AudioModel> songs = new ArrayList<>();
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songs, Context context) {
        this.songs = songs;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioModel song = songs.get(position);
        holder.textView.setText(song.getTitle());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.txtTitle);
            imageView = itemView.findViewById(R.id.iconView);
        }

    }

}
