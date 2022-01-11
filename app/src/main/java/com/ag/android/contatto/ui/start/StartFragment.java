package com.ag.android.contatto.ui.start;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.onboarding.OnboardingActivity;

public class StartFragment extends Fragment {
    private static final String TAG = "StartFragment";

    private static final String[] PHONE_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_NUMBERS,
            //Manifest.permission.READ_SMS,
            //Manifest.permission.READ_PHONE_STATE,
    };


    private String mPhoneNumber;
    private ActivityResultLauncher<String[]> mPermissionRequest;

    public static Fragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean permOk = true;
                    for (String pName: permissions.keySet()) {
                        if (!permissions.get(pName)) {
                            Log.w(TAG, pName + " is false");

                            permOk = false;
                            break;
                        }
                    }

                    if (permOk)
                        readPhoneNumber();
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        StartActivity parentActivity = (StartActivity)getActivity();

        if (parentActivity.isRegistered()) {
            parentActivity.skipOnboarding();
        } else {
            Button phoneNumberButton = v.findViewById(R.id.button_phone_number);
            phoneNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hasPhonePermission()) {
                        readPhoneNumber();
                    } else {
                        mPermissionRequest.launch(PHONE_PERMISSIONS);
                    }
                }
            });

            phoneNumberButton.setVisibility(View.VISIBLE);
        }

        return v;
    }


    @SuppressLint("MissingPermission")
    private void readPhoneNumber() {
        Context context = getActivity();

        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        Log.i(TAG, "Phone number is " + mPhoneNumber);
        //TODO: save phone number

        Intent intent = new Intent(context, OnboardingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public boolean hasPhonePermission() {
        Context context = getActivity();
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED);
    };

}
