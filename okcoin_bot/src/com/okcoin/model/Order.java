package com.okcoin.model;

public class Order {
	Double amount;
	Double avgprice;
	long creatdate;
	Double dealamount;
	String orderid;
	String ordersid;
	Double price;
	String status;
	String symbol;
	String type;
	public Double getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = Double.parseDouble(amount);
	}
	public Double getAvgprice() {
		return avgprice;
	}
	public void setAvgprice(String avgprice) {
		this.avgprice = Double.parseDouble(avgprice);
	}
	public long getCreatdate() {
		return creatdate;
	}
	public void setCreatdate(String creatdate) {
		this.creatdate = Long.parseLong(creatdate);
	}
	public Double getDealamount() {
		return dealamount;
	}
	public void setDealamount(String dealamount) {
		this.dealamount = Double.parseDouble(dealamount);
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrdersid() {
		return ordersid;
	}
	public void setOrdersid(String ordersid) {
		this.ordersid = ordersid;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = Double.parseDouble(price);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
