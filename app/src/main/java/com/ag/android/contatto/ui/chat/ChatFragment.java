package com.ag.android.contatto.ui.chat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.ag.android.contatto.R;

import java.io.IOException;
import java.io.InputStream;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        ImageView iv = v.findViewById(R.id.image);

        try {
            InputStream ims = getActivity().getAssets().open("screenshot_chat.png");
            Drawable d = Drawable.createFromStream(ims, null);
            iv.setImageDrawable(d);
            ims.close();
        } catch(IOException ioe) {
            Log.e(TAG, "Error loading image", ioe);
            //ignore
        }

        return v;
    }
}