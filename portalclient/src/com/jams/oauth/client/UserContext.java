package com.jams.oauth.client;

import org.springframework.stereotype.Component;


public class UserContext {
    private static final ThreadLocal<String> authToken= new ThreadLocal<String>();
    public static String getAuthToken() { return authToken.get(); }
    public static void setAuthToken(String aToken) {authToken.set(aToken);}
}