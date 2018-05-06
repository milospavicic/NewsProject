package com.example.korisnik.newsproject;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.example.korisnik.newsproject.fragments.SettingsFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

}
