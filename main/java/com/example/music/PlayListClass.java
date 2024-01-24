package com.example.music;

import java.util.ArrayList;

public class PlayListClass{
    private ArrayList<SinglePlayList> allPlayLists = new ArrayList<>();

    public PlayListClass(){

    }

    public int GetSongNum(){
        return allPlayLists.size();
    }

    public void AddPlayList(SinglePlayList singlePlayList){
        allPlayLists.add(singlePlayList);
    }

    public SinglePlayList GetPlayList(int position){
        return allPlayLists.get(position);
    }

    public ArrayList<SinglePlayList> GetAllPlayList(){
        return allPlayLists;
    }

    public static class SinglePlayList{
        private String name;
        private ArrayList<AudioModel> playList = new ArrayList<>();
        public SinglePlayList(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int GetSongNum(){
            return playList.size();
        }

        public void AddSong(AudioModel audioModel){
            playList.add(audioModel);
        }

        public AudioModel GetSong(int position){
            return playList.get(position);
        }

        public ArrayList<AudioModel> GetAllSongs(){
            return playList;
        }
    }
}
