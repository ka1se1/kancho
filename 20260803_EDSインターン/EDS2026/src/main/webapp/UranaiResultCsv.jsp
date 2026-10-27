<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>診断結果</title>
</head>
<body>
	<h1>★診断結果★</h1>

	<%-- -------- リクエストから各値を取得 -------- --%>
	<%
		String q1result = (String) request.getAttribute("q1result");
		String q2result = (String) request.getAttribute("q2result");
		String ageresult = (String) request.getAttribute("ageresult");
		String heightresult = (String) request.getAttribute("heightresult");
		String birthresult = (String) request.getAttribute("birthresult");
		
		String q1 = request.getParameter("q1");
		String q2 = request.getParameter("q2");
		String age = request.getParameter("age");
		String height = request.getParameter("height");
		String birth = request.getParameter("birth");
	%>
	<%-- -------- 取得した各値を使って、占い結果の表示 -------- --%>
	<%--<br> あなたが入力した値は 「<%=age%>」です。--%>
	<br> あなたは「<%=q1result%>」タイプです。
	<br> 性格は「<%=q2result%>」です。
	<br> 性格は「<%=ageresult%>」です。
	<br> 「<%=heightresult%>」が好きです。
	<br> あなたの運勢は「<%=birthresult%>」です。
	<a href="<%=request.getContextPath()%>/UranaiInputCsv.jsp">戻る</a>

</body>
</html>