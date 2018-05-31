package com.lolsearch.lolrecordsearch.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    private final static List<String> redirectUris = new ArrayList<>();
    
    static {
        redirectUris.add("/users/login");
        redirectUris.add("/users/signup");
    }
    
    public CustomSuccessHandler() {
        setUseReferer(true);
        setRedirectStrategy(new CustomRedirectStrategy());
    }
    
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        boolean json = isJsonRequest(request.getHeaders(HttpHeaders.CONTENT_TYPE));
    
        if(json) {
            String referer = request.getHeader(HttpHeaders.REFERER);
            String sessionReferer = (String) request.getSession().getAttribute("referer");
            if(sessionReferer != null) {
                referer = sessionReferer;
                request.getSession().removeAttribute("referer");
            }
            String result = makeJsonResult(referer);
            
            PrintWriter writer = response.getWriter();
            writer.print(result);
            writer.flush();
            return;
        }
    
        super.onAuthenticationSuccess(request, response, authentication);
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
    
    private String makeJsonResult(String referer) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"referer\"");
        sb.append(":");
        sb.append("\"").append(referer).append("\"");
        sb.append("}");
        sb.toString();
        return sb.toString();
    }
    
    
    private static String replaceRedirectUri(String referer) {
        
        for(String uri : redirectUris) {
            if(referer.contains(uri)) {
                return referer.replaceAll(uri + ".*", "/records");
            }
        }
        
        return referer;
    }
    
    
    
    private static class CustomRedirectStrategy extends DefaultRedirectStrategy {
        @Override
        public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
            String referer = (String) request.getSession().getAttribute("referer");
            if(referer != null) {
                request.getSession().removeAttribute("referer");
            }
            else {
                referer = request.getHeader(HttpHeaders.REFERER);
            }
            url = replaceRedirectUri(referer);
            super.sendRedirect(request, response, url);
        }
    }
    
}
