package com.example.music;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.ViewHolder>{
    private ArrayList<PlayListClass.SinglePlayList> playLists;
    private LayoutInflater layoutInflater;
    private Context context;
    public Home homeFragment;
    public PlaylistRecyclerViewAdapter(Context context, ArrayList<PlayListClass.SinglePlayList> playLists) {
        this.layoutInflater = LayoutInflater.from(context);
        this.playLists = playLists;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.song, parent, false);
        return new PlaylistRecyclerViewAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(PlaylistRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.textView.setText(playLists.get(position).getName());
        holder.txtArtist.setText(playLists.get(position).GetAllSongs().size() + " songs");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                byte[] image = getArt(songsList.get(position).getPath());
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(image != null){
//                            Glide.with(context).asBitmap().load(image).into(holder.imgIcon);
//                        }else{
//                            Glide.with(context).asBitmap().load(R.drawable.musicicon).into(holder.imgIcon);
//                        }
//                    }
//                });
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, PlayerActivity.class);
//                intent.putExtra("position", position);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView txtArtist;
        ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.songIcon);
            textView = itemView.findViewById(R.id.songTittle);
            txtArtist = itemView.findViewById(R.id.songArtist);
        }
    }

    private byte[] getArt(String uri)  {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        } catch (Exception e) {
            return null;
        }
    }

}
