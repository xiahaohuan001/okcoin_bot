package com.okcoin.strategy;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.jquantlib.JQuantLib;
import org.jquantlib.math.matrixutilities.Array;

import com.okcoin.exchange.*;
import com.okcoin.model.*;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.test.BuissnesWebSocketServiceImpl;
import com.okcoin.websocket.test.Untils;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.okcoin.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.*;
import java.util.Scanner;
public class btc_hedge_ltc1 {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	List<Depth> btcDepth = new ArrayList<>();
	List<Depth> ltcDepth = new ArrayList<>();
	int LTC_Multiple = 6;
	int BTC_Multiple = 21;
	long start = System.currentTimeMillis();
	long now;
	Double Line;
	Double AmountOnce = 21.0;
	Double BB_LS_Diff;
	Double BS_LB_Diff;
	Double DiffProfit;
	Double Profit=0.0;
	Double SumProfit=0.0;
	Double MinDiff = 0.1;
	Double Sum=0.0;
	Double LastDiff;
	Double BBuyPrice,BSellPrice,LBuyPrice,LSellPrice;	
	int Balance = 0,LossStop=0,RiskStop=0;
	int N = 120, BollRatio = 2;
	//Double Line
	List<BollClass> BollList = new ArrayList<>();
	//private BollClass tempBoll;
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

	public void btc_hedge_ltc1(JSONArray btcinfo, JSONArray ltcinfo) {
		btcDepth = new Untils().JsonToDepth(btcinfo);
		ltcDepth = new Untils().JsonToDepth(ltcinfo);
		BB_LS_Diff = CalculatePriceCanTrade(btcDepth, AmountOnce / BTC_Multiple, "bid") / BTC_Multiple - CalculatePriceCanTrade(ltcDepth, AmountOnce * LTC_Multiple, "ask") * LTC_Multiple;
		BS_LB_Diff = CalculatePriceCanTrade(btcDepth, AmountOnce / BTC_Multiple, "ask") / BTC_Multiple - CalculatePriceCanTrade(ltcDepth, AmountOnce * LTC_Multiple, "bid") * LTC_Multiple;
		
//		BBuyPrice = CalculatePriceCanTrade(btcDepth, AmountOnce / BTC_Multiple, "bid") / BTC_Multiple ;
//		BSellPrice = CalculatePriceCanTrade(btcDepth, AmountOnce / BTC_Multiple, "ask") / BTC_Multiple;
//		LBuyPrice = CalculatePriceCanTrade(ltcDepth, AmountOnce * LTC_Multiple, "bid") * LTC_Multiple ;
//		LSellPrice = CalculatePriceCanTrade(ltcDepth, AmountOnce * LTC_Multiple, "ask") * LTC_Multiple;
//		BB_LS_Diff = BBuyPrice - LSellPrice;
//		BS_LB_Diff = BSellPrice - LBuyPrice;
		
		//log.info("  BBuyPrice:"+BBuyPrice+"  BSellPrice:"+BSellPrice+"  LBuyPrice:"+LBuyPrice+"  LSellPrice:"+LSellPrice+"  BB_LS_Diff:"+BB_LS_Diff+"  BS_LB_Diff:"+BS_LB_Diff);
		if(BB_LS_Diff>100||BB_LS_Diff<0){			
			 log.info("开始");
			 log.info("BB_LS_Diff:"+BB_LS_Diff+"BS_LB_Diff:"+BS_LB_Diff);
			 log.info("处理前BTC深度:"+ btcinfo+"处理前LTC深度:"+ltcinfo);
			 log.info("处理后BTC深度:"+ btcDepth.size()+"处理后LTC深度:"+ltcDepth.size());
			 log.info("结束");
		}
		
		CalculateBollClass(btcDepth.get(0).getTime(), (BB_LS_Diff+BS_LB_Diff)/2, N, BollRatio);
		Line = BollList.get(BollList.size()-1).getMa();
		
		
		
		if(Line!=null){
			//log.info("Line:"+Line+"Boll"+BollList.get(BollList.size()-1).getBoll());
			DiffProfit = Math.max(BollList.get(BollList.size()-1).getBoll(), MinDiff);
			//DiffProfit = BollList.get(BollList.size()-1).getBoll();\
			
			if(LossStop == -1||RiskStop == -1||(Line+DiffProfit<=BS_LB_Diff&&(Balance==0||Balance==-1))){
				//Profit=
				if(Balance!=0){
				//LastDiff=BS_LB_Diff;
				Sum=Sum+BS_LB_Diff-LastDiff;				
				log.info("卖空差价              上界："+(Line+DiffProfit)+"  当前："+BS_LB_Diff+"  累计盈亏："+Sum);
				}else
					LastDiff=BS_LB_Diff;
				//log.info("卖空差价");
				Balance++;
				
				
			}
			if(LossStop == -1||RiskStop == -1||(Line-DiffProfit>=BB_LS_Diff&&(Balance==0||Balance==1))){
				if(Balance!=0){				
				Sum=Sum-BB_LS_Diff+LastDiff;				
				log.info("做多差价               下界："+(Line-DiffProfit)+"  当前："+BB_LS_Diff+"  累计盈亏："+Sum);
				}else 
					LastDiff=BB_LS_Diff;
				//log.info("做多差价");
				Balance--;
				
				
			}
			
			//log.info("差价均线："+Boll.get(Boll.size()-1).getMa());
			
			
		}
		 
		 
		// System.out.println(btcDepth.get(0).getAskprice()+"*******"+btcDepth.get(1).getAskprice());
		
		// log.info("BTC"+btcinfo);
		// log.info("LTC"+ltcinfo);

	}

