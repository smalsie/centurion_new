package com.joshuahugh.cent2;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by joshuahugh on 06/03/15.
 */
public class TimerActivity extends FragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmentManagerr adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);

        Intent intent = getIntent();
        int time = intent.getIntExtra("time", 100);

        Toast.makeText(getApplicationContext(), time + "", Toast.LENGTH_LONG).show();

        ViewPager pager = (ViewPager) findViewById(R.id.myViewPager);
        pager.setAdapter(new FragmentManagerr(getSupportFragmentManager(), time));

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);


        tabs.setViewPager(pager);

    }

    private void quit() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure you want to quit?")
                .setMessage("Quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //MainFragment.quit();


                        NotificationManager notificationManager;
                        notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();

                        //first = true;

                        //c.finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
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




}
