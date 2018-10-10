
function SOCKET_API(version) {
	return {
		reqKLine: function(t, n, e, o) {
			return {
				msg: {
					version: version || 1,
					msgType: "reqKLine",
					website: HRY.ver,symbol : "TOK_ETH",
					requestIndex: Date.now(),
					symbolId: t,
					from: e || 0,
					to: o || -1,
					period: n
				},
				msgType: "reqKLine"
			}
		},
		lastKLine: function(t, n) {
			return {
				msg: {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						lastKLine: [{
							symbolId: t,
							pushType: "pushLong",
							period: [n]
						}]
					}
				},
				unmsg: {
					msgType: "reqMsgUnsubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						lastKLine: [{
							symbolId: t,
							pushType: "pushLong",
							period: [n]
						}]
					}
				},
				msgType: "lastKLine"
			}
		},
		reqMarketDetail: function(symbol) {
			return {
				"msg": {
					version: version || 1,
					msgType: "reqMarketDetail",
					website: HRY.ver,symbol : "TOK_ETH",
					requestIndex: Date.now(),
					symbolId: symbol
				},
				"msgType": "reqMarketDetail"
			}
		},
		reqMarketDepthTop: function(symbol) {
			return {
				"msg": {
					version: version || 1,
					msgType: "reqMarketDepthTop",
					website: HRY.ver,symbol : "TOK_ETH",
					requestIndex: Date.now(),
					symbolId: symbol
				},
				"msgType": "reqMarketDepthTop"
			}
		},
        marketDepth: function(symbol, percent) {
            return {
                "msg": {
                    version: version || 1,
                    msgType: "reqMsgSubscribe",
                    website: HRY.ver,symbol : "TOK_ETH",
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDepth": [{
                            symbolId: symbol,
                            percent:percent || 10,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDepth"
            }
        },
        unMarketDepth: function(symbol, percent) {
            return {
                "msg": {
                    version: version || 1,
                    msgType: "reqMsgUnsubscribe",
                    website: HRY.ver,symbol : "TOK_ETH",
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDepth": [{
                            symbolId: symbol,
                            percent:percent || 10,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "UnMarketDepth"
            }
        },
		marketDepthTopShort: function(symbol) {
			return {
				"msg": {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"marketDepthTopShort": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"msgType": "marketDepthTopShort"
			}
		},
		marketDepthTop: function(symbol) {
			return {
				"msg": {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"marketDepthTop": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"msgType": "marketDepthTop"
			}
		},
        marketDetail: function(symbol) {
            return {
                "msg": {
                    msgType: "reqMsgSubscribe",
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail"
            }
        },
        marketDetail0: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail0": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail0": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail0"
            };
        },
        marketDetail1: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail1": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail1": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail1"
            };
        },
        marketDetail2: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail2": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail2": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail2"
            };
        },
        marketDetail3: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail3": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail3": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail3"
            };
        },
        marketDetail4: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail4": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail4": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail4"
            };
        },
        marketDetail5: function(symbol) {
            return {
                "msg": {
                    msgType: 'reqMsgSubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail5": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "unmsg": {
                    msgType: 'reqMsgUnsubscribe',
                    website: HRY.ver,symbol : "TOK_ETH",
                    version: version || 1,
                    requestIndex: Date.now(),
                    symbolList: {
                        "marketDetail5": [{
                            symbolId: symbol,
                            pushType: "pushLong"
                        }]
                    }
                },
                "msgType": "marketDetail5"
            };
        },
		marketDepthTopDiff: function(symbol) {
			return {
				"msg": {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"marketDepthTopDiff": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"unmsg": {
					msgType: "reqMsgUnsubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"marketDepthTopDiff": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"msgType": "marketDepthTopDiff"
			}
		},
		tradeDetail: function(symbol) {
			return {
				"msg": {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"tradeDetail": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"msgType": "tradeDetail"
			}
		},
		marketOverview: function(symbol) {
			return {
				"msg": {
					msgType: "reqMsgSubscribe",
					website: HRY.ver,symbol : "TOK_ETH",
					version: version || 1,
					requestIndex: Date.now(),
					symbolList: {
						"marketOverview": [{
							symbolId: symbol,
							pushType: "pushLong"
						}]
					}
				},
				"msgType": "marketOverview"
			}
		},
		reqTradeDetailTop: function(symbol, cun) {
			return {
				"msg": {
					version: version || 1,
					msgType: "reqTradeDetailTop",
					website: HRY.ver,symbol : "TOK_ETH",
					requestIndex: Date.now(),
					symbolId: symbol,
					count: cun || 150
				},
				"msgType": "reqTradeDetailTop"
			}
		}
	}
};

"function" == typeof define ? define(function() {
    return SOCKET_API
}) : "undefined" != typeof exports ? module.SOCKET_API = SOCKET_API : window.SOCKET_API = SOCKET_API;
