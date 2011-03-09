<%@ page language="java" errorPage="/common/errorPage/500.jsp"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"
	isELIgnored="false"%>
<%
	out.println(session.getId());
	Cookie[] cookies = request.getCookies();
	//for (Cookie c : cookies) {
	//	out.println(c.getName()+":"+c.getValue());
	//}
	//Cookie c1=new Cookie("haha","hehe");
	//c1.setMaxAge(-1);
	//c1.setSecure(true);

	//response.addCookie(c1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function testCookie() {
		alert(document.cookie);
	}
</script>
</head>
<body>
<button onclick="testCookie();">test</button>
</body>
</html>