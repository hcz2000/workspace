<%@ page language="java"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Hello</title>
  </head>

  <body class="bg">
		<%
			out.print("Hello Client"+request.getUserPrincipal().getName());
		%>
		
				
		<% java.util.Enumeration enu=request.getHeaderNames();//取得全部头信息
		   while(enu.hasMoreElements()){//以此取出头信息
			   	String headerName=(String)enu.nextElement();
		   		String headerValue=request.getHeader(headerName);//取出头信息内容 %>
		   		<h5><font color="red"><%=headerName%></font>
		   		    <font color="blue"><%=headerValue%></font></h5>
		<%}%>
		
		<% java.util.Enumeration params=request.getParameterNames();//取得全部头信息
		   while(params.hasMoreElements()){//以此取出头信息
			   	String parameterName=(String)params.nextElement();
		   		String parameterValue=request.getParameter(parameterName);//取出头信息内容 %>
		   		<h5><font color="blue"><%=parameterName%></font>
		   		    <font color="red"><%=parameterValue%></font></h5>
		<%}%>
  </body>
</html>
