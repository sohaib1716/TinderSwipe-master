package com.papanews.ak;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Adapter extends FragmentPagerAdapter{

    private String tabTitles[] = new String[]{"Profile","Recommended","Technology", "Politics",
            "Business","Startup","Entertaintment","Sports","International","Influences","Miscellaneous"};

    public Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Profile();
            case 1:
                return new Recomended();
            case 2:
                return new Technology();
            case 3:
                return new Politics();
            case 4:
                return new Business();
            case 5:
                return new Startup();
            case 6:
                return new entertaintment();
            case 7:
                return new sports();
            case 8:
                return new international();
            case 9:
                return new influences();
            case 10:
                return new miscellaneous();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 11;
    }

    public CharSequence getPageTitle(int position){


        return tabTitles[position];

    }

}
