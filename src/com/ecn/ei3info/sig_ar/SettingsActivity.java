package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public class SettingsActivity extends Activity {
	
	public static Activity a;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		a=this;
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
							.replace(android.R.id.content, new SettingsFragment())
							.commit();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			

			Preference readabout = (Preference) findPreference("readabout");

			readabout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					LayoutInflater inflater = a.getLayoutInflater();
					AlertDialog.Builder builder = new AlertDialog.Builder(a);
					builder.setView(inflater.inflate(R.layout.dialog_about, null));
					builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					
					AlertDialog dialog = builder.create();
					
					// To display creative commons link in "About"
					TextView license = (TextView)dialog.findViewById(R.id.license);
					license.setText(Html.fromHtml(
				            "SIG-AR is licensed under a " +
				            "<a href=\"http://creativecommons.org/licenses/by-sa/3.0/> Creative Commons Attribution-ShareAlike 3.0 Unported License</a> "));
					license.setMovementMethod(LinkMovementMethod.getInstance());
					
					dialog.show();
					return false;
				}
			});

		}

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceManager().getSharedPreferences()
			.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			super.onPause();
			getPreferenceManager().getSharedPreferences()
			.unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	        if (key.equals("pref_db_name")) {
	            Preference dbNamePref = findPreference(key);
	            String s = "Click here to modify database name. Your current database is : ";
	            s = s + sharedPreferences.getString(key, "");
	            dbNamePref.setSummary(s);
	        }
	        if (key.equals("pref_db_address")) {
	            Preference dbAddressPref = findPreference(key);
	            String s = "Click here to modify database address. Current address is : ";
	            s = s + sharedPreferences.getString(key, "");
	            dbAddressPref.setSummary(s);
	        }
	        if (key.equals("pref_db_port")) {
	            Preference dbPortPref = findPreference(key);
	            String s = "Click here to modify database port. Current port is : ";
	            s = s + sharedPreferences.getString(key, "");
	            dbPortPref.setSummary(s);
	        }
	        if (key.equals("pref_db_login")) {
	            Preference dbLoginPref = findPreference(key);
	            String s = "Click here to modify your login. Your current login is : ";
	            s = s + sharedPreferences.getString(key, "");
	            dbLoginPref.setSummary(s);
	        }
	    }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
		startActivity(intent);
	}



}

