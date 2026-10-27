<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>練習２解答</title>
</head>
<body>
	<!-- for分の開始処理 -->
	<%
		for (int i = 1; i <= 20; i++) {
	%>

	<!-- 変数の表示処理 -->
	<%=i%>
	<!-- for分の閉じカッコ -->
	<%
		}
	%>


</body>
</html>