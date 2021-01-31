package com.africanbongo.clearskyes.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.customviews.TimePreference;

/**
 *  Settings fragment to allow user more control of the functionality of the application
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG = "TimePickerPreference";

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (getParentFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }

        if (preference instanceof TimePreference) {
            final DialogFragment f = TimeDialogFragment.newInstance();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            f.setArguments(bundle);
            f.setTargetFragment(this, 0);
            f.show(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
