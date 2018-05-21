package com.jams.oauth.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class RequestAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2)
			throws IOException, ServletException {
		//response.sendError(HttpServletResponse.SC_FORBIDDEN, "{\"error\":\"invalid_client\"}");
		response.getWriter().println("{\"error\":\"invalid_client\"}");
	}

}
