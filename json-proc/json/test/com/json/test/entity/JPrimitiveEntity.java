package com.json.test.entity;

import com.maestro.xml.json.JDomElement;

public class JPrimitiveEntity {

	@JDomElement
	private int i;
	@JDomElement
	private Integer in;
	@JDomElement
	private long l;
	@JDomElement
	private Long lon;
	@JDomElement
	private short s;
	@JDomElement
	private Short sho;
	@JDomElement
	private byte b;
	@JDomElement
	private Byte by;
	@JDomElement
	private double d;
	@JDomElement
	private Double doub;
	@JDomElement
	private float f;
	@JDomElement
	private Float flo;
	@JDomElement
	private String text;
	
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public Integer getIn() {
		return in;
	}
	public void setIn(Integer in) {
		this.in = in;
	}
	public long getL() {
		return l;
	}
	public void setL(long l) {
		this.l = l;
	}
	public Long getLon() {
		return lon;
	}
	public void setLon(Long lon) {
		this.lon = lon;
	}
	public short getS() {
		return s;
	}
	public void setS(short s) {
		this.s = s;
	}
	public Short getSho() {
		return sho;
	}
	public void setSho(Short sho) {
		this.sho = sho;
	}
	public byte getB() {
		return b;
	}
	public void setB(byte b) {
		this.b = b;
	}
	public Byte getBy() {
		return by;
	}
	public void setBy(Byte by) {
		this.by = by;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public Double getDoub() {
		return doub;
	}
	public void setDoub(Double doub) {
		this.doub = doub;
	}
	public float getF() {
		return f;
	}
	public void setF(float f) {
		this.f = f;
	}
	public Float getFlo() {
		return flo;
	}
	public void setFlo(Float flo) {
		this.flo = flo;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
