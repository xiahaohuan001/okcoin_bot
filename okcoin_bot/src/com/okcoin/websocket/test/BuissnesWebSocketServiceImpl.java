package com.okcoin.websocket.test;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.WebSocketService;
import com.sun.jmx.snmp.Timestamp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.okcoin.strategy.btc_hedge_ltc;
//import com.alibaba.fastjson.JSONObject;
import com.okcoin.ui.*;

/**
 * 订阅信息处理类需要实现WebSocketService接口
 * 
 * @author okcoin
 *
 */
public class BuissnesWebSocketServiceImpl implements WebSocketService {
	private Logger log = Logger.getLogger(WebSocketBase.class);
	JSONArray msgObjList;
	btc_hedge_ltc strategy=new btc_hedge_ltc();
	public static JSONArray btcdepth=null;
	public static JSONArray ltcdepth=null;
	public static JSONArray userinfo=null;
	public static JSONArray btctick=null;
	public static JSONArray ltctick=null;
	@Override
	public void onReceive(String msg) {
		if (!msg.equals("{\"event\":\"pong\"}")) {
			//msgObjList = JSONArray.fromObject(msg);
			if (msg.indexOf("data")!=-1){				
				if(msg.indexOf("ok_sub_spotcny_btc_depth_60")!=-1)
					btcdepth = JSONArray.fromObject(msg);
				else if(msg.indexOf("ok_sub_spotcny_ltc_depth_20")!=-1)
					ltcdepth = JSONArray.fromObject(msg);
				//else if(msg.indexOf("ok_spotcny_userinfo")!=-1)
					//userinfo = JSONArray.fromObject(msg);
				//else if(msg.indexOf("btc_ticker")!=-1)
					//btctick = JSONArray.fromObject(msg);
				//else if(msg.indexOf("ltc_ticker")!=-1)
					//ltctick = JSONArray.fromObject(msg);
				if(btcdepth!=null&&ltcdepth!=null)
					try {
						//log.info(userinfo);
						strategy.btc_hedge_ltc(btcdepth,ltcdepth);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//log.info(msg);			
				}
				
			//else
				//log.info("连接信息" + msg);
		}
		//else
			//log.info("心跳检测"+msg);
		//log.info(msg);
	}
}
