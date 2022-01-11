package com.ag.android.contatto.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class OnboardingFragmentInterests extends OnboardingFragment {
    private LinearLayout mAnswerChoice;

    public static OnboardingFragment newInstance() {
        return new OnboardingFragmentInterests();
    }

    private static final int[] ANSWER_VALUES = {
            R.string.interest_male,
            R.string.interest_female,
            R.string.interest_nonbinary,
    };

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView questionText = v.findViewById(R.id.question_text);
        questionText.setText(R.string.question_interests);

        mAnswerChoice = v.findViewById(R.id.answer_choice_multi);
        mAnswerChoice.setVisibility(View.VISIBLE);
        setupChoiceMultiValues(mAnswerChoice, ANSWER_VALUES);

        return v;
    }

    @Override
    protected ValidationResult validateInput() {
        List<String> interests = getCheckedInterests();
        if (interests.isEmpty())
            return new ValidationResult(false, getString(R.string.onboarding_error_interests_missing));

        return ValidationResult.OK;
    }

    private List<String> getCheckedInterests() {
        List<String> interests = new ArrayList<>();
        int nChildren = mAnswerChoice.getChildCount();

        String nonbinaryStr = getResources().getString(R.string.interest_nonbinary);

        for (int i = 0; i < nChildren; i++) {
            final View childView = mAnswerChoice.getChildAt(i);
            if (!(childView instanceof CheckBox))
                continue;

            CheckBox checkBox = (CheckBox) childView;
            if (checkBox.isChecked()) {
                String choiceStr = checkBox.getText().toString();
                if (choiceStr.equals(nonbinaryStr))
                    choiceStr = "nonbinary";

                interests.add(choiceStr);
            }
        }

        return interests;
    }

    @Override
    protected void onNextPage() {
        List<String> interests = getCheckedInterests();

        ((OnboardingActivity)getActivity()).onInterestsInserted(interests);
    }
}
