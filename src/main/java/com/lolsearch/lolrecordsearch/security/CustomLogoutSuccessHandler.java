package com.lolsearch.lolrecordsearch.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    
//    @Autowired
//    private SessionRegistry sessionRegistry;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    
//        HttpSession session = request.getSession();
//        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
//        sessionRegistry.removeSessionInformation(session.getId());
    
        super.onLogoutSuccess(request, response, authentication);
    }
}
