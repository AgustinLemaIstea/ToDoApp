package com.istea.agustinlema.todoapp.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.settings.SettingsFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment fragment = new SettingsFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }
}
