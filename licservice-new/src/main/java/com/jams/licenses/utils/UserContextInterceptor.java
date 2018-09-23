package com.jams.licenses.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.AUTH_TOKEN, UserContext.getAuthToken());
        headers.add(UserContext.CORRELATION_ID, UserContext.getCorrelationId());
        
        logger.debug("In Licensing Service UserContextInterceptor - setting Authorization header: {}" , UserContext.getAuthToken());

        return execution.execute(request, body);
    }
}