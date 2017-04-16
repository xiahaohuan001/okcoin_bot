package com.okcoin.websocket.test;

import java.util.ArrayList;
import java.util.List;
import com.okcoin.model.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.util.logging.resources.logging;

public class Untils {
	public Tick JsonToTick(JSONArray JSON) {
		Tick result = new Tick();
		// System.out.println(JSON);
		return result;
	}

	public List<StockDepth> JsonToStockDepth(JSONArray JSON) {
		JSONObject data = new JSONObject();
		JSONArray bids = new JSONArray();
		JSONArray asks = new JSONArray();
		JSONArray bid = new JSONArray();
		JSONArray ask = new JSONArray();
		String time;
		// Depth temp;
		List<StockDepth> result = new ArrayList<>();
		data = (JSONObject) JSON.getJSONObject(0).get("data");
		bids = (JSONArray) data.get("bids");
		asks = (JSONArray) data.get("asks");
		time = data.get("timestamp").toString();
		// System.out.println("aaaa"+time);
		// System.out.println(time);
		for (int i = 0; i < bids.size(); i++) {
			StockDepth depth = new StockDepth();
			ask = (JSONArray) asks.get(bids.size() - 1 - i);
			bid = (JSONArray) bids.get(i);
			depth.setAskprice(ask.getString(0));
			depth.setAskvol(ask.getString(1));
			depth.setBidprice(bid.getString(0));
			depth.setBidvol(bid.getString(1));
			depth.setTime(time);
			result.add(depth);
		}

		// System.out.println();
		// result.sort(res);
		return result;
	}
	
	public List<FutureDepth> JsonToFutureDepth(JSONArray JSON) {
		JSONObject data = new JSONObject();
		JSONArray bids = new JSONArray();
		JSONArray asks = new JSONArray();
		JSONArray bid = new JSONArray();
		JSONArray ask = new JSONArray();
		String time;
		// Depth temp;
		List<FutureDepth> result = new ArrayList<>();
		data = (JSONObject) JSON.getJSONObject(0).get("data");
		bids = (JSONArray) data.get("bids");
		asks = (JSONArray) data.get("asks");
		time = data.get("timestamp").toString();
		// System.out.println("aaaa"+time);
		// System.out.println(time);
		for (int i = 0; i < bids.size(); i++) {
			FutureDepth depth = new FutureDepth();
			ask = (JSONArray) asks.get(bids.size() - 1 - i);
			bid = (JSONArray) bids.get(i);
			depth.setAskprice(ask.getString(0));
			depth.setAskfvol(ask.getString(1));
			depth.setAsksvol(ask.getString(2));
			depth.setAsksfvol(ask.getString(4));
			depth.setAskssvol(ask.getString(3));
			depth.setBidprice(bid.getString(0));
			depth.setBidfvol(bid.getString(1));
			depth.setBidsvol(bid.getString(2));
			depth.setBidsfvol(bid.getString(4));
			depth.setBidssvol(bid.getString(3));
			depth.setTime(time);
			result.add(depth);
		}

		// System.out.println();
		// result.sort(res);
		return result;
	}

	public StockAccount StringToStockAccount(StockAccount Account, JSONArray AccountJSON) {
		// JSONObject AccountJSON = JSONObject.fromObject(S);
		StockAccount StockAccount =new StockAccount();
		JSONObject data = (JSONObject) AccountJSON.getJSONObject(0).get("data");
		JSONObject info = (JSONObject) data.get("info");
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
		// System.out.println(free.getString("btc"));
		return StockAccount;
	}

	public List<StockOrder> StringToStockOrder(String S) {
		List<StockOrder> Orders =new ArrayList<>();
		JSONObject AccountJSON = JSONObject.fromObject(S);
		JSONArray orders = (JSONArray) AccountJSON.get("orders");
		JSONObject temporder = new JSONObject();		
			for (int i = 0; i < orders.size(); i++) {
				StockOrder Order = new StockOrder();
				temporder = (JSONObject) orders.get(i);
				Order.setAmount(temporder.getString("amount"));
				Order.setAvgprice(temporder.getString("avg_price"));
				Order.setCreatdate(temporder.getString("create_date"));
				Order.setDealamount(temporder.getString("deal_amount"));
				Order.setOrderid(temporder.getString("order_id"));
				Order.setOrdersid(temporder.getString("orders_id"));
				Order.setPrice(temporder.getString("price"));
				Order.setStatus(temporder.getString("status"));
				Order.setSymbol(temporder.getString("symbol"));
				Order.setType(temporder.getString("type"));
				Orders.add(Order);
			}
		return Orders;
	}
	
	public List<FutureOrder> StringToFutureOrder(String S) {
		List<FutureOrder> Orders =new ArrayList<>();
		JSONObject AccountJSON = JSONObject.fromObject(S);
		JSONArray orders = (JSONArray) AccountJSON.get("orders");
		JSONObject temporder = new JSONObject();		
			for (int i = 0; i < orders.size(); i++) {
				FutureOrder Order = new FutureOrder();
				temporder = (JSONObject) orders.get(i);
				Order.setAmount(temporder.getString("amount"));
				Order.setContract_name(temporder.getString("contract_name"));
				Order.setCreate_date(temporder.getString("create_date"));
				Order.setDeal_amount(temporder.getString("deal_amount"));
				Order.setFee(temporder.getString("fee"));
				Order.setOrder_id(temporder.getString("order_id"));
				Order.setPrice(temporder.getString("price"));
				Order.setStatus(temporder.getString("status"));
				Order.setSymbol(temporder.getString("symbol"));
				Order.setType(temporder.getString("type"));
				Order.setPrice_avg(temporder.getString("price_avg"));
				Order.setUnit_amount(temporder.getString("unit_amount"));
				Order.setLever_rate(temporder.getString("lever_rage"));
				Orders.add(Order);
			}
		return Orders;
	}

}
