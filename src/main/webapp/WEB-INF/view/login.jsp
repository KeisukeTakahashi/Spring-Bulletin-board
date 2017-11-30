<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/login.css" />" rel="stylesheet">
<title>ログイン画面</title>
</head>
<body>
<div id="main-contents">

<p class="form-title">ログイン</p>

	<c:if test="${ not empty errorMessages }">
		<div class="errorMessages">
			<ul>
				<c:forEach items="${errorMessages}" var="message">
					<li><c:out value="${message}" />
				</c:forEach>
			</ul>
		</div>
		<c:remove var="errorMessages" scope="session" />
	</c:if>

	<form:form modelAttribute="loginForm" method="post">
		<p>ログインID</p>
		<form:input path="loginId" />
		<br>
		<p>パスワード</p>
		<form:input path="password" type="password" />
		<br>
		<div class="submit"><input type="submit"></div>
	</form:form>
	</div>
</body>
</html>