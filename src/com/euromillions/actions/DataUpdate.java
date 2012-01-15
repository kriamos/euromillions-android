package com.euromillions.actions;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.euromillions.R;
import com.euromillions.application.EuromillionsApplication;
import com.euromillions.application.EuromillionsApplication.SHARED_PROPERTIES;
import com.euromillions.exceptions.DataNotUpdatableException;

public class DataUpdate extends AsyncTask<String, Void, String> { 

	private static final String TAG = "DataStatistic";
	
	private final String LITERAL_SEARCH_TABLE_START = "<table";
	private final String LITERAL_SEARCH_TABLE_END = "</table";
	private final String LITERAL_SEARCH_TD_START = "<td";
	private final String LITERAL_SEARCH_UPDATE_DATE = "resultado del dia"; 
	
	
	private Properties propertiesFile = null;
	private BufferedReader urlConnectedBufferedReader = null;
	
	private Context context;
	
	private boolean fileSaved = false;
	
	private boolean autoUpdate = false;
	private boolean showMessage = true;
	
	
	public DataUpdate(Context context) {
		this.context = context;
	}
	
	public DataUpdate(Context context, boolean autoUpdate) {
		this.context = context;
		this.autoUpdate = autoUpdate;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = "";
		if(!isNecesaryUpdateStatistics()){
			result = context.getString(R.string.update_not_required);
			showMessage = false;
		}else{
			result = startUpdateProcess();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(!autoUpdate){
			showMessage = true;
		}
		if(showMessage){
			Toast.makeText(this.context, result, Toast.LENGTH_LONG).show();
		}
		
	}
	
	private String startUpdateProcess(){
		String result = context.getString(R.string.update_ok);
		try{
		//Toast.makeText(this.context, R.string.update_statistics, Toast.LENGTH_LONG).show();
			if(!checkInternetConnetion() ){
				throw new IOException();
			}
			updateData();
		} catch(MalformedURLException e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
			result = context.getString(R.string.network_error);
			this.showMessage = false;
		}catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
			result = context.getString(R.string.network_error);
			this.showMessage = false;
		}catch(ParseException e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
			result = context.getString(R.string.network_error);
			this.showMessage = false;
		}catch(DataNotUpdatableException e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
			result = context.getString(R.string.update_page_not_done);
			this.showMessage = false;
		}
		return result;
	}
	
	private boolean isNecesaryUpdateStatistics(){
		long nextupdatedate = EuromillionsApplication.getSharedPropertyValueAsLong(SHARED_PROPERTIES.DATAUPDATE_NEXT_UPDATE_DATE);
		long actualDate = System.currentTimeMillis();
		return actualDate>nextupdatedate;
	}
	

	private boolean checkInternetConnetion() {
		ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return !(networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable());
	}
	
