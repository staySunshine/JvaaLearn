<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/9/16
  Time: 14:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>主页</h2>
<a href="account/findAll">查询所有</a>

<form action="account/add" method="post">
    姓名：<input type="text" name="name" /><br/>
    余额：<input type="text" name="balance" /><br/>
    <input type="submit" value="添加"/><br/>
</form>
</body>
</html>
