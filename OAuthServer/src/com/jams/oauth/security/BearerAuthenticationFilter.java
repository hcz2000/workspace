package com.jams.oauth.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jams.oauth.bean.JWToken;
import com.jams.oauth.util.JWTUtil;

public class BearerAuthenticationFilter implements javax.servlet.Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		   
	        HttpServletRequest httpRequest = (HttpServletRequest)request;  
	        String authHeader = httpRequest.getHeader("Authorization");  
	        if ((authHeader != null) && (authHeader.length() > 7))  
	        {  
	            String HeadStr = authHeader.substring(0, 6).toLowerCase();  
	            if (HeadStr.compareTo("bearer") == 0)  
	            {  
	                String tokenString = authHeader.substring(7); 
	                JWToken token=null;
	                try {
	                	 token=JWTUtil.parseJWT(tokenString);
		 	             if (System.currentTimeMillis()<token.getExpiresMills()) { 
		 	            	 	System.out.println("Grant access to "+token.getUserId());
		 	                    chain.doFilter(request, response);  
		 	                    return;  
		 	             }  
	                }catch(Exception e) {
	                	System.out.println(e.getLocalizedMessage());
	                }
	            }  
	        }  
	          
	        reject_unauthorized_request( (HttpServletResponse) response);  
	        return;  
		
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private void reject_unauthorized_request(HttpServletResponse httpResponse) throws IOException{
	    httpResponse.setCharacterEncoding("UTF-8");    
        httpResponse.setContentType("application/json; charset=utf-8");   
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
  
        ObjectMapper mapper = new ObjectMapper();  
          
        ResultMsg resultMsg = new ResultMsg("001", "InvalidToken");  
        httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));  
	}
	
	
	private class ResultMsg {
		private String code;
		private String message;
		
		public ResultMsg() {
		}
		
		public ResultMsg(String code,String message) {
			this.code=code;
			this.message=message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
