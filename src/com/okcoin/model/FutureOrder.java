package com.okcoin.model;

public class FutureOrder {
	Double amount;//委托数量
	String contract_name;//合约名称
	long create_date;//委托时间
	Double deal_amount;//成交数量
	Double fee;//手续费
	String order_id;//订单ID
	Double price;//订单价格
	Double price_avg;//平均价格
	String status;//订单状态(0等待成交 1部分成交 2全部成交 -1撤单 4撤单处理中)
	String symbol;//btc_usd:比特币,ltc_usd:莱特币
	int type;//订单类型 1：开多 2：开空 3：平多 4： 平空
	Double unit_amount;//合约面值
	int lever_rate;//杠杆倍数  value:10\20  默认10 
	public Double getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount =  Double.parseDouble(amount);
	}
	public String getContract_name() {
		return contract_name;
	}
	public void setContract_name(String contract_name) {
		this.contract_name = contract_name;
	}
	public long getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = Long.parseLong(create_date);
	}
	public Double getDeal_amount() {
		return deal_amount;
	}
	public void setDeal_amount(String deal_amount) {
		this.deal_amount =  Double.parseDouble(deal_amount);
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee =  Double.parseDouble(fee);
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price =  Double.parseDouble(price);
	}
	public Double getPrice_avg() {
		return price_avg;
	}
	public void setPrice_avg(String price_avg) {
		this.price_avg =  Double.parseDouble(price_avg);
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
	public int getType() {
		return type;
	}
	public void setType(String type) {
		this.type = Integer.parseInt(type);
	}
	public Double getUnit_amount() {
		return unit_amount;
	}
	public void setUnit_amount(String unit_amount) {
		this.unit_amount =  Double.parseDouble(unit_amount);
	}
	public int getLever_rate() {
		return lever_rate;
	}
	public void setLever_rate(String lever_rate) {
		this.lever_rate = Integer.parseInt(lever_rate);
	}
}
