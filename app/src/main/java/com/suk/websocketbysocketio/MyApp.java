package com.suk.websocketbysocketio;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Felix.Zhong on 2018/10/10 10:43
 */
public class MyApp extends Application {

    public static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    private Socket mSocket;

    {
        try {
            //1.初始化socket.io，设置链接
            IO.Options opts = new IO.Options();
            //如果服务端使用的是https 加以下两句代码,文章尾部提供SSLSocket类
            opts.sslContext = SSLSocket.genSSLSocketFactory();
            opts.hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            mSocket = IO.socket(Constant.SOCKET_IP, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.getConfig().setGlobalTag("Felix_wsbsi");
        mSocket.connect();
    }
}
