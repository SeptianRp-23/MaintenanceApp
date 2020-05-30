package com.azis.skripsiproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.UserActivity.Dashboard.DashboardActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    //user
    private static final String PREF_NAME = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String LOGED = "IS_LOGED";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String LEVEL = "LEVEL";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession (String name, String email, String level, String id){
        editor.putBoolean(LOGIN, true);
        editor.putBoolean(LOGED, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(LEVEL, level);
        editor.putString(ID, id);
        editor.apply();
    }

//    public void createSessionAdmin (String name, String email, String level, String id){
//        editor.putBoolean(LOGED, true);
//        editor.putString(NAME, name);
//        editor.putString(EMAIL, email);
//        editor.putString(LEVEL, level);
//        editor.putString(ID, id);
//        editor.apply();
//    }


    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public boolean isLogin2(){
        return sharedPreferences.getBoolean(LOGED, false);
    }


    public void checkLogin(){
        if (!this.isLogin()){
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((DashboardActivity) context).finish();
        }

//        if (LEVEL.equals("user")){
//            this.isLogin();
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//            ((HomeActivity)context).finish();
//        }
//        else if(LEVEL.equals("admin")){
//            this.isLogin2();
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//            ((HomeAdmin)context).finish();
//        }
    }

    public void checkLogin2(){
        if (!this.isLogin2()){
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((DashboardActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }
//
//    public void logout(){
//        editor.clear();
//        editor.commit();
//        Intent intent = new Intent(context, LoginActivity.class);
//        context.startActivity(intent);
//        ((Read_Profile)context).finish();
//    }
}
