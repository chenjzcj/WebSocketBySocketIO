package com.suk.socketioandroidchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Socket mSocket;
    private String uri = "https://node.tokok.com";
    //private String uri = "http://192.168.2.9:3000";

    {
        try {
            //1.初始化socket.io，设置链接
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private EditText mInputMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputMessageView = findViewById(R.id.et_msg);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_listener).setOnClickListener(this);
        findViewById(R.id.btn_senc_data).setOnClickListener(this);

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            LogUtils.i("args = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }

            //主线程调用
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/

                    // add the message to view
                    //addMessage(username, message);
                }
            });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                //2.建立socket.io服务器的连接
                mSocket.connect();
                break;
            case R.id.btn_listener:
                //监听服务端发送的数据,使用 onNewMessage 来监听服务器发来的 "new message" 事件
                mSocket.on("request", onNewMessage);
                break;
            case R.id.btn_senc_data:
                //向服务端发送数据
                attemptSend();
                break;
            default:
                break;
        }
    }


    /**
     * 发送消息的方法
     */
    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            message = "empty data";
            //return;
        }
        mInputMessageView.setText("");
        //mSocket.emit("request", message);

        TestBean testBean = new TestBean();
        testBean.setMsgType("reqMsgSubscribe");
        testBean.setWebsite("20180502cn");
        testBean.setSymbol("TOK_ETH");
        testBean.setVersion(1);
        testBean.setRequestIndex(System.currentTimeMillis());

        TestBean.SymbolListBean symbolList = new TestBean.SymbolListBean();
        List<TestBean.SymbolListBean.MarketDetail0Bean> marketDetail0 = new ArrayList<>();
        marketDetail0.add(new TestBean.SymbolListBean.MarketDetail0Bean("TOK_ETH", "pushLong"));
        symbolList.setMarketDetail0(marketDetail0);
        testBean.setSymbolList(symbolList);

        Gson gson = new Gson();
        String json = gson.toJson(testBean);
        LogUtils.i("json = "+json);
        mSocket.emit("request", json);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mSocket.disconnect();
        mSocket.off("request", onNewMessage);
    }
}
