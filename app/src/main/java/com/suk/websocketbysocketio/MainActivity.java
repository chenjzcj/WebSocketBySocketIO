package com.suk.websocketbysocketio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.suk.websocketbysocketio.websocket.DataAssembleHelper;

import org.json.JSONObject;

import java.util.Locale;

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
                break;
            case R.id.btn_senc_data:
                //向服务端发送数据
                JSONObject jsonObject = DataAssembleHelper.getInstance().genEmitMessgeJson("TOK_ETH");
                mSocket.emit("request",jsonObject);
                break;
            default:
                break;
        }
    }

    private Emitter.Listener messageListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            printArgs(args);
        }
    };

    /**
     * 打印参数
     *
     * @param args 需要打印的可变参数
     */
    private void printArgs(Object... args) {
        for (int i = 0; i < args.length; i++) {
            LogUtils.i(String.format(Locale.CHINA, "arg[%d] = %s", i, args[i]));
        }
    }
}
