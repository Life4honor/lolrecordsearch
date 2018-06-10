package com.lolsearch.lolrecordsearch.config;

import com.lolsearch.lolrecordsearch.websocket.ChatMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    
    public static final String REDIS_TOPIC = "chat";
    
    @Bean
    public MessageListenerAdapter messageListenerAdapter(ChatMessageListener chatMessageListener) {
        return new MessageListenerAdapter(chatMessageListener);
    }
    
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory
            , MessageListenerAdapter messageListenerAdapter) {
        
        
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(REDIS_TOPIC+".*"));
        
        return redisMessageListenerContainer;
    }
    
}
