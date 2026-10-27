<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>うらないシステム</title>
</head>
<body>
	<h1>うらないシステム</h1>
	<br>
	<%-- -------- 入力フォーム -------- --%>
	<form action="HanteiCsv" method="post">
	該当する項目にチェックをつけてください。<br>
	1.定期的に新しい友人を作ることを心掛けていますね。
	<input type="radio" name="q1" value="1" checked>はい
	<input type="radio" name="q1" value="2">そこそこ
	<input type="radio" name="q1" value="3">あんまり
	<input type="radio" name="q1" value="4">いいえ<br>
	2.単純明快なアイデアよりも、複雑で斬新なアイデアのほうがワクワクする。
	<input type="radio" name="q2" value="1" checked>はい
	<input type="radio" name="q2" value="2">そこそこ
	<input type="radio" name="q2" value="3">あんまり
	<input type="radio" name="q2" value="4">いいえ<br>
		年齢を入力してください。（半角）： <input type="text" name="age"><br>
		好きな数字(1-10)を入力してください。： <input type="text" name="height"><br>
		誕生月を入力してください（半角）： <input type="text" name="birth"><br> <input
			type="submit" value="うらなう！">
	</form>
	
</body>
</html>