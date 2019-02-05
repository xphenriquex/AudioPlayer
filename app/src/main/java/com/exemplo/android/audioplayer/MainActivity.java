package com.exemplo.android.audioplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageButton btn_play_pause;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.all_about_you);
        btn_play_pause = findViewById(R.id.btn_play_pause);

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mp.isPlaying()){
                    mp.pause();
                    btn_play_pause.setImageResource(R.drawable.play);
                }else{
                    mp.start();
                    btn_play_pause.setImageResource(R.drawable.pause);
                }

            }
        });


    }



}
