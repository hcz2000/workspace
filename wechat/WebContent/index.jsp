<%@page import="java.util.List"%>
<%@page import="com.jams.myapp.entity.Message"%>
<%@page import="com.jams.myapp.dao.MessageDao"%>
<%@page import="com.jams.myapp.util.SqlSessionFactoryUtil"%>
<%@ page language="java" pageEncoding="gb2312"%>
<!--  %@ page import="org.sankhya.weixin.qy.pojo.UserInfo;"%-->
<html>
<head>
	<title>OAuth2.0�ص���ҵӦ��</title>
	<meta name="viewport" content="width=device-width,user-scalable=0">
	<style type="text/css">
		*{margin:0; padding:0}
		table{border:1px dashed #B9B9DD;font-size:12pt}
		td{border:1px dashed #B9B9DD;word-break:break-all; word-wrap:break-word;}
	</style>
</head>
<body>
	<% 
		// ��ȡ��OAuthServlet�д���Ĳ���
		//org.jams.weixin.qy.pojo.UserInfo user = (org.jams.weixin.qy.pojo.UserInfo)request.getAttribute("userInfo");
		com.jams.weixin.qy.pojo.UserInfo user = (com.jams.weixin.qy.pojo.UserInfo)request.getSession().getAttribute("USER");
		if(null != user) {
	    	  MessageDao messageDao = SqlSessionFactoryUtil.openSession().getMapper(MessageDao.class);  
	          List<Message> messageList = messageDao.findMyMessage(user.getUserId());
	%>
	<h3>�û�<%=user.getUserId()%>���յ�����Ϣ</h3>
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr>
			<td>ʱ��</td>
			<td>������</td>
			<td>��Ϣ</td>
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
			out.print("δ��ȡ���û���Ϣ��");
	%>
</body>
</html>