
function connect(ws) {
    var socket = {},
        taskList = {},
        msgList = {},
        firstConn = true,
        isconnect,conn;
    ws = ws || 'ws://192.168.2.45';
    if (ws && window['socketInstanced'] && window['socketInstanced'][ws]) {
        return socketInstanced[ws];
    }
    function callback(data, isRequest) {
        var message = 'msgType',len;
        if (!data || !data[message]) return;
        //进入交易页面 就赋值 买一价=卖  卖一价=买
        /*if(data.payload!=null && data.payload!=''){
        	if(data.payload.asks!='' && data.payload.asks!=undefined){
        		if(data.payload.asks.price!='' && data.payload.asks.price!=undefined){
        			if(data.payload.asks.price[0]!=''){
        				$('#sell_price').val(data.payload.asks.price[0]);
        			}
        		}
        	}
        	if(data.payload.bids!='' && data.payload.bids!=undefined){
        		if(data.payload.bids.price!='' && data.payload.bids.price!=undefined){
        			if(data.payload.bids.price[0]!=''){
        				$('#buy_price').val(data.payload.bids.price[0]);
        			}
        		}
        	}
        }*/
        if(Array.prototype.forEach){
            taskList[data[message]] && Array.prototype.forEach.call(taskList[data[message]], function(fn, i) {
                fn(data);
            });
        }else if(taskList[data[message]]){
            len = taskList[data[message]].length;
            for(var _this = taskList[data[message]], i = 0, l = _this.length; i < l; i++){
                _this[i](data);
            }
        }
        socket.LastTime = Math.round(new Date().getTime());
    }

    function requestEvent(data) {
        callback(data, 1);
    }
    socket.reg = function(key, fn) {
        if(typeof key == 'object'){
            if(key['symbolList']){
                for(var k in key['symbolList']){
                    if( key['symbolList'].hasOwnProperty(k)){
                        taskList[k+key['symbolList'][k][0]['symbolId']] = fn;
                    }
                }
            }else if(key['msgType']){
                taskList[key.msgType+key.symbolId] ? taskList[key.msgType+key.symbolId].push(fn) : (taskList[key.msgType+key.symbolId] = [fn]);
            }

        }else{
            taskList[key] ? taskList[key].push(fn) : (taskList[key] = [fn]);
        }
    };
    socket.help = function(){
        var str= 'connect help\n'
            +'\nconnectInstance.reg()'
            +'\nType:Function'
            +'\nSignature:reg(key,fn)'
            +'\nregister callback function. arg1 is msgType, arg2 is callback function'
            +'\n\nconnectInstance.msg()'
            +'\nType:Function'
            +'\nSignature:msg(JSON Object)'
            +'\nsend message to service. arg1 is a message that type is JSON Object';
    };
    socket.fnList = taskList;
    function connectEvent(){
        if(!firstConn)return;
        msgList.userrRoom = {msgType:"user_room"};
        for (var k in msgList) {
        	
        	var  website  = msgList[k];
            conn.emit("request", msgList[k]);
        }
        firstConn = false;
        isconnect = true;
    }
    function disconnectEvent(e){
        var str = {};
        isconnect = false;
        //taskList = {};
        try{ str = JSON.stringify(e)}catch(e){}
    }
    socket.msg = function(msg, type) {
        if(type === 'unMsg'){
            if(msg['symbolList']) {
                for(var _key in msg['symbolList']){
                    if(msg['symbolList'].hasOwnProperty(_key)) {
                       delete msgList[_key + msg['symbolList'][_key][0]['symbolId']]
                    }
                }
            }else{
               delete msgList[msg];
            }
        }else{
        	
            if(msg['symbolList']){
                for(var key in msg['symbolList']){
                    if(msg['symbolList'].hasOwnProperty(key)) {
                        msgList[key + msg['symbolList'][key][0]['symbolId']] = msg;
                    }
                }
            }else if(msg['msgType']){
                msgList[msg['msgType']+msg['symbolId']] = msg;
            }
        }

        isconnect && conn.emit("request", msg);

    };
    socket.clearmsg = function() {
        msgList = {};
    };

    /**
     * 守护进程
     */
    var Daemon_callback;
    socket.Daemon = function (callback){
        Daemon_callback = callback;
    };
    function Daemon (){
        if(socket.LastTime){
            var _time = Math.round(new Date().getTime()),
                _out = _time - socket.LastTime;

            Daemon_callback && Daemon_callback(_out)
        }else{
            Daemon_callback && Daemon_callback(Number.MAX_VALUE)
        }
        setTimeout(Daemon, 1000)
    }

    /**
     * 移除消息
     */
    socket.UnMsg = function(msg){
    	
        var _unMsg = CP(msg);
        _unMsg.msgType = "reqMsgUnsubscribe";
        socket.msg(_unMsg,'unMsg');
    };

    /*拷贝对象*/
    function CP(a){
        return JSON.parse(JSON.stringify(a))
    }

    ! function() {
        conn = io.connect(ws, {
            "force new connection": 0,
            "sync disconnect on unload": 0,
            "reconnection delay": 1000,
            "reconnection delay max" : 5000,
            "max reconnection attempts": 999999
        });
        conn.emit("isWs", "222");
        conn.on('request', requestEvent);
        conn.on('message', callback);
        conn.on('connect', connectEvent);
        conn.on('error', function(){
            console.warn('error',arguments)
        });
        conn.on('reconnect', function(){
            isconnect = true;
            for(var _i in msgList ){
                conn.emit("request", msgList[_i]);
            }
        });
        conn.on('disconnect', disconnectEvent);
        if(!window['socketInstanced']){
            window['socketInstanced'] = {};
        }
        window['socketInstanced'][ws] = socket;
        socket.conn = conn;
         Daemon();
    }();
    HBCONN = socket;
    HBCONN.msgList = msgList;
    return socket;
}



"function" == typeof define ? define(function() {
    return connect
}) : "undefined" != typeof exports ? module.connect = connect : window.connect = connect;

