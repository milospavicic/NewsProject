package com.example.korisnik.newsproject.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.korisnik.newsproject.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences as configured in the /res/xml/preferences.xml file
        // and displays them.
        // The preferences will be automatically saved.
        addPreferencesFromResource(R.xml.preferences);
    }
}