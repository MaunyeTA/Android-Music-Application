package com.example.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayList extends Fragment {

    public static PlayListClass playlists;
    public static PlaylistManager playlistManager;
    private ConstraintLayout constraintLayout;
    private Button btnAddPlaylist;
    private Button btnCancel;
    private Button btnCreate;
    private EditText txtName;
    private RecyclerView recyclerView;
    private boolean blnSetRCV = false;
    public static PlayList getInstance(){
        PlayList playList = new PlayList();
        return playList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        View view = inflater.inflate(R.layout.row_playlist, container,false);

        constraintLayout = view.findViewById(R.id.newPlaylistLayout);
        constraintLayout.setVisibility(View.GONE);
        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist);
        btnCancel = view.findViewById(R.id.btnCancelPlaylist);
        btnCreate = view.findViewById(R.id.btnCreatePlaylist);
        txtName = view.findViewById(R.id.txtPlaylist);
        recyclerView = view.findViewById(R.id.recyclerPlaylist);

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        playlistManager = new PlaylistManager(getContext());
                        playlists = playlistManager.getPlaylist();

                        if(playlists == null){
                            playlists = new PlayListClass();
                            blnSetRCV = false;
                        }else{
                            blnSetRCV = true;
                            RCV();
                        }
                    }
                });
            }
        });

        SetButtons();
        return view;
    }

    private void RCV(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlaylistRecyclerViewAdapter recylerViewAdapter = new PlaylistRecyclerViewAdapter(getActivity(), playlists.GetAllPlayList());
        recyclerView.setAdapter(recylerViewAdapter);
    }

    private void SetButtons(){
        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.GONE);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = String.valueOf(txtName.getText());
                Log.d("TAG", "About to add playlist...........");
                if(strName != null && playlists != null){
                    PlayListClass.SinglePlayList singlePlayList = new PlayListClass.SinglePlayList(strName);
                    Log.d("TAG", "Adding playlist...........");
                    playlists.AddPlayList(singlePlayList);
                    PlayListClass playListClass = new PlayListClass();
                    playListClass.AddPlayList(singlePlayList);
                    playlistManager.savePlaylist(playListClass);
                    Log.d("TAG", "Playlist added...........");
                }

                constraintLayout.setVisibility(View.GONE);
            }
        });
    }

}