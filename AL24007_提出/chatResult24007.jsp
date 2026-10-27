<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>送信完了</title>
  <style>
    body { font-family: sans-serif; max-width: 500px; margin: 40px auto; }
    .msg { font-size: 18px; margin: 20px 0; }
    a { color: #0066cc; }
  </style>
</head>
<body>
  <h2>送信完了</h2>
  <p class="msg">メッセージ<strong><%= request.getAttribute("message") %></strong>を登録しました。</p>
  <a href="/WebApplicationTest/chat24007.html">戻る</a>
</body>
</html>
