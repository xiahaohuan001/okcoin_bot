package com.okcoin.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import com.okcoin.model.FutureAccount;
import com.okcoin.model.FutureDepth;
import com.okcoin.model.FutureOrder;
import com.okcoin.model.Position;
import com.okcoin.model.StockAccount;
import com.okcoin.rest.future.IFutureRestApi;
import com.okcoin.rest.future.impl.FutureRestApiV1;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.test.Untils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class dq {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	String url_prex_cn = "https://www.okcoin.cn";
	String url_prex_com = "https://www.okcoin.com";
	
	String apiKey_cn = "46674f7f-d06b-44f3-ac79-62945e4e6e6d";
	String secretKey_cn = "45EE20A0F9285983811E91A0A09E3989";
	
	String apiKey_com_b = "5a395d18-6dc9-44b9-9b88-b13c7831feb9";
	String secretKey_com_b = "6702CA3A2AE744D6810249582D132A28";
	
	String apiKey_com_h = "b940b748-835a-41e1-a8a2-87f7fca3f048";
	String secretKey_com_h = "9012DD64DA5A886F5E9A5F792BE890F2";
	
	IFutureRestApi futureGeth = new FutureRestApiV1(url_prex_com);
	IFutureRestApi futurePosth = new FutureRestApiV1(url_prex_com, apiKey_com_h, secretKey_com_h);
	
	IFutureRestApi futureGetb = new FutureRestApiV1(url_prex_com);
	IFutureRestApi futurePostb = new FutureRestApiV1(url_prex_com, apiKey_com_b, secretKey_com_b);
	
	Position positionh = new Position();
	Position positionb = new Position();
	FutureAccount Accounth = new FutureAccount();
	FutureAccount Accountb = new FutureAccount();
	List<FutureDepth> this_week = new ArrayList<>();
	List<FutureDepth> next_week = new ArrayList<>();
	List<FutureDepth> quarter = new ArrayList<>();
	Untils untils = new Untils();
	int Lock = 4;
	double lastright = 0;
	long start = System.currentTimeMillis();
	String last_contract_type;
	public void OnTick(String tick) throws HttpException, IOException {
		if (tick.indexOf("this_week") != -1)
			this_week = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		if (tick.indexOf("next_week") != -1)
			next_week = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		if (tick.indexOf("quarter") != -1)
			quarter = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		//log.info("123"+tick);
		if(Lock == 0){
			if(next_week.size()>0){				
				if(next_week.get(0).getAskprice() - next_week.get(0).getBidprice()>0.4){
					//log.info(next_week.get(0).getAskprice() - next_week.get(0).getBidprice());
					Accounth = UpdateFutureAccount(futurePosth.future_userinfo());
					Accountb = UpdateFutureAccount(futurePostb.future_userinfo());
					Double ask = next_week.get(0).getAskprice() - 0.05;
					Double bid = next_week.get(0).getBidprice() + 0.05;
					double damount = Accounth.getLtc_account_rights()*2*next_week.get(0).getBidprice();
					int amount = (int)damount;
					if(amount<1)
						Lock = 3;
					String rb = futurePostb.future_trade("ltc_usd", "next_week", ask.toString(), String.valueOf(amount), String.valueOf(2), String.valueOf(0));
					String rh = futurePosth.future_trade("ltc_usd", "next_week", ask.toString(), String.valueOf(amount), String.valueOf(1), String.valueOf(0));
					
					log.info(rb+rh+Lock);
					//last_contract_type = "next_week";
					Lock = 1;
				}
			}				
		}if(Lock == 1){
			if(next_week.size()>0){
				positionh = UpdatePosition(futurePosth.future_position("ltc_usd", "next_week"));
				positionb = UpdatePosition(futurePostb.future_position("ltc_usd", "next_week"));
				Double ask = next_week.get(0).getAskprice() - 0.05;
				Double bid = next_week.get(0).getBidprice() + 0.05;
				Double amount = positionh.getBuy_available();
				log.info(amount+"        "+bid);
				String rb = futurePostb.future_trade("ltc_usd","next_week",bid.toString(),amount.toString(),"4","0");
				String rh = futurePosth.future_trade("ltc_usd","next_week",bid.toString(),amount.toString(),"3","0");
				log.info(rb+rh+Lock);
				Lock = 3;
			}
			
				
			
			
		}if(Lock == 3)
			log.info("黑号币不够");
		if(next_week.size()>0){
			//log.info(next_week.get(0).getAskprice() - next_week.get(0).getBidprice());
			//if(next_week.get(0).getAskprice() - next_week.get(0).getBidprice()>0.3)
				//log.info(next_week.get(0).getAskprice()+"      "+next_week.get(0).getBidprice());
		}
		Accounth = untils.GetAccountFuture(futurePosth);
		log.info(Accounth.getBtc_account_rights());
			
	}
	
	
	
	public StockAccount UpdateStockAccount(String S) {
		StockAccount StockAccount = new StockAccount();
		if (S.indexOf("true") != -1) {
			JSONObject AccountJSON = JSONObject.fromObject(S);
			JSONObject info = (JSONObject) AccountJSON.get("info");
			JSONObject funds = (JSONObject) info.get("funds");
			JSONObject asset = (JSONObject) funds.get("asset");
			JSONObject free = (JSONObject) funds.get("free");
			JSONObject freezed = (JSONObject) funds.get("freezed");
			StockAccount.setMoney(free.getString("cny"));
			StockAccount.setBtcamount(free.getString("btc"));
			StockAccount.setLtcamount(free.getString("ltc"));
			StockAccount.setFrozenmoney(freezed.getString("cny"));
			StockAccount.setFrozenbtcamount(freezed.getString("btc"));
			StockAccount.setFrozenltcamount(freezed.getString("ltc"));
		}
		return StockAccount;
	}

	public FutureAccount UpdateFutureAccount(String S) {
		FutureAccount FutureAccount = new FutureAccount();
		if (S.indexOf("true") != -1) {
			JSONObject AccountJSON = JSONObject.fromObject(S);
			JSONObject info = (JSONObject) AccountJSON.get("info");
			JSONObject btc = (JSONObject) info.get("btc");
			JSONObject ltc = (JSONObject) info.get("ltc");
			FutureAccount.setBtc_account_rights(btc.getString("account_rights"));
			FutureAccount.setBtc_keep_deposit(btc.getString("keep_deposit"));
			FutureAccount.setBtc_profit_real(btc.getString("profit_real"));
			FutureAccount.setBtc_profit_unreal(btc.getString("profit_unreal"));
			FutureAccount.setBtc_risk_rate(btc.getString("risk_rate"));

			FutureAccount.setLtc_account_rights(ltc.getString("account_rights"));
			FutureAccount.setLtc_keep_deposit(ltc.getString("keep_deposit"));
			FutureAccount.setLtc_profit_real(ltc.getString("profit_real"));
			FutureAccount.setLtc_profit_unreal(ltc.getString("profit_unreal"));
			FutureAccount.setLtc_risk_rate(ltc.getString("risk_rate"));
		}
		return FutureAccount;
	}

	public Position UpdatePosition(String S) {
		Position Position = new Position();
		if (S.indexOf("true") != -1) {
			JSONObject PositonJSON = JSONObject.fromObject(S);
			Position.setForce_liqu_price(PositonJSON.getString("force_liqu_price"));
			JSONArray holding = (JSONArray) PositonJSON.get("holding");
			Position.setBuy_amount(((JSONObject) holding.get(0)).getString("buy_amount"));
			Position.setBuy_available(((JSONObject) holding.get(0)).getString("buy_available"));
			Position.setBuy_price_avg(((JSONObject) holding.get(0)).getString("buy_price_avg"));
			Position.setBuy_price_cost(((JSONObject) holding.get(0)).getString("buy_price_cost"));
			Position.setBuy_profit_real(((JSONObject) holding.get(0)).getString("buy_profit_real"));
			Position.setContract_id(((JSONObject) holding.get(0)).getString("contract_id"));
			Position.setContract_type(((JSONObject) holding.get(0)).getString("contract_type"));
			Position.setCreate_date(((JSONObject) holding.get(0)).getString("create_date"));
			Position.setLever_rate(((JSONObject) holding.get(0)).getString("lever_rate"));
			Position.setSell_amount(((JSONObject) holding.get(0)).getString("sell_amount"));
			Position.setSell_available(((JSONObject) holding.get(0)).getString("sell_available"));
			Position.setSell_price_avg(((JSONObject) holding.get(0)).getString("sell_price_avg"));
			Position.setSell_price_cost(((JSONObject) holding.get(0)).getString("sell_price_cost"));
			Position.setSell_profit_real(((JSONObject) holding.get(0)).getString("sell_profit_real"));
			Position.setSymbol(((JSONObject) holding.get(0)).getString("symbol"));
		}
		return Position;
	}
}
