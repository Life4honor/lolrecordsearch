package com.lolsearch.lolrecordsearch.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;

public class SessionCreateListener implements ApplicationListener<HttpSessionCreatedEvent> {
    
    private static final int MINUTE = 60;
    
    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent event) {
        event.getSession().setMaxInactiveInterval(MINUTE * 15);
    }
}
