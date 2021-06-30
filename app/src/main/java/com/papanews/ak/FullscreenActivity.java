package com.papanews.ak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.papanews.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullscreenActivity extends AppCompatActivity {
    ScrollView scrollview;
    RelativeLayout createvideo,createvideo2;
    ImageView imgback;
    Button rmore, cta;
    TextView discription, minidescription, headline_title, src_name, src_name2, Long_description, head2, mini, vi, vi2;

    int cc = 0;
    int muteaud=0;
    String videoURL = "https://theanantkaal.com/wp-content/uploads/2021/05/video1.mp4";

    YouTubePlayerView youTubePlayerView;

    YouTubePlayer youTubeView;

//    View customUiView = youTubePlayerView.inflateCustomPlayerUi(R.layout.activity_main);

    Button btn;
    YouTubePlayerTracker tracker;
    /////////////////////////////////////////////////////////////////////
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTextViewState;
    ImageView srcicon, srcicon2;

    String videoOfYOUTUBE;
    String videoId;

    boolean ts = true;
    TextToSpeech t1;
    Spinner lang;
    float ytd = 0;
    String audio;
    int bookm;

    ImageView save_data, share_data, tempo;
    ImageView save_data2, share_data2;
    String hashMapString;
    Drawable drawable2;
    Drawable drawable3;
    String hashMapString2;

    List<ItemModel> productFromShared = new ArrayList<>();
    String jsonPreferences;
    Map<String, String> sharedMap = new HashMap<String, String>();
    Gson gson = new Gson();

//    voice
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fullscreen);
        scrollview = findViewById(R.id.scrollview);
        vi = findViewById(R.id.view);
        vi2 = findViewById(R.id.view2);
        createvideo = findViewById(R.id.cv);
        createvideo2 = findViewById(R.id.cv2);
        imgback = findViewById(R.id.backimg);
        rmore = findViewById(R.id.rmore);
        discription = findViewById(R.id.discription);
        cta = findViewById(R.id.cta);
        minidescription = findViewById(R.id.minidis);
        headline_title = findViewById(R.id.headline);
        srcicon = findViewById(R.id.icon_val);
        srcicon2 = findViewById(R.id.icon_val2);
        src_name = findViewById(R.id.pname);
        src_name2 = findViewById(R.id.pname2);
        Long_description = findViewById(R.id.discription2);
        head2 = findViewById(R.id.headline2);
        mini = findViewById(R.id.minidis2);
        save_data = findViewById(R.id.save);
        save_data2 = findViewById(R.id.save2);
        share_data = findViewById(R.id.share);
        share_data2 = findViewById(R.id.share2);
        tempo = findViewById(R.id.temp);

        //slider
         Slidr.attach(this);
        mMediaPlayer = new MediaPlayer();


        SharedPreferences prefs2 = getSharedPreferences("saveDataPush", MODE_PRIVATE);
        jsonPreferences = prefs2.getString("savedData", "");
        bookm = prefs2.getInt("bookmark", 0);


        SharedPreferences prefs = this.getSharedPreferences("passScreen", Context.MODE_PRIVATE);
        final String shdiscprition = prefs.getString("shdisc", null);
        final String title = prefs.getString("title", null);
        final String source_image = prefs.getString("srcImage", null);
        final String source_name = prefs.getString("srcName", null);
        final String source_views = prefs.getString("srcViews", null);
        final String long_disc = prefs.getString("longDisc", null);
        final String image = prefs.getString("image", null);
        final String id = prefs.getString("id", null);
        final String converted = prefs.getString("converted", null);



        audio = prefs.getString("audioty", null);
        videoOfYOUTUBE = prefs.getString("video_youtube", null);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        Log.e("shdiscprition :: ", String.valueOf(videoOfYOUTUBE));
        initYouTubePlayerView(videoOfYOUTUBE);

        final String sv = String.valueOf((Integer.parseInt(source_views)-1));


        checkIfSaved(image,title,id,shdiscprition,source_image,source_name,sv,long_disc);


        Picasso.get().load(image).placeholder(R.drawable.sample1)
                .resize(350, 350)
                .into(tempo);


        share_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullscreenActivity.this, "Share Share", Toast.LENGTH_SHORT).show();

                BitmapDrawable drawable = (BitmapDrawable) tempo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);

                Uri uri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, title);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        share_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullscreenActivity.this, "Share Share", Toast.LENGTH_SHORT).show();

                BitmapDrawable drawable = (BitmapDrawable) tempo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);

                Uri uri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, title);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        save_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Map<String, String> myMap1 = new HashMap<String, String>();
                myMap1.put("image", image);
                myMap1.put("title", title);
                myMap1.put("shortDesc", shdiscprition);
                myMap1.put("id", id);
                myMap1.put("sourceImage", source_image);
                myMap1.put("sourceName", source_name);
                myMap1.put("audio", audio);
                myMap1.put("sourceViews", (sv));
                myMap1.put("longDesc", long_disc);
                myMap1.put("videoId", videoOfYOUTUBE);
                Log.e("check CHECK :: ", String.valueOf(hashMapString));
                if(global.myMap.contains(myMap1)){
                    drawable2 = getResources().getDrawable(R.drawable.bookwhite);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
                    Toast.makeText(FullscreenActivity.this, "Remove saved", Toast.LENGTH_SHORT).show();
//                    save_data2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    save_data.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    global.myMap.remove(myMap1);
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }else{
                    drawable2 = getResources().getDrawable(R.drawable.bookmark);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    save_data2.setBackgroundColor(Color.parseColor("#F4931D"));
//                    save_data.setBackgroundColor(Color.parseColor("#F4931D"));
                    Toast.makeText(FullscreenActivity.this, "Saving first time", Toast.LENGTH_SHORT).show();
                    global.myMap.add(myMap1);
                    Log.e("Lappas save :: ", String.valueOf(global.myMap));
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }


            }
        });



        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Map<String, String> myMap1 = new HashMap<String, String>();
                myMap1.put("image", image);
                myMap1.put("title", title);
                myMap1.put("shortDesc", shdiscprition);
                myMap1.put("id", id);
                myMap1.put("sourceImage", source_image);
                myMap1.put("sourceName", source_name);
                myMap1.put("audio", audio);
                myMap1.put("sourceViews", (sv));
                myMap1.put("longDesc", long_disc);
                myMap1.put("videoId", videoOfYOUTUBE);
                Log.e("check CHECK :: ", String.valueOf(hashMapString));

                if(global.myMap.contains(myMap1)){
                    drawable2 = getResources().getDrawable(R.drawable.bookwhite);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
                    Toast.makeText(FullscreenActivity.this, "Remove saved", Toast.LENGTH_SHORT).show();
//                    save_data2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    save_data.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    global.myMap.remove(myMap1);
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }else{
                    drawable2 = getResources().getDrawable(R.drawable.bookmark);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    save_data2.setBackgroundColor(Color.parseColor("#F4931D"));
//                    save_data.setBackgroundColor(Color.parseColor("#F4931D"));
                    Toast.makeText(FullscreenActivity.this, "Saving first time", Toast.LENGTH_SHORT).show();
                    global.myMap.add(myMap1);
                    Log.e("Lappas save :: ", String.valueOf(global.myMap));
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }
            }
        });


        vi.setText(source_views);
        vi2.setText(source_views);
        minidescription.setText(shdiscprition);
        mini.setText(shdiscprition);
        headline_title.setText(title);
        head2.setText(title);
        Long_description.setText(long_disc);

        Picasso.get().load(source_image).placeholder(R.drawable.sample1)
                .resize(150, 150)
                .into(srcicon);
        Picasso.get().load(source_image).placeholder(R.drawable.sample1)
                .resize(150, 150)
                .into(srcicon2);

        src_name.setText(source_name);
        src_name2.setText(source_name);

        if(audio.equals("1")){

            try {
                mMediaPlayer.setDataSource(converted);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }

//        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    t1.setLanguage(new Locale("hi", "IND"));
//                    Set<String> a = new HashSet<>();
//                    a.add("male");//here you can give male if you want to select male voice.
//                    Voice v = new Voice("en-us-x-sfg#male_2-local", new Locale("hi", "IND"), 400, 200, true, a);
//                    t1.setVoice(v);
//                    t1.setSpeechRate(0.8f);
//                }
//            }
//        });
//        lang = findViewById(R.id.lang);
//        String[] items = new String[]{"English", "Hindi"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_items, items);
//        lang.setAdapter(adapter);
//        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                scrollview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//                if (position == 1) {
//                    TranslateAPI translateAPI = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, Long_description.getText().toString());
//                    translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
//                        @Override
//                        public void onSuccess(String translatedText) {
//                            Long_description.setText(translatedText);
//                            if (audio.equals("1")) {
////                                    Toast.makeText(FullscreenActivity.this, "bol", Toast.LENGTH_SHORT).show();
//                                t1.speak(Long_description.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
//                            }
////                            T2S = new TextToSpeech(FullscreenActivity.this.getApplicationContext(), FullscreenActivity.this, "com.google.android.tts");
////                            Set<String> a=new HashSet<>();
////                            a.add("male");//here you can give male if you want to select male voice.
////                            Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("hi","IND"),400,200,true,a);
////                            T2S.setVoice(v);
////                            T2S.setSpeechRate(0.8f);
//                        }
//
//                        @Override
//                        public void onFailure(String ErrorText) {
//                            Toast.makeText(FullscreenActivity.this, "Failed to translate", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    TranslateAPI translateAPI1 = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, minidescription.getText().toString());
//                    translateAPI1.setTranslateListener(new TranslateAPI.TranslateListener() {
//                        @Override
//                        public void onSuccess(String translatedText) {
//                            minidescription.setText(translatedText);
//
//                        }
//
//                        @Override
//                        public void onFailure(String ErrorText) {
//                            Toast.makeText(FullscreenActivity.this, "Failed to translate", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    TranslateAPI translateAPI2 = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, mini.getText().toString());
//                    translateAPI2.setTranslateListener(new TranslateAPI.TranslateListener() {
//                        @Override
//                        public void onSuccess(String translatedText) {
//                            mini.setText(translatedText);
//
//                        }
//
//                        @Override
//                        public void onFailure(String ErrorText) {
//                            Toast.makeText(FullscreenActivity.this, "Failed to translate", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    TranslateAPI translateAPI3 = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, headline_title.getText().toString());
//                    translateAPI3.setTranslateListener(new TranslateAPI.TranslateListener() {
//                        @Override
//                        public void onSuccess(String translatedText) {
//                            headline_title.setText(translatedText);
//
//                        }
//
//                        @Override
//                        public void onFailure(String ErrorText) {
//                            Toast.makeText(FullscreenActivity.this, "Failed to translate", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    TranslateAPI translateAPI4 = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, head2.getText().toString());
//                    translateAPI4.setTranslateListener(new TranslateAPI.TranslateListener() {
//                        @Override
//                        public void onSuccess(String translatedText) {
//                            head2.setText(translatedText);
//
//                        }
//
//                        @Override
//                        public void onFailure(String ErrorText) {
//                            Toast.makeText(FullscreenActivity.this, "Failed to translate", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    if (audio.equals("1")) {
////                            Toast.makeText(FullscreenActivity.this, "bol", Toast.LENGTH_SHORT).show();
//                        minidescription.setText(shdiscprition);
//                        mini.setText(shdiscprition);
//                        headline_title.setText(title);
//                        head2.setText(title);
//                        Long_description.setText(long_disc);
//                        t1.speak(Long_description.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }
//
//                //Toast.makeText(FullscreenActivity.this, "Selected", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });




//        Youtube player view

        ///////////////////////////////////////
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        mTextViewState = findViewById(R.id.text_view_state);
//        Button buttonExpand = findViewById(R.id.button_expand);
//        Button buttonCollapse = findViewById(R.id.button_collapse);
        rmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cc == 0) {
                    cc = 1;
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    cc = 0;
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
//
//        buttonCollapse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        mTextViewState.setText("Collapsed");
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
//                        mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
//                        mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
//                        mTextViewState.setText("Settling...");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                mTextViewState.setText("Sliding...");
            }
        });



        cta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker = new YouTubePlayerTracker();
                youTubeView.addListener(tracker);
                Log.e("dataaaaaa1", String.valueOf(tracker.getState()));
                Log.e("dataaaaaa2", String.valueOf(tracker.getCurrentSecond()));
                Log.e("dataaaaaa3", String.valueOf(tracker.getVideoDuration()));

            }
        });


        cta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lapaas.com/"));
                startActivity(intent);
            }
        });

