package com.okcoin.model;

public class Order {
	double amount;
	double avgprice;
	long creatdate;
	double dealamount;
	long orderid;
	long ordersid;
	double price;
	int status;
	String symbol;
	String type;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getAvgprice() {
		return avgprice;
	}
	public void setAvgprice(double avgprice) {
		this.avgprice = avgprice;
	}
	public long getCreatdate() {
		return creatdate;
	}
	public void setCreatdate(long creatdate) {
		this.creatdate = creatdate;
	}
	public double getDealamount() {
		return dealamount;
	}
	public void setDealamount(double dealamount) {
		this.dealamount = dealamount;
	}
	public long getOrderid() {
		return orderid;
	}
	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}
	public long getOrdersid() {
		return ordersid;
	}
	public void setOrdersid(long ordersid) {
		this.ordersid = ordersid;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
