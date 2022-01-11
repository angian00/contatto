package com.ag.android.contatto.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

public class OnboardingFragmentGender extends OnboardingFragment {
    private RadioGroup mAnswerChoice;

    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentGender();
    }

    private static final int[] ANSWER_VALUES = {
            R.string.gender_male,
            R.string.gender_female,
            R.string.gender_nonbinary,
    };

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_gender);

        mAnswerChoice = v.findViewById(R.id.answer_choice_single);
        mAnswerChoice.setVisibility(View.VISIBLE);
        setupChoiceSingleValues(mAnswerChoice, ANSWER_VALUES);

        return v;
    }

    @Override
    protected ValidationResult validateInput() {
        if (mAnswerChoice.getCheckedRadioButtonId() == -1)
            return new ValidationResult(false, getString(R.string.onboarding_error_gender_missing));
        else
            return ValidationResult.OK;
    }

    @Override
    protected void onNextPage() {
        RadioButton choice = mAnswerChoice.findViewById(mAnswerChoice.getCheckedRadioButtonId());
        String choiceStr = choice.getText().toString();

        String nonbinaryStr = getResources().getString(R.string.gender_nonbinary);
        if (choiceStr.equals(nonbinaryStr))
            choiceStr = "nonbinary";

        ((OnboardingActivity)getActivity()).onGenderInserted(choiceStr);
    }
}
