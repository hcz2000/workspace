<%@ page language="java"%>
<html lang="en">
  <head>
    <title>OAuth Test</title>
    <script src="jquery-1.10.2.min.js" ></script>
    <script>
    	$(document).ready(function(){
    		$("#goButton").click(makeRequest);
    	});
    	function makeRequest(){
    		var AUTH_ENDPOINT="http://localhost:8080/oauth/api/authorize";
    		var RESPONSE_TYPE="token";
    		var CLIENT_ID="<%=request.getParameter("client_id")%>";
    		var REDIRECT_URI="<%=request.getParameter("redirect_uri")%>";
    		var SCOPE="public_profile user_posts";
    		var STATE="mystate";
    		var requestEndpoint=AUTH_ENDPOINT+"?"+
    				"response_type="+encodeURIComponent(RESPONSE_TYPE)+"&"+
    				"client_id="+encodeURIComponent(CLIENT_ID)+"&"+
    				"redirect_uri="+encodeURIComponent(REDIRECT_URI)+"&"+
    				"scope="+encodeURIComponent(SCOPE)+"&"+
    				"state="+encodeURIComponent(STATE);
    		window.location.href=requestEndpoint;
    	}
    </script>
   </head>

  <body class="bg">
  		Hello <%=request.getUserPrincipal().getName() %>,OAuth2.0 Server will grant your access token to <%=request.getParameter("client_id")%> 
		<button id="goButton" type="button">Grant!</button>
		<div id="results"></div>
  </body>
  
</html>
