package com.ag.android.contatto.ui.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.ag.android.contatto.util.GuiUtils;
import com.ag.android.contatto.R;
import com.ag.android.contatto.ui.ValidationResult;

public abstract class OnboardingFragment extends Fragment {
    private static final String TAG = "OnboardingFragment";

    //---------------- Lifecycle methods -------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onboarding_question, container, false);

        hideCustomFields(v);

        ViewPager2 viewPager = getActivity().findViewById(R.id.onboarding_view_pager);
        Button nextButton = v.findViewById(R.id.button_next);
        nextButton.setOnClickListener(view -> {
            ValidationResult valResult = validateInput();

            if (!valResult.isOk()) {
                GuiUtils.showAlert(v, valResult.getMessage());
                return;
            }

            onNextPage();

            if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            } else {
                ((OnboardingActivity)getActivity()).onOnboardingComplete();
            }
        });

        return v;
    }

    //---------------- abstract methods -------------------------
    protected abstract ValidationResult validateInput();
    protected abstract void onNextPage();


    //---------------- utility methods -------------------------
    private void hideCustomFields(View v) {
        v.findViewById(R.id.answer_edit).setVisibility(View.GONE);
        v.findViewById(R.id.answer_button).setVisibility(View.GONE);
        v.findViewById(R.id.answer_photo_thumbnail).setVisibility(View.GONE);
        v.findViewById(R.id.answer_choice_multi).setVisibility(View.GONE);
        v.findViewById(R.id.answer_choice_single).setVisibility(View.GONE);
    }


    protected void setupChoiceSingleValues(RadioGroup radioGroup, int[] values) {
         int i = 0;

         while (true) {
             int buttonId = this.getResources().getIdentifier("choice_single_" + (i+1), "id", "com.ag.android.contatto");
             RadioButton button = radioGroup.findViewById(buttonId);

             if (button == null) {
                 if (i < values.length)
                     Log.e(TAG, "Too many values for radio group");
                 break;
             }

             if (i < values.length) {
                 button.setText(values[i]);
                 button.setVisibility(View.VISIBLE);

             } else {
                 button.setVisibility(View.GONE);
             }

             i++;
         }
    }

    protected void setupChoiceMultiValues(View parentView, int[] values) {
        int i = 0;

        while (true) {
            int checkboxId = this.getResources().getIdentifier("choice_multi_" + (i+1), "id", "com.ag.android.contatto");
            CheckBox checkbox = parentView.findViewById(checkboxId);

            if (checkbox == null) {
                if (i < values.length)
                    Log.e(TAG, "Too many values for multi choice");
                break;
            }

            if (i < values.length) {
                checkbox.setText(values[i]);
                checkbox.setVisibility(View.VISIBLE);

            } else {
                checkbox.setVisibility(View.GONE);
            }

            i++;
        }
    }
}
