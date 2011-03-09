<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Insert title here</title>
<script src="<c:url value='/script/unJs/un_core.js'/>"
	type="text/javascript"></script>
<link href="<c:url value='/style/css/index_layout.css'/>"
	type="text/css" rel="stylesheet" />
<script type="text/javascript">
Un.ready(function(){


var testJsonBtn=Un.Element.get("testJson");
var send=function(){
	alert(3);
	Un.Ajax.request({
        url:'example.do?method=sayHelloJson&id=5',
        success:function(t){
        	var data = eval("(" + t + ")");
            alert(data.hello);
        },
        failure:function(t){
            alert(t);
        }
    });
};
testJsonBtn.addListener('click',send);

});
</script>
</head>
<body>
<c:out value="${hello}"></c:out>

<c:out value="${id}"></c:out>
<button id="testJson">testJson</button>
</body>
</html>
