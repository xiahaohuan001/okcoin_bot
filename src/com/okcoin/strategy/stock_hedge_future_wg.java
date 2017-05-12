package com.okcoin.strategy;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ThresholdingOutputStream;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import com.okcoin.model.FutureDepth;
import com.okcoin.model.FutureOrder;
import com.okcoin.model.StockDepth;
import com.okcoin.model.StockOrder;
import com.okcoin.rest.future.IFutureRestApi;
import com.okcoin.rest.future.impl.FutureRestApiV1;
import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.test.Untils;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import net.sf.json.JSONArray;

public class stock_hedge_future_wg extends Thread {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	String url_prex_cn = "https://www.okcoin.cn";
	String url_prex_com = "https://www.okcoin.com";	
	String apiKey_cn = "46674f7f-d06b-44f3-ac79-62945e4e6e6d";
	String secretKey_cn = "45EE20A0F9285983811E91A0A09E3989";	
	String apiKey_com = "5a395d18-6dc9-44b9-9b88-b13c7831feb9";
	String secretKey_com = "6702CA3A2AE744D6810249582D132A28";	
	IFutureRestApi futureGet = new FutureRestApiV1(url_prex_com);
	IFutureRestApi futurePost = new FutureRestApiV1(url_prex_com, apiKey_com, secretKey_com);	
	IStockRestApi stockGet = new StockRestApi(url_prex_com);
	IStockRestApi stockPost = new StockRestApi(url_prex_com, apiKey_com, secretKey_com);
	Untils untils = new Untils();
	String symbolstock = "btc_usd";
	String symbolfutrue = "btc_usd";
	String contract_type = "this_week";
	List<FutureDepth> F_Depth = new ArrayList<>();
	List<StockDepth> S_Depth = new ArrayList<>();
	List<StockOrder> StockOrder = new ArrayList<>();
	List<FutureOrder> FutureOrder = new ArrayList<>();
	boolean cantrade = false;
	double FMinAmount = 1;
	int []zbalance = new int[80];
	int []fbalance = new int[80];
	double []open = new double[80];
	double []cover = new double[80];
	double []lastdiff = new double[80];	
	double[] profit = new double[80];
	double sumprofit = 0;
	double FBuyPrice,FSellPrice,SBuyPrice,SSellPrice,FB_SS_Diff,FS_SB_Diff;		
	double rate; 
	//时钟区
	long start = System.currentTimeMillis()/1000;
	long now;
	long clock1 = 0, clock2 = 0, clock3 = 0, clock4 = 0;
	long F_Time = 2;
	public stock_hedge_future_wg(){
		for(int i = 0;i < 80 ; i++){
			zbalance[i] = 0;
			fbalance[i] = 0;
			cover[i] = i;
			open[i] = cover[i] + 3;
			profit[i] = 0;
		}
		try {
			this.rate = untils.UpdateOkRate(futureGet);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();
	}
	public void OnTick(String tick) throws HttpException, IOException {
		if (tick.indexOf("future") != -1)
			F_Depth = untils.JsonToFutureDepth(JSONArray.fromObject(tick));
		if (tick.indexOf("spot") != -1)
			S_Depth = untils.JsonToStockDepth(JSONArray.fromObject(tick));

		if(F_Depth.size() > 1&& S_Depth.size() > 1)
			cantrade = true;
		
		if(cantrade){			
			FBuyPrice = CalculateFuturePriceCanTrade(FMinAmount, F_Depth, "bid");
			FSellPrice = CalculateFuturePriceCanTrade(FMinAmount, F_Depth, "ask");
			SBuyPrice = CalculateStockPriceCanTrade((FMinAmount*100)/FBuyPrice, S_Depth, "bid");
			SSellPrice = CalculateStockPriceCanTrade((FMinAmount*100)/FSellPrice, S_Depth, "ask");
			FB_SS_Diff =  (double) Math.round((FBuyPrice - SSellPrice)*100)/100;
			FS_SB_Diff =  (double) Math.round((FSellPrice - SBuyPrice)*100)/100;
			//System.out.println(FBuyPrice+" "+SBuyPrice+" "+(FMinAmount*100)/FBuyPrice);
			for(int zindex = 0;zindex < zbalance.length;zindex ++){
				if(FB_SS_Diff >= open[zindex] && zbalance[zindex] == 0){//正溢价开仓
					log.info("第"+zindex+"组正溢价期卖现买"+"期:+"+FSellPrice+"现:-"+SBuyPrice);
					zbalance[zindex]++;
					lastdiff[zindex] = FB_SS_Diff;
				}
				if(FS_SB_Diff <= cover[zindex] && zbalance[zindex] > 0){//正溢价平仓					
					zbalance[zindex]--;
					profit[zindex] = lastdiff[zindex] - FS_SB_Diff;
					sumprofit += profit[zindex];
					lastdiff[zindex] = FS_SB_Diff;
					log.info("第"+zindex+"组正溢价期买现卖"+"   期:-"+FBuyPrice+"现:+"+SSellPrice+"   本次收益:"+profit[zindex]+"总收益:"+sumprofit);
				}
			}
			for(int findex = 0;findex < fbalance.length;findex ++){
				if(FS_SB_Diff <= -1 * open[findex] && fbalance[findex] == 0){//负溢价开仓
					log.info("第"+findex+"组负溢价期买现卖"+"期:-"+FBuyPrice+"现:+"+SSellPrice);
					fbalance[findex]--;
					lastdiff[findex] = FS_SB_Diff;
				}
				if(FB_SS_Diff >= -1 * cover[findex] && fbalance[findex] < 0){//负溢价平仓					
					fbalance[findex]++;
					profit[findex] = FB_SS_Diff - lastdiff[findex];
					sumprofit += profit[findex];
					lastdiff[findex] = FB_SS_Diff;
					log.info("第"+findex+"组负溢价期卖现买"+"   期:+"+FSellPrice+"现:-"+SBuyPrice+"   本次收益:"+profit[findex]+"总收益:"+sumprofit);
				}
			}
		}
	}	
	public void run() {
        while (true) {
        	now = System.currentTimeMillis()/1000;   	
        	try {
        		if((now - start) % 3600 <= 1){
            		rate = untils.UpdateOkRate(futureGet);
            	}
        		CancelThenSendStockOrders(now, symbolstock);
				CancelThenSendfutureOrders(now, symbolfutrue, contract_type);
			} catch (HttpException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}      		
			if((now - 114600) % 604800 < 60){
				System.out.println("即将交割,平掉所有仓位");
				cantrade = false;
			}
			if(cantrade = false)
				if((now - 115200) % 604800 < 60){
					System.out.println("交割结束,交易开启");
					cantrade = true;
				}	
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	
	public void CancelThenSendStockOrders(long now, String symbol) throws HttpException, IOException {
		StockOrder = untils.GetStockOrders(stockPost, symbol);
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
		FutureOrder = untils.GetFutrueOrders(futurePost, symbol, contract_type);
		for (int j = 0; j < FutureOrder.size(); j++) {
			if (now - FutureOrder.get(j).getCreate_date() > F_Time * 1000) {
				log.info("订单：" + FutureOrder.get(j).getOrder_id() + "超时");
				Double amount = FutureOrder.get(j).getAmount() - FutureOrder.get(j).getDeal_amount();
				futurePost.future_cancel(symbol, contract_type, FutureOrder.get(j).getOrder_id());
				int itype = FutureOrder.get(j).getType();
				if(itype == 1){
					log.info(futurePost.future_trade(symbol, contract_type, "1000000", String.valueOf(itype),
							amount.toString(), "1"));
				}else if(itype == 4){
					log.info(futurePost.future_trade(symbol, contract_type, "1000000", String.valueOf(itype),
							amount.toString(), "1"));
				}
				else if(itype == 2){
					log.info(futurePost.future_trade(symbol, contract_type, "0.0001", String.valueOf(itype),
							amount.toString(), "1"));
				}
				else if(itype == 3){
					log.info(futurePost.future_trade(symbol, contract_type, "0.0001", String.valueOf(itype),
							amount.toString(), "1"));
				}
			}
		}
	}
	
	
	
	
	
}
