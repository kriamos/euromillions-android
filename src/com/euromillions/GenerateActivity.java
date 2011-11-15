package com.euromillions;

import com.euromillions.adapters.MenuImageButtonAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class GenerateActivity extends Activity {
	
	private Integer[] imageButtons = {
			R.drawable.frequent_list,
			R.drawable.generate_frequent_list,
			R.drawable.generate_frequent_list
	};
	
	private int[] imageButtonsIds = {
			R.id.button_frequent_list,
			R.id.button_generate_frequent_list,
			R.id.button_generate_aleatory_frequent_list
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.generate);
		
		GridView gridMenu = (GridView) findViewById(R.id.grid_number_generate);
	    gridMenu.setAdapter(new MenuImageButtonAdapter(this,imageButtons,imageButtonsIds));

	}
	
}
