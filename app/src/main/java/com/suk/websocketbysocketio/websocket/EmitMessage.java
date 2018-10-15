package com.suk.websocketbysocketio.websocket;

import java.util.List;

/**
 * Created by Felix.Zhong on 2018/10/10 15:40
 * 向服务器发射的消息实体
 */
public class EmitMessage {

    /**
     * msgType : reqMsgSubscribe
     * website : 20180502cn
     * symbol : TOK_ETH
     * version : 1
     * requestIndex : 456345645
     * symbolList : {"marketDetail0":[{"symbolId":"TOK_ETH","pushType":"pushLong"}]}
     */

    private String msgType;
    private String website;
    private String symbol;
    private int version;
    private long requestIndex;
    private SymbolListBean symbolList;

    @Override
    public String toString() {
        return "EmitMessage{" +
                "msgType='" + msgType + '\'' +
                ", website='" + website + '\'' +
                ", symbol='" + symbol + '\'' +
                ", version=" + version +
                ", requestIndex=" + requestIndex +
                ", symbolList=" + symbolList +
                '}';
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getRequestIndex() {
        return requestIndex;
    }

    public void setRequestIndex(long requestIndex) {
        this.requestIndex = requestIndex;
    }

    public SymbolListBean getSymbolList() {
        return symbolList;
    }

    public void setSymbolList(SymbolListBean symbolList) {
        this.symbolList = symbolList;
    }

    public static class SymbolListBean {
        private List<MarketDetail0Bean> marketDetail0;

        @Override
        public String toString() {
            return "SymbolListBean{" +
                    "marketDetail0=" + marketDetail0 +
                    '}';
        }

        public List<MarketDetail0Bean> getMarketDetail0() {
            return marketDetail0;
        }

        public void setMarketDetail0(List<MarketDetail0Bean> marketDetail0) {
            this.marketDetail0 = marketDetail0;
        }

        public static class MarketDetail0Bean {
            /**
             * symbolId : TOK_ETH
             * pushType : pushLong
             */

            private String symbolId;
            private String pushType;

            public MarketDetail0Bean(String symbolId, String pushType) {
                this.symbolId = symbolId;
                this.pushType = pushType;
            }

            @Override
            public String toString() {
                return "MarketDetail0Bean{" +
                        "symbolId='" + symbolId + '\'' +
                        ", pushType='" + pushType + '\'' +
                        '}';
            }

            public String getSymbolId() {
                return symbolId;
            }

            public void setSymbolId(String symbolId) {
                this.symbolId = symbolId;
            }

            public String getPushType() {
                return pushType;
            }

            public void setPushType(String pushType) {
                this.pushType = pushType;
            }
        }
    }
}
