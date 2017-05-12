package com.okcoin.websocket.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.WebSocketService;
import com.sun.jmx.snmp.Timestamp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.java2d.pipe.LoopBasedPipe;

import com.okcoin.strategy.*;
//import com.alibaba.fastjson.JSONObject;
import com.okcoin.ui.*;

/**
 * 订阅信息处理类需要实现WebSocketService接口
 * 
 * @author okcoin
 *
 */
public class BuissnesWebSocketServiceImpl extends Thread implements WebSocketService  {
	private Logger log = Logger.getLogger(WebSocketBase.class);		
	public static String stock_depth = null;
	public static String stock_tick = null;
	public static String stock_user_info = null;	
	public static String future_depth = null;
	public static String future_tick = null;
	public static String future_user_info = null;
	public static String depth = null;
	future_hedge_future future_hedge_future = new future_hedge_future();	
	stock_hedge_future_wg stock_hedge_future_wg = new stock_hedge_future_wg();
	dq dq = new dq();
	@Override
	
	public void onReceive(String msg) {
		if (!msg.equals("{\"event\":\"pong\"}")) {
			if (msg.indexOf("data")!=-1){
				if(msg.indexOf("depth")!=-1){
					depth = msg;
					try {	
//						if(msg.indexOf("ltc")!=-1)
						//dq.OnTick(depth);
//						if(msg.indexOf("btc")!=-1)
						stock_hedge_future_wg.OnTick(depth);
						
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(msg.indexOf("spot")!=-1){
					if(msg.indexOf("ticker")!=-1)
						stock_tick = msg;
					if(msg.indexOf("depth")!=-1)
						stock_depth= msg;
					if(msg.indexOf("userinfo")!=-1)
						stock_user_info= msg;
				}
				if(msg.indexOf("future")!=-1){
					if(msg.indexOf("ticker")!=-1)
						future_tick= msg;
					if(msg.indexOf("depth")!=-1){
//						future_depth= msg;
//						try {
//							stock_hedge_future_wg.OnTick(future_depth);
//						} catch (HttpException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						//System.out.println("size" + future_depth.size());
					}						
					if(msg.indexOf("userinfo")!=-1)
						future_user_info= msg;
				}

				//log.info(msg);			
				}
				
			//else
				//log.info("连接信息" + msg);
		}
		//else
			//log.info("心跳检测"+msg);
		//frmMain.LoadMsg(msg);
		//log.info(msg);
//		if(msg.indexOf("this_week")!=-1)
//			log.info("当周："+msg);
//		if(msg.indexOf("quarter")!=-1)
//			log.info("季度："+msg);
	}
}
