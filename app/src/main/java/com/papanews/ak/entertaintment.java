package com.papanews.ak;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.papanews.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.papanews.R.drawable.ic_baseline_navigate_before_24;
import static com.papanews.R.drawable.ic_baseline_pause_24;
import static com.papanews.R.drawable.ic_baseline_play_arrow_24;
import static com.papanews.ak.App.CHANNEL_ID_1;

public class entertaintment extends Fragment implements CardStackListener {

    private static final String TAG = "Recomended";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    List<ItemModel> items_politics;

    TextView textView,normal;
    LinearLayout submit_report;
    ImageView rewind_view;
    CardStackView cardStackView;
    ImageView imageView,share;
    Drawable drawable;
    List<ItemModel> productFromShared = new ArrayList<>();
    String jsonPreferences;
    SharedPreferences sharedpreferences;
    int view_update = 0;
    int x = 0;
    String share_Tilte;
    int positionchange = 0;


    JSONObject product;
    String hashMapString;
    Drawable drawable2;
    int sh;


    ImageView fl,sav,shr;
    SharedPreferences.Editor editor;

    //Audio player media
    private MediaPlayer mMediaPlayer;
    Button stop, pause,forward;
    ImageView play, playimage;
    TextView title, subtitle;
    int flag, flagflag=0;


    //    notification
    private NotificationManagerCompat notificationManager;
    private MediaSessionCompat mediaSession;
    int notimedia;
    Context context;
    PendingIntent btPendingIntent;
    int playpausenoti = 0;
    int value = 0;



    //    Dynamic product bringing activity here
    private static final String URL_RECOMENDED = "http://192.168.31.131/LoginRegister/getPolitics.php";

    private static final String ServerURL = "http://192.168.31.131/LoginRegister/insertPolitics.php" ;

    TextView news;
    TextView daten;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.item_card, container, false);
        rewind_view = view.findViewById(R.id.rewind);
        textView = view.findViewById(R.id.newTitle);
//        submit_report = view.findViewById(R.id.cv);
        normal = view.findViewById(R.id.nrmlText);
        news = view.findViewById(R.id.nonews);
        daten = view.findViewById(R.id.datenews);

        //        Audio player media
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        playimage = view.findViewById(R.id.playimage);
        title = view.findViewById(R.id.audioTitlle);
        subtitle = view.findViewById(R.id.audioSub);
        forward = view.findViewById(R.id.forward);
        mMediaPlayer = new MediaPlayer();
        title.setSelected(true);

        //        Notification manager
        notificationManager = NotificationManagerCompat.from(getActivity());
        mediaSession = new MediaSessionCompat(getActivity(), "tag");



        if(addList().isEmpty()){
            news.setVisibility(View.VISIBLE);
        }else if(!addList().isEmpty()){
            news.setVisibility(View.GONE);
        }


        imageView = view.findViewById(R.id.doodle);
        drawable  = getResources().getDrawable(R.drawable.entertainmentbgbg);
        imageView.setImageDrawable(drawable);
        manager = new CardStackLayoutManager(getContext(), this);
        adapter = new CardStackAdapter(addList(), getContext());
        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        manager.setTranslationInterval(8f);
        manager.setDirections(Direction.VERTICAL);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setCanScrollHorizontal(false);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);



