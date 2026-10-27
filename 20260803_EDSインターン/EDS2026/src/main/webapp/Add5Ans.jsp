<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>追加練習5_ans</title>
</head>
<body>
	<!-- 変数宣言 -->
	<%
	int price = 1500;
	boolean result;
	%>
	<!-- 改行コード -->
	<br>
	<!-- result 設定 -->
	<%
	result = (1000 <= price) && (price <= 2000);
	%>
	<!-- 結果表示 -->
	<%
	out.println("結果：" + result);
	%>
</body>
</html>