package com.example.kiube9.aplikasi_chating_budiluhur;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kiube9 on 10/27/2016.
 */

public class IntroManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;


    public IntroManager(Context context){
        this.context = context;
        pref=context.getSharedPreferences("first",0);
        editor = pref.edit();
    }
    public void setFirst(boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public  boolean Check(){

        return  pref.getBoolean("check",true);
    }
}
