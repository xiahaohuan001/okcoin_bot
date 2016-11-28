package com.okcoin.websocket.test;
import java.util.ArrayList;
import java.util.List;
import com.okcoin.model.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Untils {	
	public Tick JsonToTick(JSONArray JSON) {
		Tick result = new Tick();
		//System.out.println(JSON);		
		return result;		
	}

	public List<Depth> JsonToDepth(JSONArray JSON) {
		JSONObject data = new JSONObject();
		JSONArray bids = new JSONArray();
		JSONArray asks = new JSONArray();
		JSONArray bid = new JSONArray();
		JSONArray ask = new JSONArray();
		//Depth temp;
		List<Depth> result = new ArrayList<>();
		data = (JSONObject) JSON.getJSONObject(0).get("data");
		bids=(JSONArray) data.get("bids");
		asks=(JSONArray) data.get("asks");
		//System.out.println(asks);
		for (int i = 0; i < bids.size(); i++) {
			Depth depth = new Depth();
			ask=(JSONArray) asks.get(bids.size()-1-i);
			bid=(JSONArray) bids.get(i);
			depth.setAskprice(ask.get(0).toString());
			depth.setAskvol(ask.get(1).toString());
			depth.setBidprice(bid.get(0).toString());
			depth.setBidvol(bid.get(1).toString());
			result.add(depth);
		}
		
		//System.out.println();
		//result.sort(res);
		return result;
    }

	

	

}
