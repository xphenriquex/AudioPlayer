package com.exemplo.android.audioplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    TextView txtForward;
    TextView txtRewind;
    private double startTime = 0;
    private double finalTime = 0;
    private static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.all_about_you);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        seekBar = findViewById(R.id.seek_bar);
        txtForward = findViewById(R.id.txt_forward);
        txtRewind = findViewById(R.id.txt_rewind);

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    mp.pause();
                    btn_play_pause.setImageResource(R.drawable.play);
                } else {
                    mp.start();
                    btn_play_pause.setImageResource(R.drawable.pause);

                    startTime = mp.getCurrentPosition();
                    finalTime = mp.getDuration();

                    long minutos = TimeUnit.MILLISECONDS.toMinutes((long) finalTime);
                    long segundos = TimeUnit.MILLISECONDS.toSeconds((long) finalTime);
                    long mintoSeg = TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime));
                    String result = String.format(Locale.getDefault(), "%d:%d", minutos, segundos - mintoSeg);

                    txtForward.setText(String.format(Locale.getDefault(), "%d:%d", minutos, segundos - mintoSeg));



                }

            }
        });


    }


}
