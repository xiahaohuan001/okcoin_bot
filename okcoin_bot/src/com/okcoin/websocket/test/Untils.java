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

	public List<Depth> JsonToDepth(JSONArray JSON) {
		JSONObject data = new JSONObject();
		JSONArray bids = new JSONArray();
		JSONArray asks = new JSONArray();
		JSONArray bid = new JSONArray();
		JSONArray ask = new JSONArray();
		String time;
		// Depth temp;
		List<Depth> result = new ArrayList<>();
		data = (JSONObject) JSON.getJSONObject(0).get("data");
		bids = (JSONArray) data.get("bids");
		asks = (JSONArray) data.get("asks");
		time = data.get("timestamp").toString();
		// System.out.println("aaaa"+time);
		// System.out.println(time);
		for (int i = 0; i < bids.size(); i++) {
			Depth depth = new Depth();
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

	public void StringToAccount(Account Account, JSONArray AccountJSON) {
		// JSONObject AccountJSON = JSONObject.fromObject(S);
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
	}

	public List<Order> StringToOrder(String S) {
		List<Order> Orders =new ArrayList<>();
		JSONObject AccountJSON = JSONObject.fromObject(S);
		JSONArray orders = (JSONArray) AccountJSON.get("orders");
		JSONObject temporder = new JSONObject();		
		//if (orders.size() > 0) {
			for (int i = 0; i < orders.size(); i++) {
				Order Order = new Order();
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
		//}
		return Orders;
	}

}
