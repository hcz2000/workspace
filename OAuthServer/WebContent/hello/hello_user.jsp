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
			out.print("Hello User"+request.getUserPrincipal().getName());
		%>
		
		<h2>--------- Header -------------</h2>
		<% java.util.Enumeration enu=request.getHeaderNames();//取得全部头信息
		   while(enu.hasMoreElements()){//以此取出头信息
			   	String headerName=(String)enu.nextElement();
		   		String headerValue=request.getHeader(headerName);//取出头信息内容 %>
		   		<h5><font color="red"><%=headerName%></font>
		   		    <font color="blue"><%=headerValue%></font></h5>
		<%}%>
		
		<h2>--------- Parameter -------------</h2>
		<% java.util.Enumeration params=request.getParameterNames();//取得全部头信息
		   while(params.hasMoreElements()){//以此取出头信息
			   	String parameterName=(String)params.nextElement();
		   		String parameterValue=request.getParameter(parameterName);//取出头信息内容 %>
		   		<h5><font color="blue"><%=parameterName%></font>
		   		    <font color="red"><%=parameterValue%></font></h5>
		<%}%>
	
		<h2>--------- Session -------------</h2>	
		<% java.util.Enumeration attrs=request.getSession().getAttributeNames();
		   while(attrs.hasMoreElements()){//以此取出头信息
			   	String attrName=(String)attrs.nextElement();
		   		String attrValue=request.getSession().getAttribute(attrName).toString();%>
		   		<h5><font color="blue"><%=attrName%></font>
		   		    <font color="red"><%=attrValue%></font></h5>
		<%}%>

		<h2>--------- Cookie -------------	</h2>	
		 <% Cookie[] cookies = request.getCookies();  
 		    if(cookies != null)
                for(int i=0; i<cookies.length; i++) {  
                	Cookie cookie = cookies[i];  
           			String name = cookie.getName();  
            		String value = cookie.getValue();%>
            		<h5><font color="blue"><%=name%></font>
		   		    <font color="red"><%=value%></font></h5>
    	<%}%> 
  </body>
</html>
