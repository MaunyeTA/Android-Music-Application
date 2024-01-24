package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements Home.HomeInterfaces, PlayerActivity.PlayerInterfaces{

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private LinearLayout currentLayout;
    private ImageView playingImage;
    private TextView currentTitle;
    private TextView currentArtist;
    private ImageButton currentPlayPause;
    static ArrayList<AudioModel> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        currentLayout = findViewById(R.id.currentPlaying);
        playingImage = findViewById(R.id.imgCurrentPlaying);
        currentTitle = findViewById(R.id.txtCurrentTitle);
        currentArtist = findViewById(R.id.txtCurrentArtist);
        currentPlayPause = findViewById(R.id.currentPlayPause);
        //currentPlayPause.setImageResource(R.drawable.new_play_rmvbg_24px);

        getTabs();

    }

    boolean checkPermission(){
        int results = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);

        return results == PackageManager.PERMISSION_GRANTED;

    }

    void getPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO)){
            Toast.makeText(MainActivity.this, "Read permission is required, please allow from settings", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 123);
        }
    }

    public void getTabs(){

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        ArrayList<String> tabNames = new ArrayList<>();
        tabNames.add("Songs");
        tabNames.add("Playlist");
        tabNames.add("Library");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager2.setAdapter(viewPagerAdapter);

                        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                            tab.setText(tabNames.get(position));
                        }).attach();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ArrayList<AudioModel> LoadSongs() {

        if(!checkPermission()){
            getPermission();
            return null;
        }

        songsList = getAllAudio(this);

        if(!songsList.isEmpty()){
            songsList.sort(Comparator.comparing(AudioModel::getTitle));
            //SetCurrentPlaying();
            return songsList;
        }else{
            return null;
        }

    }

    public static ArrayList<AudioModel> getAllAudio(Context context){
        ArrayList<AudioModel> audioModels = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String data = cursor.getString(3);
                String artist = cursor.getString(4);

                AudioModel audioModel = new AudioModel(data, title, duration, album, artist);
                Log.e("path : \n" + data + "\n", "album : " + album );
                audioModels.add(audioModel);
            }
            cursor.close();
        }

        return audioModels;
    }

    public static ArrayList<AudioModel> getSongsList() {
        return songsList;
    }

    @Override
    public void onResume() {
        SetCurrentPlaying();
        super.onResume();
    }

    @Override
    public void SetCurrentPlaying() {


        if(PlayerActivity.mediaPlayer != null){
            currentLayout.setVisibility(View.VISIBLE);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            currentTitle.setText(MainActivity.songsList.get(PlayerActivity.position).getTitle());
            currentArtist.setText(MainActivity.songsList.get(PlayerActivity.position).getArtist());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] image = PlayerActivity.getArt(MainActivity.songsList.get(PlayerActivity.position).getPath());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AddImage(image);
                        }
                    });
                }
            });
            currentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPlayPause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(PlayerActivity.mediaPlayer.isPlaying()){
                                PlayerActivity.mediaPlayer.pause();
                            }else{
                                PlayerActivity.mediaPlayer.start();
                            }
                        }
                    });
                    setIntent();
                }
            });
        }else {
            currentLayout.setVisibility(View.GONE);
        }
    }

    public void setIntent(){
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("position", PlayerActivity.position);
        startActivity(intent);
    }

    private void AddImage(byte[] image){
        if(image != null){
            Glide.with(this).asBitmap().load(image).into(playingImage);
        }else{
            Glide.with(this).asBitmap().load(R.drawable.musicicon).into(playingImage);
        }
    }
}