package com.jams.weixin.qy.myapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jams.weixin.core.Token;
import com.jams.weixin.qy.pojo.UserInfo;
import com.jams.weixin.qy.util.ComUtil;

/**
 * ��Ȩ��Ļص�������
 * 
 * @author HCZ
 * @date 2013-11-12
 */
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = -1847238807216447030L;
	
	                                         
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// �û�ͬ����Ȩ���ܻ�ȡ��code
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		System.out.println("CODE:"+code+" STATE:"+state);

		// �û�ͬ����Ȩ
		if (!"authdeny".equals(code)) {
			// ��ȡ��ҳ��Ȩaccess_token
			
			Token token=ComUtil.getToken(Constants.CORPID,Constants.CORPSECRECT);
			// ��ҳ��Ȩ�ӿڷ���ƾ֤
			String accessToken = token.getAccessToken();
			
		    // ��ȡ�û���Ϣ
			UserInfo userInfo =ComUtil.getOAuthUser(accessToken, code);

			// ����Ҫ���ݵĲ���
			//request.setAttribute("userInfo", userInfo);
			if(userInfo!=null)
				request.getSession().setAttribute("USER", userInfo);
		}
		// ��ת��index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
