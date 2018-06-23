<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<p>认证成功！</p>
<%session.setAttribute("currUser",session.getAttribute("currUser")); %>
<a href="#" onclick="history.go(-1);">返回</a>
</body>
</html>