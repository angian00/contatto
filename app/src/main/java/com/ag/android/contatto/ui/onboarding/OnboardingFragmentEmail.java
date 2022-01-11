package com.ag.android.contatto.ui.onboarding;

import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

public class OnboardingFragmentEmail extends OnboardingFragment {
    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentEmail();
    }

    private EditText mAnswerField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_email);

        mAnswerField = v.findViewById(R.id.answer_edit);
        mAnswerField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mAnswerField.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    protected ValidationResult validateInput() {
        if (mAnswerField.getText().length() == 0)
            return new ValidationResult(false, getString(R.string.onboarding_error_email_missing));
        if  (!Patterns.EMAIL_ADDRESS.matcher(mAnswerField.getText()).matches())
            return new ValidationResult(false, getString(R.string.onboarding_error_email_invalid));

        return ValidationResult.OK;
    }

    @Override
    protected void onNextPage() {
        ((OnboardingActivity)getActivity()).onEmailInserted(mAnswerField.getText().toString());
    }
}
