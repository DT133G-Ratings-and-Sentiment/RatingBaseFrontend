package com.dt002g.reviewapplication.frontend.util;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class PieChartHolder {
	private PieChart pieChart;
	private Label label;
	
	public PieChartHolder(PieChart pieChart, Label label) {
		this.pieChart = pieChart;
		this.label = label;
	}
	
	public Label getLabel() {
		return this.label;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public PieChart getPieChart() {
		return this.pieChart;
	}
	
	public void setPieChart(PieChart pieChart) {
		this.pieChart = pieChart;
	}

}
