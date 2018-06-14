package com.lolsearch.lolrecordsearch.config;


import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.initialize();
        return taskExecutor;
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/404").setViewName("exception/404");
        registry.addViewController("/403").setViewName("exception/403");
    }
    
    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return (registry) -> registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
    }
    
}
