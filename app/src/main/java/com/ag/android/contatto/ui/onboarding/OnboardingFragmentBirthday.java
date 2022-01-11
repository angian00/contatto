package com.ag.android.contatto.ui.onboarding;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ag.android.contatto.util.MiscUtils;
import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnboardingFragmentBirthday extends OnboardingFragment {
    private static final String TAG = "OnboardingFragmentBirthday";


    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentBirthday();
    }

    private EditText mAnswerField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_birthday);

        mAnswerField = v.findViewById(R.id.answer_edit);
        mAnswerField.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        mAnswerField.setVisibility(View.VISIBLE);

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected ValidationResult validateInput() {
        String dateStr = mAnswerField.getText().toString();
        if (dateStr == null || dateStr.length() == 0)
            return new ValidationResult(false, getString(R.string.onboarding_error_birthday_missing));

        Date birthday;
        try {
            birthday = MiscUtils.str2date(dateStr);
        } catch (ParseException e) {
            return new ValidationResult(false, getString(R.string.onboarding_error_birthday_invalid));
        }

        if (!MiscUtils.isOver18(birthday))
            return new ValidationResult(false, getString(R.string.onboarding_error_birthday_under_18));

        return ValidationResult.OK;
    }

    @Override
    protected void onNextPage() {
        Date birthday = null;
        try {
            birthday = MiscUtils.str2date(mAnswerField.getText().toString());
            ((OnboardingActivity)getActivity()).onBirthdayInserted(birthday);
        } catch (ParseException e) {
            Log.e(TAG, "Invalid birthday: " + mAnswerField.getText().toString());
        }
    }


}
