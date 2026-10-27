<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>最新チャットメッセージ20件</title>
  <style>
    body { font-family: sans-serif; margin: 20px auto; max-width: 900px; }
    h2 { text-align: center; }
    table {
      border-collapse: collapse;
      width: 100%;
      margin: 0 auto;
    }
    th, td {
      border: 1px solid #aaa;
      padding: 6px 10px;
    }
    th {
      background-color: #dde;
      text-align: center;
    }
    td.datetime { text-align: center; }
    td.my-msg { color: #005500; }
    a { color: #0066cc; }
  </style>
</head>
<body>
<%
  @SuppressWarnings("unchecked")
  ArrayList<String[]> messages = (ArrayList<String[]>) request.getAttribute("messages");
  String today = (String) request.getAttribute("today");
%>
<h2>最新チャットメッセージ20件</h2>
<table>
  <tr>
    <th>発信者</th>
    <th>友達メッセージ</th>
    <th>更新日/時刻</th>
    <th>私のメッセージ</th>
  </tr>
<%
  if (messages != null) {
    for (String[] row : messages) {
      String id          = row[0];
      String name        = row[1];
      String msg         = row[2];
      String writtendate = row[3]; // "yyyy-MM-dd HH:mm:ss.n"

      // Date formatting
      String dateStr = writtendate.substring(0, 10); // "yyyy-MM-dd"
      String timeDisplay;
      if (dateStr.equals(today)) {
        timeDisplay = writtendate.substring(11, 16); // "HH:mm"
      } else {
        timeDisplay = writtendate.substring(5, 7) + "/" + writtendate.substring(8, 10); // "MM/DD"
      }

      boolean isMe = "AL24007".equals(id);
%>
  <tr>
    <td><%= isMe ? "" : (name != null ? name : "") %></td>
    <td><%= isMe ? "" : (msg  != null ? msg  : "") %></td>
    <td class="datetime"><%= timeDisplay %></td>
    <td class="my-msg"><%= isMe ? (msg != null ? msg : "") : "" %></td>
  </tr>
<%
    }
  }
%>
</table>
<br>
<a href="/WebApplicationTest/chat24007.html">戻る</a>
</body>
</html>
