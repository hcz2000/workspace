package com.jams.organization.utils;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        System.out.println("****** I am entering the organization service id with auth token: " + httpServletRequest.getHeader("Authorization"));


        UserContext.setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        UserContext.setUserId( httpServletRequest.getHeader(UserContext.USER_ID) );
        UserContext.setAuthToken( httpServletRequest.getHeader(UserContext.AUTH_TOKEN) );
        UserContext.setOrgId( httpServletRequest.getHeader(UserContext.ORG_ID) );
        
        /*
        String authToken = UserContext.getAuthToken().replace("Bearer ","");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("345345fsdfsf5345".getBytes("UTF-8"))
                    .parseClaimsJws(authToken).getBody();
           String result = (String) claims.get("organization");
           System.out.println("****** UserContextFilter:"+result);
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}