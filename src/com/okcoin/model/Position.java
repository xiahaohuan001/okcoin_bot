package com.okcoin.model;

public class Position {
	Double buy_amount;//多仓数量
	Double buy_available;//多仓可平仓数量
	Double buy_price_avg;//开仓平均价
	Double buy_price_cost;//结算基准价
	Double buy_profit_real;//多仓已实现盈余
	Long contract_id;//合约id
	Long create_date;//创建日期
	String lever_rate;//杠杆倍数
	Double sell_amount;//空仓数量
	Double sell_available;//空仓可平仓数量
	Double sell_price_avg;//开仓平均价
	Double sell_price_cost;//结算基准价
	Double sell_profit_real;//空仓已实现盈余
	String symbol;//btc_usd;//比特币,ltc_usd;//莱特币
	String contract_type;//合约类型
	String force_liqu_price;//预估爆仓价
	public Double getBuy_amount() {
		return buy_amount;
	}
	public void setBuy_amount(String buy_amount) {
		this.buy_amount = Double.parseDouble(buy_amount);
	}
	public Double getBuy_available() {
		return buy_available;
	}
	public void setBuy_available(String buy_available) {
		this.buy_available = Double.parseDouble(buy_available);
	}
	public Double getBuy_price_avg() {
		return buy_price_avg;
	}
	public void setBuy_price_avg(String buy_price_avg) {
		this.buy_price_avg = Double.parseDouble(buy_price_avg);
	}
	public Double getBuy_price_cost() {
		return buy_price_cost;
	}
	public void setBuy_price_cost(String buy_price_cost) {
		this.buy_price_cost = Double.parseDouble(buy_price_cost);
	}
	public Double getBuy_profit_real() {
		return buy_profit_real;
	}
	public void setBuy_profit_real(String buy_profit_real) {
		this.buy_profit_real = Double.parseDouble(buy_profit_real);
	}
	public Long getContract_id() {
		return contract_id;
	}
	public void setContract_id(String contract_id) {
		this.contract_id = Long.parseLong(contract_id);
	}
	public Long getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = Long.parseLong(create_date);
	}
	public String getLever_rate() {
		return lever_rate;
	}
	public void setLever_rate(String lever_rate) {
		this.lever_rate = lever_rate;
	}
	public Double getSell_amount() {
		return sell_amount;
	}
	public void setSell_amount(String sell_amount) {
		this.sell_amount = Double.parseDouble(sell_amount);
	}
	public Double getSell_available() {
		return sell_available;
	}
	public void setSell_available(String sell_available) {
		this.sell_available = Double.parseDouble(sell_available);
	}
	public Double getSell_price_avg() {
		return sell_price_avg;
	}
	public void setSell_price_avg(String sell_price_avg) {
		this.sell_price_avg = Double.parseDouble(sell_price_avg);
	}
	public Double getSell_price_cost() {
		return sell_price_cost;
	}
	public void setSell_price_cost(String sell_price_cost) {
		this.sell_price_cost = Double.parseDouble(sell_price_cost);
	}
	public Double getSell_profit_real() {
		return sell_profit_real;
	}
	public void setSell_profit_real(String sell_profit_real) {
		this.sell_profit_real = Double.parseDouble(sell_profit_real);
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getContract_type() {
		return contract_type;
	}
	public void setContract_type(String contract_type) {
		this.contract_type = contract_type;
	}
	public String getForce_liqu_price() {
		return force_liqu_price;
	}
	public void setForce_liqu_price(String force_liqu_price) {
		this.force_liqu_price = force_liqu_price;
	}
	
}