//        submit_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), uploadvideo.class);
//                startActivity(i);
//            }
//        });
//
//        rewind_view.bringToFront();


        rewind_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                RewindAnimationSetting settings = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(10000)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();


                manager = new CardStackLayoutManager(getContext(), entertaintment.this);
                adapter = new CardStackAdapter(addList(), getContext());
                cardStackView = view.findViewById(R.id.card_stack_view);
                cardStackView.setLayoutManager(manager);
                cardStackView.setAdapter(adapter);
                manager.setRewindAnimationSetting(settings);
                manager.setTranslationInterval(8f);
                manager.setDirections(Direction.VERTICAL);
                manager.setStackFrom(StackFrom.Top);
                manager.setVisibleCount(3);
                manager.setScaleInterval(0.95f);
                manager.setSwipeThreshold(0.3f);
                manager.setMaxDegree(20.0f);
                manager.setCanScrollHorizontal(false);
                manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                cardStackView.rewind();
                rewind_view.setVisibility(View.GONE);
                normal.setVisibility(View.GONE);

            }
        });

        return view;

    }

    private void setDataFromSharedPreferences(List<String> save, String datatype){
        SharedPreferences sharedPref = getContext().getSharedPreferences("savePosts", MODE_PRIVATE);
        editor = sharedPref.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(save);
        editor.putStringSet(datatype, set);
        Log.e("list list ::", String.valueOf(set));
        editor.commit();
    }


    public void sendNotification(int icon, final String title, final String text, String image) {

        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                Intent buttonIntent = new Intent(getActivity(), MediaPlayerService.class);
                buttonIntent.putExtra("pause", 1);
                btPendingIntent = PendingIntent.getBroadcast(getContext(), 1, buttonIntent, 0);


                Notification channel = new NotificationCompat.Builder(getContext(), CHANNEL_ID_1)
                        .setSmallIcon(R.drawable.papanews)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setLargeIcon(bitmap)
                        .addAction(R.drawable.ic_baseline_content_paste_24, "like", null)
                        .addAction(ic_baseline_navigate_before_24, "Play", null)
                        .addAction(ic_baseline_pause_24, "pause", btPendingIntent)
                        .addAction(R.drawable.ic_baseline_navigate_next_24, "next", null)
                        .addAction(R.drawable.ic_baseline_content_paste_24, "dislike", null)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(1, 2, 3))
                        .build();
                notificationManager.notify(1, channel);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });

    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("sender reciever", "Got message: " + message);
        }
    };

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myReceiver);
        super.onDestroyView();
    }




    @SuppressLint("LongLogTag")
    private List<ItemModel> addList(){
        Gson gson = new Gson();
        SharedPreferences sharedPref = getContext().getSharedPreferences("urlData", MODE_PRIVATE);
        jsonPreferences = sharedPref.getString("entertaintment", "");

        Type type = new TypeToken<List<ItemModel>>() {}.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        Log.e("productFromShared sohaib :: ", String.valueOf(jsonPreferences));

        return productFromShared;
    }



    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (manager.getTopPosition() == adapter.getItemCount()) {
            daten.setText("");
            // -------------------- last position reached, do something ---------------------
            rewind_view.setVisibility(View.VISIBLE);
            normal.setVisibility(View.VISIBLE);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                flagflag = 0;
                flag = 0;
                play.setBackgroundResource(ic_baseline_play_arrow_24);
            }
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }


    @Override
    public void onCardAppeared(final View view, final int position) {


        sh = 0;
        flag = 0;
        sav = view.findViewById(R.id.save_card);
        shr = view.findViewById(R.id.share_card);
        share = view.findViewById(R.id.item_image);
        JSONArray array = null;


        try {
            array = new JSONArray(jsonPreferences);
            product = array.getJSONObject(position);
//            Log.e("buhuhuhu :: ", String.valueOf(product));
            daten.setText(product.getString("date"));

            Log.e("buhuhuhu :: ", product.getString("converted"));



            Picasso.get().load(product.getString("image")).placeholder(R.drawable.noimage)
                    .resize(250, 250)
                    .into(playimage);

            title.setText(product.getString("title"));
            subtitle.setText(product.getString("sourcename"));

            global.mMediaPlayer.reset();
            global.mMediaPlayer.setDataSource(product.getString("converted"));
            global.mMediaPlayer.prepare();


            global.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //here you can stop your recording thread.
                    notimedia = 1;
                    cardStackView.swipe();
                }
            });

            if (flagflag == 1) {
                play.setBackgroundResource(ic_baseline_pause_24);
                global.mMediaPlayer.start();
            } else if (flagflag == 0) {
                play.setBackgroundResource(ic_baseline_play_arrow_24);
            }


