package com.okcoin.model;

public class Depth {
	double askprice;
	double askvol;
	double bidprice;
	double bidvol;
	public Double getAskprice() {
		return askprice;
	}
	public void setAskprice(String askprice) {
		this.askprice = Double.parseDouble(askprice);
	}
	public Double getAskvol() {
		return askvol;
	}
	public void setAskvol(String askvol) {
		this.askvol = Double.parseDouble(askvol);
	}
	public Double getBidprice() {
		return bidprice;
	}
	public void setBidprice(String bidprice) {
		this.bidprice = Double.parseDouble(bidprice);
	}
	public Double getBidvol() {
		return bidvol;
	}
	public void setBidvol(String bidvol) {
		this.bidvol = Double.parseDouble(bidvol);
	}
	
}
