package com.okcoin.model;

public class StockAccount {
	Double money;
	Double frozenmoney;
	Double btcamount;
	Double frozenbtcamount;
	Double ltcamount;
	Double frozenltcamount;
	public Double getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = Double.parseDouble(money);
	}
	public Double getFrozenmoney() {
		return frozenmoney;
	}
	public void setFrozenmoney(String frozenmoney) {
		this.frozenmoney = Double.parseDouble(frozenmoney);
	}
	public Double getBtcamount() {
		return btcamount;
	}
	public void setBtcamount(String btcamount) {
		this.btcamount = Double.parseDouble(btcamount);
	}
	public Double getFrozenbtcamount() {
		return frozenbtcamount;
	}
	public void setFrozenbtcamount(String frozenbtcamount) {
		this.frozenbtcamount = Double.parseDouble(frozenbtcamount);
	}
	public Double getLtcamount() {
		return ltcamount;
	}
	public void setLtcamount(String ltcamount) {
		this.ltcamount = Double.parseDouble(ltcamount);
	}
	public Double getFrozenltcamount() {
		return frozenltcamount;
	}
	public void setFrozenltcamount(String frozenltcamount) {
		this.frozenltcamount = Double.parseDouble(frozenltcamount);
	}
	
}
