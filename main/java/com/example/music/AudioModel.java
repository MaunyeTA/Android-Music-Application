package com.example.music;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String path;
    String title;
    String duration;
    String album;
    String artist;


    public AudioModel(String path, String title, String duration, String album, String artist) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.album = album;
        this.artist = artist;
    }

    public AudioModel(String path, String title, String duration) {
        this.path = path;
        this.title = title;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }
}
