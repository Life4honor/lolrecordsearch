package com.lolsearch.lolrecordsearch.config;

import com.lolsearch.lolrecordsearch.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private JpaPersistentTokenRepositoryImpl persistentTokenRepository;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/h2-console/**").permitAll()
             .antMatchers("/users/login").permitAll()
             .antMatchers("/users/signup").permitAll()
             .antMatchers("/users/findEmail").permitAll()
             .antMatchers("/users/findPassword").permitAll()
             .antMatchers("/users/findResult").permitAll()
             .antMatchers("/api/users/**").permitAll()
             .antMatchers("/records/**").permitAll()
             .antMatchers("/users").permitAll()
             .antMatchers("/users/**").hasRole("USER")
             .antMatchers("/chatrooms/**").hasRole("USER")
             .antMatchers("/").permitAll()
            .anyRequest().authenticated()
        .and()
            .csrf().disable()
            .headers().frameOptions().disable()
        .and()
            .formLogin().loginPage("/users/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(new CustomSuccessHandler())
                        .failureHandler(new CustomFailureHandler())
        .and()
            .logout().logoutUrl("/users/logout")
            .logoutSuccessUrl("/records")
            .deleteCookies("remember-me")
//            .invalidateHttpSession(false)
            .logoutSuccessHandler(customLogoutSuccessHandler)
        .and()
            .rememberMe().rememberMeParameter("remember-me")
                         .rememberMeCookieName("remember-me")
                         .userDetailsService(userDetailsService)
                         .tokenRepository(persistentTokenRepository)
                         .tokenValiditySeconds(60*60*24*7)
        .and()
            .exceptionHandling().accessDeniedPage("/403")
        .and()
            .sessionManagement().maximumSessions(1)
                                .expiredUrl("/users/login")
                                .maxSessionsPreventsLogin(true)
                                .sessionRegistry(sessionRegistry());
    }
    
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {

        ServletListenerRegistrationBean<HttpSessionEventPublisher> listener = new ServletListenerRegistrationBean<>();
        listener.setListener(new HttpSessionEventPublisher());
        
        return listener;
    }
    
    @Bean
    public ApplicationListener<HttpSessionCreatedEvent> httpSessionCreatedEventApplicationListener() {
        return new SessionCreateListener();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
}
