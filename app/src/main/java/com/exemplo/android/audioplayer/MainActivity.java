package com.exemplo.android.audioplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
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
    private ImageButton btnPlayPause;
    private SeekBar seekBar;
    private TextView txtFinalTime;
    private TextView txtInicialTime;
    private Handler myHandler = new Handler();
    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private static int oneTimeOnly = 0;
    private AudioManager mAudioManager;
    private Boolean mPlayOnAudioFocus;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener =
            new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.e("AudioFocus", "AUDIOFOCUS_GAIN");
                    play();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.e("AudioFocus", "AUDIOFOCUS_LOSS");
                    mAudioManager.abandonAudioFocus(this);
                    pause();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.e("AudioFocus", "AUDIOFOCUS_LOSS_TRANSIENT");
                    if (mp.isPlaying()){
                        pause();
                    }
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.e("AudioFocus", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    mp.setVolume(0.2f, 0.2f);
                    break;

                default:
                    //
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.ate_que_nao_viva_mais_eu);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        ImageButton btnForward = findViewById(R.id.btn_forward);
        ImageButton btnBackWard = findViewById(R.id.btn_backward);
        seekBar = findViewById(R.id.seek_bar);
        txtFinalTime = findViewById(R.id.txt_final_time);
        txtInicialTime = findViewById(R.id.txt_inicial_time);

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mp.isPlaying()) {

                    pause();

                } else {

                    boolean focus = requestAudiofocus(MainActivity.this);

                    if (focus){
                        play();
                    }

                }

            }
        });


        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if( (temp + forwardTime) <= finalTime){
                    startTime += forwardTime;
                    mp.seekTo((int) startTime);
                }
                Log.e("StartTime",temp + "");
            }
        });

        btnBackWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if( (temp - backwardTime) > 0){
                    startTime -= backwardTime;
                    mp.seekTo((int) startTime);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mp != null && fromUser){
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mp.getCurrentPosition();

            long minutosInicial = TimeUnit.MILLISECONDS.
                    toMinutes(
                            (long) startTime
                    );
            long segundosInicial = TimeUnit.MILLISECONDS.
                    toSeconds(
                            (long) startTime
                    );
            long mintoSegInicial = TimeUnit.MINUTES.
                    toSeconds(
                            TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)
                    );

            //Adicionando o zero a esquerda aos segundos
            String segundos = "";
            if ((segundosInicial - mintoSegInicial) < 10) {
                segundos = "0" + String.valueOf(segundosInicial - mintoSegInicial);
            } else {
                segundos = String.valueOf(segundosInicial - mintoSegInicial);
            }

            //Formatando o tempo em m:ss
            txtInicialTime.setText(
                    String.format(
                            Locale.getDefault(),
                            "%d:%s",
                            minutosInicial,
                            segundos
                    )
            );
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private boolean requestAudiofocus(Context context){
        mAudioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        //Request focus for playback
        int result = mAudioManager.requestAudioFocus(
                mOnAudioFocusListener,

                // use the music stram
                AudioManager.STREAM_MUSIC,

                //Request permanent focus
                AudioManager.AUDIOFOCUS_GAIN
        );

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            Log.e("AudioFocus", "Audio focus recebido");
            return true;
        }else{
            Log.e("AudioFocus", "Audio focus não recebido");
            return false;
        }
    }

    private void pause(){
        mp.pause();
        btnPlayPause.setImageResource(R.drawable.play);
    }

    private void stop(){
        mp.stop();
        btnPlayPause.setImageResource(R.drawable.play);
    }

    private void play(){
        mp.start();
        btnPlayPause.setImageResource(R.drawable.pause);

        startTime = mp.getCurrentPosition();
        finalTime = mp.getDuration();

        //Setando maximo do seekBar
        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        //Pegando minutos Total, segundos Total e
        // transformando minutos em segundos

        long minutosFinal = TimeUnit.MILLISECONDS.
                toMinutes(
                        (long) finalTime
                );
        long segundosFinal = TimeUnit.MILLISECONDS.
                toSeconds(
                        (long) finalTime
                );
        long mintoSegFinal = TimeUnit.MINUTES.
                toSeconds(
                        TimeUnit.MILLISECONDS.
                                toMinutes((long) finalTime)
                );

        //Formatando tempo
        String tempoFinal = String.format(
                Locale.getDefault(),
                "%d:%d",
                minutosFinal, segundosFinal - mintoSegFinal
        );

        //Setando tempo final da musica
        txtFinalTime.setText(tempoFinal);


        //Pegando minutos Total, segundos Total e
        // transformando minutos em segundos
        long minutosInicial = TimeUnit.MILLISECONDS.
                toMinutes(
                        (long) startTime
                );

        long segundosInicial = TimeUnit.MILLISECONDS.
                toSeconds(
                        (long) startTime
                );
        long mintoSegInicial = TimeUnit.MINUTES.
                toSeconds(
                        TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)
                );

        //Adicionando o zero a esquerda aos segundos
        String segundos = "";
        if ((segundosInicial  - mintoSegInicial) < 10) {
            segundos = "0" + String.valueOf(segundosInicial - mintoSegInicial);
        } else {
            segundos = String.valueOf(segundosInicial - mintoSegInicial);
        }

        //Formatando tempo em m:ss
        String tempoInicial = String.format(
                Locale.getDefault(),
                "%d:%s",
                minutosInicial,
                segundos
        );

        //Setando tempo inicial da musica
        txtInicialTime.setText(tempoInicial);

        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);

    }

}
