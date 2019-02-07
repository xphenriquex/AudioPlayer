package com.exemplo.android.audioplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private TextView txtFinalTime;
    private TextView txtInicialTime;
    private Handler myHandler = new Handler();
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
        txtFinalTime = findViewById(R.id.txt_final_time);
        txtInicialTime = findViewById(R.id.txt_inicial_time);

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
                    if((segundosInicial - mintoSegInicial) < 10){
                        segundos = "0" + String.valueOf(segundosInicial - mintoSegInicial);
                    }else{
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
            if((segundosInicial - mintoSegInicial) < 10){
                segundos = "0" + String.valueOf(segundosInicial - mintoSegInicial);
            }else{
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
}
