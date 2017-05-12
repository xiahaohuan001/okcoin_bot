package com.okcoin.websocket.test;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpException;

import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.okcoin.strategy.btc_hedge_ltc;
import com.okcoin.strategy.stock_hedge_future_wg;
import com.okcoin.ui.FrmMain;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.WebSocketService;

/**
 * WebSocket API使用事例
 * 
 * @author okcoin
 * 
 */
public class Main{
	
	public static void main(String[] args) throws HttpException, IOException{

		// apiKey 为用户申请的apiKey
		//btc_hedge_ltc strategy =new btc_hedge_ltc();
//		String apiKey_cn = "46674f7f-d06b-44f3-ac79-62945e4e6e6d";
//		String secretKey_cn = "45EE20A0F9285983811E91A0A09E3989";		
//		String apiKey_com = "5a395d18-6dc9-44b9-9b88-b13c7831feb9";
//		String secretKey_com = "6702CA3A2AE744D6810249582D132A28";

		String url_com_ws = "wss://real.okcoin.com:10440/websocket/okcoinapi";
		String url_com_rest = "https://www.okcoin.com/api/v1";
		String url_cn_ws = "wss://real.okcoin.cn:10440/websocket/okcoinapi";
		String url_cn_rest = "https://www.okcoin.cn/api/v1";

		// 订阅消息处理类,用于处理WebSocket服务返回的消息
		WebSocketService service = new BuissnesWebSocketServiceImpl();
		//System.out.println(service.getClass());

		// WebSocket客户端
		WebSoketClient client_com = new WebSoketClient(url_com_ws, service);
		//WebSoketClient client_cn = new WebSoketClient(url_cn_ws, service);
		// 启动客户端
		client_com.start();
		//client_cn.start();

		// 添加订阅
		//client.addChannel("ok_sub_spotcny_ltc_ticker");
		//client_cn.addChannel("ok_sub_futureusd_btc_depth_this_week_60");
//		client_com.addChannel("ok_sub_futureusd_ltc_depth_quarter_60");
//		client_com.addChannel("ok_sub_futureusd_ltc_depth_this_week_60");
//		client_com.addChannel("ok_sub_futureusd_ltc_depth_next_week_60");
		client_com.addChannel("ok_sub_spotusd_btc_depth_60");
		client_com.addChannel("ok_sub_futureusd_btc_depth_this_week_60");
		//client_cn.addChannel("ok_sub_spotcny_btc_depth_60");
//		BuissnesWebSocketServiceImpl e = new BuissnesWebSocketServiceImpl();
//		e.start();
		//client.addChannel("ok_sub_spotcny_ltc_depth_20");
		//client.addChannel("ok_sub_spotcny_btc_depth_60");
		//uinfoclint.getUserInfo(apiKey,secretKey);
		
		//((WebSocketBase) strategy).start();
		//IStockRestApi stockGet = new StockRestApi(url_rest);
		//IStockRestApi stockPost = new StockRestApi(url_rest, apiKey, secretKey);
		//stockGet.ticker("btc_cny");
		
		
		// 删除定订阅
		// client.removeChannel("ok_sub_spotusd_btc_ticker");

		// 合约下单交易
		// client.futureTrade(apiKey, secretKey, "btc_usd", "this_week", 2.3, 2,
		// 1, 0, 10);

		// 实时交易数据 apiKey
		// client.futureRealtrades(apiKey, secretKey);

		// 取消合约交易
		// client.cancelFutureOrder(apiKey, secretKey, "btc_usd", 123456L,
		// "this_week");

		// 现货下单交易
		// client.spotTrade(apiKey, secretKey, "btc_usd", 3.2, 2.3, "buy");

		// 现货交易数据
		// client.realTrades(apiKey, secretKey);

		// 现货取消订单
		// client.cancelOrder(apiKey, secretKey, "btc_usd", 123L);

		// 获取用户信息
		// client.getUserInfo(apiKey,secretKey);
	}
}
