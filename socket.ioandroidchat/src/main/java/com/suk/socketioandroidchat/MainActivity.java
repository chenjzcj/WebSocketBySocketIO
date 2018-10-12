package com.suk.socketioandroidchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Socket mSocket;

    private String uri = "https://node.tokok.com";
    //private String uri = "https://socket-io-chat.now.sh/";

    private TextView tvInfo;

    private Boolean isConnected = false;
    private String mUsername = "这个名字好";
    private List<Message> mMessages = new ArrayList<>();

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
        tvInfo = findViewById(R.id.tv_info);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_listener).setOnClickListener(this);
        findViewById(R.id.btn_senc_data).setOnClickListener(this);

    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onConnect = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        if (null != mUsername) {
                            mSocket.emit("add user", mUsername);
                        }
                        addInfo("Connected");
                        isConnected = true;
                    }
                }
            });
        }
    };

    private void addInfo(String info) {
        String oldInfo = tvInfo.getText().toString();
        StringBuilder sb = new StringBuilder(oldInfo);
        sb.append("\n").append(info);
        tvInfo.setText(sb.toString());
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onDisconnect = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addInfo("Disconnected, Please check your internet connection");
                    isConnected = false;
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onConnectError = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addInfo("Failed to connect");
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("args = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        LogUtils.i(e.getMessage());
                        return;
                    }
                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("onUserJoined = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        LogUtils.i(e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        addInfo(username + ":" + message);
        // mAdapter.notifyItemInserted(mMessages.size() - 1);
        //scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        //mAdapter.notifyItemInserted(mMessages.size() - 1);
        //scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                //mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void addLog(String message) {
        addInfo(message);
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        //mAdapter.notifyItemInserted(mMessages.size() - 1);
        //scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("onUserLeft = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        LogUtils.i(e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("onTyping = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        LogUtils.i(e.getMessage());
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtils.i("onStopTyping = " + args.length);
            for (int i = 0; i < args.length; i++) {
                LogUtils.i(String.format("arg[%d] = %s", i, args[i]));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        LogUtils.i(e.getMessage());
                        return;
                    }
                    removeTyping(username);
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
                listener();
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
     * 监听服务端发送的数据,使用 onNewMessage 来监听服务器发来的 "new message" 事件
     */
    private void listener() {
        //前面几个是系统默认的事件，后面的是自定义的事件
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        //mSocket.on("new message", onNewMessage);
        //mSocket.on("user joined", onUserJoined);
        //mSocket.on("user left", onUserLeft);
        //mSocket.on("typing", onTyping);
        //mSocket.on("stop typing", onStopTyping);
        //mSocket.on("message", onNewMessage);


        //mSocket.on("login", commonListener);
        //mSocket.on("msg", commonListener);
        mSocket.on("message", commonListener);
        //mSocket.on("marketDetail0", commonListener);
        mSocket.on("heartbeat", heartbeatListener);
        //mSocket.on("data", commonListener);
        //mSocket.on("onlineLength", commonListener);
        //mSocket.on("messageBack", commonListener);
        //mSocket.on("request", commonListener);
        //mSocket.on("formalTrade", commonListener);
        //mSocket.on("kline", commonListener);
        //mSocket.on("marketDetail0", commonListener);
    }

    private Emitter.Listener heartbeatListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("heartbeatListener = " + args.length);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof JSONObject){
                    LogUtils.json(arg.toString());
                }
                if (arg != null) {
                    LogUtils.i(String.format("arg[%d] = %s", i, arg));
                }
            }
        }
    };

    private Emitter.Listener commonListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onStopTyping = " + args.length);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof JSONObject){
                    LogUtils.json(arg.toString());
                }
                if (arg != null) {
                    LogUtils.i(String.format("arg[%d] = %s", i, arg));
                }
            }
        }
    };

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
        //testBean.setSymbol("TOK_ETH");
        testBean.setSymbol("EOS_ETH");
        testBean.setVersion(1);
        testBean.setRequestIndex(System.currentTimeMillis());

        TestBean.SymbolListBean symbolList = new TestBean.SymbolListBean();
        List<TestBean.SymbolListBean.MarketDetail0Bean> marketDetail0 = new ArrayList<>();
        marketDetail0.add(new TestBean.SymbolListBean.MarketDetail0Bean("TOK_ETH", "pushLong"));
        symbolList.setMarketDetail0(marketDetail0);
        testBean.setSymbolList(symbolList);

        Gson gson = new Gson();
        String json = gson.toJson(testBean);
        LogUtils.i("json = " + json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //此处一定把把json字符串转换成jsonobject,否则不行
        mSocket.emit("request", jsonObject);

        //addMessage(mUsername, message);

        //mSocket.emit("new message", message);
        //mSocket.emit("message", message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);

        mSocket.off("login", commonListener);
        mSocket.off("msg", commonListener);
        mSocket.off("message", commonListener);
        mSocket.off("onlineLength", commonListener);
        mSocket.off("messageBack", commonListener);
        mSocket.off("request", commonListener);
        mSocket.off("formalTrade", commonListener);
        mSocket.off("kline", commonListener);
    }
}