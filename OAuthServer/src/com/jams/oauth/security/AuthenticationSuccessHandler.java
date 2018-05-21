package com.jams.oauth.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private Map<String, String> authDispatcherMap;
	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// ��ȡ�û�Ȩ��
		Collection<? extends GrantedAuthority> authCollection = authentication.getAuthorities();
		if (authCollection.isEmpty()) {
			return;
		}

		// ��֤�ɹ��󣬻�ȡ�û���Ϣ����ӵ�session��
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		//request.getSession().setAttribute("user", userDetails.getUsername());

		String url = null;
		// �ӱ������ҳ����ת�����������savedRequest��Ϊ��
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			url = savedRequest.getRedirectUrl();
			//getRedirectStrategy().sendRedirect(request, response, url);
		}

		// ֱ�ӵ����¼ҳ�棬���ݵ�¼�û���Ȩ����ת����ͬ��ҳ��
		if (url == null) {
			for (GrantedAuthority auth : authCollection) {
				url = authDispatcherMap.get(auth.getAuthority());
			}
			if(url != null)
				getRedirectStrategy().sendRedirect(request, response, url);
		}

		super.onAuthenticationSuccess(request, response, authentication);

	}

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	public Map<String, String> getAuthDispatcherMap() {
		return authDispatcherMap;
	}

	public void setAuthDispatcherMap(Map<String, String> authDispatcherMap) {
		this.authDispatcherMap = authDispatcherMap;
	}

}