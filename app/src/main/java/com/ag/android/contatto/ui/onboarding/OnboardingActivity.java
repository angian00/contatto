package com.ag.android.contatto.ui.onboarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ag.android.contatto.Gender;
import com.ag.android.contatto.Profile;
import com.ag.android.contatto.R;
import com.ag.android.contatto.providers.ProfileProvider;
import com.ag.android.contatto.ui.navigation.NavigationActivity;
import com.ag.android.contatto.util.MiscUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnboardingActivity extends FragmentActivity {
    private static final String TAG = "OnboardingActivity";

    private Profile mOnboardingData;

    private ViewPager2 mViewPager;
    private FragmentStateAdapter mPagerAdapter;



    //--------------------- Lifecycle methods ---------------------

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mOnboardingData = new Profile();
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mOnboardingData.setPhone(tMgr.getLine1Number());

        mViewPager = findViewById(R.id.onboarding_view_pager);
        mViewPager.setUserInputEnabled(false);

        mPagerAdapter = new OnboardingPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    //--------------------- UI fragment callbacks ---------------------

    public void onEmailInserted(String emailStr) {
        mOnboardingData.setEmail(emailStr);
    }

    public void onNameInserted(String name) {
        mOnboardingData.setName(name);
    }

    public void onBirthdayInserted(Date birthday) {
        mOnboardingData.setBirthday(birthday);
    }

    public void onGenderInserted(String genderStr) {
        try {
            mOnboardingData.setGender(MiscUtils.str2gender(genderStr));
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, "Unknown gender: " + genderStr);
        }
    }

    public void onInterestsInserted(List<String> interestsStr) {
        List<Gender> interests = new ArrayList<>();

        for (String interestStr: interestsStr) {
            interests.add(MiscUtils.str2gender(interestStr));
        }

        mOnboardingData.setInterests(interests);
    }


    public void onDescriptionInserted(String desc) {
        mOnboardingData.setDescription(desc);
    }

    public void onPhotoInserted(String photoPath) {
        mOnboardingData.setPhotoPath(photoPath);
    }

    public void onOnboardingComplete() {
        ProfileProvider persistence = new ProfileProvider(this);
        persistence.saveProfile(mOnboardingData);

        Intent intent = new Intent(this, NavigationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //--------------------- viewPager setup ---------------------

    private static final OnboardingFragment[] pageFragments = new OnboardingFragment[] {
            OnboardingFragmentEmail.newInstance(),
            OnboardingFragmentName.newInstance(),
            OnboardingFragmentBirthday.newInstance(),
            OnboardingFragmentGender.newInstance(),
            OnboardingFragmentInterests.newInstance(),
            OnboardingFragmentDescription.newInstance(),
            OnboardingFragmentPhotos.newInstance(),
            OnboardingFragmentLocation.newInstance(),
    };



    private class OnboardingPagerAdapter extends FragmentStateAdapter {
        public OnboardingPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return pageFragments[position];
        }

        @Override
        public int getItemCount() {
            return pageFragments.length;
        }
    }


    //--------------------- utlity methods ---------------------

}
