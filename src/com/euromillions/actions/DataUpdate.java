package com.euromillions.actions;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;

import com.euromillions.R;
import com.euromillions.application.EuromillionsApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DataUpdate extends AsyncTask<String, Void, String> { 

	private static final String TAG = "DataStatistic";
	
	private final String LITERAL_BUSCAR_TABLA_INICIO = "<table";
	private final String LITERAL_BUSCAR_TABLA_FIN = "</table";
	private final String LITERAL_BUSCAR_TD_INICIO = "<td";
	private final String LITERAL_BUSCAR_FECHA_ACTUALIZACION = "resultado del dia"; 
	
	
	private Properties propertiesFile = null;
	private BufferedReader urlConnectedBufferedReader = null;
	
	private Context context;
	
	
	public DataUpdate(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = context.getString(R.string.update_ok);
		if(!checkInternetConnetion() || !updateData()){
			result = context.getString(R.string.network_error);
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(this.context, result, Toast.LENGTH_LONG).show();
	}
	

	private boolean checkInternetConnetion() {
		ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return !(networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable());
	}
	
	private boolean isNecesaryUpdateStatistics(){
		long nextupdatedate = EuromillionsApplication.getSharedPropertyValueAsLong("dataupdate.nextupdatedate");
		long actualDate = System.currentTimeMillis();
		return actualDate>nextupdatedate;
	}
	
	
	public boolean updateData()
	{	
		boolean updatedData = false;
		 try {
			 if(isNecesaryUpdateStatistics()){
				findData(EuromillionsApplication.getSharedPropertyValue("dataupdate.urlNumbers"),
						EuromillionsApplication.getSharedPropertyValue("nameFileNumbers"),
						EuromillionsApplication.getSharedPropertyValue("titleFileNumbers"));
				findData(EuromillionsApplication.getSharedPropertyValue("dataupdate.ulrStars"),
						EuromillionsApplication.getSharedPropertyValue("nameFileStars"),
						EuromillionsApplication.getSharedPropertyValue("titleFileStars"));
			 }
			updatedData = true;
		} catch(MalformedURLException e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
		}catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
		}catch(ParseException e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage(),e);
		}
		return updatedData;
	}
	
	
	private void findData(String urlToConnect, String targetFileName, String titleFile) throws MalformedURLException, IOException, ParseException{
			this.openConnectedBufferedReader(urlToConnect);
			this.readDataIntoPropertiesFile();
			this.savePropertiesFileToFile(titleFile, targetFileName);
	}
	
	private void openConnectedBufferedReader(String urlToConnect) throws MalformedURLException,IOException{
		URL url = this.getURL(urlToConnect);
		URLConnection urlConnection = this.openConnection(url);
		this.loadUrlConnectedBufferedReader(urlConnection);
	}
	
	private void readDataIntoPropertiesFile() throws IOException, ParseException{
		this.propertiesFile = new Properties();
		String  inputLine;
		while ((inputLine = this.urlConnectedBufferedReader.readLine()) != null){
			if(inputLine.indexOf(LITERAL_BUSCAR_TABLA_INICIO)>0){
			  this.readTdNumbers();
			  if(!isNecesaryUpdateStatistics()){ break;}
			}
			if(inputLine.indexOf(LITERAL_BUSCAR_FECHA_ACTUALIZACION)>0){
				updateDateStatisticsUpdate(inputLine);
			}
		}
	}
	
	private void updateDateStatisticsUpdate(String lineDate) throws ParseException{
		String date = getDateFromLineDate(lineDate);
		long upatedDateInMilliseconds = getDateInMilliseconds(date);
		long nextUpdateDate = getNextUpdateDate(upatedDateInMilliseconds);
		EuromillionsApplication.setSharedPropertyValue("dataupdate.nextupdatedate",String.valueOf(nextUpdateDate));
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
			if(dataLine.indexOf(LITERAL_BUSCAR_TD_INICIO) > 0){
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
			}else if(dataLine.indexOf(LITERAL_BUSCAR_TABLA_FIN)>0){
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
	}

}
