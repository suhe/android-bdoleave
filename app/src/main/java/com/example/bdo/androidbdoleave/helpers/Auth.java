package com.example.bdo.androidbdoleave.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by suhe on 16/02/16.
 */
public class Auth {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Auth";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String ID = "employee_id";
    public static final String NAME = "employee_name";
    public static final String EMAIL = "employee_email";


    public Auth(Context context) {
        this._context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void login (boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.commit();
    }

    public void logout() {
        login(false);
    }

    public boolean isLogin() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public void setId(String data) {
        editor.putString(ID,data);
        editor.commit();
    }

    public String getId() {
        return preferences.getString(ID, "DEFAULT");
    }

    public void setName(String data) {
        editor.putString(NAME,data);
        editor.commit();
    }

    public String getName() {
        return preferences.getString(NAME, "DEFAULT");
    }

    public void setEmail(String data) {
        editor.putString(EMAIL,data);
        editor.commit();
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "DEFAULT");
    }




}
