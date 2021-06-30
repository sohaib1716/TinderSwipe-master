package com.papanews.ak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.papanews.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ViewPager viewPager;
    ProgressBar progressBar;
    ImageView nav;
    int check = 0;
    int pre_array = 0;
    int finalcheck;
    int doitnow;
    Bitmap bmp;

    Calendar c;


    List<String> recomend = new ArrayList<String>();
    List<String> send_notification = new ArrayList<String>();
    List<ItemModel> itemsreco = new ArrayList<>();
    TabLayout tabLayout;
    Handler mHandler;
    SharedPreferences prefs;
    JSONArray array;
    int flag = 0;
    SharedPreferences.Editor editor;
    GoogleSignInClient mGoogleSignInClient;
    Button refresh_but;
    Adapter adapter;
    int count = 0;
    int checkNoti;

    String timefinal;
    HorizontalScrollView scrollView;
    ImageView imageView;



    ConstraintLayout relativeLayout;
    private static final String URL_RECOMENDED = "http://192.168.31.131/LoginRegister/getPolitics.php";
    private static final String URL_POLITICS = "http://papanews.in/PapaNews/getPolitics.php";
    private static final String URL_TECHNOLOGY = "http://papanews.in/PapaNews/getTech.php";
    private static final String URL_BUISNESS = "http://papanews.in/PapaNews/getBusiness.php";
    private static final String URL_STARTUP = "http://papanews.in/PapaNews/getStartup.php";
    private static final String URL_ENTERTAIN = "http://papanews.in/PapaNews/getEntertain.php";
    private static final String URL_SPORT = "http://papanews.in/PapaNews/getSports.php";
    private static final String URL_INTERNATIONAL = "http://papanews.in/PapaNews/getIntetrnational.php";
    private static final String URL_INFLUENCE = "http://papanews.in/PapaNews/getInfluence.php";
    private static final String URL_MISCEL = "http://papanews.in/PapaNews/getMiscel.php";
    private Context context;
    String selectedlang;
    RelativeLayout intro;

    @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = findViewById(R.id.prnav);
        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progress);
        relativeLayout = findViewById(R.id.relative);
        intro = findViewById(R.id.intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intro.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        }, 30000);

        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, likhaputti.class);
            intent.putExtra("EXTRA_SESSION_ID", "noi");
            startActivity(intent);
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "notify", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final String user = sharedPreferences.getString("username", "");
        final int checkcheck = sharedPreferences.getInt("checkcheck", 0);
        final String fb_full = sharedPreferences.getString("facebook_fullname", "");
        Log.e("Username fullname :: ", fb_full);
        final String fb_user = sharedPreferences.getString("facebook_username", "");
        final String fb_pass = sharedPreferences.getString("facebook_password", "");
        final String fb_email = sharedPreferences.getString("facebook_email", "");
        final String fb_image = sharedPreferences.getString("facebook_image", "");
        selectedlang = sharedPreferences.getString("selectedlang", "English");
//        Toast.makeText(MainActivity.this, selectedlang, Toast.LENGTH_SHORT).show();


        Log.e("Username Activity :: ", user);
        finalcheck = sharedPreferences.getInt("notify", 0);

        this.mHandler = new Handler();
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor edit = prefs.edit();
        Set<String> set = prefs.getStringSet("recomended", null);
        Set<String> noti_Set = prefs.getStringSet("notification", null);
        final List<String> sample = new ArrayList<String>(set);

        final List<String> noti_sample = new ArrayList<String>(noti_Set);
        recomend = sample;
        send_notification = noti_sample;
        global.cat_set.addAll(send_notification);
        Log.e("catsetcat :: ", String.valueOf(global.cat_set));
        Log.e("Recomended :: ", String.valueOf(recomend));
        Log.e("sample :: ", String.valueOf(noti_sample));

        editor = sharedPreferences.edit();

        //        checking notification
        checkNoti = sharedPreferences.getInt("reguCustome", 0);
        timefinal = sharedPreferences.getString("realTime", "");
        Log.e("checkNotification :: ", String.valueOf(checkNoti));
        Log.e("timeFinal :: ", String.valueOf(timefinal));


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (checkcheck == 3) {
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Log.e("Google photo :: ", String.valueOf(personPhoto));
                Log.e("useruser photo :: ", String.valueOf(personName));
                editor.putString("user_final", personName);
                Log.e("Username google :: ", user);
                addDataToDatabase(personName, personName, personName, personEmail, String.valueOf(personPhoto));
                editor.putString("google_photo", String.valueOf(personPhoto));
                editor.putInt("method", 1);
                editor.putString("google_image", String.valueOf(personPhoto));
            }
            Log.e("Username google :: ", user);
        }

        if (checkcheck == 1) {
            editor.putString("user_final", user);
            Log.e("Username normal :: ", user);
        }
        if (checkcheck == 2) {
            editor.putString("user_final", fb_full);
            Log.e("Username facebook :: ", user);
        }


        editor.putInt("key", 4);
        if (!user.equals("")) {
        }

        if (!fb_full.equals("")) {
            editor.putString("image_fb", fb_image);
        }

        editor.apply();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        updateTimeOnEachSecond();


        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jk = new Intent(MainActivity.this, Profile.class);
                startActivity(jk);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

