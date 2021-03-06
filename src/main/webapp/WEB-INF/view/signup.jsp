<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/signup.css" />" rel="stylesheet">
<title>新規ユーザー登録</title>
</head>
<body>
	<div class="header">
		<a href="./">ホーム</a>
		<a href="control">管理画面</a><br />
	</div>

	<div class="main-contents">
		<p class="form-title">新規ユーザー登録</p>

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

		<div class="form-data">
			<form:form modelAttribute="signUpForm" method="post">
		<p>名前(1～10文字以内)</p>
		<form:input path="name" />
				<br><br>
		<p>ログインID(半角英数字で6～20文字以内)</p>
		<form:input path="loginId" />
				<br><br>
		<p>パスワード(半角文字で6～20文字以内)</p>
		<form:input path="password" type="password" />
				<br><br>
		<p>確認用パスワード(半角文字で6～20文字以内)</p>
		<form:input path="password2" type="password" />
				<br><br>
		<p>支店名</p>
		<form:select path="branchId">
					<c:forEach items="${branches}" var="branch">
						<c:if test="${branchId == branch.id}">
							<option value="${branch.id}" selected>${branch.name}</option>
						</c:if>
						<c:if test="${branchId != branch.id}">
							<option value="${branch.id}">${branch.name}</option>
						</c:if>
					</c:forEach>
				</form:select>
				<br><br>
		<p>部署・役職</p>
		<form:select path="departmentId">
					<c:forEach items="${departments}" var="department">
						<c:if test="${departmentId == department.id}">
							<option value="${department.id}" selected>${department.name}</option>
						</c:if>
						<c:if test="${departmentId != department.id}">
							<option value="${department.id}">${department.name}</option>
						</c:if>
					</c:forEach>
				</form:select>
				<br><br><br>
				<div class="submit"><input type="submit" value="登録"></div>
			</form:form>
		</div>
	</div>
</body>
</html>