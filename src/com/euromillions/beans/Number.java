package com.euromillions.beans;

import java.io.Serializable;

public class Number implements Comparable<Number>, Serializable {
	
	private static final long serialVersionUID = -3092621129697984863L;
	
	private int times = 0;
	private int number = 0;
	
	public Number(int times, int number){
		super();
		this.times = times;
		this.number = number;
	}
	
	public int compareTo(Number number) {
		return -(new Integer(getTimes()).compareTo(new Integer(number.getTimes())));
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getTimes() {
		return times;
	}
	
}
