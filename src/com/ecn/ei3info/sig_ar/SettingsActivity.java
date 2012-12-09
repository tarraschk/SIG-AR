package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public class SettingsActivity extends Activity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	    }
	    
	    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            addPreferencesFromResource(R.xml.preferences);
	            
	            // Pour ouvrir la pop-up "About" lors d'un clic sur la préférence readabout
	            Preference readabout = (Preference) findPreference("readabout");
	            
	            readabout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                    	LayoutInflater inflater = this.getLayoutInflater();
                		AlertDialog.Builder builder = new AlertDialog.Builder(this);
                		builder.setView(inflater.inflate(R.layout.dialog_about, null));
                		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                	           public void onClick(DialogInterface dialog, int id) {
                	        	   dialog.cancel();
                	           }
                	           });
                		 AlertDialog dialog = builder.create();
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
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				
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
    
