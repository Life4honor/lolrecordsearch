package com.lolsearch.lolrecordsearch.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        if(attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            session.removeAttribute(key);
        }
        
        super.onLogoutSuccess(request, response, authentication);
    }
}
