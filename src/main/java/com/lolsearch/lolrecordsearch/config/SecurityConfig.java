package com.lolsearch.lolrecordsearch.config;

import com.lolsearch.lolrecordsearch.security.CustomFailureHandler;
import com.lolsearch.lolrecordsearch.security.CustomSuccessHandler;
import com.lolsearch.lolrecordsearch.security.JpaPersistentTokenRepositoryImpl;
import com.lolsearch.lolrecordsearch.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
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
             .antMatchers("/users/**").hasRole("USER")
             .antMatchers("/chatrooms/**").hasRole("USER")
//            .anyRequest().authenticated()
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
        .and()
            .rememberMe().rememberMeParameter("remember-me")
                         .rememberMeCookieName("remember-me")
                         .userDetailsService(userDetailsService)
                         .tokenRepository(persistentTokenRepository)
                         .tokenValiditySeconds(60*60*24*7);
    }
}