//        refresh_but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                m_Runnable.run();
//            }
//        });


        m_Runnable.run();
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }


    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            Log.e("Main recomended list ::", String.valueOf(recomend));
            addListReco();
            addListPolitics();
            addListTechnology();
            addListBusiness();
            addListEntertain();
            addListSport();
            addListStartup();
            addListInternational();
            addListMiscell();
            addListInfluence();
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    adapter = new Adapter(getSupportFragmentManager());
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = findViewById(R.id.mainTitletab);
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.getTabAt(1).select();
                }
            }, 2000);

//            Toast.makeText(MainActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            MainActivity.this.mHandler.postDelayed(m_Runnable, 1200000);
        }

    };


    private void addListReco() {
        for (int k = 0; k < recomend.size(); k++) {
            Log.e("recomended array :: ", String.valueOf(recomend.get(k)));
            StringRequest stringRequest = new StringRequest(Request.Method.GET, recomend.get(k),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Map<String, String> notiFirst = new HashMap<String, String>();
                            try {
                                //converting the string to json array object
                                array = new JSONArray(response);

                                Log.e("response :: ", String.valueOf(response));
                                Log.e("response length :: ", String.valueOf(array.length()));
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);
                                    //adding the product to product list

                                    if (selectedlang.equals(product.getString("language"))) {
                                        itemsreco.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date")

                                        ));
                                        setDataFromSharedPreferences(itemsreco, "recomended");
                                    }

                                    Log.e("dhurval final :: ", String.valueOf(product));

                                    notiFirst.put("image", product.getString("image"));
                                    notiFirst.put("title", product.getString("title"));
                                    notiFirst.put("shortDesc", product.getString("shortDesc"));
                                    notiFirst.put("id", product.getString("id"));
                                    notiFirst.put("sourceImage", product.getString("sourceImage"));
                                    notiFirst.put("sourceName", product.getString("sourceName"));
                                    notiFirst.put("sourceViews", product.getString("sourceViews"));
                                    notiFirst.put("longDesc", product.getString("longDesc"));
                                    notiFirst.put("videoId", product.getString("videoId"));
                                    notiFirst.put("audio", product.getString("audioType"));
                                    global.notificationArray.add(notiFirst);

                                    Log.e("notification check :: ", String.valueOf(global.notificationArray));
                                    Log.e("notification length :: ", String.valueOf(global.notificationArray.size()));
                                    Log.e("notification inside :: ",
                                            String.valueOf(global.notificationArray.get(0).get("image")));
                                }


                                //creating adapter object and setting it to recyclerview
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        }

//        if(pre_array != finalcheck && flag==1){
//            Log.e("notification final :: ", String.valueOf(global.notificationArray));
//            flag = 0;
//            pre_array = finalcheck;
//            for(int jk=pre_array;jk<array.length();jk++){
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(MainActivity.this,"notify");
//                builder.setContentTitle(global.notificationArray.get(jk).get("sourceName"));
//                builder.setContentText(global.notificationArray.get(jk).get("sourceImage "));
//                builder.setSmallIcon(R.drawable.toi);
//                builder.setAutoCancel(true);
//
//                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
//                managerCompat.notify(check,builder.build());
//                check++;
//            }
//        }
    }

    int min;
    int hrs;
    int sec;

    public void updateTimeOnEachSecond() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                c = Calendar.getInstance();
                Log.d("myapp", "time changed");
                hrs = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);
                sec = c.get(Calendar.SECOND);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("timetime hours", String.valueOf(hrs));
                        Log.e("timetime min", String.valueOf(min));
                        Log.e("timetime check", hrs + ":" + min + ":" + sec);
