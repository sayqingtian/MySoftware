<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎界面</title>
</head>
<body>
<h1>欢迎你！<%=session.getAttribute("currUser").toString()%></h1>
<a href="verify">认证</a>
<a href="download">下载资源</a>
<a href="Login">退出登录</a>
</body>
</html>