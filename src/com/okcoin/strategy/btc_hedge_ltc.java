package com.okcoin.strategy;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.jquantlib.JQuantLib;
import org.jquantlib.math.matrixutilities.Array;

import com.okcoin.exchange.*;
import com.okcoin.model.*;
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

public class btc_hedge_ltc {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	List<StockDepth> btcDepth = new ArrayList<>();
	List<StockDepth> ltcDepth = new ArrayList<>();	
	StockAccount Account = new StockAccount();
	List<StockOrder> Orders = new ArrayList<>();
	int LTC_Multiple = 6;
	int BTC_Multiple = 21;
	long start = System.currentTimeMillis();
	long now;
	int T_Times = 0, T_Times1 = 0;
	int pc1 = 0, pc2 = 0, pc3 = 0;
	long bbstart, bsstart, fstart;
	long HD_Time = 120, P_Time = 150, F_Time = 300;
	String [] OrderType = {"btc_cny","ltc_cny"};
	Double Line;
	Double AmountOnce = 1.0,BTCAmount=AmountOnce/BTC_Multiple,LTCAmount=AmountOnce*LTC_Multiple;
	Double P_Diff = 0.3, M_Diff = 0.3, B_Diff = 0.3;
	Double BB_LS_Diff;
	Double BS_LB_Diff;
	Double DiffProfit;
	Double Profit = 0.0;
	Double SumProfit = 0.0;
	Double MinDiff = 0.05;
	Double Sum = 0.0;
	Double LastDiff = 0.0, LastDiffProfit, LastAvgDiff;
	Double DiffSum = 0.0;
	Double UpLine = null, DownLine = null;
	Double BBuyPrice, BSellPrice, LBuyPrice, LSellPrice;
	long clock1, clock2, clock3, clock4;
	int zs = 0;
	int Balance = 0, LossStop = 0, RiskStop = 0;
	int N = 120, BollRatio = 2;
	int DiffCount = 0;
	Untils untils = new Untils();
	//List<BollClass> BollList = new ArrayList<>();
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String api_key = "46674f7f-d06b-44f3-ac79-62945e4e6e6d"; 
	String secret_key = "45EE20A0F9285983811E91A0A09E3989"; 														// 申请的secret_key
	String url_prex = "https://www.okcoin.cn"; 	
	IStockRestApi stockGet = new StockRestApi(url_prex);
	IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);

	public void btc_hedge_ltc(JSONArray btcdepth, JSONArray ltcdepth) throws HttpException, IOException {
		btcDepth = untils.JsonToStockDepth(btcdepth);
		ltcDepth = untils.JsonToStockDepth(ltcdepth);
		BBuyPrice = CalculatePriceCanTrade("btc_cny", BTCAmount, "bid");
		LSellPrice = CalculatePriceCanTrade("ltc_cny", LTCAmount, "ask");
		BSellPrice = CalculatePriceCanTrade("btc_cny", BTCAmount, "ask");
		LBuyPrice = CalculatePriceCanTrade("ltc_cny", LTCAmount, "bid");
		BB_LS_Diff = BBuyPrice / BTC_Multiple - LSellPrice * LTC_Multiple;
		BS_LB_Diff = BSellPrice / BTC_Multiple - LBuyPrice * LTC_Multiple;
		//CalculateBollClass(btcDepth.get(0).getTime(), (BB_LS_Diff + BS_LB_Diff) / 2, N, BollRatio);
		//log.info(stockPost.userinfo());
		if (Line != null) {
			//log.info(Line);
			if (LossStop == -1 || RiskStop == -1|| ((Line + 0.5 * P_Diff) <= BS_LB_Diff && (Balance == 0 || Balance == -1))) {
				UpdateAccount(stockPost.userinfo());
				if (Account.getBtcamount() > BTCAmount&& Account.getMoney() > LTCAmount * LBuyPrice) {
					log.info("做空价差,当前Line:" + Line + "   价差：" + BS_LB_Diff + "   累计盈亏：" + Sum * AmountOnce+stockPost.trade("btc_cny", "sell", BSellPrice.toString(), BTCAmount.toString())+stockPost.trade("ltc_cny", "buy", LBuyPrice.toString(), LTCAmount.toString()));										
					if (Balance != 0)
						Sum = Sum + BS_LB_Diff - LastDiff;
					LastDiff = BS_LB_Diff;
					Balance++;
					T_Times++;
					pc2 = 0;
					pc3 = 0;
					start = System.currentTimeMillis();
					bbstart = System.currentTimeMillis();
					bsstart = System.currentTimeMillis();
					if (LossStop == -1) {
						// Line=LastAvgDiff;
						LossStop = 0;
						Balance = 0;
						Line = null;
						// LastDiff=null;
					}
					if (RiskStop == -1) {
						RiskStop = 0;
						Balance = 0;
						Line = null;
						// LastDiff=null;
					}
				}
			}
		}
		//UpdateAccount(stockPost.userinfo());
		if (Line != null) {
			if (LossStop == 1 || RiskStop == 1|| ((BB_LS_Diff <= Line - 0.5 * P_Diff) && (Balance == 0 || Balance == 1))) {
				UpdateAccount(stockPost.userinfo());
				if (Account.getLtcamount() > LTCAmount&& Account.getMoney() > BTCAmount * BBuyPrice) {
					log.info("做多价差,当前Line:" + Line + "   价差：" + BB_LS_Diff + "   累计盈亏：" + Sum * AmountOnce+stockPost.trade("btc_cny", "buy", BBuyPrice.toString(), BTCAmount.toString())+stockPost.trade("ltc_cny", "sell", LSellPrice.toString(), LTCAmount.toString()));					
					if (Balance != 0)
						Sum = Sum + LastDiff - BB_LS_Diff;					
					LastDiff = BB_LS_Diff;
					Balance--;
					T_Times++;
					pc2 = 0;
					pc3 = 0;
					start = System.currentTimeMillis();
					bbstart = System.currentTimeMillis();
					bsstart = System.currentTimeMillis();
					if (LossStop == 1) {
						// Line=LastAvgDiff;
						LossStop = 0;
						Balance = 0;
						Line = null;
						// LastDiff=null;
					}
					if (RiskStop == 1) {
						RiskStop = 0;
						Balance = 0;
						Line = null;
						// LastDiff=null;
					}
				}
			}

			// log.info("差价均线："+Boll.get(Boll.size()-1).getMa());

		}
		now = System.currentTimeMillis();
		//clock = now - start / 1000;
		clock1 = now - start;
		clock2 = now - bsstart;
		clock3 = now - bbstart;
		DiffSum = DiffSum + (BS_LB_Diff + BB_LS_Diff) / 2;
		DiffCount++;
		LastAvgDiff = DiffSum / DiffCount;
		if (Balance == 0) {	
			//log.info(clock1);
			if (clock1 >= HD_Time * 1000) {
				start = now;
				Line = LastAvgDiff;
				log.info("平衡状态，修改差价线为:" + Line);
				DiffSum = 0.0;
				DiffCount = 0;
			}
		} else {
			if (Balance == -1) {
				if ((clock3 >= P_Time * 1000) && (clock3 < P_Time * 3000)) {
					if ((clock3 >= P_Time * 1000) && (clock3 < P_Time * 2000)) {
						if (pc3 == 0) {
							Line = Line - M_Diff / 2;
							log.info("做多价差后等待状态，第一次修改差价线为:" + Line);
							pc3 = 1;
						}
					} else if ((clock3 >= P_Time * 2000) && (clock3 < P_Time * 3000)) {
						if (pc3 == 1) {
							Line = Line - M_Diff / 2;
							log.info("做多价差后等待状态，第二次修改差价线为:" + Line);
							pc3 = 0;
						}
					}
					if (LastAvgDiff - LastDiff < -1 * B_Diff) {
						Line = LastAvgDiff - 1 * B_Diff;
						log.info("价差下降波动过大，强制搬平止损，并修改差价线为：" + Line);
						RiskStop = -1;
						bbstart = now;
						pc3 = 0;
						DiffSum = 0.0;
						DiffCount = 0;
						zs = 1;
					}
				} else if (clock3 >= P_Time * 3000) {
					// if (LastAvgDiff - Line > - 1* P_Diff) {
					LossStop = -1;
					Line = LastAvgDiff;
					log.info("处于做多价差状态过久，强制搬平止损，并修改差价线为：" + Line);
					bbstart = now;
					pc3 = 0;
					DiffSum = 0.0;
					DiffCount = 0;
					// }
				}
			} else if (Balance == 1) {
				if ((clock2 >= P_Time * 1000) && (clock2 < P_Time * 3000)) {
					if ((clock2 >= P_Time * 1000) && (clock2 < P_Time * 2000)) {
						if (pc2 == 0) {
							Line = Line + M_Diff / 2;
							log.info("做空价差后等待状态，第一次尝试修改差价线为:" + Line);
							pc2 = 1;
						}
					} else if ((clock2 >= P_Time * 2000) && (clock2 < P_Time * 3000)) {
						if (pc2 == 1) {
							Line = Line + M_Diff / 2;
							log.info("做空价差后等待状态，第二次尝试修改差价线为:" + Line);
							pc2 = 0;
						}
					}
					if (LastAvgDiff - LastDiff >= B_Diff) {
						Line = LastAvgDiff + 1 * B_Diff;
						log.info("差价上升波动过大，强制搬平止损，并修改差价线为：" + Line);
						RiskStop = 1;
						bsstart = now;
						pc2 = 0;
						DiffSum = 0.0;
						DiffCount = 0;
						zs = 1;
					}
				} else if (clock2 >= P_Time * 3000) {
					// if (LastAvgDiff - Line <= P_Diff) {
					LossStop = 1;
					Line = LastAvgDiff;
					log.info("处于做空价差状态过久，强制搬平止损，并修改差价线为：" + Line);
					bsstart = now;
					pc2 = 0;
					DiffSum = 0.0;
					DiffCount = 0;
					// }
				}
			}

		}

		clock4 = now - fstart;
	    if (clock4 >= 60000) {
	    	BalanceAccount(now);
	        fstart = System.currentTimeMillis();
	    }

	}

	public void BalanceAccount(long now) throws HttpException, IOException {
		for (int i = 0; i < OrderType.length; i++) {
			Orders = untils.StringToStockOrder(stockPost.order_info(OrderType[i], "-1"));
			for (int j = 0; j < Orders.size(); j++) {
				if(now - Orders.get(j).getCreatdate() > F_Time*1000){
					log.info("订单："+Orders.get(j).getOrderid()+"超时");
					Double amount = Orders.get(j).getAmount()-Orders.get(j).getDealamount();
					stockPost.cancel_order(OrderType[i], Orders.get(j).getOrderid());
					if(Orders.get(j).getType().indexOf("buy")!=-1)
						log.info(stockPost.trade(OrderType[i], "buy", CalculatePriceCanTrade(OrderType[i], amount, "bid").toString(), amount.toString()));
					else if(Orders.get(j).getType().indexOf("sell")!=-1)
						log.info(stockPost.trade(OrderType[i], "sell", CalculatePriceCanTrade(OrderType[i], amount, "ask").toString(), amount.toString()));					
				}
			}
		}

	}

	public void UpdateAccount(String S) {
		if(S.indexOf("true")!=-1){
		JSONObject AccountJSON = JSONObject.fromObject(S);
		//JSONObject data = (JSONObject) AccountJSON.get("data");
		JSONObject info = (JSONObject) AccountJSON.get("info");
		JSONObject funds = (JSONObject) info.get("funds");
		JSONObject asset = (JSONObject) funds.get("asset");
		JSONObject free = (JSONObject) funds.get("free");
		JSONObject freezed = (JSONObject) funds.get("freezed");
		Account.setMoney(free.getString("cny"));
		Account.setBtcamount(free.getString("btc"));
		Account.setLtcamount(free.getString("ltc"));
		Account.setFrozenmoney(freezed.getString("cny"));
		Account.setFrozenbtcamount(freezed.getString("btc"));
		Account.setFrozenltcamount(freezed.getString("ltc"));
		}
		//log.info("当前账户信息");
	}

	public Double CalculatePriceCanTrade(String Symbol, Double Amount, String flag) {
		List<StockDepth> depth = new ArrayList<>();
		if(Symbol.equals("btc_cny"))
			depth = btcDepth;
		else if(Symbol.equals("ltc_cny"))
			depth = ltcDepth;
		Double result = 0.0;
		Double vol = 0.0;
		Double sum = 0.0;
		Double amount = Amount;
		int i;
		if (flag.equals("ask")) {
			for (i = 0; i < depth.size(); i++) {
				// System.out.println(depth.get(i).getAskvol()+"==="+depth.get(i).getAskvol().getClass());
				amount = amount - depth.get(i).getBidvol();
				// vol[i]=depth.get(i).getBidvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getBidprice()) / Amount;
					break;
					// System.out.println(amount);
				}
				sum = sum + depth.get(i).getBidvol() * depth.get(i).getBidprice();
				vol = vol + depth.get(i).getBidvol();
			}
			// if(result<0||result>100)
			// System.out.println(" Amount:"+Amount+" vol:"+vol+" sum:"+sum+"
			// amount:"+amount+" result:"+result);

		} else {
			for (i = 0; i < depth.size(); i++) {
				// System.out.println(depth.get(i).getAskvol()+"==="+depth.get(i).getAskvol().getClass());
				amount = amount - depth.get(i).getAskvol();
				// vol[i]=depth.get(i).getBidvol();
				if (amount <= 0) {
					result = (sum + (Amount - vol) * depth.get(i).getAskprice()) / Amount;
					break;
					// System.out.println(amount);
				}
				sum = sum + depth.get(i).getAskvol() * depth.get(i).getAskprice();
				vol = vol + depth.get(i).getAskvol();
			}
		}
		// if(result<0||result>100)
		// System.out.println(" Amount:"+Amount+" vol:"+vol+" sum:"+sum+"
		// amount:"+amount+" result:"+result);
		return result;
	}

