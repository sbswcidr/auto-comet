/**
 * @param url[String]
 *            连接地址
 * @param accept[Function]
 *            接受数据处理方法
 * @param o.async[boolean]异步接受数据？默认false
 */
Un.Comet = {
	extend : Un.Observable,
	static : {
		/** 请求参数名：同步 */
		SYNCHRONIZE_KEY : "_S_COMET",
		/** 同步值：创建连接 */
		CONNECTION_VALUE : "C",
		/** 同步值：断开连接 */
		DISCONNECT_VALUE : "D",
		/** 返回参数名：连接ID */
		CONNECTIONID_KEY : "_C_COMET"
	},
	public : {
		url : null,
		cid : null,
		accept : Un.emptyFunciton,
		async : false,
		/**
		 * 开始链接
		 *
		 * @param userParam
		 * @param success
		 *            连接成功处理方法
		 * @param failure
		 *            连接失败处理方法
		 *
		 */
		connection : function(userParam, success, failure, caller) {
			if (!userParam) {
				userParam = {};
			}
			userParam[Un.Comet.SYNCHRONIZE_KEY] = Un.Comet.CONNECTION_VALUE;
			var req = {
				url : this.url,
				param : userParam,
				caller : this,
				success : function(result) {
					var data = eval("(" + result + ")");
					cid = data[Un.Comet.CONNECTIONID_KEY];
					caller = caller ? caller : null;
					if (null == cid) {// 拒接连接
						if (failure) {
							failure.call(caller);
						}
					} else {// 连接成功
						if (success) {
							success.call(caller);
						}
						this.cid = cid;
						this.polling(cid);
					}
				}
			};
			Un.AjaxUtils.request(req);
		},
		isDisconnectObj : function(o) {
			return o[Un.Comet.SYNCHRONIZE_KEY] == Un.Comet.DISCONNECT_VALUE;
		},
		/**
		 * 处理数组中指定长度的数据
		 */
		acceptDatasByLength : function(datas, len, disconnect) {
			for ( var i = 0; i < len; i++) {
				var data = datas[i];
				this.accept(data);
			}
		},
		acceptDatas : function(datas) {
			// 接受的最后一个消息
			var lastData = datas[datas.length - 1];
			// 如果是断开连接
			var disconnect = this.isDisconnectObj(lastData);
			var len = datas.length;
			if (disconnect) {
				len--;
			}
			if (!disconnect) {// 如果不是断开连接，继续轮询
				if (this.async) {// 如果是异步处理
					this.polling(cid);
					this.acceptDatasByLength(datas, len);
				} else {
					this.acceptDatasByLength(datas, len);
					this.polling(cid);
				}
			} else {
				this.acceptDatasByLength(datas, len);
			}
		},
		/** 轮询 */
		polling : function(cid) {
			var param = {};
			param[Un.Comet.CONNECTIONID_KEY] = cid;
			var req = {
				url : this.url,
				method : "GET",
				param : param,
				caller : this,
				success : function(result) {
					var datas = eval("(" + result + ")");
					this.acceptDatas(datas);
				}
			};
			Un.AjaxUtils.request(req);
		},
		/** 断开连接 */
		disconnect : function(userParam, callback, caller) {
			var param = userParam ? userParam : {};
			param[Un.Comet.SYNCHRONIZE_KEY] = Un.Comet.DISCONNECT_VALUE;
			param[Un.Comet.CONNECTIONID_KEY] = this.cid;
			var req = {
				url : this.url,
				method : "GET",
				param : param,
				caller : this,
				success : function(result) {
					caller = caller ? caller : null;
					if (callback) {
						callback.call(caller);
					}
				}
			};
			Un.AjaxUtils.request(req);
		}
	},
	constructor : function() {
	}
};
Un.Comet = Un.newClass(Un.Comet);