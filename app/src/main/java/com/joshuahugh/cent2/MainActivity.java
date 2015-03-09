package com.joshuahugh.cent2;
/**
 *  The Main Class that is run on startup.
 *  Allows the user to test the buzzer and
 *  set the number of minutes to run for.
 */
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private TextView timeLeft, shots, shotsHad;
    private Vibrator vibrator;
    private MediaPlayer buzzer;
    private EditText startTime;
    private int time = 0;
    private Button submitButton, buzzerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        buzzer = MediaPlayer.create(this, R.raw.buzzer);

        shots = (TextView) findViewById(R.id.shots);
        shotsHad = (TextView) findViewById(R.id.shotsHad);

        setContentView(R.layout.activity_main);

        submitButton = (Button)findViewById(R.id.submitButton);
        buzzerButton = (Button)findViewById(R.id.buzzerButton);
        submitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startTime = (EditText) findViewById(R.id.startTime);
                if(startTime.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a time!", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(startTime.getText().toString()) <= 0 ){

                    Toast.makeText(getApplicationContext(), "Please enter in a time grater than 0 minutes!", Toast.LENGTH_SHORT).show();

                } else {
                    int minutesToRun = Integer.parseInt(startTime.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("time", minutesToRun);
                    startActivity(intent);

                }

            }

        });

        buzzerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                testBuzzer();

            }

        });

    }







    public void testBuzzer(){
        buzzer.start();

        if(vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            //quit();

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }





}
