package com.dt002g.reviewapplication.frontend.util;

import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utility {

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	   try {
		   BigDecimal bd = BigDecimal.valueOf(value);
		   bd = bd.setScale(places, RoundingMode.HALF_UP);
		   return bd.doubleValue();
	   }
	   catch(NumberFormatException e){
		   return value;
	   }

	}
	
	public static void customResize(TableView<?> view) {
		double largest = Double.NEGATIVE_INFINITY;
		Text largestText = new Text("");
		for(int i = 0; i < 1000; i++) {
			try {
				if(view.getColumns().get(2).getCellData(i).toString().length() > largest) {
					largest = view.getColumns().get(2).getCellData(i).toString().length();
					largestText.setText(view.getColumns().get(2).getCellData(i).toString());
				}
			}
			catch(NullPointerException e) {
				break;
			}
		}   
	    view.getColumns().get(2).setMinWidth(largestText.getLayoutBounds().getWidth()+50);
	}
}
