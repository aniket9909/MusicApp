package com.example.musicplayerapp;

import static com.example.musicplayerapp.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.service.controls.actions.FloatAction;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    TextView song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextBtn,preBtn,backBtn,shuffleBtn,repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position=-1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    private Handler handler = new Handler();
    private  Thread playThread,prevThread,nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
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
                if (mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }
    @Override
    protected  void onResume(){
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                preBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size()-1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.start();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size()-1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.start();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_play);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else{
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentPosition);
//                        duration_played.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }


    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String second = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":"+second;
        totalNew = minutes + ":"+"0"+second;
        if (second.length() == 1){
            return totalNew;
        }else{
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        listSongs = musicFiles;
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri= uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    public void initViews(){
        song_name=findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationplayed);
        duration_total = findViewById(R.id.durationtotal);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.id_next);
        preBtn  = findViewById(R.id.id_previous);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekbar);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bimap ;
        if (art != null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
            bimap = BitmapFactory.decodeByteArray(art,0,art.length);
            Palette.from(bimap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable @androidx.annotation.Nullable Palette palette) {
                    Palette.Swatch swatch =palette.getDominantSwatch();
                    if(swatch != null){
                        ImageView gradiant = findViewById(R.id.imageViewGrediant);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradiant.setBackgroundResource(R.drawable.gradiant_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),0x00000000});
                        gradiant.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBg);
                        song_name.setTextColor(swatch.getTitleTextColor());
                        artist_name.setTextColor(swatch.getBodyTextColor());
                    }else{
                        ImageView gradiant = findViewById(R.id.imageViewGrediant);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradiant.setBackgroundResource(R.drawable.gradiant_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0x00000000});
                        gradiant.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        mContainer.setBackground(gradientDrawableBg);
                        song_name.setTextColor(Color.WHITE);
                        artist_name.setTextColor(Color.DKGRAY);
                    }
                }
            });
        }else{
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.ic_pause)
                    .into(cover_art);
            ImageView gradiant = findViewById(R.id.imageViewGrediant);
            RelativeLayout mContainer = findViewById(R.id.mContainer);
            gradiant.setBackgroundResource(R.drawable.gradiant_bg);
            mContainer.setBackgroundResource(R.drawable.main_bg);
            song_name.setTextColor(Color.WHITE);
            artist_name.setTextColor(Color.DKGRAY);
        }
    }
}