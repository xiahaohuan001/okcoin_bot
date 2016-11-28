package com.okcoin.model;

public class Account {
	double money;
	double frozenmoney;
	double stocks;
	double frozenstocks;
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getFrozenmoney() {
		return frozenmoney;
	}
	public void setFrozenmoney(double frozenmoney) {
		this.frozenmoney = frozenmoney;
	}
	public double getStocks() {
		return stocks;
	}
	public void setStocks(double stocks) {
		this.stocks = stocks;
	}
	public double getFrozenstocks() {
		return frozenstocks;
	}
	public void setFrozenstocks(double frozenstocks) {
		this.frozenstocks = frozenstocks;
	}
}
