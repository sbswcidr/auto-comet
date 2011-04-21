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
		send : function() {
			var text = this.editor.getValue();
			this.sender.send({
				param : {
					userId : this.userId,
					targetUserId : this.targetUserId,
					message : text
				},
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

		this.userArea = new Un.Component({});
		this.userArea.addClass("chat_users_layout");
		this.addContent(this.userArea);

		this.body.setStyle("backgroundColor", "#f7f7f5");
		this.body.setStyle("border", "none");
	}
};
ChatRoomWindow = Un.newClass(ChatRoomWindow);
