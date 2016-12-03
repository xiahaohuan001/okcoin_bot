package com.okcoin.websocket.test;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpException;

import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;
import com.okcoin.strategy.btc_hedge_ltc;
import com.okcoin.ui.FrmMain;
import com.okcoin.websocket.WebSocketBase;
import com.okcoin.websocket.WebSocketService;

/**
 * WebSocket API使用事例
 * 
 * @author okcoin
 * 
 */
public class Main {
	public static void main(String[] args) throws HttpException, IOException{

		// apiKey 为用户申请的apiKey
		//btc_hedge_ltc strategy =new btc_hedge_ltc();
		String apiKey = "46674f7f-d06b-44f3-ac79-62945e4e6e6d";

		// secretKey为用户申请的secretKey
		String secretKey = "45EE20A0F9285983811E91A0A09E3989";
		//new FrmMain();
		//apiKey = "dfbb23da-5cac-4bec-b4ce-ed828f8a5458";
		//secretKey = "B0FA4F10E14364A0C7562DDFBBFD432C";

		// 国际站WebSocket地址 注意如果访问国内站 请将 real.okcoin.com 改为 real.okcoin.cn
		String url = "wss://real.okcoin.cn:10440/websocket/okcoinapi";
		String url_rest ="https://www.okcoin.cn";

		// 订阅消息处理类,用于处理WebSocket服务返回的消息
		WebSocketService service = new BuissnesWebSocketServiceImpl();
		//System.out.println(service.getClass());

		// WebSocket客户端
		WebSoketClient client = new WebSoketClient(url, service);
		//WebSoketClient uinfoclint = new WebSoketClient(url, service);
		//WebSoketClient ltctick = new WebSoketClient(url, service);
		//WebSoketClient btcdepth = new WebSoketClient(url, service);
		//WebSoketClient ltcdepth = new WebSoketClient(url, service);
		// 启动客户端
		client.start();
		//uinfoclint.start();
		//ltctick.start();

		// 添加订阅
		//client.addChannel("ok_sub_spotcny_ltc_ticker");
		//client.addChannel("ok_sub_spotcny_btc_ticker");
		client.addChannel("ok_sub_spotcny_ltc_depth_20");
		client.addChannel("ok_sub_spotcny_btc_depth_60");
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
