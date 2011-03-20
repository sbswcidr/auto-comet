Un.Comet = {
	extend : Un.Observable,
	static : {
		/** 请求参数名：链接ID */
		CONNECTIONID_KEY : "_C_COMET",
		/** 请求参数名：同步 */
		SYNCHRONIZE_KEY : "_S_COMET",
		/** 同步值：创建链接 */
		CONNECTION_VALUE : "C",
		/** 同步值：断开链接 */
		DISCONNECT_VALUE : "D"
	},
	public : {
		url : null,
		accept : Un.emptyFunciton,
		/** 开始链接 */
		connection : function(userParam) {
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
					this.polling(cid);
				}
			};
			Un.Ajax.request(req);
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
					for ( var i = 0, len = datas.length; i < len; i++) {
						var data = datas[i];
						if (data[Un.Comet.SYNCHRONIZE_KEY] == Un.Comet.DISCONNECT_VALUE) {
							// 断开链接,停止轮询
							return;
						}
						this.accept(data);
					}
					this.polling(cid);
				}
			};

			Un.Ajax.request(req);
		},
		handleData : function(d) {

		},
		/** 断开链接 */
		disconnect : function() {
			var param = {};
			param[Un.Comet.SYNCHRONIZE_KEY] = Un.Comet.DISCONNECT_VALUE;
			var req = {
				url : this.url,
				method : "GET",
				param : param,
				caller : this,
				success : function(result) {
					// TODO
				}
			};
			Un.Ajax.request(req);
		}
	},
	constructor : function() {
	}
};
Un.newClass(Un.Comet);