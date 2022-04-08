package com.dt002g.reviewapplication.frontend.util;

import com.stanford_nlp.SentimentAnalyser.SentenceScore;
import com.stanford_nlp.SentimentAnalyser.SentimentAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVHandler {
	private static CSVHandler instance = null;
	private CSVHandler() {}
	public static CSVHandler getInstance() {
		if(instance == null)
			instance = new CSVHandler();
		return instance;
	}
	
	
	public ArrayList<String> getHeades(File file){
		ArrayList<String> headersToUse = new ArrayList<String>();
	    String headersRow;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			headersRow = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	if(headersRow.charAt(0) == '\"' && headersRow.charAt(headersRow.length()-1) == '\"') {
    		headersRow = headersRow.substring(1, headersRow.length()-1);
    	}
	    String[] headers = headersRow.split(",");
	    for(int i = 0; i < headers.length; i++) {
	    	headersToUse.add(headers[i]);		
	    }
		return headersToUse;
	}
	
	public ArrayList<String> parseCSVFile(ArrayList<String> neededHeaders, File file, int minRating, int maxRating){
		ArrayList<String> data = new ArrayList<String>();
		SentimentAnalyser senti = new SentimentAnalyser();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String headersRow = br.readLine();
		    String[] headers = headersRow.split(",");
		    ArrayList<Integer> headerIndexes = new ArrayList<>();
	    	for(String s: neededHeaders) {
	    		for(int i = 0; i < headers.length; i++) {
		    		if(s.equalsIgnoreCase(headers[i])) {
		    			headerIndexes.add(i);
		    		}
	    		}
	    	}
		    if(headerIndexes.size() != neededHeaders.size()) {
		    	return null;
		    }
		    String line; 
		    int numberOfLines = 0;
		    while ((line = br.readLine()) != null) {
		    	numberOfLines++;
		    	if(line.charAt(0) == '\"' && line.charAt(line.length()-1) == '\"') {
		    		line = line.substring(1, line.length()-1);
		    	}
			    String row = "";
			    ArrayList<String> rowData = new ArrayList<>();
			    while(line.length() > 0) {
				    if(line.charAt(0) == '\"') {
				    	while(line.charAt(0) == '\"' && line.length() >2) {
				    		line = line.substring(line.indexOf("\"") +1);
				    	}
				    	if(line.indexOf(",") == -1) {
				    		rowData.add(line);
				    		line = "";
				    		break;
				    	}
				    	else {
				    		if(line.indexOf("\",", 0) != -1) {
				    			row = line.substring(0, line.indexOf("\","));
						    	line = line.substring(line.indexOf("\","));
				    		}
				    		else{
				    			row = line.substring(0, line.indexOf(","));
						    	line = line.substring(line.indexOf(","));
				    		}
				    	}
				    	while(line.charAt(0) == '\"' && line.length() > 2) {
				    		line = line.substring(line.indexOf("\"") +1);
				    	}
				    	if(line.indexOf(",") == -1)
				    		break;
				    	line = line.substring(line.indexOf(","));
				    	if(line.length() > 0)
				    		line = line.substring(1);
				    	rowData.add(row);
				    }
				    else {
				    	if(line.indexOf(",") == -1) {
				    		rowData.add(line);
				    		break;
				    	}				    		
				    	row = line.substring(0, line.indexOf(","));
				    	line = line.substring(line.indexOf(","));
				    	if(line.length() > 0)
				    		line = line.substring(1);
				    	rowData.add(row);				    	
				    }
			    }
			    try {
			    	String tempRow = "";
				    for(int i = 0; i < headerIndexes.size(); i++) {
				    	if(i == 0) {
							// Transform to a common rating scale of 0-100  with formula:
							// (rating – 1)/(number of response categories – 1) × 100.
							// If the actual rating is bigger than the anonced maxrating it get max value
							//if its lower it get 0 value.
							double rawRating = Double.parseDouble(rowData.get(headerIndexes.get(i)));
							if(rawRating > maxRating){
								rawRating = maxRating;
							}
							else if(rawRating < minRating){
								rawRating = minRating;
							}
							int rating = (int)Math.round((rawRating -1)/ ((((double)maxRating+1)-(double)minRating) -1) * 100);
							tempRow += rating + ";#";
							/*old algorithm
							int temp = Integer.parseInt(rowData.get(headerIndexes.get(i)))%(maxRating+1);
							int divider = 100/(maxRating -minRating);
							int result = divider * (temp-minRating);
							if(temp == maxRating)
								result = 100;
							tempRow += result + ";#";*/
				    	}
				    	else if(i ==1){
				    		//tempRow += rowData.get(headerIndexes.get(i)) + ";#";
							tempRow += handleFreeText(rowData.get(headerIndexes.get(i)));
				    	}
				    }
				    //data.add(tempRow.substring(0, tempRow.length() -2));
					data.add(tempRow);
			    }catch(NumberFormatException e) {
			    	System.out.println("NumberFormatException: " + numberOfLines );
			    	e.printStackTrace();
			    }catch(java.lang.IndexOutOfBoundsException e){
			    	System.out.println("NumberFormatException: " + numberOfLines );
			    	e.printStackTrace();
			    }
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return data;
	}

	private String handleFreeText(String freeText){
		String result = freeText + ";#";
		SentimentAnalyser senti = new SentimentAnalyser();
		List<SentenceScore> sentenceScore =  senti.getSentimentResult(freeText);
		//String[] sentences =freeText.split(".");

		//3;#okay. You are fine and good and fine. Great for you.;#okay.;£0.0;¤10.0;¤40.0;¤30.0;¤20.0;@You are fine and good and fine.;£30.0;¤20.0;¤30.0;¤15.0;¤5.0;£fine;¤good;¤fine;@Great for you.;£0.0;¤20.0;¤30.0;¤30.0;¤20.0;£Great;#4
		for(SentenceScore s: sentenceScore){
			result += s.getSentence() + ";£" + s.getVeryPositive() + ";¤" + s.getPositive() + ";¤" + s.getNeutral() + ";¤" + s.getNegative() + ";¤" + s.getVeryNegative() +";£" + getScoreInNumber(s.getSentimentType());
			List<String> adjectives = senti.getAdjectives(s.getSentence());
			if(adjectives.size() > 0) {
				result += ";£";
				for (String a : adjectives) {
						result += a + ";¤";
				}
				result = result.substring(0, result.length() -2);
			}
			result += ";@";
		}
		result = result.substring(0, result.length() -2);
		return result;
	}
	 private int getScoreInNumber(String score){
		switch (score){
			case "Very Positive":
				return 5;
			case "Positive":
				return 4;
			case "Neutral":
				return 3;
			case "Negative":
				return 2;
			case "Very Negative":
				return 1;
		}
		return 0;
	 }
}
