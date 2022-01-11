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

public class OnboardingFragmentName extends OnboardingFragment {
    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentName();
    }

    private EditText mAnswerField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_name);

        mAnswerField = v.findViewById(R.id.answer_edit);
        mAnswerField.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        mAnswerField.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    protected ValidationResult validateInput() {
        if (mAnswerField.getText().length() == 0)
            return new ValidationResult(false, getString(R.string.onboarding_error_name_missing));
        else {
            return ValidationResult.OK;
        }
    }

    @Override
    protected void onNextPage() {
        ((OnboardingActivity)getActivity()).onNameInserted(mAnswerField.getText().toString());
    }
}
