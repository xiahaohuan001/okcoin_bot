package com.okcoin.strategy;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.jquantlib.JQuantLib;
import org.jquantlib.math.matrixutilities.Array;
import com.okcoin.exchange.*;
import com.okcoin.model.*;
import com.okcoin.rest.future.IFutureRestApi;
import com.okcoin.rest.future.impl.FutureRestApiV1;
import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.test.BuissnesWebSocketServiceImpl;
import com.okcoin.websocket.test.Untils;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.okcoin.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.*;
import java.net.Socket;

public class future_hedge_future {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	List<FutureDepth> F_Depth = new ArrayList<>();
	List<FutureDepth> S_Depth = new ArrayList<>();
	FutureAccount FutureAccount = new FutureAccount();
	Position Position = new Position();
	StockAccount StockAccount = new StockAccount();
	List<StockOrder> StockOrder = new ArrayList<>();
	List<FutureOrder> FutureOrder = new ArrayList<>();
	long start = System.currentTimeMillis();
	long lasttradetime;
	long now = System.currentTimeMillis();
	long clock1 = 0, clock2 = 0, clock3 = 0, clock4 = 0;
	long F_Time = 2;
	String[] OrderType = { "btc_cny", "ltc_cny" };
	String[] Contract_Type = { "this_week", "next_week", "quarter" };
	Double Line;
	Double P_Diff = 0.3, M_Diff = 0.3, B_Diff = 0.3;
	Double LastDiff;
	Double FB_SS_Diff;
	Double FS_SB_Diff;
	Double UP_Profit, Down_Profit;
	Double Profit = 0.0;
	Double SumProfit = 0.0;
	Double MinProfit = 2.0;
	Double MinAmount = 1.0;
	Double MaxAmount = 100.0;
	Double zksum  = 0.0;
	Double zdsum = 0.0;
	Double Sum = 0.0;
	Double FS_SB_Sum = 0.0, FB_SS_Sum = 0.0;
	Double FBuyPrice, FSellPrice, SBuyPrice, SSellPrice;
	int Balance = 0, LossStop = 0, RiskStop = 0;
	int DiffCount = 0, T_Times = 0;
	Untils untils = new Untils();
	List<Track> track = new ArrayList<>();
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String api_key_cn = "46674f7f-d06b-44f3-ac79-62945e4e6e6d";
	String secret_key_cn = "45EE20A0F9285983811E91A0A09E3989"; // 申请的secret_key
	String url_prex_cn = "https://www.okcoin.cn";
	String api_key_com = "5a395d18-6dc9-44b9-9b88-b13c7831feb9";
	String secret_key_com = "6702CA3A2AE744D6810249582D132A28"; // 申请的secret_key
	String url_prex_com = "https://www.okcoin.com";
	IStockRestApi stockGet = new StockRestApi(url_prex_cn);
	IStockRestApi stockPost = new StockRestApi(url_prex_cn, api_key_cn, secret_key_cn);
	IFutureRestApi futureGet = new FutureRestApiV1(url_prex_com);
	IFutureRestApi futurePost = new FutureRestApiV1(url_prex_com, api_key_com, secret_key_com);

