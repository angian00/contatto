package com.ag.android.contatto.ui.onboarding;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

public class OnboardingFragmentDescription extends OnboardingFragment {
    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentDescription();
    }

    private static int MIN_DESC_LENGTH = 20;
    private static int MAX_DESC_LENGTH = 100;

    private EditText mAnswerField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_description);

        mAnswerField = v.findViewById(R.id.answer_edit);
        mAnswerField.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        mAnswerField.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    protected ValidationResult validateInput() {
        if (mAnswerField.getText().length() == 0)
            return new ValidationResult(false, getString(R.string.onboarding_error_description_missing));

        if (mAnswerField.getText().length() < MIN_DESC_LENGTH) {
            String msg = getResources().getString(R.string.onboarding_error_desc_too_short, MIN_DESC_LENGTH);
            return new ValidationResult(false, msg);
        }

        if (mAnswerField.getText().length() > MAX_DESC_LENGTH) {
            String msg = getResources().getString(R.string.onboarding_error_desc_too_long, MAX_DESC_LENGTH);
            return new ValidationResult(false, msg);
        }

        return ValidationResult.OK;
    }

    @Override
    protected void onNextPage() {
        ((OnboardingActivity)getActivity()).onDescriptionInserted(mAnswerField.getText().toString());
    }
}
