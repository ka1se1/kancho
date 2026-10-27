<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>おみくじシステム</title>
</head>
<body>
	<h1>おみくじシステム</h1>
	<br>
	<%-- -------- 入力フォーム -------- --%>
	<form action="Hantei" method="post">
		好きな数字を入力してください（半角）： <input type="text" name="num"> <br> <input
			type="submit" value="うらなう！">
	</form>

</body>
</html>