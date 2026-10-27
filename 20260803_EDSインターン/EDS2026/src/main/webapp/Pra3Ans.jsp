<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>練習３解答</title>
</head>
<body>
	<!-- for分の開始処理 -->
	<%
		for (int i = 1; i <= 20; i++) {
	%>

	<!-- 変数の表示処理 -->
	<%=i%>
	<!-- if文で3,5,3と5の最小公倍数で割り切れるかを判定する処理 -->
	<%
		if (i % 15 == 0) {
	%>
	:FizzBuss
	<%
		} else if (i % 5 == 0) {
	%>
	:Buss
	<%
		} else if (i % 3 == 0) {
	%>
	:Fizz
	<%
		}
	%>
	<br>

	<!-- for分の閉じカッコ -->
	<%
		}
	%>


</body>
</html>