	public void updateData() throws MalformedURLException, IOException, ParseException, DataNotUpdatableException
	{	
		findData(EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.DATAUPDATE_URL_NUMBERS),
				EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.NAME_FILE_NUMBERS),
				EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.TITLE_FILE_NUMBERS));
		fileSaved = false;
		findData(EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.DATAUPDATE_URL_STARS),
				EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.NAME_FILE_STARS),
				EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.TITLE_FILE_STARS));
	}
	
	
	private void findData(String urlToConnect, String targetFileName, String titleFile) throws MalformedURLException, 
		IOException, ParseException, DataNotUpdatableException{
			this.openConnectedBufferedReader(urlToConnect);
			this.readDataIntoPropertiesFile();
			if(!fileSaved) {this.savePropertiesFileToFile(titleFile, targetFileName);}
	}
	
	private void openConnectedBufferedReader(String urlToConnect) throws MalformedURLException,IOException{
		URL url = this.getURL(urlToConnect);
		URLConnection urlConnection = this.openConnection(url);
		this.loadUrlConnectedBufferedReader(urlConnection);
	}
	
	private void readDataIntoPropertiesFile() throws IOException, ParseException, DataNotUpdatableException{
		this.propertiesFile = new Properties();
		String  inputLine;
		while ((inputLine = this.urlConnectedBufferedReader.readLine()) != null){
			if(inputLine.indexOf(LITERAL_SEARCH_TABLE_START)>0){
			  this.readTdNumbers();
			  if(!isNecesaryUpdateStatistics()){ break;}
			}
			if(inputLine.indexOf(LITERAL_SEARCH_UPDATE_DATE)>0){
				updateDateStatisticsUpdate(inputLine);
			}
		}
	}
	
	private void updateDateStatisticsUpdate(String lineDate) throws ParseException, DataNotUpdatableException{
		String date = getDateFromLineDate(lineDate);
		long upatedDateInMilliseconds = getDateInMilliseconds(date);
		long nextUpdateDate = getNextUpdateDate(upatedDateInMilliseconds);
		long actualData = System.currentTimeMillis();
		if(nextUpdateDate<actualData){
			throw new DataNotUpdatableException();
		}
		EuromillionsApplication.setSharedPropertyValue(
				SHARED_PROPERTIES.DATAUPDATE_NEXT_UPDATE_DATE,String.valueOf(nextUpdateDate));
		EuromillionsApplication.setSharedPropertyValue(
				SHARED_PROPERTIES.DATAUPDATE_LAST_UPDATE_DATE,String.valueOf(actualData));
		
		
	}
	
	
	private String getDateFromLineDate(String lineDate){
		String[] splitted = lineDate.split(" ",0);
		return splitted[splitted.length-1];
	}
	
	private long getDateInMilliseconds(String date) throws ParseException{
		long dateInMilliseconds = DateFormat.getDateInstance(DateFormat.DATE_FIELD,new Locale("ES")).parse(date).getTime();
		return dateInMilliseconds;
	}
	
	private long getNextUpdateDate(long actualUpdateDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(actualUpdateDate);
		if(calendar.get(Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY){
			actualUpdateDate = addDaysToDate(actualUpdateDate,5);
		}else if(calendar.get(Calendar.DAY_OF_WEEK ) == Calendar.TUESDAY){
			actualUpdateDate = addDaysToDate(actualUpdateDate,4);
		}
		return actualUpdateDate;
	}
	
	private long addDaysToDate(long date,int numberOfDays){
		int hoursInDay = 24, minutsInHour = 60, secondsInMinut = 60, millisecondInSecond = 1000;
		long timeToAdd = numberOfDays * (hoursInDay*minutsInHour*secondsInMinut*millisecondInSecond);
		return date + timeToAdd;
	}
	
	private void readTdNumbers() throws IOException{
		String dataLine, number="0";
		int tdNumber=0; 
		while ((dataLine = this.urlConnectedBufferedReader.readLine()) != null){
			if(dataLine.indexOf(LITERAL_SEARCH_TD_START) > 0){
				if(tdNumber == 0){ //read number
				  dataLine = this.urlConnectedBufferedReader.readLine();
				  dataLine = dataLine.trim().equals("")?this.urlConnectedBufferedReader.readLine():dataLine;
				  number = dataLine.trim();
				}
				if(tdNumber == 1){//read times for number
				  dataLine = this.urlConnectedBufferedReader.readLine();
				  this.propertiesFile.setProperty(number,dataLine.trim());
				}
				tdNumber++; 
				if(tdNumber == 4){tdNumber=0;}
			}else if(dataLine.indexOf(LITERAL_SEARCH_TABLE_END)>0){
				break;
			}
		}
	}
	
	private void loadUrlConnectedBufferedReader(URLConnection urlConnection) throws IOException{
		this.urlConnectedBufferedReader = new BufferedReader(
			new InputStreamReader(urlConnection.getInputStream()));
	}
	
	private URL getURL(String urlToConnect) throws MalformedURLException{
		URL url = new URL(urlToConnect);
		return url;
	}
	
	private URLConnection openConnection(URL url) throws IOException{
	    URLConnection urlConnection = url.openConnection();
		return urlConnection;
	}
	
	private void savePropertiesFileToFile(String title, String fileName) throws IOException{
		 FileOutputStream fout =  this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
	     this.propertiesFile.store(fout,title);
	     fout.close();
	     fileSaved = true;
	}
	
}
