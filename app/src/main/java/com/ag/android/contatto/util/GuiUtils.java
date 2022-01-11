package com.ag.android.contatto.util;

import android.view.View;
import android.widget.TextView;

import com.ag.android.contatto.R;
import com.google.android.material.snackbar.Snackbar;

public class GuiUtils {
    public static void showAlert(View parentView, String msg) {
        Snackbar snackbar = Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG);
        View v = snackbar.getView();
        TextView textView = (TextView)v.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_alert, 0, 0, 0);
        textView.setCompoundDrawablePadding(15);

        snackbar.show();
    }
}
