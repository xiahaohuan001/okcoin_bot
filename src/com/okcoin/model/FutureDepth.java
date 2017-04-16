package com.okcoin.model;

public class FutureDepth {
	Double askprice;	
	Double askfvol;
	Double asksvol;
	Double asksfvol;
	Double askssvol;
	Double bidprice;
	Double bidfvol;
	Double bidsvol;
	Double bidsfvol;
	Double bidssvol;
	public Double getAsksfvol() {
		return asksfvol;
	}
	public void setAsksfvol(String asksfvol) {
		this.asksfvol = Double.parseDouble(asksfvol);
	}
	public Double getAskssvol() {
		return askssvol;
	}
	public void setAskssvol(String askssvol) {
		this.askssvol = Double.parseDouble(askssvol);
	}
	public Double getBidsfvol() {
		return bidsfvol;
	}
	public void setBidsfvol(String bidsfvol) {
		this.bidsfvol = Double.parseDouble(bidsfvol);
	}
	public Double getBidssvol() {
		return bidssvol;
	}
	public void setBidssvol(String bidssvol) {
		this.bidssvol = Double.parseDouble(bidssvol);
	}
	long time;
	public Double getAskprice() {
		return askprice;
	}
	public void setAskprice(String askprice) {
		this.askprice = Double.parseDouble(askprice);
	}
	public Double getAskfvol() {
		return askfvol;
	}
	public void setAskfvol(String askfvol) {
		this.askfvol = Double.parseDouble(askfvol);
	}
	public Double getAsksvol() {
		return asksvol;
	}
	public void setAsksvol(String asksvol) {
		this.asksvol = Double.parseDouble(asksvol);
	}
	public Double getBidprice() {
		return bidprice;
	}
	public void setBidprice(String bidprice) {
		this.bidprice = Double.parseDouble(bidprice);
	}
	public Double getBidfvol() {
		return bidfvol;
	}
	public void setBidfvol(String bidfvol) {
		this.bidfvol = Double.parseDouble(bidfvol);
	}
	public Double getBidsvol() {
		return bidsvol;
	}
	public void setBidsvol(String bidsvol) {
		this.bidsvol = Double.parseDouble(bidsvol);
	}
	public long getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = Long.parseLong(time);
	}	
}
