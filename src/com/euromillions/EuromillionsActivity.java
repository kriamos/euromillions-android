package com.euromillions;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.euromillions.actions.DataUpdate;
import com.euromillions.adapters.MenuImageButtonAdapter;
import com.euromillions.application.EuromillionsApplication;

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
   
    	checkStartActions();
    	
    	GridView gridMenu = (GridView) findViewById(R.id.grid_menu);
    	gridMenu.setAdapter(new MenuImageButtonAdapter(this,imageButtons,imageButtonsIds));
        
    }
    
    
    private void checkStartActions(){
    	if(EuromillionsApplication.getSharedPropertyValueAsBoolean("dataupdate.autoupdate")){
    		new DataUpdate(getApplicationContext()).execute("");
    	}
    	
    }
    
  
}