//                        txt_hrs.setText(String.valueOf(hrs));
//                        txt_mins.setText(String.valueOf(min));
//                        txt_sec.setText(String.valueOf(sec));

                        if (timefinal.equals(hrs + ":" + min + ":" + sec)) {
                            Log.e("nownow min", "now send notification");
                            doitnow = 1;
                            addListPolitics();
                            addListSport();
                            addListEntertain();
                            addListBusiness();
                            addListTechnology();
                            addListStartup();
                            addListInternational();
                            addListMiscell();
                            addListInfluence();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doitnow = 0;
                                }
                            }, 2000);
                        }

                    }
                });
            }
        }, 0, 1000);
    }

    @SuppressLint("WrongConstant")
    private void notificationFunction(String title, String Short){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this, "notify");
        builder.setContentTitle(title);
        builder.setContentText(Short);
        builder.setBadgeIconType(R.drawable.papanews);
        builder.setSmallIcon(R.drawable.papanews);
        builder.setAutoCancel(true);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(check, builder.build());
        check++;

    }


    private List<ItemModel> addListPolitics() {

        final List<ItemModel> items_politics = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POLITICS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("langlang Activity :: ", selectedlang);
                                Log.e("ididid politics :: ", String.valueOf(product.get("id")));
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));


                                    if (send_notification.contains("politics")){

                                        if(checkNoti==0){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if(checkNoti==1 && doitnow==1){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }
                                setDataFromSharedPreferences(items_politics, "politics");
                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }


    private List<ItemModel> addListTechnology() {
        final List<ItemModel> items_politics = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TECHNOLOGY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("Technology array :: ", String.valueOf(array));
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("language technology :: ", String.valueOf(product.get("language")));

                                if (selectedlang.equals(product.getString("language"))) {

                                    //adding the product to product list
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));


                                    if (send_notification.contains("tech")) {
                                        if (checkNoti == 0) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }

                                setDataFromSharedPreferences(items_politics, "technology");
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListBusiness() {
        final List<ItemModel> items_politics = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_BUISNESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("Technology array :: ", String.valueOf(array));
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                Log.e("language Business :: ", String.valueOf(product.get("language")));


                                if (selectedlang.equals(product.getString("language"))) {
                                    //adding the product to product list
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));

                                    if (send_notification.contains("business")) {
                                        if (checkNoti == 0) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }
                                setDataFromSharedPreferences(items_politics, "business");
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListEntertain() {
        final List<ItemModel> items_politics = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_ENTERTAIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("Technology array :: ", String.valueOf(array));
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                Log.e("language Entertain :: ", String.valueOf(product.get("language")));
                                //adding the product to product list

                                if (selectedlang.equals(product.getString("language"))) {
                                    //adding the product to product list
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));

                                    if (send_notification.contains("entertain")) {
                                        if (checkNoti == 0) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }
                                }

                                setDataFromSharedPreferences(items_politics, "entertaintment");
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListSport() {
        final List<ItemModel> items_politics = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("Technology array :: ", String.valueOf(array));
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("language Sports :: ", String.valueOf(product.get("language")));
                                //adding the product to product list

                                if (selectedlang.equals(product.getString("language"))) {
                                    //adding the product to product list
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")

                                    ));

                                    if (send_notification.contains("sport")) {
                                        if (checkNoti == 0) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }
                                }

                                setDataFromSharedPreferences(items_politics, "sport");
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListStartup() {
        final List<ItemModel> items_politics = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_STARTUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("Technology array :: ", String.valueOf(array));
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("language startup :: ", String.valueOf(product.get("language")));
                                //adding the product to product list

                                if (selectedlang.equals(product.getString("language"))) {
                                    //adding the product to product list
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")

                                    ));

                                    if (send_notification.contains("startup")) {
                                        if (checkNoti == 0) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }
                                }

                                setDataFromSharedPreferences(items_politics, "startup");
                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListInternational() {

        final List<ItemModel> items_politics = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_INTERNATIONAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("langlang Activity :: ", selectedlang);
                                Log.e("ididid :: ", String.valueOf(product.get("id")));
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));


                                    if (send_notification.contains("international")){

                                        if(checkNoti==0){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if(checkNoti==1 && doitnow==1){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }
                                setDataFromSharedPreferences(items_politics, "international");
                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListInfluence() {

        final List<ItemModel> items_politics = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_INFLUENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("langlang Activity :: ", selectedlang);
                                Log.e("ididid :: ", String.valueOf(product.get("id")));
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));


                                    if (send_notification.contains("influence")){

                                        if(checkNoti==0){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if(checkNoti==1 && doitnow==1){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }
                                setDataFromSharedPreferences(items_politics, "influence");
                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListMiscell() {

        final List<ItemModel> items_politics = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_MISCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                Log.e("langlang Activity :: ", selectedlang);
                                Log.e("ididid :: ", String.valueOf(product.get("id")));
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {
                                    items_politics.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date")
                                    ));


                                    if (send_notification.contains("miscell")){

                                        if(checkNoti==0){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if(checkNoti==1 && doitnow==1){
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }
                                setDataFromSharedPreferences(items_politics, "miscell");
                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }




    private void addDataToDatabase(final String fullname, final String username,
                                   final String password, final String email, final String image) {

        String url = "http://papanews.in/PapaNews/googleSignIn.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                if (response.equals("success")) {
                    Toast.makeText(MainActivity.this, "Upload " + response, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "User Already registered", Toast.LENGTH_SHORT).show();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullname);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("image", image);
                return params;

            }
        };

        queue.add(request);
    }


    private void setDataFromSharedPreferences(List<ItemModel> politicsnews, String datatype) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(politicsnews);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("urlData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.e("jsonCurProduct :: ", String.valueOf(jsonCurProduct));

        editor.putString(datatype, jsonCurProduct);
        editor.commit();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finishAffinity();
    }
}
