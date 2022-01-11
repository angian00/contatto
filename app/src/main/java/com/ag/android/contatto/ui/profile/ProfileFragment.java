package com.ag.android.contatto.ui.profile;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ag.android.contatto.Gender;
import com.ag.android.contatto.Profile;
import com.ag.android.contatto.R;
import com.ag.android.contatto.providers.ProfileProvider;
import com.ag.android.contatto.ui.start.StartActivity;
import com.ag.android.contatto.util.MiscUtils;

import java.io.File;

public class ProfileFragment extends Fragment {
    private static final long UNREGISTER_DELAY = 400;

    private ProfileProvider mProfileProvider;
    private Profile mProfile;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProfileProvider = new ProfileProvider(getActivity());
        mProfile = mProfileProvider.loadProfile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        Resources res = getResources();

        TextView nameView = v.findViewById(R.id.name);
        nameView.setText(mProfile.getName());

        ImageView photoView = v.findViewById(R.id.photo);
        photoView.setImageURI(Uri.fromFile(new File(mProfile.getPhotoPath())));

        TextView phoneView = v.findViewById(R.id.phone);
        phoneView.setText(mProfile.getPhone());

        TextView emailView = v.findViewById(R.id.email);
        emailView.setText(mProfile.getEmail());

        TextView descView = v.findViewById(R.id.description);
        descView.setText(mProfile.getDescription());

        TextView genderView = v.findViewById(R.id.gender);
        String genderStr = MiscUtils.gender2str(mProfile.getGender(), false);
        genderView.setText(res.getString(R.string.profile_gender, genderStr));

        TextView interestsView = v.findViewById(R.id.interests);
        String interestsStr = "";
        int i = 0;
        for (Gender g: mProfile.getInterests()) {
            if (i > 0)
                interestsStr += ", ";

            interestsStr += MiscUtils.gender2str(g, true);
            i++;
        }
        interestsView.setText(res.getString(R.string.profile_interests, interestsStr));

        TextView birthdayView = v.findViewById(R.id.birthday);
        String birthdayStr = MiscUtils.date2str(mProfile.getBirthday());
        birthdayView.setText(res.getString(R.string.profile_birthday, birthdayStr));


        Button unregisterButton = v.findViewById(R.id.button_unregister);
        unregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileProvider.deleteProfile();

                Runnable launchTask = () -> {
                    Intent intent = new Intent(getActivity(), StartActivity.class);
                    startActivity(intent);
                };

                new Handler().postDelayed(launchTask, UNREGISTER_DELAY);
            }
        });

        return v;
    }
}