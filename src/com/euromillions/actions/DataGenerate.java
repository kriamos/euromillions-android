package com.euromillions.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;

import com.euromillions.application.EuromillionsApplication;
import com.euromillions.application.EuromillionsApplication.SHARED_PROPERTIES;
import com.euromillions.beans.Number;
import com.euromillions.beans.Ticket;

public class DataGenerate extends AsyncTask<Integer, Void, Ticket>{
	
	public static final int SHOW_FREQUENT_LIST = 0;
	public static final int GENERATE_ALEATORY_LIST = 1;
	public static final int GENERATE_FREQUENT_LIST = 2;
	public static final int GENERATE_FREQUENT_ALEATORY_LIST = 3;
	
	
	public static final int MAX_NUMBERS_SIZE = 50;
	public static final int MAX_STARS_SIZE = 11;
	public static final int MAX_GENERATED_NUMBERS_IN_TICKET = 5;
	public static final int MAX_GENERATED_STARS_IN_TICKET = 2;
	
	
	private Context context;
	
	private int selectedAction = 0;
	
	

	public DataGenerate(Context context) {
		this.context = context;
	}
	
	@Override
	protected Ticket doInBackground(Integer... params) {
		this.selectedAction = params[0];
		try{
			return doAction();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(NumberFormatException e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Ticket result) {}
	
	private Ticket doAction() throws IOException, NumberFormatException{
		Number[] numbers =  readNumbersFromProperties(
			EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.NAME_FILE_NUMBERS), 
			MAX_NUMBERS_SIZE);
		Number[] stars =  readNumbersFromProperties( 
			EuromillionsApplication.getSharedPropertyValue(SHARED_PROPERTIES.NAME_FILE_STARS),
			MAX_STARS_SIZE);
		switch(selectedAction){
			case DataGenerate.SHOW_FREQUENT_LIST:
				return showFrequentList(numbers,stars);
			case DataGenerate.GENERATE_FREQUENT_LIST:
				return generateFrequentList(numbers,stars);
			case DataGenerate.GENERATE_FREQUENT_ALEATORY_LIST:
				return generateFrequentAleatoryList(numbers,stars);
			case DataGenerate.GENERATE_ALEATORY_LIST:
				return generateAleatoryList(numbers,stars);
			default: return null;
		}
	}
	
	
	private Ticket generateAleatoryList(Number[] numbers, Number[] stars){
		int maxPositionOnArrayNumbers = MAX_NUMBERS_SIZE;
		int maxPositionOnArrayStars = MAX_STARS_SIZE;
		
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		
		Number[] generatedNumbers = generateNumbersList(random, numbers, maxPositionOnArrayNumbers);
		Number[] generatedStars = generateStarsList(random,stars,maxPositionOnArrayStars);
		
		return new Ticket(generatedNumbers, generatedStars);
	}
	
	private Ticket generateFrequentAleatoryList(Number[] numbers, Number[] stars){
		int maxPositionOnArrayNumbers = 20;
		int maxPositionOnArrayStars = 11;
		
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		
		Number[] generatedNumbers = generateNumbersList(random, numbers, maxPositionOnArrayNumbers);
		Number[] generatedStars = generateStarsList(random,stars,maxPositionOnArrayStars);
		
		return new Ticket(generatedNumbers,generatedStars);
	}
	
	private Number[] generateNumbersList(Random random, Number[] totalNumbers, int maxPositionOnArrayNumbers){
		Number[] generatedNumbers = new Number[MAX_GENERATED_NUMBERS_IN_TICKET];
		List<Integer> generatedList = new ArrayList<Integer>();
		for(int generatedArrayPosition=0;generatedArrayPosition<MAX_GENERATED_NUMBERS_IN_TICKET;generatedArrayPosition++){
			generatedNumbers[generatedArrayPosition] = totalNumbers[generateNumber(random, maxPositionOnArrayNumbers,generatedList)];
		}
		return generatedNumbers;
	}
	
	private Number[] generateStarsList(Random random, Number[] totalStars, int maxPositionOnArrayStars){
		Number[] generatedStars = new Number[MAX_GENERATED_STARS_IN_TICKET];
		List<Integer> generatedList = new ArrayList<Integer>();
		for(int generatedArrayPosition=0;generatedArrayPosition<MAX_GENERATED_STARS_IN_TICKET;generatedArrayPosition++){
			generatedStars[generatedArrayPosition] = totalStars[generateNumber(random, maxPositionOnArrayStars,generatedList)];
		}
		return generatedStars;
	}
	
	private int generateNumber(Random random,int maxNumber, List<Integer> generatedList){
	    int number = random.nextInt(maxNumber);
	    while(number==0 || generatedList.contains(number)){number = random.nextInt(maxNumber);}
	    generatedList.add(number);
	    return number;
    }
	
	private Ticket generateFrequentList(Number[] numbers , Number[] stars){
		Number[] generatedNumbers = new Number[MAX_GENERATED_NUMBERS_IN_TICKET];
		Number[] generatedStars = new Number[MAX_GENERATED_STARS_IN_TICKET];
		System.arraycopy(numbers,0,generatedNumbers,0,MAX_GENERATED_NUMBERS_IN_TICKET);
		System.arraycopy(stars,0,generatedStars,0,MAX_GENERATED_STARS_IN_TICKET);
		return new Ticket(generatedNumbers, generatedStars);
	}
	

	private Ticket showFrequentList(Number[] numbers , Number[] stars){
		return new Ticket(numbers, stars);   
	}
	
	private Number[] readNumbersFromProperties(String fileProperties, int numberNumbers) 
				throws IOException, NumberFormatException{
		Number[] numbers = new Number[numberNumbers];
	    Properties prop = loadFileProperties(fileProperties);
	    for(int contNumbers=1;contNumbers<=numberNumbers;contNumbers++){
			numbers[contNumbers-1] =  getNumberFromProperty(prop, contNumbers) ;
	    }
	    Arrays.sort(numbers);
		return numbers;
	}
	
	
	private Properties loadFileProperties(String file) throws IOException{
		Properties prop = new Properties();
	    prop.load(context.openFileInput(file));
		return prop;
	}
	
	private Number getNumberFromProperty(Properties prop, int number) throws NumberFormatException{
	        String value = prop.getProperty(String.valueOf(number));
	        Number numberForProperty = new Number(Integer.parseInt(value),number);
	        return numberForProperty;
	}

}
