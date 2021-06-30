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

    RadioButton bt1,bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manager);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        time = (TextView) findViewById(R.id.textView1);
        calendar = Calendar.getInstance();
        check = findViewById(R.id.fal);
        show = findViewById(R.id.shownot);
        bt1 = findViewById(R.id.not);
        bt2 = findViewById(R.id.regular);


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

        if(format.equals("PM")){
            if(hour==1){
                hour=13;
            }
            if(hour==2){
                hour=14;
            }
            if(hour==3){
                hour=15;
            }
            if(hour==4){
                hour=16;
            }
            if(hour==5){
                hour=17;
            }
            if(hour==6){
                hour=18;
            }
            if(hour==7){
                hour=19;
            }
            if(hour==8){
                hour=20;
            }
            if(hour==9){
                hour=21;
            }
            if(hour==10){
                hour=22;
            }
            if(hour==11){
                hour=23;
            }
            if(hour==12){
                hour=24;
            }
        }

        gettime = (new StringBuilder().append(hour).append(":").append(min)
                .append(":").append(0));

        Log.e("gettime :: ", String.valueOf(gettime));

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
                    check.setVisibility(View.GONE);
                    editor = sharedpreferences.edit();
                    editor.putInt("reguCustome", 0);
                    editor.apply();
                break;
            case R.id.not:
                if (checked)
                    show.setVisibility(View.VISIBLE);
                    check.setVisibility(View.GONE);
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
}
