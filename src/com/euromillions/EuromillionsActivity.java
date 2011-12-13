package com.euromillions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.euromillions.actions.DataUpdate;
import com.euromillions.adapters.MenuImageButtonAdapter;
import com.euromillions.application.EuromillionsApplication;
import com.euromillions.application.EuromillionsApplication.SHARED_PROPERTIES;

public class EuromillionsActivity extends Activity {
	
	private Integer[] imageButtons = {
			R.drawable.refresh,
			R.drawable.euromillones
	};
	
	private int[] imageButtonsIds = {
			R.id.button_refresh,
			R.id.button_euromillon
	};
	
	private static final String TAG = "EuromillionsActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.main);       
    	
    	/*
    	try {
			EuromillionsApplication.resetProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    	
		checkStartActions();
    	
    	GridView gridMenu = (GridView) findViewById(R.id.grid_menu);
    	gridMenu.setAdapter(new MenuImageButtonAdapter(this,imageButtons,imageButtonsIds));
        
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.principal_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case R.id.principal_menu_settings:
				Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(settings);
				break;
			case R.id.principal_menu_help:
				Toast.makeText(getApplicationContext(),"Help", Toast.LENGTH_SHORT).show();
				break;
		}
    	return true;
    }
    
    private void checkStartActions(){
    	if(EuromillionsApplication.getSharedPropertyValueAsBoolean(SHARED_PROPERTIES.DATAUPDATE_AUTOUPDATE)){
			new DataUpdate(getApplicationContext(),true).execute("");
    	}
    	
    }
    
  
}