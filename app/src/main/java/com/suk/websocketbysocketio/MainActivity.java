package com.suk.websocketbysocketio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyApp app;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.mPresenter = createPresenter(mPresenter);
        //loading = DialogUtil.getNewLoadingDialog(mFragAct);
        app = MyApp.getInstance();
        //spUtil = app.getmSpUtil();
        mSocket = app.getSocket();

        findViewById(R.id.btn_listener).setOnClickListener(this);
        findViewById(R.id.btn_senc_data).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_listener:
                //监听服务端发送的数据
                mSocket.on("message", messageListener);
                mSocket.on("onlineLength",messageListener);
                mSocket.on("kline",messageListener);
                mSocket.on("messageBack",messageListener);
                mSocket.on("request",messageListener);
                mSocket.on("formalTrade",messageListener);
                mSocket.on("login",messageListener);
                break;
            case R.id.btn_senc_data:
                //向服务端发送数据
                mSocket.emit("message", "你好");
                mSocket.emit("onlineLength","你好");
                break;
            default:
                break;
        }
    }

    private Emitter.Listener messageListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("args");
            //主线程调用
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args[0];
                    //根据个人业务处理
                    //LogUtils.json(data.toString());
                }
            });
        }
    };
}
