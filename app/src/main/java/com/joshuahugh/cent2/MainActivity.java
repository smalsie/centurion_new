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

    private TextView timeLeft, shots, shotsHad, timeRunFor, textTime;
    private TextView drinkCounterLabel;
    private CountDownTimer counter;
    private Vibrator vibrator;
    private MediaPlayer buzzer;
    private EditText startTime;
    private int minutesToRun;
    private int shotsSet;
    private int time = 0;
    private int count = 0;
    private int no;
    private static Handler handler;
    private Button startButton, bottomButton;
    private boolean running;
    private static NotificationManager mNotificationManager;
    private long delay = 1000; //1 min

    private int minutes, seconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        buzzer = MediaPlayer.create(this, R.raw.buzzer);

        shots = (TextView) findViewById(R.id.shots);
        shotsHad = (TextView) findViewById(R.id.shotsHad);

        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.startButton);
        bottomButton = (Button)findViewById(R.id.bottomButton);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startTime = (EditText) findViewById(R.id.startTime);
                if(startTime.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a time!", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(startTime.getText().toString()) <= 0 ){

                    Toast.makeText(getApplicationContext(), "Please enter in a time grater than 0 minutes!", Toast.LENGTH_SHORT).show();

                } else {
                    minutesToRun = Integer.parseInt(startTime.getText().toString());

                    setUp();

                }

            }

        });

        bottomButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                testBuzzer();

            }

        });

    }

    private void setUp() {
        shots = (TextView) findViewById(R.id.shots);
        shotsHad = (TextView) findViewById(R.id.shotsHad);
        timeRunFor = (TextView) findViewById(R.id.timeRunFor);
        textTime = (TextView) findViewById(R.id.textTime);

        textTime.setVisibility(View.VISIBLE);

        shots.setVisibility(View.VISIBLE);
        shotsHad.setVisibility(View.VISIBLE);

        startTime.setVisibility(View.GONE);
        timeRunFor.setVisibility(View.GONE);

        startButton.setText("Start");

        shotsSet = time = minutesToRun;

        minutes = time;
        seconds = 0;

        no = 0;
        running = false;

        shots.setText(getResources().getString(R.string.shots) + " " + time);
        shotsHad.setText(getResources().getString(R.string.shotsHad) + " " + no);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setText("Start");

        bottomButton = (Button) findViewById(R.id.bottomButton);
        bottomButton.setText("Stop");

        String secs = "";

        if(seconds <= 9) {
            secs = "0" + seconds;
        } else {
            secs = "" + seconds;
        }

        textTime.setText(minutes + ":" + secs);

        handler = new Handler();

        buzzer = MediaPlayer.create(this, R.raw.buzzer);

        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                notification(null);

                if(!running) {
                    handler.postDelayed(runnable, delay);
                    startButton.setText("Pause");
                    running = true;
                } else {
                    handler.removeCallbacks(runnable);

                    notification("Paused: ");

                    startButton.setText("Resume");
                    running = false;
                }



            }

        });

        bottomButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                handler.removeCallbacks(runnable);

                shots.setText("Finished!");
                shotsHad.setText("You have had " + no + " shots!");
                notification("Finished: ");
                bottomButton.setText("Quit!");
                bottomButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        quit();

                    }

                });

                startButton.setText("Resume?");
                startButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        resume();

                    }

                });


            }

        });

        notification(null);

    }


    private void notification(String s) {

        if(mNotificationManager != null)
            mNotificationManager.cancelAll();

        NotificationCompat.Builder mBuilder =

                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("ACSS Centurion")
                        .setOngoing(true);

        if(s == null)
            mBuilder.setContentText(no + "/" + shotsSet + " Shots Drank!");
        else
            mBuilder.setContentText(s + no + "/" + shotsSet + " Shots Drank!");


        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());


    }

    private boolean buzz() {
        count++;

        seconds--;
        if(seconds < 0) {
            seconds = 59;
            minutes --;


        }

        String secs = "";

        if(seconds <= 9) {
            secs = "0" + seconds;
        } else {
            secs = "" + seconds;
        }

        textTime.setText(minutes + ":" + secs);

        if(count == 60) {

            count = 0;
            buzzer.start();

            if(vibrator.hasVibrator()) {
                vibrator.vibrate(500);
            }

            time--;
            no++;

            notification(null);


            if(time > 0) {
                shots.setText(getResources().getString(R.string.shots) + " " + time);

                shotsHad.setText(getResources().getString(R.string.shotsHad) + " " + no);
                return true;
            }else {

                shots.setText("Finished: ");
                shotsHad.setText("You have had " + no + " shots!");

                bottomButton.setText("Quit!");
                notification("Finished ");
                bottomButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        quit();

                    }

                });


                startButton.setText("Restart?");
                startButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        setUp();

                    }

                });

                return false;
            }

        }

        return true;
    }

    public void setTextViews(){
        timeLeft.setText("Stopped");
        drinkCounterLabel.setText("Stopped");
    }

    public String getMillis(long milliseconds){
        String hms = String.format("%02d",

                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return hms;
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
            quit();

        }
        return super.onKeyDown(keyCode, event);
    }

    private void quit() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure you want to quit?")
                .setMessage("Quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        handler.removeCallbacks(runnable);

                        NotificationManager notificationManager;
                        notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();

                        //first = true;

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onDestroy() {
        Log.i("YO", "onDestroy called");

        handler.removeCallbacks(runnable);

        if(mNotificationManager != null)
            mNotificationManager.cancelAll();

        finish();

        super.onDestroy();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
			/* do what you need to do */



            if(buzz()) {
				/* and here comes the "trick" */
                handler.postDelayed(this, delay);

            }

        }
    };

    private void resume() {
        handler.postDelayed(runnable, delay);
        startButton.setText("Pause");
        bottomButton.setText("Stop");
        running = true;
        shots.setText(getResources().getString(R.string.shots) + " " + time);
        shotsHad.setText(getResources().getString(R.string.shotsHad) + " " + no);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                notification(null);

                if(!running) {
                    handler.postDelayed(runnable, delay);
                    startButton.setText("Pause");
                    running = true;
                } else {
                    handler.removeCallbacks(runnable);

                    notification("Paused: ");

                    startButton.setText("Resume");
                    running = false;
                }



            }

        });

        bottomButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                handler.removeCallbacks(runnable);

                shots.setText("Finished!");
                shotsHad.setText("You have had " + no + " shots!");
                notification("Finished: ");
                bottomButton.setText("Quit!");
                bottomButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        quit();

                    }

                });

                startButton.setText("Resume?");
                startButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        resume();

                    }

                });


            }

        });

        notification(null);
    }
}
