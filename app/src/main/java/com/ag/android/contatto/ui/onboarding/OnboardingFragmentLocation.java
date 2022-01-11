package com.ag.android.contatto.ui.onboarding;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

public class OnboardingFragmentLocation extends OnboardingFragment {
    private static final String TAG = "OnboardingFragmentLocation";

    private ImageButton mAnswerButton;

    private ActivityResultLauncher<String> mPermissionRequest;
    private boolean mLocationGranted;


    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentLocation();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationGranted = hasLocationPermission();

        if (!mLocationGranted) {
            mPermissionRequest = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    permission -> {
                        mLocationGranted = permission;
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_location);

        mAnswerButton = v.findViewById(R.id.answer_button);
        mAnswerButton.setImageResource(R.drawable.icon_location);
        mAnswerButton.setVisibility(View.VISIBLE);

        if (mLocationGranted) {
            mAnswerButton.setEnabled(false);
        } else {
            mAnswerButton.setEnabled(true);
            mAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
            });
        }

        return v;
    }


    @Override
    protected ValidationResult validateInput() {
        if (!mLocationGranted) {
            return new ValidationResult(false, getString(R.string.onboarding_error_location_missing));
        }

        return ValidationResult.OK;
    }


    @Override
    protected void onNextPage() {
        //do nothing: location is temporary, not part of OnboardingData
    }


    public boolean hasLocationPermission() {
        Context context = getActivity();
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
}
