<%@page import="java.util.List"%>
<%@page import="com.jams.myapp.entity.Message"%>
<%@page import="com.jams.myapp.dao.MessageDao"%>
<%@page import="com.jams.myapp.util.SqlSessionFactoryUtil"%>
<%@ page language="java" pageEncoding="gb2312"%>
<!--  %@ page import="org.sankhya.weixin.qy.pojo.UserInfo;"%-->
<html>
<head>
	<title>OAuth2.0回调企业应用</title>
	<meta name="viewport" content="width=device-width,user-scalable=0">
	<style type="text/css">
		*{margin:0; padding:0}
		table{border:1px dashed #B9B9DD;font-size:12pt}
		td{border:1px dashed #B9B9DD;word-break:break-all; word-wrap:break-word;}
	</style>
</head>
<body>
	<% 
		// 获取由OAuthServlet中传入的参数
		//org.jams.weixin.qy.pojo.UserInfo user = (org.jams.weixin.qy.pojo.UserInfo)request.getAttribute("userInfo");
		com.jams.weixin.qy.pojo.UserInfo user = (com.jams.weixin.qy.pojo.UserInfo)request.getSession().getAttribute("USER");
		if(null != user) {
	    	  MessageDao messageDao = SqlSessionFactoryUtil.openSession().getMapper(MessageDao.class);  
	          List<Message> messageList = messageDao.findMyMessage(user.getUserId());
	%>
	<h3>用户<%=user.getUserId()%>所收到的信息</h3>
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr>
			<td>时间</td>
			<td>发送人</td>
			<td>信息</td>
			</tr>
		<%
		for(int i=0;i<messageList.size();i++){
			Message message = messageList.get(i);
		%>
		<tr>
			<td><%=message.getCreateTime()%></td>
			<td><%=message.getSender() %></td>
			<td><%=message.getMessage() %></td>
		</tr> 
		<%
 		}
		%>
	</table>
	<%
		}
		else 
			out.print("未获取到用户信息！");
	%>
</body>
</html>