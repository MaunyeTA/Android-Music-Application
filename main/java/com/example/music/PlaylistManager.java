package com.example.music;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PlaylistManager {

    private static final String PREF_NAME = "MyPlaylistPrefs";
    private static final String KEY_PLAYLIST = "playlist";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PlaylistManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void savePlaylist(PlayListClass playLists){
        String playlistJson = gson.toJson(playLists);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PLAYLIST, playlistJson);
        editor.apply();
    }

    public PlayListClass getPlaylist(){
        String playlistJson = sharedPreferences.getString(KEY_PLAYLIST, "");
        return gson.fromJson(playlistJson, PlayListClass.class);
    }

}
