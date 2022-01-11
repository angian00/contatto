package com.ag.android.contatto.ui.onboarding;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;
import com.ag.android.contatto.util.GuiUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class OnboardingFragmentPhotos extends OnboardingFragment {
    private static final String TAG = "OnboardingFragmentPhotos";
    private static final int REQUEST_IMAGE_CAPTURE = 0;

    private static final String[] CAMERA_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private ActivityResultLauncher<String[]> mPermissionRequest;

    private Bitmap mPhotoData;
    private String mPhotoPath;
    private ImageButton mAnswerButton;
    private ImageView mAnswerPhotoThumbnail;


    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentPhotos();
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
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_photo);

        mAnswerButton = v.findViewById(R.id.answer_button);
        mAnswerButton.setImageResource(R.drawable.icon_camera);
        mAnswerButton.setEnabled(true);
        mAnswerButton.setVisibility(View.VISIBLE);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getActivity();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //File file = new File(Environment.getExternalStorageDirectory(),
                //        );
                File file = null;
                try {
                    file = File.createTempFile(
                            "Contatto_profile_photo_" + String.valueOf(System.currentTimeMillis()),
                            ".jpg",
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    );
                } catch (IOException ioe) {
                    Log.e(TAG, "Could not create photo file", ioe);
                    return;
                }

                mPhotoPath = file.getAbsolutePath();

                if (!hasCameraPermissions()) {
                    Uri intentUri = FileProvider.getUriForFile(context, "com.ag.android.contatto.provider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, intentUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                        GuiUtils.showAlert(v, getString(R.string.onboarding_error_photo_failed));
                    }
                } else {
                    //GuiUtils.showAlert(v, "Missing camera permissions");
                    mPermissionRequest.launch(CAMERA_PERMISSIONS);
                }

            }
        });

        mAnswerPhotoThumbnail = v.findViewById(R.id.answer_photo_thumbnail);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(mPhotoPath);
            try {
                InputStream ims = new FileInputStream(file);
                mPhotoData = BitmapFactory.decodeStream(ims);
            } catch (FileNotFoundException fnfe) {
                Log.e(TAG, "Photo file was not found", fnfe);
                return;
            }

            mAnswerPhotoThumbnail.setImageBitmap(mPhotoData);
            mAnswerPhotoThumbnail.setVisibility(View.VISIBLE);
            mAnswerButton.setVisibility(View.GONE);
        }
    }

    private boolean hasCameraPermissions() {
        Context context = getActivity();
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    protected ValidationResult validateInput() {
        if (mPhotoData == null) {
            return new ValidationResult(false, getString(R.string.onboarding_error_photo_missing));
        }

        return ValidationResult.OK;
    }

    @Override
    protected void onNextPage() {

        ((OnboardingActivity)getActivity()).onPhotoInserted(mPhotoPath);
    }
}
