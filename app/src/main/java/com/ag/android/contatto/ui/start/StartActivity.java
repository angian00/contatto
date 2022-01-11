package com.ag.android.contatto.ui.start;

import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.ag.android.contatto.providers.ProfileProvider;
import com.ag.android.contatto.ui.SingleFragmentActivity;
import com.ag.android.contatto.ui.navigation.NavigationActivity;
import com.ag.android.contatto.Profile;

public class StartActivity extends SingleFragmentActivity {
    //private static final boolean DEBUG_ONBOARDING = true;
    private static final boolean DEBUG_ONBOARDING = false;

    private static final long SPLASH_DELAY = 1000; //in ms


    @Override
    protected Fragment createFragment() {
        return StartFragment.newInstance();
    }


    public boolean isRegistered() {
        if (DEBUG_ONBOARDING)
            return false;

        ProfileProvider persistence = new ProfileProvider(this);
        Profile profile = persistence.loadProfile();

        return (profile != null && profile.isValid());
    }


    public void skipOnboarding() {
        Runnable launchTask = () -> {
            Intent intent = new Intent(StartActivity.this, NavigationActivity.class);
            startActivity(intent);
        };

        new Handler().postDelayed(launchTask, SPLASH_DELAY);
    }

}