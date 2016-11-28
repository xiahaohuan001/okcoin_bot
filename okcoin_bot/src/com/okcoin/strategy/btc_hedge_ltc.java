package com.okcoin.strategy;

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

public class btc_hedge_ltc {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	List<Depth> btcDepth = new ArrayList<>();
	List<Depth> ltcDepth = new ArrayList<>();
	int LTC_Multiple = 6;
	int BTC_Multiple = 21;
	long start = System.currentTimeMillis();
	long now;
	double Line;
	double AmountOnce = 21.0;
	double BB_LS_Diff;
	double BS_LB_Diff;
	BollClass Boll;
	public void btc_hedge_ltc(JSONArray btcinfo, JSONArray ltcinfo) {
		btcDepth = new Untils().JsonToDepth(btcinfo);
		ltcDepth = new Untils().JsonToDepth(ltcinfo);
		BB_LS_Diff=CalculatePriceCanTrade(btcDepth, AmountOnce/BTC_Multiple, "bid")/BTC_Multiple-CalculatePriceCanTrade(ltcDepth, AmountOnce*LTC_Multiple, "ask")*LTC_Multiple;
		BS_LB_Diff=CalculatePriceCanTrade(btcDepth, AmountOnce/BTC_Multiple, "ask")/BTC_Multiple-CalculatePriceCanTrade(ltcDepth, AmountOnce*LTC_Multiple, "bid")*LTC_Multiple;
		
		
		
		
		
		
		//if(BS_LB_Diff-BB_LS_Diff>=0.0)
        //log.info(BS_LB_Diff+"******"+BB_LS_Diff);		
		// System.out.println(btcDepth.get(0).getAskprice()+"*******"+btcDepth.get(1).getAskprice());
//		for (int i = 0; i < btcDepth.size(); i++) {
//			 System.out.println("买"+(i+1)+" :"+btcDepth.get(i).getBidprice()+"量"+(i+1)+":"+btcDepth.get(i).getBidvol()+"****************卖"+(i+1)+":"+btcDepth.get(i).getAskprice()+" 量"+(i+1)+":"+btcDepth.get(i).getAskvol());
//		}
//		for (int i = 0; i < ltcDepth.size(); i++) {
//			 System.out.println("买"+(i+1)+" :"+ltcDepth.get(i).getBidprice()+"量"+(i+1)+":"+ltcDepth.get(i).getBidvol()+"****************卖"+(i+1)+":"+ltcDepth.get(i).getAskprice()+" 量"+(i+1)+":"+ltcDepth.get(i).getAskvol());
//		}
		// log.info("BTC"+btcinfo);
		// log.info("LTC"+ltcinfo);

	}

	public double CalculatePriceCanTrade(List<Depth> depth, double Amount, String flag) {
		double result = 0;
		double vol = 0;
		double sum = 0;
		double amount = Amount;
		int i;
		if (flag.equals("ask")) {
			for (i = 0; i < depth.size(); i++) {
				// System.out.println(depth.get(i).getAskvol()+"==="+depth.get(i).getAskvol().getClass());
				amount = amount - depth.get(i).getBidvol();
				// vol[i]=depth.get(i).getBidvol();
				if (amount < 0) {
					result = (sum + (Amount - vol) * depth.get(i).getBidprice()) / Amount;
					break;
					// System.out.println(amount);
				}
				sum = sum + depth.get(i).getBidvol() * depth.get(i).getBidprice();
				vol = vol + depth.get(i).getBidvol();
			}

		} else {
			for (i = 0; i < depth.size(); i++) {
				// System.out.println(depth.get(i).getAskvol()+"==="+depth.get(i).getAskvol().getClass());
				amount = amount - depth.get(i).getAskvol();
				// vol[i]=depth.get(i).getBidvol();
				if (amount < 0) {
					result = (sum + (Amount - vol) * depth.get(i).getAskprice()) / Amount;
					break;
					// System.out.println(amount);
				}
				sum = sum + depth.get(i).getAskvol() * depth.get(i).getAskprice();
				vol = vol + depth.get(i).getAskvol();
			}
		}
		return result;
	}
	
	public BollClass CalculateBollClass(long time)


}
