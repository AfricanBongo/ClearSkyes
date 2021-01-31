package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.africanbongo.clearskyes.controller.customviews.TimePreference;

public class TimeDialogFragment extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment {

    private TimePicker picker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picker = new TimePicker(getContext());
        picker.setIs24HourView(true);
    }

    public static TimeDialogFragment newInstance() {

        Bundle args = new Bundle();

        TimeDialogFragment fragment = new TimeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            TimePreference preference = (TimePreference) getPreference();
            String time = preference.setTime(picker.getHour(), picker.getMinute());
            preference.callChangeListener(time);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        TimePreference timePreference = (TimePreference) getPreference();
        int hour = timePreference.getHour();
        int min = timePreference.getMinute();
        picker.setHour(hour);
        picker.setMinute(min);
    }

    @Override
    protected View onCreateDialogView(Context context) {
        return picker;
    }

    @Nullable
    @Override
    public Preference findPreference(@NonNull CharSequence key) {
        return getPreference();
    }
}
