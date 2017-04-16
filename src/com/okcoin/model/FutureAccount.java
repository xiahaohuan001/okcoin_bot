package com.okcoin.model;

public class FutureAccount {
	Double btc_account_rights;//账户权益
	Double btc_keep_deposit;//保证金
	Double btc_profit_real;//已实现盈亏
	Double btc_profit_unreal;//未实现盈亏
	Double btc_risk_rate;//保证金率
	
	Double ltc_account_rights;//账户权益
	Double ltc_keep_deposit;//保证金
	Double ltc_profit_real;//已实现盈亏
	Double ltc_profit_unreal;//未实现盈亏
	Double ltc_risk_rate;//保证金率
	public Double getBtc_account_rights() {
		return btc_account_rights;
	}
	public void setBtc_account_rights(String btc_Daccount_rights) {
		this.btc_account_rights = Double.parseDouble(btc_Daccount_rights);
	}
	public Double getBtc_keep_deposit() {
		return btc_keep_deposit;
	}
	public void setBtc_keep_deposit(String btc_keep_deposit) {
		this.btc_keep_deposit = Double.parseDouble(btc_keep_deposit);
	}
	public Double getBtc_profit_real() {
		return btc_profit_real;
	}
	public void setBtc_profit_real(String btc_profit_real) {
		this.btc_profit_real = Double.parseDouble(btc_profit_real);
	}
	public Double getBtc_profit_unreal() {
		return btc_profit_unreal;
	}
	public void setBtc_profit_unreal(String btc_profit_unreal) {
		this.btc_profit_unreal = Double.parseDouble(btc_profit_unreal);
	}
	public Double getBtc_risk_rate() {
		return btc_risk_rate;
	}
	public void setBtc_risk_rate(String btc_risk_rate) {
		this.btc_risk_rate = Double.parseDouble(btc_risk_rate);
	}
	public Double getLtc_account_rights() {
		return ltc_account_rights;
	}
	public void setLtc_account_rights(String ltc_account_rights) {
		this.ltc_account_rights = Double.parseDouble(ltc_account_rights);
	}
	public Double getLtc_keep_deposit() {
		return ltc_keep_deposit;
	}
	public void setLtc_keep_deposit(String ltc_keep_deposit) {
		this.ltc_keep_deposit = Double.parseDouble(ltc_keep_deposit);
	}
	public Double getLtc_profit_real() {
		return ltc_profit_real;
	}
	public void setLtc_profit_real(String ltc_profit_real) {
		this.ltc_profit_real = Double.parseDouble(ltc_profit_real);
	}
	public Double getLtc_profit_unreal() {
		return ltc_profit_unreal;
	}
	public void setLtc_profit_unreal(String ltc_profit_unreal) {
		this.ltc_profit_unreal = Double.parseDouble(ltc_profit_unreal);
	}
	public Double getLtc_risk_rate() {
		return ltc_risk_rate;
	}
	public void setLtc_risk_rate(String ltc_risk_rate) {
		this.ltc_risk_rate = Double.parseDouble(ltc_risk_rate);
	}
}
