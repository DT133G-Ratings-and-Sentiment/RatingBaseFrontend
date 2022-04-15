package com.dt002g.reviewapplication.frontend.util;

import com.dt002g.reviewapplication.frontend.service.UploadCSVFileCallBack;
import com.stanford_nlp.SentimentAnalyser.SentenceScore;
import com.stanford_nlp.SentimentAnalyser.SentimentAnalyser;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CSVHandler implements UploadCSVFileCallBack {
	private static CSVHandler instance = null;
	private CSVHandler() {}
	private static ExecutorService executorService = Executors.newFixedThreadPool(1);
	public boolean cancel = false;
	public IntegerProperty numberOfParsedRows = new SimpleIntegerProperty(0);
	public IntegerProperty numberOfExportedRows = new SimpleIntegerProperty(0);
	public IntegerProperty numberOfFailedRows = new SimpleIntegerProperty(0);
	public StringProperty failedLineProp = new SimpleStringProperty("");
	private ArrayList<String> failedLines;
	public static CSVHandler getInstance() {
		if(instance == null)
			instance = new CSVHandler();
		return instance;
	}
	
	
	public ArrayList<String> getHeaders(File file){
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
	
	/*public ArrayList<String> parseCSVFile(ArrayList<String> neededHeaders, File file, int minRating, int maxRating){
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
				System.out.println(line);
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
							//old algorithm
							//int temp = Integer.parseInt(rowData.get(headerIndexes.get(i)))%(maxRating+1);
							//int divider = 100/(maxRating -minRating);
							//int result = divider * (temp-minRating);
							//if(temp == maxRating)
							//	result = 100;
							//tempRow += result + ";#";
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
	}*/

	public boolean parseCSVFileAndSend(ArrayList<String> neededHeaders, File file, int minRating, int maxRating){
		numberOfParsedRows.setValue(0);
		numberOfExportedRows.setValue(0);
		numberOfFailedRows.setValue(0);
		failedLines = new ArrayList<>();
		cancel = false;
		int fileNr = 1;
		int nrOfRows = 0;
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
				return false;
			}
			String line;
			int numberOfLines = 0;
			while ((line = br.readLine()) != null && !cancel) {
				String rawLine = line;
				numberOfLines++;
				if(line.startsWith("\"") && line.endsWith("\"")) {
					line = line.substring(1, line.length()-1);
				}
				String row = "";
				ArrayList<String> rowData = new ArrayList<>();
				while(line.length() > 0) {
					line = line.trim();
					if(line.startsWith("\"")) {
						while(line.charAt(0) == '\"' && line.length() >2) {
							line = line.substring(line.indexOf("\"") +1);
						}
						if(line.indexOf(",") == -1) {
							System.out.println("here");
							rowData.add(line);
							line = "";
							break;
						}
						else {
							if(line.indexOf("\",", 0) != -1) {
								row = line.substring(0, line.indexOf("\","));
								line = line.substring(line.indexOf("\","));
							}
							else if(line.endsWith("\"")){
								row = line.substring(0, line.lastIndexOf("\""));
								line = line.substring(line.lastIndexOf("\""));
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
					boolean addRow = true;
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


						}
						else if(i ==1){
							if(rowData.get(headerIndexes.get(i)).length() > 2000) {
								addRow = false;
							}
							else {
								tempRow += handleFreeText(rowData.get(headerIndexes.get(i)));
							}
						}
					}
					if(addRow) {
						data.add(tempRow);
						nrOfRows += 1;
						numberOfParsedRows.setValue(numberOfParsedRows.getValue() + 1);
					}
					else{
						failedLines.add(rawLine);
						failedLineProp.setValue(rawLine);
						numberOfFailedRows.setValue(failedLines.size());
						System.out.println("Free text longer than 2000 characters: " + numberOfLines );
					}
				}catch(NumberFormatException e) {
					System.out.println("NumberFormatException: " + numberOfLines );
					failedLines.add(rawLine);
					failedLineProp.setValue(rawLine);
					numberOfFailedRows.setValue(failedLines.size());
					e.printStackTrace();
				}catch(java.lang.IndexOutOfBoundsException e){
					failedLines.add(rawLine);
					failedLineProp.setValue(rawLine);
					numberOfFailedRows.setValue(failedLines.size());
					System.out.println("NumberFormatException: " + numberOfLines );
					e.printStackTrace();
				}catch(Exception e){
					failedLines.add(rawLine);
					failedLineProp.setValue(rawLine);
					numberOfFailedRows.setValue(failedLines.size());
					e.printStackTrace();
				}
				if(nrOfRows == 100){
					ArrayList clonedList = (ArrayList) data.clone();
					SendCSVFileTask task = new SendCSVFileTask(clonedList, "localCsvFile" + fileNr + ".csv", this);
					executorService.execute(task);
					//sendBatch(clonedList, "localCsvFile" + fileNr + ".csv");
					data.clear();
					fileNr += 1;
					nrOfRows = 0;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data.size() > 0 && !cancel){
			SendCSVFileTask task = new SendCSVFileTask(data, "localCsvFile" + fileNr + ".csv", this);
			executorService.execute(task);
		}
		return true;
	}

	/*private void sendBatch(ArrayList<String> data, String fileName){
		File myObj = new File(fileName);
		try {
			if (myObj.createNewFile()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
				for(String s: data) {
					writer.write(s + "\n");

				}
				writer.close();

				ReviewBackendAPIService.getInstance().uploadCSVFile(myObj);
			}
		} catch (IOException | SecurityException e) {
			System.out.println(e.getMessage());
		}

	}*/

	private String handleFreeText(String freeText){
		String result = freeText + ";#";
		SentimentAnalyser senti = new SentimentAnalyser();
		List<SentenceScore> sentenceScore =  senti.getSentimentResult(freeText);
		//String[] sentences =freeText.split(".");

		//3;#okay. You are fine and good and fine. Great for you.;#okay.;£0.0;¤10.0;¤40.0;¤30.0;¤20.0;@You are fine and good and fine.;£30.0;¤20.0;¤30.0;¤15.0;¤5.0;£fine;¤good;¤fine;@Great for you.;£0.0;¤20.0;¤30.0;¤30.0;¤20.0;£Great;#4
		double totalScore = 0;
		ArrayList<Double> scores = new ArrayList<>();
		for(SentenceScore s: sentenceScore){
			int scoreNumber = getScoreInNumber(s.getSentimentType());
			double normalisedScore = (((double)scoreNumber -1.0)/ 4.0) * 100.0;
			scores.add(normalisedScore);
			totalScore += normalisedScore;
			result += s.getSentence() + ";£" + s.getVeryPositive() + ";¤" + s.getPositive() + ";¤" + s.getNeutral() + ";¤" + s.getNegative() + ";¤" + s.getVeryNegative() +";£" + scoreNumber +";£" +normalisedScore;
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
		double avgScore = totalScore /sentenceScore.size();
		Collections.sort(scores);
		double medianScore = 0;
		if(scores.size() >=2 && scores.size() %2 ==0) {
			medianScore = (scores.get((scores.size() / 2)) + scores.get((scores.size() / 2)-1))/2;
		}
		else if(scores.size() >=2){
			medianScore = scores.get((scores.size() / 2));
		}
		else{
			medianScore = scores.get(0);
		}
		result += ";#" + avgScore + ";#" + medianScore;
		//result = result.substring(0, result.length() -2);
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

	@Override
	public void processUploadCSVFileCallBack(int amount) {
		numberOfExportedRows.setValue(numberOfExportedRows.getValue() +amount);
	}

	public ArrayList<String> getFailedLines(){
		return failedLines;
	}
}
