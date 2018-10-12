package com.suk.socketioandroidchat.websocket;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Felix.Zhong on 2018/10/12 11:12
 * https://blog.csdn.net/l_lhc/article/details/68950278
 */
public class WebSocketHelper {
    /**
     * 一个项目中只能有一个Socket，将WebSocketHelper弄成单例
     */
    private static WebSocketHelper instance;

    private WebSocketHelper() {
    }

    public static WebSocketHelper getInstance() {
        if (instance == null) {
            instance = new WebSocketHelper();
        }
        return instance;
    }

    /**
     * 服务器地址
     */
    private static String WEBSOCKET_URL = "https://node.tokok.com";
    /**
     * 自定义监听事件
     */
    private static final String[] ON_EVENTS = {"message"};

    /**
     * 自定义发射事件
     */
    public static final String[] EMIT_EVENTS = {"request"};

    private Socket mSocket;

    /**
     * 初始化
     */
    public void init() {
        try {
            //1.初始化socket.io，设置链接
            mSocket = IO.socket(WEBSOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接连接
     */
    public void connect() {
        //2.建立socket.io服务器的连接
        mSocket.connect();
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        mSocket.disconnect();
    }

    /**
     * 释放资源
     */
    public void release() {
        disconnect();
        unListener();
    }

    /**
     * 发射数据
     *
     * @param event 事件
     * @param args  数据
     */
    public void emit(final String event, final Object... args) {
        mSocket.emit(event, args);
    }

    /**
     * 监听服务端发送的数据,如使用 onMessageListener 来监听服务器发来的 "message" 事件
     */
    public void listener() {
        //系统默认的事件
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        //后面的是自定义的事件
        mSocket.on(ON_EVENTS[0], onMessageListener);
    }

    /**
     * 注销事件，释放资源
     */
    private void unListener() {
        //系统默认的事件
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        //后面的是自定义的事件
        mSocket.off(ON_EVENTS[0], onMessageListener);
    }

    /**
     * 连接成功回调
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onConnect 连接成功 args.length = " + args.length);
            printArgs(args);
        }
    };

    /**
     * 连接断开回调
     */
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onDisconnect 连接断开 args.length = " + args.length);
            printArgs(args);
        }
    };

    /**
     * 连接错误回调
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onConnectError 连接错误 args.length = " + args.length);
            printArgs(args);
        }
    };

    /**
     * 自定义的主要数据事件监听
     */
    private Emitter.Listener onMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.i("onMessageListener 新数据来啦 args.length = " + args.length);
            handlerData(args);
        }
    };

    /**
     * 主要数据处理
     *
     * @param args 参数
     */
    private void handlerData(Object... args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof JSONObject) {
                LogUtils.json(arg.toString());
            }
            if (arg != null) {
                LogUtils.i(String.format(Locale.CHINA, "arg[%d] = %s", i, arg));
            }
        }
    }

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
