package com.euromillions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.euromillions.adapters.MenuImageButtonAdapter;

public class GenerateActivity extends Activity {
	
	private Integer[] imageButtons = {
			R.drawable.frequent_list,
			R.drawable.ticket_frequent_list,
			R.drawable.ticket_aleatory_frequent_list,
			R.drawable.ticket_aleatory_list
	};
	
	private int[] imageButtonsIds = {
			R.id.button_frequent_list,
			R.id.button_ticket_frequent_list,
			R.id.button_ticket_aleatory_frequent_list,
			R.id.button_ticket_aleatory_list
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.generate);
		
		GridView gridMenu = (GridView) findViewById(R.id.grid_number_generate);
	    gridMenu.setAdapter(new MenuImageButtonAdapter(this,imageButtons,imageButtonsIds));

	}
	
}