//        rmore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                scrollview.scrollToDescendant(discription);
//                scrollToView(scrollview, discription);
//            }
//        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(FullscreenActivity.this, Recomended.class));
                onBackPressed();


            }
        });
        createvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullscreenActivity.this, uploadvideo.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                muteaud =1;
                mMediaPlayer.stop();
            }
        });

        createvideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullscreenActivity.this, uploadvideo.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                muteaud =1;
                mMediaPlayer.stop();
            }
        });

        scrollview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkIfSaved(String image, String title,String id, String shdiscprition, String source_image, String source_name,
                              String source_views, String long_disc){
        Map<String, String> myMap1 = new HashMap<String, String>();
        myMap1.put("image", image);
        myMap1.put("title", title);
        myMap1.put("shortDesc", shdiscprition);
        myMap1.put("id", id);
        myMap1.put("sourceImage", source_image);
        myMap1.put("sourceName", source_name);
        myMap1.put("audio", audio);
        myMap1.put("sourceViews", (source_views));
        myMap1.put("longDesc", long_disc);
        myMap1.put("videoId", videoOfYOUTUBE);
        Log.e("local myMap :: ", String.valueOf(myMap1));
        Log.e("local global :: ", String.valueOf(global.myMap));


        if(global.myMap.contains(myMap1)){
            Log.e("local checking :: ", "Yess yess");
            drawable2 = getResources().getDrawable(R.drawable.bookmark);
            save_data.setImageDrawable(drawable2);
            save_data2.setImageDrawable(drawable2);

        }else{
            drawable2 = getResources().getDrawable(R.drawable.bookwhite);
            save_data.setImageDrawable(drawable2);
            save_data2.setImageDrawable(drawable2);
        }

    }



    private void initYouTubePlayerView(final String video) {
//        youTubePlayerView.inflateCustomPlayerUi(R.layout.activity_fullscreen);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(0)
                .rel(0)
                .ivLoadPolicy(3)
                .ccLoadPolicy(1)
                .build();

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                setPlayNextVideoButtonClickListener(youTubePlayer);

//                YouTubePlayerUtils.loadOrCueVideo(
//                        youTubePlayer, getLifecycle(),
//                        "3pX8ZNp5Y_k",0f
//                );
                youTubePlayer.loadVideo(video, 0);
                if (audio.equals("1")) {
//                    Toast.makeText(FullscreenActivity.this, "vmute", Toast.LENGTH_SHORT).show();
//                    youTubePlayer.setVolume(0);
                    youTubePlayer.mute();
                }
//                YouTubePlayerTracker tracker = new YouTubePlayerTracker();
//////                youTubePlayer.addListener(tracker);

//


                youTubePlayer.addListener(new YouTubePlayerListener() {
                    @Override
                    public void onReady(@NotNull YouTubePlayer youTubePlayer) {

                    }

                    @Override
                    public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {

                    }

                    @Override
                    public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {
                        if (v > (ytd - 1)) {
                            youTubePlayer.seekTo(0);
                        }if(muteaud ==1){
                            youTubePlayer.mute();
//                            mMediaPlayer.pause();
                        }
                        Log.e("yyyy", "cs " + v);
                    }

                    @Override
                    public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {
                        ytd = v;
                        Log.e("yyyyyyyy", "ytd " + ytd);
                    }

                    @Override
                    public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

                    }

                    @Override
                    public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

                    }
                });

            }
        });
    }


    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    public void onPause() {
//        if (t1 != null) {
//            t1.stop();
//        t1.shutdown();
            mMediaPlayer.stop();
//        }
        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        hideSystemUI();
    }


    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
    }
}