	public void OnTick(String tick) throws HttpException, IOException {
		
		if (tick.indexOf("this_week") != -1)
			F_Depth = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		if (tick.indexOf("quarter") != -1)
			S_Depth = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		if (F_Depth.size() > 0 && S_Depth.size() > 0) {
			FBuyPrice = CalculateFuturePriceCanTrade(MinAmount, F_Depth, "bid");
			FSellPrice = CalculateFuturePriceCanTrade(MinAmount, F_Depth, "ask");
			//System.out.println(FB_SS_Diff);
			SBuyPrice = CalculateFuturePriceCanTrade(MinAmount, S_Depth, "bid");
			SSellPrice = CalculateFuturePriceCanTrade(MinAmount, S_Depth, "ask");
			FB_SS_Diff =  (double) Math.round((FBuyPrice - SSellPrice)*100)/100;
			FS_SB_Diff =  (double) Math.round((FSellPrice - SBuyPrice)*100)/100;
			if (track.size() > 0) {
				Line = track.get(track.size() - 1).getMid_st();
				UP_Profit = Math.max(MinProfit, track.get(track.size() - 1).getUp_st());
				Down_Profit = Math.max(MinProfit, track.get(track.size() - 1).getDown_st());
			}
			if (Line != null) {
				if ((Line + 0.5 * UP_Profit <= FB_SS_Diff) && Balance < MaxAmount) {
					UpdateFutureAccount(futurePost.future_userinfo());
					// if (FutureAccount.getBtcamount() > MinAmount &&
					// StockAccount.getMoney() > LTCAmount * LBuyPrice) {
					log.info("做空价差,当前Line:" + Line + "   价差：" + FB_SS_Diff + "   交易次数：" + T_Times
							+ stockPost.trade("btc_cny", "sell", FSellPrice.toString(), MinAmount.toString())
							+ stockPost.trade("ltc_cny", "buy", SBuyPrice.toString(), MinAmount.toString()));					
					LastDiff = FB_SS_Diff;
					Balance++;
					T_Times++;
					lasttradetime = System.currentTimeMillis();
					if (Balance != 0)
						zksum = zksum + FB_SS_Diff;
					if (Balance == 0)
						log.info("累计盈亏:" + (zdsum - zksum));
				}

				if ((FS_SB_Diff <= Line - 0.5 * Down_Profit) && Balance > -1 * MaxAmount) {
					UpdateFutureAccount(futurePost.future_userinfo());
					// if (StockAccount.getLtcamount() > MinAmount &&
					// StockAccount.getMoney() > BTCAmount * BBuyPrice) {
					log.info("做多价差,当前Line:" + Line + "   价差：" + FS_SB_Diff + "   交易次数：" + T_Times
							+ stockPost.trade("btc_cny", "buy", FBuyPrice.toString(), MinAmount.toString())
							+ stockPost.trade("ltc_cny", "sell", SSellPrice.toString(), MinAmount.toString()));
					
					LastDiff = FS_SB_Diff;
					Balance--;
					T_Times++;
					lasttradetime = System.currentTimeMillis();
					if (Balance != 0)
						zdsum = zdsum + FS_SB_Diff;
					if (Balance == 0)
						log.info("累计盈亏:" + (zdsum - zksum));
					// }
				}
				if (now - clock4 >= 2000) {
					// log.info("定时撤单");
					//log.info("当前：" + FB_SS_Diff + "   上轨：" + (Line + UP_Profit * 0.5) + "   中轨：" + Line + "   下轨：" + (Line - Down_Profit * 0.5));
					clock4 = System.currentTimeMillis();
				}
			}
			now = System.currentTimeMillis();
			FB_SS_Sum = (double) Math.round(FB_SS_Sum + FB_SS_Diff);
			FS_SB_Sum = (double) Math.round(FS_SB_Sum + FS_SB_Diff);
			DiffCount++;
			if (DiffCount >= 200) {
				track = CalculateTrack(track, (FS_SB_Sum / DiffCount), (FB_SS_Sum / DiffCount), 300, 100, 2.71828);
				DiffCount = 0;
				FB_SS_Sum = 0.0;
				FS_SB_Sum = 0.0;
			}			
		}
	}

	public void CancelThenSendStockOrders(long now, String symbol) throws HttpException, IOException {
		StockOrder = untils.StringToStockOrder(stockPost.order_info(symbol, "-1"));
		for (int j = 0; j < StockOrder.size(); j++) {
			if (now - StockOrder.get(j).getCreatdate() > F_Time * 1000) {
				log.info("订单：" + StockOrder.get(j).getOrderid() + "超时");
				Double amount = StockOrder.get(j).getAmount() - StockOrder.get(j).getDealamount();
				stockPost.cancel_order(symbol, StockOrder.get(j).getOrderid());
				if (StockOrder.get(j).getType().indexOf("buy") != -1)
					log.info(stockPost.trade(symbol, "buy", "1000000", amount.toString()));
				else if (StockOrder.get(j).getType().indexOf("sell") != -1)
					log.info(stockPost.trade(symbol, "sell", "0.0000001", amount.toString()));
			}
		}
	}

