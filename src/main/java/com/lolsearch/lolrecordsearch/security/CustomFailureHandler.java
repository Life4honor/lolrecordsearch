package com.lolsearch.lolrecordsearch.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    public CustomFailureHandler() {

    }
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("login exception : {}", exception);
        if(exception instanceof SessionAuthenticationException) {
            exception = new SessionAuthenticationException("이미 로그인된 사용자 입니다.");
        }
        boolean json = isJsonRequest(request.getHeaders(HttpHeaders.CONTENT_TYPE));
        
        if(json) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
            return;
        }
        setDefaultFailureUrl("/users/login");
        super.onAuthenticationFailure(request, response, exception);
    }
    
    private boolean isJsonRequest(Enumeration<String> headers) {
        boolean json = false;
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if(header.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                json = true;
                break;
            }
        }
        return json;
    }
}
