package com.euromillions.adapters;


import com.euromillions.listeners.MenuImageButtonOnClickListener;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

public class MenuImageButtonAdapter extends BaseAdapter {
	
	private Context context;
	
	private Integer[] imageButtons;
	private int[] imageButtonsIds;
	
	public MenuImageButtonAdapter(Context context, Integer[] imageButtons, int[] imageButtonsIds){
		this.context = context;
		this.imageButtons = imageButtons;
		this.imageButtonsIds = imageButtonsIds;
	}
	
	
	
	public int getCount() {
		return imageButtons.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageButton imageButton;
		if(convertView == null){
			imageButton = new ImageButton(context);
			GridView.LayoutParams layoutParams = new GridView.LayoutParams(100, 100);
			imageButton.setLayoutParams(layoutParams);
			
		}else{
			imageButton = (ImageButton)convertView;
		}
		imageButton.setImageResource(this.imageButtons[position]);
		imageButton.setId(this.imageButtonsIds[position]);
		imageButton.setOnClickListener(new MenuImageButtonOnClickListener());
		return imageButton;
	}

}
