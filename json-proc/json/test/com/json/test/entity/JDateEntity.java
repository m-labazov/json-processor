package com.json.test.entity;

import java.util.Date;

import com.maestro.xml.json.JDomElement;

public class JDateEntity {

	@JDomElement
	private Date simpleDate;
	@JDomElement
	private Date monthDate;
	@JDomElement
	private Date timeDate;
	public Date getSimpleDate() {
		return simpleDate;
	}
	public void setSimpleDate(Date simpleDate) {
		this.simpleDate = simpleDate;
	}
	public Date getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(Date monthDate) {
		this.monthDate = monthDate;
	}
	public Date getTimeDate() {
		return timeDate;
	}
	public void setTimeDate(Date timeDate) {
		this.timeDate = timeDate;
	}
	
}
