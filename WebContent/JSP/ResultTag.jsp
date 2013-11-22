<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tag Recommender</title>
</head>
<body>

	<table border=5 align="center" >

<%
try
{
	ArrayList<String> list = (ArrayList)request.getAttribute("tags");
	
	for(int i=0;i< list.size();i++)
	{
		%>
     <tr>
     <td align="center"><font color="black"><b><%=(i+1)%></b></font></td><td align="center"><font color="black"><b><%=list.get(i)%></b></font></td>
     </tr>
<%	}
}
catch(Exception e)
{
	e.printStackTrace();	
}
%>
</table>
</body>
</html>