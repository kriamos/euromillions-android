package com.euromillions.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.euromillions.GenerateActivity;
import com.euromillions.NumberListActivity;
import com.euromillions.R;
import com.euromillions.TicketActivity;
import com.euromillions.actions.DataGenerate;
import com.euromillions.actions.DataUpdate;
import com.euromillions.beans.Ticket;

public class MenuImageButtonOnClickListener implements OnClickListener {
	
	private static final String TAG = "MenuImageButtonOnClickListener:";
	
	private Context context;
	
	public void onClick(View v) {
		this.context = v.getContext();
		try {
			if(v.getId() == R.id.button_refresh){
				actionRefresh();
			}else if(v.getId() == R.id.button_euromillon){
				actionEuromillon();
			}else if(v.getId() == R.id.button_frequent_list){
				actionShowFrequentList();
			}else if(v.getId() == R.id.button_generate_frequent_list){
				actionGenerateFrequentList();
			}else if(v.getId() == R.id.button_generate_aleatory_frequent_list){
				actionGenerateFrequentAleattiryList();
			}
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage(),e);
			//ALERT
		} catch (ExecutionException e) {
			Log.e(TAG, e.getMessage(),e);
			//ALERT
		}
	}
	
	private void actionRefresh(){
		Toast.makeText(context, context.getString(R.string.update_statistics), Toast.LENGTH_SHORT).show();
		new DataUpdate(context).execute("");
	}
	
	private void actionEuromillon(){
		Intent intent = new Intent(context,GenerateActivity.class);
		context.startActivity(intent);
	}
	
	private void actionShowFrequentList() throws InterruptedException, ExecutionException{
		Ticket ticket = new DataGenerate(context).execute(new Integer[]{DataGenerate.SHOW_FREQUENT_LIST}).get();
		Intent intent = new Intent(context,NumberListActivity.class);
			intent.putExtra("ticket", ticket);
		context.startActivity(intent);
	}
	
	private void actionGenerateFrequentList() throws InterruptedException, ExecutionException{
		Ticket ticket = new DataGenerate(context).execute(new Integer[]{DataGenerate.GENERATE_FREQUENT_LIST}).get();
		Intent intent = new Intent(context,TicketActivity.class);
			intent.putExtra("ticket", ticket);
			intent.putExtra("selectedAction",DataGenerate.GENERATE_FREQUENT_LIST);
		context.startActivity(intent);
	}
	
	private void actionGenerateFrequentAleattiryList() throws InterruptedException, ExecutionException{
		Ticket ticket = new DataGenerate(context).execute(new Integer[]{DataGenerate.GENERATE_FREQUENT_ALEATORY_LIST}).get();
		Intent intent = new Intent(context,TicketActivity.class);
			intent.putExtra("ticket", ticket);
			intent.putExtra("selectedAction",DataGenerate.GENERATE_FREQUENT_ALEATORY_LIST);
		context.startActivity(intent);
	}

}
