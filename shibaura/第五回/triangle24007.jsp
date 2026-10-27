<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Triangle</title>
</head>
<body>
<%
    int n = Integer.parseInt(request.getParameter("NUM"));
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < i; j++) {
            out.print("*");
        }
        out.println("<br>");
    }
%>
</body>
</html>
