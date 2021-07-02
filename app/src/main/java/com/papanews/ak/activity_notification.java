package com.papanews.ak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.papanews.R;

import java.util.Calendar;

import pl.droidsonroids.gif.GifDecoder;
import pl.droidsonroids.gif.GifImageView;

public class activity_notification extends AppCompatActivity {

    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar;
    private String format = "";
    RelativeLayout visi;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    StringBuilder gettime;
    TextView check;
    LinearLayout show;
    int checkIf;
    String timeof,timeofnew;
    String showingtime;

    GifImageView bell;
    RadioButton bt1,bt2;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manager);
        relativeLayout = findViewById(R.id.relat);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
//        timePicker1.setIs24HourView(true);
        time = (TextView) findViewById(R.id.textView1);
        calendar = Calendar.getInstance();
        check = (TextView) findViewById(R.id.fal);
        show = findViewById(R.id.shownot);
        bt1 = findViewById(R.id.not);
        bt2 = findViewById(R.id.regular);
        bell = (GifImageView) findViewById(R.id.bell);


        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        checkIf = sharedpreferences.getInt("reguCustome", 0);
        timeof = sharedpreferences.getString("realTime", "");
        timeofnew = sharedpreferences.getString("showingTime", "");
        Log.e("activitynoti mode :: ", String.valueOf(checkIf));
        Log.e("activitynoti time :: ", String.valueOf(timeof));

        if(checkIf==1){
            show.setVisibility(View.VISIBLE);
            bt1.setChecked(true);
            time.setText(timeofnew);
            editor = sharedpreferences.edit();
            editor.putString("realTime", String.valueOf(gettime));
            editor.putString("showingTime", String.valueOf(showingtime));
            editor.putInt("reguCustome", 1);
            editor.apply();
        }


    }

    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);

    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        showingtime = hour+":"+min+" " + format;

        time.setText(new StringBuilder().append(hour).append(":").append(min)
                .append(" ").append(format));

        gettime = (new StringBuilder().append(hour).append(":").append(min)
                .append(":").append(0));

        Log.e("timeset noti :: ", String.valueOf(gettime));

        global.timenotify = String.valueOf(gettime);

    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.regular:
                if (checked)
                    show.setVisibility(View.GONE);
                    check.setVisibility(View.VISIBLE);
                    bell.setVisibility(View.VISIBLE);
                    editor = sharedpreferences.edit();
                    editor.putInt("reguCustome", 0);
                    editor.apply();
                break;
            case R.id.not:
                if (checked)
                    show.setVisibility(View.VISIBLE);
                    check.setVisibility(View.GONE);
                    bell.setVisibility(View.GONE);
                    editor = sharedpreferences.edit();
                    editor.putString("realTime", String.valueOf(gettime));
                    editor.putString("showingTime", String.valueOf(showingtime));
                    editor.putInt("reguCustome", 1);
                    editor.apply();
                break;
            case R.id.nonoti:
                if (checked)
                    show.setVisibility(View.GONE);
                    check.setVisibility(View.GONE);
                    bell.setVisibility(View.GONE);
                    editor = sharedpreferences.edit();
                    editor.putInt("reguCustome", 2);
                    editor.apply();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity_notification.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


}
