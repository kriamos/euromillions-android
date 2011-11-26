package com.euromillions;

import com.euromillions.application.EuromillionsApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends Activity{
	
	private static final String TAG = "SettingsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_settings);
		
		CheckBox checkBox = (CheckBox)findViewById(R.id.settings_check_autoupdate);
		boolean checked = EuromillionsApplication.getSharedPropertyValueAsBoolean(
				EuromillionsApplication.SHARED_PROPERTIES.DATAUPDATE_AUTOUPDATE);
		checkBox.setChecked(checked);
		
		addCheckBoxPadding(checkBox);
		
	}


	private void addCheckBoxPadding(CheckBox checkBox) {
		final float scale = this.getResources().getDisplayMetrics().density;
		int padLeft = checkBox.getPaddingLeft() + (int)(10.0f * scale + 45f);
		checkBox.setPadding(padLeft,
		        checkBox.getPaddingTop(),
		        checkBox.getPaddingRight(),
		        checkBox.getPaddingBottom());
	}
	
	
	public void onClickAutoupdate(View view){
		CheckBox checkBox = (CheckBox)findViewById(view.getId());
		boolean isChecked = checkBox.isChecked();
		EuromillionsApplication.setSharedPropertyValue(
				EuromillionsApplication.SHARED_PROPERTIES.DATAUPDATE_AUTOUPDATE, String.valueOf(isChecked));
	}
	
	
	
}
