package com.suk.socketioandroidchat.websocket.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix.Zhong on 2018/10/12 12:30
 */
public class DataAssembleHelper {
    private static DataAssembleHelper instance;
    private Gson mGson;

    private DataAssembleHelper() {
        mGson = new Gson();
    }

    public static DataAssembleHelper getInstance() {
        if (instance == null) {
            instance = new DataAssembleHelper();
        }
        return instance;
    }

    /**
     * 生成 EmitMessge 的Json格式
     *
     * @param symbol 如：TOK_ETH ，EOS_ETH
     * @return JSONObject
     */
    public JSONObject genEmitMessgeJson(String symbol) {
        EmitMessage emitMessage = new EmitMessage();
        emitMessage.setMsgType("reqMsgSubscribe");
        emitMessage.setWebsite("20180502cn");
        emitMessage.setSymbol(symbol);
        emitMessage.setVersion(1);
        emitMessage.setRequestIndex(System.currentTimeMillis());

        EmitMessage.SymbolListBean symbolList = new EmitMessage.SymbolListBean();
        List<EmitMessage.SymbolListBean.MarketDetail0Bean> marketDetail0 = new ArrayList<>();
        marketDetail0.add(new EmitMessage.SymbolListBean.MarketDetail0Bean(symbol, "pushLong"));
        symbolList.setMarketDetail0(marketDetail0);
        emitMessage.setSymbolList(symbolList);


        String json = mGson.toJson(emitMessage);
        //此处一定把把json字符串转换成jsonobject,否则不行
        //LogUtils.i("json = " + json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
