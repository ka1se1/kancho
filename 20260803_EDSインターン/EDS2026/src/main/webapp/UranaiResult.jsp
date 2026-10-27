<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>おみくじ結果</title>
</head>
<body>
	<h1>★おみくじ結果★</h1>

	<%-- -------- リクエストから各値を取得 -------- --%>
	<%
		String result = (String) request.getAttribute("result");
		String num = request.getParameter("num");
	%>
	<%-- -------- 取得した各値を使って、占い結果の表示 -------- --%>
	<br> あなたが入力した値は 「<%=num%>」です。
	<br> あなたの運勢は「<%=result%>」です。
	<a href="<%=request.getContextPath()%>/UranaiInput.jsp">戻る</a>

</body>
</html>