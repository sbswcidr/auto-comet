<%@ page language="java" errorPage="/common/errorPage/500.jsp"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"
	isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%--@ taglib uri="http://www.springmodules.org/tags/commons-validator" prefix="v" --%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%--@ taglib uri="http://displaytag.sf.net" prefix="display" --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%--@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"--%>
<%--@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"--%>

<c:set var="datePattern">
	<fmt:message key="date.format"></fmt:message>
</c:set>
