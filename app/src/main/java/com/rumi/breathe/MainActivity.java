package com.rumi.breathe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.rumi.breathe.util.Prefs;
import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private TextView breathsTxt, timeTxt, sessionTxt, guideTxt;
    private Button startBtn;

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageView);
        breathsTxt = (TextView) findViewById(R.id.breatheTakentxt);
        timeTxt = (TextView) findViewById(R.id.lasttxt);
        sessionTxt = (TextView) findViewById(R.id.todayMinutesTxt);
        guideTxt = (TextView) findViewById(R.id.guideTxt);
        prefs = new Prefs(this);

        introAnimation();

        sessionTxt.setText(MessageFormat.format("{0} min today", prefs.getSessions()));
        breathsTxt.setText(MessageFormat.format("{0} Breaths", prefs.getBreaths()));
        timeTxt.setText(prefs.getDate());


        startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

    }

    private void introAnimation(){
        ViewAnimator
                .animate(guideTxt)
                .scale(0, 1)
                .duration(1500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTxt.setText("Breathe");
                    }
                })
                .start();
    }

    private void startAnimation(){
        ViewAnimator
                .animate(image)
                .alpha(0, 1)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTxt.setText("Inhale... Exhale");
                    }
                })
                .decelerate()
                .duration(1000)
                .thenAnimate(image)
                .scale(0.02f, 1.5f, 0.02f)
                .rotation(360)
                .repeatCount(6)
                .accelerate()
                .duration(5000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        guideTxt.setText("Good Job");
                        image.setScaleX(1.0f);
                        image.setScaleY(1.0f);

                        prefs.setSessions(prefs.getSessions() + 1);
                        prefs.setBreaths(prefs.getBreaths() + 1);
                        prefs.setDate(System.currentTimeMillis());

                        //refresh activity
                        new CountDownTimer(2000, 1000) {

                            @Override
                            public void onTick(long l) {
                                //put code to show ticking... 1, 2, 3...
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }.start();
                    }
                })
                .start();
    }
}
