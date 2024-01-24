package com.example.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private HomeInterfaces homeInterfaces;
    private ArrayList<AudioModel> songList;

    public interface HomeInterfaces{
        ArrayList<AudioModel> LoadSongs();
    }

    public static Home getInstance(){
        return new Home();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof HomeInterfaces){
            homeInterfaces = (HomeInterfaces) context;
        }else{
            throw new ClassCastException(context.toString() + " Must implement HomeInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_home, container,false);
        recyclerView = view.findViewById(R.id.recyclerViewSongs);
        setup();
        return view;
    }

    private void setup(){
        if(homeInterfaces != null){
            songList = homeInterfaces.LoadSongs();
            if(songList != null){
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                RecylerViewAdapter recylerViewAdapter = new RecylerViewAdapter(getActivity(), songList);
                recylerViewAdapter.homeFragment = this;
                recyclerView.setAdapter(recylerViewAdapter);
            }
        }
    }
}
