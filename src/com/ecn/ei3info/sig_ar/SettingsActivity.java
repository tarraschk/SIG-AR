package com.ecn.ei3info.sig_ar;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity  extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String settings = getArguments().getString("settings");
        if ("arview".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_arview);
        } 
        else if ("database".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_database);
        }
        else if ("about".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_about);
        }
    }
}
