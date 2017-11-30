<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/control.css" />" rel="stylesheet">
<title>管理画面</title>
</head>
<body>
	<div class="main-contents">
		<a href="./">ホーム</a> <a href="signup">ユーザー新規登録</a>
	</div>
	<br />
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
	<br />

	<div class="lists">
		<table border="1">
			<tr>
				<th>名前</th>
				<th>支店名</th>
				<th>部署・役職</th>
				<th>状態</th>
				<th>編集</th>
			</tr>

			<tbody>
				<c:forEach items="${users}" var="user">
					<tr>
						<td><c:out value="${user.name}"></c:out>
							<c:forEach items="${branches}" var="branch">
								<c:if test="${user.branchId == branch.id}">
									<td><c:out value="${branch.name}"></c:out>
								</c:if>
							</c:forEach>
							<c:forEach items="${departments}" var="department">
								<c:if test="${user.departmentId == department.id}">
									<td><c:out value="${department.name}"></c:out>
								</c:if>
							</c:forEach>
						<td>
						<c:choose>
				 			 <c:when test="${ user.id != loginUser.id }">
							 	<form action="control" method="post">
									<c:choose>
										<c:when test="${ user.isWorking == 1 }">
											<input type="hidden" value="${user.id}" name="id"/>
											<input type="hidden" value="0" name="isWorking"/>
											<input type="submit" value="停止" id="submit_button1" onClick="return confirm('このユーザーを停止しますか？')"/>
										</c:when>
										<c:otherwise>
											<input type="hidden" value="${user.id}" name="id"/>
											<input type="hidden" value="1" name="isWorking"/>
											<input type="submit" value="復活" id="submit_button2" onClick="return confirm('このユーザーを復活しますか？')"/>
										</c:otherwise>
									</c:choose>
								</form>
				  			</c:when>
				 			 <c:otherwise>
								  ログイン中
				 			 </c:otherwise>
						 </c:choose>
						</td>
						<td><a href="settings?id=${user.id}">編集</a>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>