package com.okcoin.model;

public class Track {
	Double up_value;
	Double down_value;
	Double up_st;
	Double mid_st;
	Double down_st;
	long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Double getUp_value() {
		return up_value;
	}
	public void setUp_value(Double up_value) {
		this.up_value = up_value;
	}
	public Double getDown_value() {
		return down_value;
	}
	public void setDown_value(Double down_value) {
		this.down_value = down_value;
	}
	public Double getUp_st() {
		return up_st;
	}
	public void setUp_st(Double up_st) {
		this.up_st = up_st;
	}
	public Double getMid_st() {
		return mid_st;
	}
	public void setMid_st(Double mid_st) {
		this.mid_st = mid_st;
	}
	public Double getDown_st() {
		return down_st;
	}
	public void setDown_st(Double down_st) {
		this.down_st = down_st;
	}
}
