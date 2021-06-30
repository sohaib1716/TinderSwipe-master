package com.papanews.ak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.papanews.R;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Profile extends Fragment implements CardStackListener {


    private static final String TAG = "Recomended";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    CardStackView cardStackView;
    SharedPreferences sharedPreferences;
    TextView user_name, cat, save_news, about_us, terms,lang,notification_send;
    ImageView imageView;
    TextView logout;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        final SharedPreferences.Editor edit = prefs.edit();

        sharedPreferences = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final String user = sharedPreferences.getString("user_final", "");
        final String user_google = sharedPreferences.getString("user_Google", "");
        final String google_image = sharedPreferences.getString("google_image", "");
        final String image_fb = sharedPreferences.getString("image_fb", "");
        Log.e("Username Profile :: ", user);
        Log.e("google Profile :: ", google_image);

        user_name = view.findViewById(R.id.finalUsername);
        imageView = view.findViewById(R.id.profile_image);
        lang = view.findViewById(R.id.proflang);
        logout = view.findViewById(R.id.logout);
        cat = view.findViewById(R.id.categories);
        save_news = view.findViewById(R.id.savedNews);
        about_us = view.findViewById(R.id.about1);
        terms = view.findViewById(R.id.tanc1);
        notification_send = view.findViewById(R.id.notification_Send);


        user_name.setText(user);

        if (!image_fb.equals("")) {
            Picasso.get().load(image_fb).placeholder(R.drawable.pr)
                    .resize(350, 350)
                    .into(imageView);
        }


        if(!google_image.equals("")){
            Picasso.get().load(google_image).placeholder(R.drawable.pr)
                    .resize(350, 350)
                    .into(imageView);
        }


        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), likhaputti.class);
                intent.putExtra("EXTRA_SESSION_ID", "abtus");
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), likhaputti.class);
                intent.putExtra("EXTRA_SESSION_ID", "tandc");
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences;


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putInt("key", 0);
                editor.remove("user_final");
                editor.remove("google_image");
                editor.remove("image_fb");
                editor.putInt("checkcheck",0);
                editor.apply();

                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);

                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ctegory Activity", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), select_category.class);
                startActivity(i);
            }
        });

        save_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ki = new Intent(getActivity(), saved_posts.class);
                startActivity(ki);
            }
        });

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), likhaputti.class);
                intent.putExtra("EXTRA_SESSION_ID", "lang");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
//                finish();
            }
        });

        notification_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent li = new Intent(getActivity(), activity_notification.class);
                startActivity(li);
            }
        });

//        user_name = view.findViewById(R.id.username_lo);

//        sharedPreferences = getContext().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
//        final String un = sharedPreferences.getString("username", "");
//        user_name.setText(un);

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
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        return view;
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("http://192.168.31.131/LoginRegister/images/poli2",
                "Markonah", "24",
                "Jember", "http://192.168.31.131/LoginRegister/images/poli2",
                "", "", "", "",
                "","",""));
        return items;
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}