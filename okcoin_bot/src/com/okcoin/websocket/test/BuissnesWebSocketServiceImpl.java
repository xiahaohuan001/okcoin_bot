package com.okcoin.websocket.test;

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
	// public static String MSG=null;
	// FrmMain F=new FrmMain();
	JSONArray msgObjList;
	//JSONObject msgObj;
	btc_hedge_ltc strategy=new btc_hedge_ltc();
	public static JSONArray btcinfo;
	public static JSONArray ltcinfo;
	@Override
	public void onReceive(String msg) {
		if (!msg.equals("{\"event\":\"pong\"}")) {
			msgObjList = JSONArray.fromObject(msg);
			if (msgObjList.getJSONObject(0).get("data") != null){
				
				if(msgObjList.getJSONObject(0).toString().indexOf("btc")!=-1)
					btcinfo = msgObjList;
					//System.out.println(msgObjList.getJSONObject(0).toString());
				else
					ltcinfo = msgObjList;
					//System.out.println(msgObjList.getJSONObject(0).toString());
				if((ltcinfo!=null)&&(btcinfo!=null))
				strategy.btc_hedge_ltc(btcinfo,ltcinfo);
				//log.info("数据消息" + msg);
				}
				
			else
				log.info("连接信息" + msg);
		}
		//else
			//log.info("心跳检测"+msg);
		//log.info(msg);
	}
}
