package com.euromillions.beans;

import java.io.Serializable;

public class Ticket implements Serializable {
	
	private static final long serialVersionUID = -9163427066175045336L;
	
	private long time;
	private Number[] numbers;
	private Number[] stars;
	
	public Ticket(Number[] numbers, Number[] stars) {
		this.time = System.currentTimeMillis();
		this.numbers = numbers;
		this.stars = stars;
	}
	
	
	public long getTime() {
		return time;
	}
	
	public Number[] getNumbers() {
		return numbers;
	}
	
	public Number[] getStars() {
		return stars;
	}
}