//	public void CalculateBollClass(long time, Double diff, int N, int BollRatio) {
//		Double sumDiff = 0.0, sumDiff1 = 0.0;
//		int iSize = BollList.size();
//		BollClass tempBoll = new BollClass();
//		// System.out.println(tempBoll);
//		tempBoll.setTime(time);
//		// System.out.println("aaaaa");
//		tempBoll.setDiff(diff);
//		// tempBoll.setMa((Double) null);
//		// tempBoll.setBoll((Double) null);
//		int Flag = 0;
//		if (iSize == 0) {
//			// TempBoll.ma=null;
//			// Flag=0;
//			BollList.add(tempBoll);
//			// System.out.println(0000);
//			// boll = null;
//		} else if ((BollList.get(iSize - 1).getTime() - BollList.get(0).getTime() < N * 1000) && (Flag != 2)) {
//			// TempBoll.ma = null;
//			Flag = 1;
//			BollList.add(tempBoll);
//			// System.out.println(Boll.get(iSize - 1).getTime() -
//			// Boll.get(0).getTime());
//			// boll = null;
//		} else if (BollList.get(iSize - 1).getTime() - BollList.get(0).getTime() >= N * 1000) {
//			// System.out.print(Boll.size());
//			Flag = 2;
//			BollList.remove(0);
//			// System.out.print(Boll.size());
//			BollList.add(tempBoll);
//			// System.out.println(Boll.size());
//			// System.out.println(Boll.get(iSize - 1).getTime() -
//			// Boll.get(0).getTime());
//			for (int i = 0; i < BollList.size(); i++) {
//				sumDiff = sumDiff + BollList.get(i).getDiff();
//				sumDiff1 = sumDiff1 + BollList.get(i).getDiff() * BollList.get(i).getDiff();
//			}
//			// Boll[Boll.size() - 1].m_Sum = sumDiff;
//			BollList.get(BollList.size() - 1).setBoll(BollRatio * Math
//					.sqrt(sumDiff1 / BollList.size() - (sumDiff / BollList.size()) * (sumDiff / BollList.size())));
//			BollList.get(BollList.size() - 1).setMa(sumDiff / BollList.size());
//		}
//
//		// return Boll;
//	}

}