//            notification manager
            if (notimedia == 1) {
                notimedia = 0;
                try {
                    sendNotification(ic_baseline_pause_24, product.getString("title"),
                            product.getString("sourcename"),
                            product.getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notimedia = 1;
                    cardStackView.swipe();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    Log.e("gfdsfsfs one :: ", String.valueOf(flag));
                    if (global.mMediaPlayer.isPlaying()) {
                        try {
                            sendNotification(ic_baseline_play_arrow_24, product.getString("title"),
                                    product.getString("sourcename"),
                                    product.getString("image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        global.mMediaPlayer.pause();
                        flagflag = 0;
//                        draw = getResources().getDrawable(ic_baseline_play_arrow_24);
//                        play.setImageDrawable(draw);
                        play.setBackgroundResource(ic_baseline_play_arrow_24);

                    } else {
                        try {
                            sendNotification(ic_baseline_pause_24, product.getString("title"),
                                    product.getString("sourcename"),
                                    product.getString("image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        flag = 1;
                        flagflag = 1;
//                        draw = getResources().getDrawable(ic_baseline_pause_24);
                        play.setBackgroundResource(ic_baseline_pause_24);
                        global.mMediaPlayer.start();
                    }
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    Log.e("gfdsfsfs :: ", String.valueOf(flag));
                    if (flag == 1) {
                        global.mMediaPlayer.reset();
                        try {
                            play.setBackgroundResource(ic_baseline_pause_24);
                            global.mMediaPlayer.setDataSource(product.getString("converted"));
                            global.mMediaPlayer.prepare();
                            global.mMediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        flag = 0;
                        flagflag = 1;
                    } else if (flag == 0) {
                        cardStackView.rewind();
                    }
                }
            });


            checkIfSaved(product.getString("image"),product.getString("title"),product.getString("longd")
                    ,product.getString("shortd"),product.getString("sourceimage"),
                    product.getString("sourcename"),product.getString("Views"),
                    product.getString("LongText"),product.getString("audioType"),product.getString("video"));


            sav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,String> myMap1 = new HashMap<String, String>();
                    try {
                        myMap1.put("image", product.getString("image"));
                        myMap1.put("title", product.getString("title"));
                        myMap1.put("shortDesc", product.getString("shortd"));
                        myMap1.put("id", product.getString("longd"));
                        myMap1.put("sourceImage", product.getString("sourceimage"));
                        myMap1.put("sourceName", product.getString("sourcename"));
                        myMap1.put("sourceViews", product.getString("Views"));
                        myMap1.put("longDesc", product.getString("LongText"));
                        myMap1.put("videoId", product.getString("video"));
                        myMap1.put("audio", product.getString("audioType"));
                        Log.e("check CHECK :: ", String.valueOf(hashMapString));

                        if(global.myMap.contains(myMap1)){
                            drawable2 = getResources().getDrawable(R.drawable.bookwhite);
                            sav.setImageDrawable(drawable2);
                            Toast.makeText(getActivity(), "Remove saved", Toast.LENGTH_SHORT).show();
//                            sav.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            global.myMap.remove(myMap1);
                            Gson gson = new Gson();
                            hashMapString = gson.toJson(global.myMap);
                            Log.e("myMap :: ", String.valueOf(global.myMap));
                            Log.e("position :: ", String.valueOf(jsonPreferences));
                            SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                            prefs.edit().putString("savedData", hashMapString).apply();
                            prefs.edit().putInt("bookmark", sh).apply();
                            Log.e("storing data :: ", String.valueOf(hashMapString));
                        }else{
                            sh = 2;
                            drawable2 = getResources().getDrawable(R.drawable.bookmark);
                            sav.setImageDrawable(drawable2);
//                            sav.setBackgroundColor(Color.parseColor("#F4931D"));
                            Toast.makeText(getActivity(), "Saving first time", Toast.LENGTH_SHORT).show();
                            global.myMap.add(myMap1);
                            Log.e("Lappas save :: ", String.valueOf(global.myMap));
                            Gson gson = new Gson();
                            hashMapString = gson.toJson(global.myMap);
                            Log.e("myMap :: ", String.valueOf(global.myMap));
                            Log.e("position :: ", String.valueOf(jsonPreferences));
                            SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                            prefs.edit().putString("savedData", hashMapString).apply();
                            prefs.edit().putInt("bookmark", sh).apply();
                            Log.e("storing data :: ", String.valueOf(hashMapString));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            shr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Share Share", Toast.LENGTH_SHORT).show();

                    BitmapDrawable drawable = (BitmapDrawable)share.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);

                    Uri uri = Uri.parse(bitmapPath);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    try {
                        intent.putExtra(Intent.EXTRA_TEXT,product.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            });


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getActivity().getSharedPreferences("passScreen", MODE_PRIVATE);//Frequent to get SharedPreferences need to add a step getActivity () method
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    editor.putString("shdisc", product.getString("shortd"));
                    editor.putString("title", product.getString("title"));
                    editor.putString("srcName", product.getString("sourcename"));
                    editor.putString("srcImage", product.getString("sourceimage"));
                    editor.putString("longDisc", product.getString("LongText"));
                    editor.putString("video_youtube", product.getString("video"));
                    editor.putString("image", product.getString("image"));
                    editor.putString("audioty", product.getString("audioType"));
                    editor.putString("id", product.getString("longd"));
                    editor.putString("audio", product.getString("audioType"));
                    editor.putString("converted", product.getString("converted"));

                    share_Tilte = product.getString("title");

                    view_update = Integer.parseInt(product.getString("Views")) + 1;
                    editor.putString("srcViews", String.valueOf(view_update));
                    addDataToDatabase(String.valueOf(view_update),product.getString("image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("position :: ", String.valueOf(jsonPreferences));
                SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                prefs.edit().putString("savedData", hashMapString).apply();
                Log.e("sohaib sohaib :: ", String.valueOf(hashMapString));
                editor.commit();
                Intent i = new Intent(getActivity(), FullscreenActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);


            }
        });

    }
    private void checkIfSaved(String image, String title,String id, String shdiscprition, String source_image, String source_name,
                              String source_views, String long_disc,String audio,String videoOfYOUTUBE){
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
        Log.e("checking myMap :: ", String.valueOf(myMap1));
        Log.e("checking global :: ", String.valueOf(global.myMap));


        if(global.myMap.contains(myMap1)){
            Log.e("cgecking checking :: ", "Yess done done");
            drawable2 = getResources().getDrawable(R.drawable.bookmark);
            sav.setImageDrawable(drawable2);


        }else{
            drawable2 = getResources().getDrawable(R.drawable.bookwhite);
            sav.setImageDrawable(drawable2);

        }

    }


    private void addDataToDatabase(final String Views, final String imageId) {

        String url = "http://papanews.in/PapaNews/updateViews/viewsEntertain.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(
                Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("sourceViews", Views);
                params.put("image", imageId);
                return params;
            }
        };

        queue.add(request);
    }




    @Override
    public void onCardDisappeared(View view, int position) {

    }



}
