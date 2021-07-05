package com.papanews.ak;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class global {
    public static final List<Map<String , String>> myMap = new ArrayList<Map<String,String>>();

    public static final List<Map<String , String>> notificationArray = new ArrayList<Map<String,String>>();

    public static final List<String> cat_set = new ArrayList<String>();

    public static final List<String> userlogged = new ArrayList<String>();

    public static final List<Integer> priority = new ArrayList<>();

    public static String date = "";
    public static String timenotify = "";

    public static String hindilang = "";

    public static int logincheck = 0;

//  notification mediplayer
    public static int playpause = 0;

    public static MediaPlayer mMediaPlayer = new MediaPlayer();




    public static String convertHindi(String data){
        final TranslateAPI translateAPI = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, data);
        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                hindilang = translatedText;
                Log.e("hellohello h", hindilang);
            }
            @Override
            public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
            }
        });

        return hindilang;
    }
}