	public void CancelThenSendfutureOrders(long now, String symbol, String contract_type)
			throws HttpException, IOException {
		FutureOrder = untils.StringToFutureOrder(futurePost.future_order_info(symbol, contract_type, String.valueOf(-1),
				String.valueOf(1), String.valueOf(1), String.valueOf(50)));
		for (int j = 0; j < FutureOrder.size(); j++) {
			if (now - FutureOrder.get(j).getCreate_date() > F_Time * 1000) {
				log.info("订单：" + FutureOrder.get(j).getOrder_id() + "超时");
				Double amount = FutureOrder.get(j).getAmount() - FutureOrder.get(j).getDeal_amount();
				futurePost.future_cancel(symbol, contract_type, FutureOrder.get(j).getOrder_id());
				for (int itype = 1; itype < 5; itype++) {
					log.info(futurePost.future_trade(symbol, contract_type, "1000000", String.valueOf(itype),
							amount.toString(), String.valueOf(1)));
				}
			}
		}
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
			JSONObject holding = (JSONObject) PositonJSON.get("holding");
			Position.setBuy_amount(holding.getString("buy_amount"));
			Position.setBuy_available(holding.getString("buy_available"));
			Position.setBuy_price_avg(holding.getString("buy_price_avg"));
			Position.setBuy_price_cost(holding.getString("buy_price_cost"));
			Position.setBuy_profit_real(holding.getString("buy_profit_real"));
			Position.setContract_id(holding.getString("contract_id"));
			Position.setContract_type(holding.getString("contract_type"));
			Position.setCreate_date(holding.getString("create_date"));
			Position.setLever_rate(holding.getString("lever_rate"));
			Position.setSell_amount(holding.getString("sell_amount"));
			Position.setSell_available(holding.getString("sell_available"));
			Position.setSell_price_avg(holding.getString("sell_price_avg"));
			Position.setSell_price_cost(holding.getString("sell_price_cost"));
			Position.setSell_profit_real(holding.getString("sell_profit_real"));
			Position.setSymbol(holding.getString("symbol"));
		}
		return Position;
	}

	public Double CalculateStockPriceCanTrade(Double Amount, List<StockDepth> depth, String flag) {
		Double result = 0.0;
		Double vol = 0.0;
		Double sum = 0.0;
		Double amount = Amount;
		int i;
		if (flag.equals("bid")) {
			for (i = 0; i < depth.size(); i++) {
				amount = amount - depth.get(i).getBidvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getBidprice()) / Amount;
					break;
				}
				sum = sum + depth.get(i).getBidvol() * depth.get(i).getBidprice();
				vol = vol + depth.get(i).getBidvol();
			}
		} else {
			for (i = 0; i < depth.size(); i++) {
				amount = amount - depth.get(i).getAskvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getAskprice()) / Amount;
					break;
				}
				sum = sum + depth.get(i).getAskvol() * depth.get(i).getAskprice();
				vol = vol + depth.get(i).getAskvol();
			}
		}
		return result;
	}
	
	public Double CalculateFuturePriceCanTrade(Double Amount, List<FutureDepth> depth, String flag) {
		Double result = 0.0;
		Double vol = 0.0;
		Double sum = 0.0;
		Double amount = Amount;
		int i;
		if (flag.equals("bid")) {
			for (i = 0; i < depth.size(); i++) {
				amount = amount - depth.get(i).getBidfvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getBidprice()) / Amount;
					break;
				}
				sum = sum + depth.get(i).getBidfvol() * depth.get(i).getBidprice();
				vol = vol + depth.get(i).getBidfvol();
			}
		} else {
			for (i = 0; i < depth.size(); i++) {
				amount = amount - depth.get(i).getAskfvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getAskprice()) / Amount;
					break;
				}
				sum = sum + depth.get(i).getAskfvol() * depth.get(i).getAskprice();
				vol = vol + depth.get(i).getAskfvol();
			}
		}
		return result;
	}

	public List<Track> CalculateTrack(List<Track> track, Double upvalue, Double downvalue, int masize, int stsize,
			Double stradio) {
		Double sum_up = 0.0, sum_up1 = 0.0, sum_down = 0.0, sum_down1 = 0.0, avgup = 0.0, avgdown = 0.0;
		Track tmp = new Track();
		tmp.setUp_value(upvalue);
		tmp.setDown_value(downvalue);
		if (track.size() >= masize)
			track.remove(0);
		track.add(tmp);
		int iSize = track.size();
		if (track.size() <= stsize) {
			for (int i = 0; i < track.size(); i++) {
				sum_up += track.get(i).getUp_value() ;
				sum_up1 += track.get(i).getUp_value() * track.get(i).getUp_value();
				sum_down += track.get(i).getDown_value();
				sum_down1 += track.get(i).getDown_value() * track.get(i).getDown_value();
			}
			tmp.setUp_st(
					stradio * Math.sqrt(sum_up1 / track.size() - (sum_up / track.size()) * (sum_up / track.size())));
			avgup = sum_up / track.size();
			tmp.setDown_st(stradio
					* Math.sqrt(sum_down1 / track.size() - (sum_down / track.size()) * (sum_down / track.size())));
			avgdown = sum_down / track.size();
		} else {
			for (int i = track.size() - stsize; i < track.size(); i++) {
				sum_up += track.get(i).getUp_value();
				sum_up1 += track.get(i).getUp_value() * track.get(i).getUp_value();
				sum_down += track.get(i).getDown_value();
				sum_down1 += track.get(i).getDown_value() * track.get(i).getDown_value();
			}
			tmp.setUp_st(stradio * Math.sqrt(sum_up1 / stsize - (sum_up / stsize) * (sum_up / stsize)));
			avgup = sum_up / stsize;
			tmp.setDown_st(stradio * Math.sqrt(sum_down1 / stsize - (sum_down / stsize) * (sum_down / stsize)));
			avgdown = sum_down / stsize;
		}
		tmp.setMid_st(0.5 * (avgdown + avgup));
		track.set(iSize - 1, tmp);
		return track;
	}
}
