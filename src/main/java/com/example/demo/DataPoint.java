package com.example.demo;


public class DataPoint {
	private String xAxisValue;
	private int yAxisValue;
	public String getxAxisValue() {
		return xAxisValue;
	}
	public void setxAxisValue(String xAxisValue) {
		this.xAxisValue = xAxisValue;
	}
	public int getyAxisValue() {
		return yAxisValue;
	}
	public void setyAxisValue(int yAxisValue) {
		this.yAxisValue = yAxisValue;
	}
	public DataPoint(String xAxisValue, int yAxisValue) {
//		super();
		this.xAxisValue = xAxisValue;
		this.yAxisValue = yAxisValue;
	}
	

}
