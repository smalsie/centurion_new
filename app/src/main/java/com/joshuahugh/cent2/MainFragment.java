package com.joshuahugh.cent2;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by joshuahugh on 06/03/15.
 */
public class MainFragment  extends AbsFrag {

    private TextView timeLeft, shots, shotsHad, timeRunFor, textTime;
    private TextView drinkCounterLabel;
    private CountDownTimer counter;
    private int count = 0;
    private int no;
    private Handler handler;
    private int minutesToRun = 100;
    private int shotsSet;
    private Context c;
    private Button startButton, bottomButton;
    private int time = 100;
    private boolean running;
    private static NotificationManager mNotificationManager;
    private long delay = 1000; //1 min

    private Vibrator vibrator;
    private MediaPlayer buzzer;

    private int minutes, seconds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer, container, false);

        c = rootView.getContext();

        Bundle bundle = this.getArguments();
        minutesToRun = bundle.getInt("time", 100);

        vibrator = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        shots = (TextView) rootView.findViewById(R.id.shots);
        shotsHad = (TextView) rootView.findViewById(R.id.shotsHad);
        timeRunFor = (TextView) rootView.findViewById(R.id.timeRunFor);
        textTime = (TextView) rootView.findViewById(R.id.textTime);

        textTime.setVisibility(View.VISIBLE);

        shots.setVisibility(View.VISIBLE);
        shotsHad.setVisibility(View.VISIBLE);


        shotsSet = time = minutesToRun;

        minutes = time;
        seconds = 0;

        no = 0;
        running = false;

        shots.setText(getResources().getString(R.string.shots) + " " + time);
        shotsHad.setText(getResources().getString(R.string.shotsHad) + " " + no);

        startButton = (Button) rootView.findViewById(R.id.startButton);
        startButton.setText("Start");

        bottomButton = (Button) rootView.findViewById(R.id.bottomButton);
        bottomButton.setText("Stop");

        String secs = "";

        if(seconds <= 9) {
            secs = "0" + seconds;
        } else {
            secs = "" + seconds;
        }

        textTime.setText(minutes + ":" + secs);

        handler = new Handler();

        buzzer = MediaPlayer.create(rootView.getContext(), R.raw.buzzer);

        startButton.setOnClickListener(new View.OnClickListener() {

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

        bottomButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                handler.removeCallbacks(runnable);

                shots.setText("Finished!");
                shotsHad.setText("You have had " + no + " shots!");
                notification("Finished: ");
                bottomButton.setText("Quit!");
                bottomButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //quit();

                    }

                });

                startButton.setText("Resume?");
                startButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        resume();

                    }

                });


            }

        });

        notification(null);


        getActivity().setTitle(R.string.app_name);
        return rootView;
    }


    private void setUp() {

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

            DBHelper dH = new DBHelper(c);
            SQLiteDatabase db = dH.getReadableDatabase();
            dH.increment(db);

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
                bottomButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //quit();

                    }

                });


                startButton.setText("Restart?");
                startButton.setOnClickListener(new View.OnClickListener() {

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

    private void notification(String s) {

        if(mNotificationManager != null)
            mNotificationManager.cancelAll();

        NotificationCompat.Builder mBuilder =

                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("ACSS Centurion")
                        .setOngoing(true);

        if(s == null)
            mBuilder.setContentText(no + "/" + shotsSet + " Shots Drank!");
        else
            mBuilder.setContentText(s + no + "/" + shotsSet + " Shots Drank!");


        mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());


    }


    @Override
    public void onDestroy() {
        Log.i("YO", "onDestroy called");

        handler.removeCallbacks(runnable);

        if(mNotificationManager != null)
            mNotificationManager.cancelAll();

        //finish();

        super.onDestroy();


      }

    private void resume() {
        handler.postDelayed(runnable, delay);
        startButton.setText("Pause");
        bottomButton.setText("Stop");
        running = true;
        shots.setText(getResources().getString(R.string.shots) + " " + time);
        shotsHad.setText(getResources().getString(R.string.shotsHad) + " " + no);
        startButton.setOnClickListener(new View.OnClickListener() {

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

        bottomButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                handler.removeCallbacks(runnable);

                shots.setText("Finished!");
                shotsHad.setText("You have had " + no + " shots!");
                notification("Finished: ");
                bottomButton.setText("Quit!");
                bottomButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //quit();

                    }

                });

                startButton.setText("Resume?");
                startButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        resume();

                    }

                });


            }

        });

        notification(null);
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

    public void setTime(int time) {

        if(time ==0)
            this.time = 100;
        else
            this.time = time;
    }


    @Override
    public String getName() {
        return "Centurion";
    }
}
