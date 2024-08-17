package com.example.musicbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btnPlay,btnNext, btnPrevious, btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar seekMusicBar;
    BarVisualizer barVisualizer;
//    BlastVisualizer blastVisualizer;
    ImageView imageView;
    String songName;
    public  static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    Thread updateSeekBar;

    @Override
    public  boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Music");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnFastBackward=findViewById(R.id.btnFastBackward);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPre);
        btnFastForward = findViewById(R.id.btnFastForward);
        txtSongName = findViewById(R.id.txtSong);
        txtSongStart = findViewById(R.id.txtSongStart);
        txtSongEnd = findViewById(R.id.txtSongEnd);
        seekMusicBar = findViewById(R.id.seekBar);
        barVisualizer = findViewById(R.id.wave);
        imageView = findViewById(R.id.imgView);

        if(mediaPlayer!=null){
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs= (ArrayList) bundle.getParcelableArrayList("songs");
        String sName = bundle.getString("songname");
        position = bundle.getInt("pos");
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        txtSongName.setText(songName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateSeekBar();

        String endTime=createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final  Handler handler = new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String curr=createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(curr);
                handler.postDelayed(this, delay);
                if(txtSongStart.getText().toString().equals(txtSongEnd.getText().toString())){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    position=(position+1)%mySongs.size();
                    Uri uri = Uri.parse(mySongs.get(position).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    songName = mySongs.get(position).getName();
                    txtSongName.setText(songName);
                    mediaPlayer.start();

                    startAnimation(imageView,360f);
                    btnPlay.setBackgroundResource(R.drawable.baseline_pause_24);
                    updateSeekBar();
                }
            }
        },delay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.baseline_play_arrow_24);
                } else {
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.baseline_pause_24);
                    TranslateAnimation animation = new TranslateAnimation(-25, 25, -25, 25);
                    animation.setDuration(600);
                    animation.setFillEnabled(true);
                    animation.setFillAfter(true);
                    animation.setRepeatMode(Animation.REVERSE);
                    animation.setRepeatCount(1);
                    imageView.startAnimation(animation);
                }
            }
        });

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            barVisualizer.setAudioSessionId(audioSessionId);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=(position+1)%mySongs.size();
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();

                startAnimation(imageView,360f);

                updateSeekBar();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position-1<0) {
                    position = mySongs.size() - 1;
                } else {
                    position--;
                }
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();

                startAnimation(imageView, -360f);

                updateSeekBar();
            }
        });

//        btnPrevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                if (position - 1 < 0) {
//                    position = mySongs.size() - 1;
//                } else {
//                    position--;
//                }
//                Uri uri = Uri.parse(mySongs.get(position).toString());
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//                songName = mySongs.get(position).getName();
//                txtSongName.setText(songName);
//                mediaPlayer.start();
//                startAnimation(imageView, -360f);
//            }
//        });

        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });

        btnFastBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setBackgroundResource(R.drawable.baseline_play_arrow_24);
            }
        });
    }

    public void startAnimation(View view, Float degree){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f,degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public  String createTime(int duration){
        String time="";
        int min=duration/1000/60;
        int sec=duration/1000%60;
        time=time+min+" : ";
        if (sec < 10) {
            time = time + "0" + sec;
        } else {
            time = time + sec;
        }
        return time;
    }


    public void updateSeekBar(){
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekMusicBar.setProgress(currentPosition);
//                        txtSongStart.setText(String.format("%d:%d", (currentPosition / 1000) / 60, (currentPosition / 1000) % 60));
                    } catch (Exception ee){}
                }
            }
        };

        seekMusicBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusicBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.MULTIPLY);
        seekMusicBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_IN);

        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    mediaPlayer.seekTo(progress);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
}