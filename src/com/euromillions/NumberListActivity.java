package com.euromillions;


import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.euromillions.application.EuromillionsApplication;
import com.euromillions.application.EuromillionsApplication.SHARED_PROPERTIES;
import com.euromillions.beans.Number;
import com.euromillions.beans.Ticket;

public class NumberListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.number_list);
		
		Bundle extras = getIntent().getExtras();
		
		Object o = extras.get("ticket");
		Ticket ticket = (Ticket)o;
		
		Context context = getApplicationContext();
		
		TextView textLastUpdateDate = (TextView)findViewById(R.id.number_list_last_update_date);
		showLastUpdateDate(textLastUpdateDate);
		
		
		TableLayout table = (TableLayout)findViewById(R.id.numbers_table_layout);
		
		//TableRow titleNumber = addTableHeader(new String[]{"NUMBERS FREQUENCY"},4f);
		String[] tableHeaderTitles =  new String[]{
			context.getString(R.string.table_rowsubheader_numbers_number),
			context.getString(R.string.table_rowsubheader_numbers_times),
			context.getString(R.string.table_rowsubheader_starts_number),
			context.getString(R.string.table_rowsubheader_starts_times)
		};
		TableRow titleNumber2 = addTableHeader(tableHeaderTitles,1f);
        
       /* table.addView(titleNumber, new TableLayout.LayoutParams(
        		TableLayout.LayoutParams.FILL_PARENT,
        		TableLayout.LayoutParams.WRAP_CONTENT));
        */table.addView(titleNumber2, new TableLayout.LayoutParams(
        		TableLayout.LayoutParams.FILL_PARENT,
        		TableLayout.LayoutParams.WRAP_CONTENT));
        
        addTableRows(table, ticket.getNumbers(),ticket.getStars());
        
        
        //TableLayout tableStarts = (TableLayout)findViewById(R.id.starts_table_layout);
       /* TableLayout tableStarts = table;
		
		TableRow titleStarts = addTableHeader(new String[]{"STARTS FREQUENCY"});
		TableRow titleStarts2 = addTableHeader(new String[]{"STARTS","TIMES"});
        
		tableStarts.addView(titleStarts, new TableLayout.LayoutParams(
        		TableLayout.LayoutParams.FILL_PARENT,
        		TableLayout.LayoutParams.WRAP_CONTENT));
		tableStarts.addView(titleStarts2, new TableLayout.LayoutParams(
        		TableLayout.LayoutParams.FILL_PARENT,
        		TableLayout.LayoutParams.WRAP_CONTENT));
        
        addTableRows(tableStarts, ticket.getStarts());*/
        
        
        
	}
	
	

    private void showLastUpdateDate(TextView textFooter){
    	long date = EuromillionsApplication.getSharedPropertyValueAsLong(
				SHARED_PROPERTIES.DATAUPDATE_LAST_UPDATE_DATE);
    	if(date!=0){
	    	Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date);
			String lastUpdate = c.get(Calendar.DAY_OF_MONTH)+"/"+
					(c.get(Calendar.MONTH)+1)+"/"+
					c.get(Calendar.YEAR);
			textFooter.setText(getApplicationContext().getString(R.string.main_date_update)+" "+lastUpdate);
    	}
    }
	
	
	private void addTableRows(TableLayout table, Number[] numbers, Number[] stars){
		//for(Number element : elements){
		for(int pos=0;pos<numbers.length;pos++){
			TableRow tableRow =  pos>=stars.length?createTableDataRow(numbers[pos],null):
				createTableDataRow(numbers[pos],stars[pos]);
			table.addView(tableRow,
					new TableLayout.LayoutParams(
	        		TableLayout.LayoutParams.FILL_PARENT,
	        		TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}
	
	private TableRow createTableDataRow(Number number,Number star){
		TableRow dataRow = new TableRow(getApplicationContext());
		
		MarginLayoutParams marginLayoutParams = 
			new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		//marginLayoutParams.setMargins(1, 1,1, 1);
		
		dataRow.setLayoutParams(new TableRow.LayoutParams(marginLayoutParams));  
		//dataRow.setWeightSum(1.0f);
		
		dataRow.setBackgroundColor(Color.BLACK);
		
		dataRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		dataRow.setVerticalGravity(Gravity.CENTER_VERTICAL);
		
		createDataColumn(dataRow, ""+number.getNumber());
		createDataColumn(dataRow, ""+number.getTimes());
		createDataColumn(dataRow, star!=null?""+star.getNumber():"");
		createDataColumn(dataRow, star!=null?""+star.getTimes():"");
		
		return dataRow;
	}
	
	private void createDataColumn(TableRow row, String value) {
		// Create a TextView to house the name of the province
		android.widget.TextView colNumber = new android.widget.TextView(getApplicationContext());
		colNumber.setText(""+value);
		colNumber.setTextColor(Color.BLACK);
		
		TableRow.LayoutParams layout = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT,1.0f);
		layout.setMargins(1, 1, 1, 1);
		//marginLayoutParams.setMargins(1, 1,1, 1);
		colNumber.setLayoutParams(layout);
		colNumber.setBackgroundColor(Color.parseColor("#DCDCDC"));
		
		//nom.setWidth(60);
		
		colNumber.setGravity(Gravity.CENTER);
		
		row.addView(colNumber);
		
	}
	

	private TableRow addTableHeader(String[] columnTitles, float columnsize) {
		TableRow titleNumber = new TableRow(getApplicationContext());
		titleNumber.setLayoutParams(new TableRow.LayoutParams (
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));   
		//titleNumber.setWeightSum(columnsize);
		
		titleNumber.setBackgroundColor(Color.parseColor("#4169E1"));
		
		for(String columnTitle:columnTitles){
			 createHeaderColumn(titleNumber, columnTitle);
		}
		
		return titleNumber;
	}

	private void createHeaderColumn(TableRow row, String columnTitle) {
		// Create a TextView to house the name of the province
		android.widget.TextView colNumber = new android.widget.TextView(getApplicationContext());
		colNumber.setText(columnTitle);
		colNumber.setTextColor(Color.parseColor("#DCDCDC"));//Color.WHITE);
		colNumber.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT,1.0f));
		//nom.setWidth(60);
		colNumber.setGravity(Gravity.CENTER);
		
		row.addView(colNumber);
	}
	
}
