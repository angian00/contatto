package com.ag.android.contatto.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ag.android.contatto.Profile;
import com.google.gson.Gson;

public class ProfileProvider {
    private Context mContext;
    private SharedPreferences mPrefs;


    public ProfileProvider(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveProfile(Profile profile) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(profile);

        mPrefs.edit()
                .putString("profile", jsonStr)
                .commit();
    }

    public Profile loadProfile() {
        String jsonStr = mPrefs.getString("profile", null);
        if (jsonStr == null)
            return null;

        Profile profile = new Gson().fromJson(jsonStr, Profile.class);
        return profile;
    }


    public void deleteProfile() {
        mPrefs.edit()
                .remove("profile")
                .commit();
    }
}
