<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet">
<title>ホーム</title>
</head>
<body>
	<div class="main-contents">
<div class="header">
     <c:choose>
        <c:when test="${ loginUser.departmentId == 1 }">
		<a href="message">新規投稿</a>
		<a href="control">管理画面</a>
		<a href="logout">ログアウト</a>
        </c:when>
		<c:otherwise>
		<a href="message">新規投稿</a>
		<a href="logout">ログアウト</a>
		</c:otherwise>
	 </c:choose>
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
	<c:remove var="errorMessages" scope="session"/>
</c:if><br />

<div class="user_info">
  <p>ログインユーザー情報</p>
  <p>名前&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${loginUser.name}"></c:out></p>

  <c:forEach items="${branches}" var="branch">
  <c:if test="${loginUser.branchId == branch.id}">
  <input type="hidden" value="${loginUser.branchId}" name="branchId"/>
  <p>支店&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${branch.name}" /></p>
  </c:if>
  </c:forEach>
  <c:forEach items="${departments}" var="department">
  <c:if test="${loginUser.departmentId == department.id}">
  <input type="hidden" value="${loginUser.departmentId}" name="departmentId"/>
  <p>部署・役職：<c:out value="${department.name}" /></p>
  </c:if>
  </c:forEach>
  </div><br />

		<div class="search_area">
 		 <form action="./" method="get">
 		 <p>カテゴリー(単語を空白で区切ると複数検索出来ます)</p>
  			<p><input type="text" name="category" value="${category}" list="categorys" autocomplete="off" id="lists.category"><p>
  			 <datalist id="categorys">
				<c:forEach items="${categorys}" var="lists">
						<option value="${lists.category}" selected>${lists.category}</option>
				</c:forEach>
			</datalist><br>
	     	<p>カレンダー</p>
	     	<p><input type="date" name="startDay" value="${calendarStart}" id="startDay"/> ～
	     	<input type="date" name="endDay" value="${calendarEnd}" id="endDay"/></p><br>
		 	<input type="submit" value="検索" id="submitSearch">
		 	</form><br>
	     	<form action="./" method="get">
		 	<input type="submit" value="リセット" id="submitSearch">
		 </form>
		</div>
		<br />

		<div class="messages">
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<div class="message-item">
					<p class="subject">[件名]<c:out value="${message.subject}"></c:out></p>
					<p class="category">カテゴリー：<c:out value="${message.category}"></c:out></p>
					<p class="name">投稿者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${message.name}"></c:out></p>
					<p class="date">投稿日時&nbsp;&nbsp;&nbsp;：<fmt:formatDate value="${message.createdDate}" pattern="yyyy年MM月dd日（E） HH時mm分ss秒" /></p>
					<p>
						<c:forEach var="messageText"
							items="${ fn:split(message.message,'
							') }">
							<c:out value="${messageText}" />
							<br>
						</c:forEach>
					</p>
						<c:if test="${ loginUser.id == message.userId && loginUser.departmentId != 2 && loginUser.departmentId != 3 }">
							<form action="DeleteMessage" method="post" class="delete-action">
								<input type="hidden" value="${message.id}" name="id" class="id"/>
				 				<input type="submit" value="削除" id="submit_button1"  onClick="return confirm('この投稿を削除しますか？')"/>
				 			</form>
				 		</c:if>
				 		<c:if test="${ loginUser.departmentId == 2 || loginUser.departmentId == 3 && message.branchId == loginUser.branchId }">
							<form action="DeleteMessage" method="post" class="delete-action">
								<input type="hidden" value="${message.id}" name="id" class="id"/>
				 				<input type="submit" value="削除" id="submit_button1"  onClick="return confirm('この投稿を削除しますか？')"/>
				 			</form>
				 		</c:if>
				 		</div>

					<div class="comments" id="comment-list">
						<c:forEach items="${comments}" var="comment" varStatus="status">
							<c:if test="${message.id == comment.messageId }">
								<div class="comment" id="comment">
									<p class="comment_name">投稿者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${comment.name}" /></p>
									<p class="comment_date">投稿日時&nbsp;&nbsp;&nbsp;：<fmt:formatDate value="${comment.createdDate}" pattern="yyyy年MM月dd日（E） HH時mm分ss秒" /></p>
									<p class="comment_text"><c:forEach var="commentText"
											items="${ fn:split(comment.comment,'
											') }">
											<c:out value="${commentText}" />
											<br>
										</c:forEach>
									</p>
										<c:if test="${ loginUser.id == comment.userId && loginUser.departmentId != 2 && loginUser.departmentId != 3 }">
											<form action="DeleteComment" method="post" class="delete-action">
												<input type="hidden" value="${comment.id}" name="id" class="id"/>
				 								<input type="submit" value="削除" id="submit_button1"  onClick="return confirm('このコメントを削除しますか？')"/>
				 							</form>
				 						</c:if>
				 						<c:if test="${ loginUser.departmentId == 2 || loginUser.departmentId == 3 && comment.branchId == loginUser.branchId }">
											<form action="DeleteComment" method="post" class="delete-action">
												<input type="hidden" value="${comment.id}" name="id" class="id"/>
				 								<input type="submit" value="削除" id="submit_button1"  onClick="return confirm('このコメントを削除しますか？')"/>
				 							</form>
				 						</c:if>
								</div>
								<br>
							</c:if>
						</c:forEach>
					</div>

					<div class="form-data">
						<form:form modelAttribute="newCommentForm" action="insertComment" method="post">
							<span class="box-title">コメント(500文字以内)</span>
							<br>
							<form:textarea path="comment" rows="5" cols="95"/>
							<br>
							<input type="hidden" value="${message.id}" name="messageId">
							<input type="submit" id="submit_button2" value="コメント投稿">
						</form:form>
					</div>
				</div>
				<br>
			</c:forEach>
		</div>
	</div>
</body>
</html>