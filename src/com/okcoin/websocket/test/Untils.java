package com.okcoin.websocket.test;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NoHttpResponseException;

import com.okcoin.model.*;
import com.okcoin.rest.future.IFutureRestApi;
import com.okcoin.rest.stock.IStockRestApi;

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
		StockAccount StockAccount = new StockAccount();
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
		List<StockOrder> Orders = new ArrayList<>();
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
		List<FutureOrder> Orders = new ArrayList<>();
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

	public List<FutureOrder> GetFutrueOrders(IFutureRestApi f, String symbol, String contract_type)
			throws HttpException, IOException {
		String str;
		while (true) {
			try {
				str = f.future_order_info(symbol, contract_type, "-1", "1", "1", "50");
				int failed = 0;
				if (str.indexOf("true") == -1)
					failed++;
				if (failed == 0)
					return StringToFutureOrder(str);
				if (failed > 0) {
					System.out.println("获取期货挂单信息出错,尝试重试");
					Thread.sleep(100);
				}
			} catch (NoHttpResponseException e1) {
				System.out.println("获取期货挂单信息出错,尝试重试");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (SocketException e2) {
				// TODO Auto-generated catch block
				System.out.println("获取期货挂单信息出错,尝试重试");
			}
		}
	}

	public List<StockOrder> GetStockOrders(IStockRestApi s, String symbol) throws HttpException, IOException {
		String str;
		while (true) {
			try {
				str = s.order_info(symbol, "-1");
				int failed = 0;
				if (str.indexOf("true") == -1)
					failed++;
				if (failed == 0)
					return StringToStockOrder(str);
				if (failed > 0) {
					System.out.println("获取现货挂单信息出错,尝试重试");
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoHttpResponseException e1) {
				System.out.println("获取现货挂单信息出错,尝试重试");
			}
			catch (SocketException e2) {
				System.out.println("获取现货挂单信息出错,尝试重试");
			}
		}
	}

	public FutureAccount GetAccountFuture(IFutureRestApi f) throws HttpException, IOException {
		String str;
		while (true) {
			try {
				str = f.future_userinfo();
				int failed = 0;
				if (str.indexOf("true") == -1)
					failed++;
				if (failed == 0)
					return StringToFutureAccount(str);
				if (failed > 0) {
					System.out.println("获取期货账户信息出错,尝试重试");
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoHttpResponseException e1) {
				System.out.println("获取期货账户信息出错,尝试重试");
			}
			catch (SocketException e2) {
				System.out.println("获取期货账户信息出错,尝试重试");
			}
		}
	}

	public StockAccount GetAccountStock(IStockRestApi s) throws HttpException, IOException {
		String str;
		while (true) {
			try {
				str = s.userinfo();
				int failed = 0;
				if (str.indexOf("true") == -1)
					failed++;
				if (failed == 0)
					return StringToStockAccount(str);
				if (failed > 0) {
					System.out.println("获取现货账户信息出错,尝试重试");

					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoHttpResponseException e1) {
				System.out.println("获取现货账户信息出错,尝试重试");
			}catch (SocketException e2) {
				System.out.println("获取现货账户信息出错,尝试重试");
			}
		}
	}

	public StockAccount StringToStockAccount(String S) {
		StockAccount StockAccount = new StockAccount();
		if (S.indexOf("true") != -1) {
			JSONObject AccountJSON = JSONObject.fromObject(S);
			JSONObject info = (JSONObject) AccountJSON.get("info");
			JSONObject funds = (JSONObject) info.get("funds");
			JSONObject asset = (JSONObject) funds.get("asset");
			JSONObject free = (JSONObject) funds.get("free");
			JSONObject freezed = (JSONObject) funds.get("freezed");
			StockAccount.setMoney(free.getString("cny"));
			StockAccount.setBtcamount(free.getString("btc"));
			StockAccount.setLtcamount(free.getString("ltc"));
			StockAccount.setFrozenmoney(freezed.getString("cny"));
			StockAccount.setFrozenbtcamount(freezed.getString("btc"));
			StockAccount.setFrozenltcamount(freezed.getString("ltc"));
		}
		return StockAccount;
	}

	public FutureAccount StringToFutureAccount(String S) {
		FutureAccount FutureAccount = new FutureAccount();
		if (S.indexOf("true") != -1) {
			JSONObject AccountJSON = JSONObject.fromObject(S);
			JSONObject info = (JSONObject) AccountJSON.get("info");
			JSONObject btc = (JSONObject) info.get("btc");
			JSONObject ltc = (JSONObject) info.get("ltc");
			FutureAccount.setBtc_account_rights(btc.getString("account_rights"));
			FutureAccount.setBtc_keep_deposit(btc.getString("keep_deposit"));
			FutureAccount.setBtc_profit_real(btc.getString("profit_real"));
			FutureAccount.setBtc_profit_unreal(btc.getString("profit_unreal"));
			FutureAccount.setBtc_risk_rate(btc.getString("risk_rate"));

			FutureAccount.setLtc_account_rights(ltc.getString("account_rights"));
			FutureAccount.setLtc_keep_deposit(ltc.getString("keep_deposit"));
			FutureAccount.setLtc_profit_real(ltc.getString("profit_real"));
			FutureAccount.setLtc_profit_unreal(ltc.getString("profit_unreal"));
			FutureAccount.setLtc_risk_rate(ltc.getString("risk_rate"));
		}
		return FutureAccount;
	}

	public double UpdateOkRate(IFutureRestApi f) throws HttpException, IOException {
		String str;
		while (true) {
			try {
				str = f.exchange_rate();
				int failed = 0;
				if (str.indexOf("rate") == -1)
					failed++;
				if (failed == 0)
					return StringToRate(str);
				if (failed > 0) {
					System.out.println("获取汇率出错,尝试重试");

					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoHttpResponseException e1) {
				System.out.println("获取汇率出错,尝试重试");
			}catch (SocketException e1) {
				System.out.println("获取汇率出错,尝试重试");
			}
		}
	}

	public double StringToRate(String S) {
		double rate = 0;
		if (S.indexOf("rate") != -1) {
			JSONObject J = JSONObject.fromObject(S);
			String Rate = J.getString("rate");
			rate = Double.parseDouble(Rate);
		}
		return rate;
	}
}
