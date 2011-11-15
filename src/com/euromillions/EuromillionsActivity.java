package com.euromillions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.euromillions.adapters.MenuImageButtonAdapter;

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
    	
    	GridView gridMenu = (GridView) findViewById(R.id.grid_menu);
    	gridMenu.setAdapter(new MenuImageButtonAdapter(this,imageButtons,imageButtonsIds));
        
    }
    
  
}