/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */

/** root */
var UN_URL = "../../js/unJs/";

/** files */
var SCRIPTS = [ "un_core.js", "un_tabTree.js" ];
var STYLES = [ "css/reset.css", "css/un_tabTree.css" ];

/** $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */

if (typeof Un == "undefined")
	Un = {};

Un.load = function() {
	this.load.addScripts(this.load.files.scripts);
	this.load.addStyles(this.load.files.styles);
};

Un.load.files = {
	scripts : SCRIPTS,
	styles : STYLES
};

Un.load.addScript = function(src, tag) {
	var head = document.getElementsByTagName("head")[0];
	var script = tag.appendChild(document.createElement("script"));
	script.src = src;

};

Un.load.addStyle = function(src, tag) {
	var head = document.getElementsByTagName("head")[0];
	var link = tag.appendChild(document.createElement("link"));
	link.href = src;
	link.rel = "stylesheet";
	link.type = "text/css";
};

Un.load.addScripts = function(scripts) {
	for ( var i in scripts) {
		this.addScript(UN_URL + scripts[i], this.tag);
	}
};

Un.load.addStyles = function(styles) {
	for ( var i in styles) {
		this.addStyle(UN_URL + styles[i], this.tag);
	}
};

Un.load.tag = document.getElementsByTagName("head")[0];
Un.load();
