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
 * 授权后的回调请求处理
 * 
 * @author HCZ
 * @date 2013-11-12
 */
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = -1847238807216447030L;
	
	                                         
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		System.out.println("CODE:"+code+" STATE:"+state);

		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			
			Token token=ComUtil.getToken(Constants.CORPID,Constants.CORPSECRECT);
			// 网页授权接口访问凭证
			String accessToken = token.getAccessToken();
			
		    // 获取用户信息
			UserInfo userInfo =ComUtil.getOAuthUser(accessToken, code);

			// 设置要传递的参数
			//request.setAttribute("userInfo", userInfo);
			if(userInfo!=null)
				request.getSession().setAttribute("USER", userInfo);
		}
		// 跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