	public Double CalculatePriceCanTrade(List<Depth> depth, Double Amount, String flag) {
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
			//if(result<0||result>100)
				//System.out.println("  Amount:"+Amount+"  vol:"+vol+"  sum:"+sum+"  amount:"+amount+"  result:"+result);

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
		//if(result<0||result>100)
			//System.out.println("  Amount:"+Amount+"  vol:"+vol+"  sum:"+sum+"  amount:"+amount+"  result:"+result);
		return result;
	}

	public void CalculateBollClass(long time, Double diff, int N, int BollRatio) {
		Double sumDiff = 0.0, sumDiff1 = 0.0;
		int iSize = BollList.size();
		BollClass tempBoll =  new BollClass();
		 //System.out.println(tempBoll);
		 tempBoll.setTime(time);
		 //System.out.println("aaaaa");
		 tempBoll.setDiff(diff);
		 //tempBoll.setMa((Double) null);
		 //tempBoll.setBoll((Double) null);
		 int Flag = 0;
		if (iSize == 0) {
			// TempBoll.ma=null;
			//Flag=0;
			BollList.add(tempBoll);
			//System.out.println(0000);
			// boll = null;
		} else if ((BollList.get(iSize - 1).getTime() - BollList.get(0).getTime() < N*1000)&&(Flag!=2)) {
			// TempBoll.ma = null;
			Flag=1;
			BollList.add(tempBoll);
			//System.out.println(Boll.get(iSize - 1).getTime() - Boll.get(0).getTime());
			// boll = null;
		} else if (BollList.get(iSize - 1).getTime() - BollList.get(0).getTime() >= N*1000) {
			//System.out.print(Boll.size());
			Flag=2;
			BollList.remove(0);
			//System.out.print(Boll.size());
			BollList.add(tempBoll);
			//System.out.println(Boll.size());
			//System.out.println(Boll.get(iSize - 1).getTime() - Boll.get(0).getTime());
			for (int i = 0; i < BollList.size(); i++) {
				sumDiff = sumDiff + BollList.get(i).getDiff();
				sumDiff1 = sumDiff1 +  BollList.get(i).getDiff() * BollList.get(i).getDiff();
			}
			// Boll[Boll.size() - 1].m_Sum = sumDiff;
			BollList.get(BollList.size() - 1).setBoll(
					BollRatio * Math.sqrt(sumDiff1 / BollList.size() - (sumDiff / BollList.size()) * (sumDiff / BollList.size())));
			BollList.get(BollList.size() - 1).setMa(sumDiff / BollList.size());
		}

		// return Boll;
	}

}
