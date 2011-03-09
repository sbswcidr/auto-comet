var ChatWindow = {
	extend : Un.Window,
	public : {
		reader : null,
		editor : null,
		sendButton : null,
		comet : null,
		userId : null,
		targetUserId : null,
		sender : null,
		receive : function(c) {
			var node = new Un.Element("div");
			node.setInnerHtml(c);
			this.reader.appendChild(node);
			this.reader.setScrollTop(this.reader.getScrollHeight());
		},
		send : function() {
			var text = this.editor.getValue();
			this.sender.send({
				param : {
					userId : this.userId,
					targetUserId : this.targetUserId,
					message : text
				},
				caller : this,
				success : function() {
					var data = eval("(" + result + ")");
					if (data.success == "true") {
						this.editor.clear();
					} else {
						alert("发送失败");
					}
				}
			});
		}
	},
	constructor : function() {
		this.reader = new Un.Component({});
		this.reader.addClass("chat_reader_layout");
		this.editor = new Un.Editor();
		this.sendButton = new Un.Button({
			className : "chat_btn_send"
		});
		this.sendButton.setInnerHtml("send");
		this.sendButton.addEventListener('click', this.send, this);

		this.addContent(this.reader);
		this.addContent(this.editor);
		this.addContent(this.sendButton);
		this.body.setStyle("backgroundColor", "#f7f7f5");
		this.body.setStyle("border", "none");
	}
};
ChatWindow = Un.newClass(ChatWindow);

var MenuWindow = {
	extend : Un.Window,
	public : {
		userId : null,
		users : null,
		sender : null,
		openChatWindow : function(targetUserId) {
			var chatWindow = new ChatWindow({
				title : targetUserId,
				resizable : false,
				borderWidth : 10,
				height : 360,
				width : 600,
				maximizable : false,
				userId : this.userId,
				sender : this.sender,
				// hasShadow : false,
				targetUserId : targetUserId
			});
			chatWindow.show();
			this.users[targetUserId].window = chatWindow;
		},
		addUser : function(userId) {
			var btn = new Un.Button({
				className : "menu_btn_user"
			});
			btn.setInnerHtml(userId);
			btn.addEventListener('click', function() {
				this.openChatWindow(userId);
			}, this);
			this.addContent(btn);
			this.users[userId] = {
				panel : btn
			};
		},
		removeUser : function(userId) {
			var obj = this.users[userId];
			if (!obj) {
				return;
			}
			var btn = obj.panel;
			if (btn) {
				this.users[userId] = null;
				this.body.removeChild(btn);
			}
		},
		receiveMessage : function(friend, text) {
			var obj = this.users[friend];
			if (!obj) {
				return;
			}
			var win = obj.window;
			if (win) {
				win.receive(text);
			}
		}
	},
	constructor : function() {
		this.users = {};
		this.manager.setLastWin(null);
	}
};

MenuWindow = Un.newClass(MenuWindow);