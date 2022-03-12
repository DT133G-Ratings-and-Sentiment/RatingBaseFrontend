package com.dt002g.reviewapplication.frontend.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.text.Text;

public class Utility {

	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
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
