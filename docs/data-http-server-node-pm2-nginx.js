"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const http = require("http");
const io = require("socket.io");
const request = require("request");
let NSeverInfo = {
    PORT: 3000,
    NginxAgentPORT: 3131 + parseInt(process.env.NODE_APP_INSTANCE),
    CMD: parseInt(process.env.REMOTE_CMD),
    DoMian: "http://node.tokok.com"
};
let TvParameter = {
    website: "cn",
    symbol: "BTC",
    period: "1min",
    marketDetail: 0
};
let domain_url = "https://www.tokok.com";
const app = express();
const httpServer = http.createServer(app);
const sio = io(httpServer);
app.get('/', function (req, res) {
    if (req.protocol === 'https') {
        res.status(200).send('<h1>Data NodeJS Web Socket Https Server to Safety Land!</h1>');
    }
    else {
        res.status(200).send('<h1>Data NodeJS Web Socket Server</h1>');
    }
});
let onlineUsers = [];
let onlineCount = 0;
sio.sockets.on('connection', function (socket) {
    socket.on('login', function (obj) {
        if (obj.room.indexOf("kline") != -1) {
            let i = 0;
            console.log("---------------行情中心退房算法--------------------");
            for (let key in socket.adapter.rooms) {
                console.log("key==" + key);
                i++;
                if (i > 2) {
                    if (key != obj.room) {
                        console.log("退出" + key + "房间");
                        socket.leave(key);
                    }
                }
            }
            console.log("加入专业交易" + obj.room);
            socket.join(obj.room);
        }
        else if (obj.room.indexOf("formalTrade") != -1) {
            let i = 0;
            console.log("---------------专业交易退房算法--------------------");
            for (let key in socket.adapter.rooms) {
                console.log("key==" + key);
                i++;
                if (i > 2) {
                    if (key != obj.room) {
                        console.log("退出" + key + "房间");
                        socket.leave(key);
                    }
                }
            }
            console.log("加入专业交易" + obj.room);
            socket.join(obj.room);
        }
        else {
            socket.join(obj.room);
            console.log("加入----------------------" + obj.room);
        }
        socket.id = obj.userid;
        if (!onlineUsers.hasOwnProperty(obj.userid)) {
            onlineUsers[obj.userid] = obj.username;
            onlineCount++;
        }
        sio.emit('login', {
            onlineUsers: onlineUsers,
            onlineCount: onlineCount,
            user: obj
        });
    });
    socket.on('message', function (data, isRequest) {
        sio.emit('message', data, isRequest);
    });
    socket.on('loginChat', function (obj) {
        socket.id = obj.username;
        if (onlineUsers.indexOf(obj.username) == -1) {
            onlineUsers.push(obj.username);
            onlineCount++;
            console.log(obj.username + '加入');
        }
        socket.emit('onlineLength', onlineCount);
        console.log(obj.username + '加入了聊天室');
    });
    socket.on('messageSend', function (data) {
        console.log(data.name + '发言');
        sio.sockets.emit('messageBack', data);
    });
    socket.on('disconnect', function () {
        if (onlineUsers.indexOf(Number.parseInt(socket.id)) > -1) {
            onlineUsers.splice(onlineUsers.indexOf(Number.parseInt(socket.id)), 1);
            onlineCount--;
        }
        console.log(socket.id + '退出');
    });
    socket.on('request', function (obj) {
        if (obj.website != undefined && obj.website.indexOf("en") != -1) {
            TvParameter.website = "en";
        }
        if (obj.symbol != undefined) {
            TvParameter.symbol = obj.symbol;
        }
        if (obj.msgType == "reqMsgUnsubscribe") {
            if (obj.symbolList.lastKLine != undefined) {
                TvParameter.period = obj.symbolList.lastKLine[0].period[0];
                let kroom = TvParameter.website + TvParameter.symbol + TvParameter.period;
                console.log("离开" + kroom + "房间");
                socket.leave(kroom);
            }
            for (let i = 0; i <= 5; i++) {
                if (eval("obj.symbolList.marketDetail" + i) != undefined) {
                    TvParameter.marketDetail = i;
                    let droom = TvParameter.website + TvParameter.symbol + TvParameter.marketDetail;
                    console.log("离开" + droom + "房间");
                    socket.leave(droom);
                }
            }
        }
        if (obj.msgType == "reqMsgSubscribe") {
            try {
                if (obj.symbolList.lastKLine != undefined) {
                    TvParameter.period = obj.symbolList.lastKLine[0].period[0];
                    let kroom = TvParameter.website + TvParameter.symbol + TvParameter.period;
                    console.log("加入" + kroom + "房间");
                    socket.join(kroom);
                    if ("cn" == TvParameter.website) {
                        request(domain_url + '/klinevtwo/con?symbol=' + TvParameter.symbol + '&period=' + TvParameter.period, function (error, response, body) {
                            if (!error && response.statusCode == 200) {
                                let data = JSON.parse(body);
                                console.log("加载K线数据" + TvParameter.website + TvParameter.symbol + data.reqKLine.payload.period);
                                sio.in(TvParameter.website + TvParameter.symbol + data.reqKLine.payload.period).emit('request', data.reqMsgSubscribe, 200);
                                sio.in(TvParameter.website + TvParameter.symbol + data.reqKLine.payload.period).emit('request', data.reqKLine, 200);
                            }
                        });
                    }
                }
                for (let i = 0; i <= 5; i++) {
                    if (eval("obj.symbolList.marketDetail" + i) != undefined) {
                        TvParameter.marketDetail = i;
                        let droom = TvParameter.website + TvParameter.symbol + TvParameter.marketDetail;
                        console.log("加入" + droom + "房间");
                        socket.join(droom);
                    }
                }
            }
            catch (e) {
            }
        }
        if (obj.msgType == "user_room") {
            console.log("--------------加入user_room房间");
            socket.join("user_room");
        }
    });
    socket.on('reconnect', function (type, obj) {
    });
    socket.on('error', function (type, obj) {
    });
});
setInterval(() => {
    request(domain_url + '/klinevtwo/message', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            try {
                let data = JSON.parse(body);
                for (let i = 0; i < data.productList.length; i++) {
                    let symbol = data.productList[i];
                    let marketDetail = eval("data.marketDetail." + symbol);
                    for (let j = 0; j < marketDetail.length; j++) {
                        let droom = "cn" + symbol + j;
                        sio.in(droom).emit('message', marketDetail[j], 200);
                        let formalTradeRoom = "formalTrade_cn_" + symbol + "_" + j;
                        sio.in(formalTradeRoom).emit('formalTrade', marketDetail[j]);
                        if (j == 0) {
                            let klineRoom = "kline_cn_" + symbol;
                            sio.in(klineRoom).emit('kline', symbol, marketDetail[j]);
                        }
                    }
                    let lastKLine = eval("data.lastKLine." + symbol);
                    for (let j = 0; j < lastKLine.length; j++) {
                        let kroom = "cn" + symbol + lastKLine[j].payload.period;
                        sio.in(kroom).emit('message', lastKLine[j], 200);
                    }
                }
            }
            catch (e) {
            }
        }
    });
}, 2000);
httpServer.listen(NSeverInfo.PORT, function () {
    console.log('listening on *:' + NSeverInfo.PORT);
});
