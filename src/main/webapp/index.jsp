<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
String username=request.getParameter("login");
String pw=request.getParameter("pw");
System.out.println("username="+username+",pw="+pw);

session.setAttribute("login",username);
session.setAttribute("pw",pw);
%>
 <head><meta http-equiv="Refresh" content="0;url=accueil.xhtml"/></head>
<title>Index</title>
</head>

<body>

</body>
</html>