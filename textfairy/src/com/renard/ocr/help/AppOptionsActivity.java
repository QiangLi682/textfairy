/*
 * Copyright (C) 2012,2013 Renard Wellnitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.renard.ocr.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lamerman.FileDialog;
import com.lamerman.SelectionMode;
import com.renard.ocr.R;
import com.renard.ocr.cropimage.BaseActivityInterface;
import com.renard.ocr.cropimage.MonitoredActivity;
import com.renard.util.PreferencesUtils;

/**
 * preferences dialog for app wide settings or info stuff
 * 
 * @author renard
 * 
 */
public class AppOptionsActivity extends SherlockPreferenceActivity implements
		BaseActivityInterface {
	
    private static final String MARKET_URL            = "market://details?id=com.renard.ocr";
	protected static final int REQUEST_LOAD_FILE = 474;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		final Preference languagePreference = findPreference(getString(R.string.pref_key_default_ocr_lang));
		languagePreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						startActivity(new Intent(AppOptionsActivity.this,
								OCRLanguageActivity.class));
						return true;
					}
				});

		final Preference helpPreference = findPreference(getString(R.string.pref_key_help));
		helpPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						startActivity(new Intent(AppOptionsActivity.this,
								HelpActivity.class));
						return true;
					}
				});

		final Preference contact = findPreference(getString(R.string.pref_key_contact));
		contact.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(AppOptionsActivity.this,
						ContactActivity.class));
				return true;
			}
		});

		final Preference licence = findPreference(getString(R.string.pref_key_license));
		licence.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(AppOptionsActivity.this,
						LicenseActivity.class));
				return true;
			}
		});
		
		final Preference rate = findPreference(getString(R.string.pref_key_rate));
		rate.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
			     Intent intent = new Intent(Intent.ACTION_VIEW);
			        Uri url = Uri.parse(MARKET_URL);
			        intent.setData(url);
			        startActivity(intent);				
			        return true;
			}
		});
		
		final Preference data = findPreference(getString(R.string.pref_key_data_directory));
		data.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				 Intent intent = new Intent(getBaseContext(), FileDialog.class);
		         
		         intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getPath());
		         
		         //can user select directories or not
		         intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
		         intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);
		         //alternatively you can set file filter
		         intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "" });
		         
		         startActivityForResult(intent, REQUEST_LOAD_FILE);
		         return true;
			}
		});
		MonitoredActivity.initAppIcon(this, -1);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setDialogId(int dialogId) {
		// ignored
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == REQUEST_LOAD_FILE) {
	    	 if (resultCode == RESULT_OK) {
                 String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                 if (filePath != null) {
                     if (filePath.endsWith("tessdata")){
                    	 filePath = filePath.substring(0,filePath.length()-"tessdata".length());
                     } else {
                    	 filePath+="/";
                     }
                	 PreferencesUtils.saveTessDir(this, filePath);
                 }
             }

	    }
	}

}
