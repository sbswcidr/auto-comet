var UserList = {
	extend : Un.Element,
	public : {
		users : null,
		sender : null,
		setFirstUser : function(userId) {
			var obj = this.users[userId];
			if (!obj) {
				return;
			}
			var panel = obj.panel;
			if (panel) {
				Un.Element.insertBefore(panel, this.getChildren()[0]);
			}
		},
		addUser : function(userId) {
			if (this.users[userId]) {
				return;
			}
			var userPanel = new Un.Element({
				className : "menu_btn_user"
			});
			userPanel.setInnerHtml(userId);
			this.appendChild(userPanel);
			this.users[userId] = {
				panel : userPanel
			};
		},
		removeAllUser : function() {
			this.removeAllChild();
			this.users = {};
		},
		removeUser : function(userId) {
			var obj = this.users[userId];
			if (!obj) {
				return;
			}
			var btn = obj.panel;
			if (btn) {
				this.users[userId] = null;
				this.removeChild(btn);
			}
		}
	},
	constructor : function() {
		this.users = {};
	}
};

UserList = Un.newClass(UserList);

var ChatRoomWindow = {
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
		addUser : function(userId) {
			this.userList.addUser(userId);
		},
		removeUser : function(userId) {
			this.userList.removeUser(userId);
		},
		send : function() {
			var text = this.editor.getValue();
			var param = {
				userId : this.userId,
				message : text
			};
			this.sender.send({
				param : param,
				caller : this,
				success : function(result) {
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
		this.editorArea = new Un.Component({});
		this.editorArea.addClass("chat_editor_layout");
		this.editor = new Un.Editor();
		this.sendButton = new Un.Button({
			className : "chat_btn_send"
		});
		this.sendButton.setInnerHtml("send");
		this.sendButton.addEventListener('click', this.send, this);

		this.editorArea.appendChild(this.editor);
		this.editorArea.appendChild(this.sendButton);

		this.addContent(this.reader);
		this.addContent(this.editorArea);

		this.userList = new UserList({});
		this.userList.addClass("chat_users_layout");
		this.addContent(this.userList);

		this.body.setStyle("backgroundColor", "#f7f7f5");
		this.body.setStyle("border", "none");
	}
};
ChatRoomWindow = Un.newClass(ChatRoomWindow);
