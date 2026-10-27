<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>練習１解答</title>
</head>
<body>
	<!-- a,bの画面表示 -->
	a = 5 b = 12
	<!-- 改行コード -->
	<br>
	<!-- 任意の数a,bの定義 -->
	<%
		int a = 5;
		int b = 12;
	%>

	<!-- 画面表示と計算 -->
	足し算：<%=a + b%>
	<br> 引き算：<%=a - b%>
	<br> 掛け算：<%=a * b%>
	<br> 割り算：<%=a / b%>
	<br>

</body>
</html>