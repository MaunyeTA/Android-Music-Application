package com.example.music;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.blurry.Blurry;

public class PlayerActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtTitle;
    private TextView txtDuration;
    private TextView txtTimer;
    private TextView txtArtist;
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnRepeate;
    private ImageButton btnPrev;
    private ImageButton btnShuffle;
    private SeekBar seekBar;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    public static int position = -1;
    private Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread, repeateThread, shuffleThread;
    private static int currentPlaying = -1;
    public static int intRepeate = 0;
    public static boolean blnShuffle = false;

    public interface PlayerInterfaces{
        void SetCurrentPlaying();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        setViews();
        getIntentMethod();
        setMetaData();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    txtTimer.setText(formatDuration(mediaPlayer.getCurrentPosition()));
//                    if(mediaPlayer.getCurrentPosition() == Integer.parseInt(songsList.get(position).getDuration()) && intRepeate == 2){
//                        Log.d("TAG", "The end of the song reached");
//                        nextBtnClicked();
//                        return;
//                    }
                }
                handler.postDelayed(this, 500);
            }
        });

    }

    private void setViews(){
        imageView = findViewById(R.id.imageView2);
        txtTitle = findViewById(R.id.textView2);
        txtArtist = findViewById(R.id.textView4);
        txtDuration = findViewById(R.id.textView6);
        txtTimer = findViewById(R.id.textView5);
        btnPlayPause = findViewById(R.id.btnPausePlay);
        btnNext = findViewById(R.id.btnNext);
        btnRepeate = findViewById(R.id.btnRepeate);
        btnPrev= findViewById(R.id.btnPrev);
        btnShuffle= findViewById(R.id.btnShuffle);
        seekBar = findViewById(R.id.seekBar);
    }

    private void getIntentMethod(){
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        this.songsList = MainActivity.getSongsList();
    }

    private void setMetaData(){
        if(songsList != null){

            byte[] image = getArt(songsList.get(position).getPath());
            if(image != null){
                Glide.with(this).asBitmap().load(image).into(imageView);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.musicicon).into(imageView);
            }

            txtTitle.setText(songsList.get(position).getTitle());
            txtArtist.setText(songsList.get(position).getArtist());
            txtDuration.setText(formatDuration(songsList.get(position).getDuration()));

            btnPlayPause.setImageResource(R.drawable.pause_70);
            uri = Uri.parse(songsList.get(position).getPath());

        }
        if(mediaPlayer != null){
            if(currentPlaying != position){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                currentPlaying = position;
            }
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            currentPlaying = position;
        }
        assert songsList != null;
        seekBar.setMax(Integer.parseInt(songsList.get(position).getDuration()) / 1000);
    }

    @Override
    protected void onPostResume() {
        btnPlayThread();
        btnNextThread();
        btnPrevThread();
        btnRepeateThread();
        btnShuffleThread();
        super.onPostResume();
    }

    private void btnRepeateThread() {
        repeateThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnRepeate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        repeateBtnClicked();
                    }
                });
            }
        };
        repeateThread.start();
    }

    private void repeateBtnClicked() {
        if(intRepeate == 0){
            intRepeate = 1;
            btnRepeate.setImageResource(R.drawable.epeat_one_on_24);
        } else if (intRepeate == 1) {
            intRepeate = 2;
            btnRepeate.setImageResource(R.drawable.repeat_on_24);
        }else{
            intRepeate = 0;
            btnRepeate.setImageResource(R.drawable.repeat_24);
        }

        if(mediaPlayer != null){
            mediaPlayer.setLooping(intRepeate == 1);
        }

    }

    private void btnShuffleThread() {
        shuffleBtnClicked();
    }

    private void shuffleBtnClicked(){
    }

    private void btnPrevThread() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(position > 0){
            position--;
        }
        setMetaData();
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 500);
            }
        });
    }

    private void btnNextThread() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        Log.d("TAG", "On to the next song.....");
        if(position < songsList.size()-1){
            position++;
        }
        setMetaData();
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 500);
            }
        });
    }

    private void btnPlayThread() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }
    private void playPauseBtnClicked(){
        if(mediaPlayer.isPlaying()){
            btnPlayPause.setImageResource(R.drawable.play_70);
            mediaPlayer.pause();
            seekBar.setMax(Integer.parseInt(songsList.get(position).getDuration()) / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }else{
            btnPlayPause.setImageResource(R.drawable.pause_70);
            mediaPlayer.start();
            seekBar.setMax(Integer.parseInt(songsList.get(position).getDuration()) / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        txtTimer.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                    }
                    handler.postDelayed(this, 500);
                }
            });
        }
    }

    private static String formatDuration(String strDuration){
        try {
            long mili = Long.parseLong(strDuration);
            long min = TimeUnit.MILLISECONDS.toMinutes(mili);
            long sec = TimeUnit.MILLISECONDS.toSeconds(mili)-TimeUnit.MINUTES.toSeconds(min);
            long hrs = TimeUnit.MINUTES.toHours(min);

            if(hrs>=1){
                Long remMin = TimeUnit.MILLISECONDS.toMinutes(mili)-TimeUnit.HOURS.toMinutes(hrs);
                Long remSec = TimeUnit.MILLISECONDS.toSeconds(mili)-TimeUnit.MINUTES.toSeconds(min);

                return String.format("%d:%02d:%02d", hrs, remMin, remSec);
            }else{
                return String.format("%d:%02d", min, sec);
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
            return "Invalid Duration";
        }
    }

    private static String formatDuration(int Duration){
        try {
            long mili = Duration;
            long min = TimeUnit.MILLISECONDS.toMinutes(mili);
            long sec = TimeUnit.MILLISECONDS.toSeconds(mili)-TimeUnit.MINUTES.toSeconds(min);
            long hrs = TimeUnit.MINUTES.toHours(min);

            if(hrs>=1){
                Long remMin = TimeUnit.MILLISECONDS.toMinutes(mili)-TimeUnit.HOURS.toMinutes(hrs);
                Long remSec = TimeUnit.MILLISECONDS.toSeconds(mili)-TimeUnit.MINUTES.toSeconds(min);

                return String.format("%d:%02d:%02d", hrs, remMin, remSec);
            }else{
                return String.format("%d:%02d", min, sec);
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
            return "Invalid Duration";
        }
    }

    static byte[] getArt(String uri){

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
