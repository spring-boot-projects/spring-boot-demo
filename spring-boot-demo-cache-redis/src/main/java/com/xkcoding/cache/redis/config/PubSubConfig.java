package com.xkcoding.cache.redis.config;

import com.google.common.collect.Lists;
import com.xkcoding.cache.redis.config.serializer.GenericRedisSerializer;
import com.xkcoding.cache.redis.listener.ConcreteMessageHandler;
import com.xkcoding.cache.redis.listener.RedisExpiredListener;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 项目名称：
 * 类名称：PubSubListenerConfig
 * 类描述：发布订阅监听配置
 * @author liubo
 * 创建时间：2019/12/19 16:46
 */
@Slf4j
@Configuration
public class PubSubConfig {

    /**
     * 默认使用MessageListenerAdapter处理监听单个channel
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                log.info("onMessage(message={}, channel={})", message, new String(pattern, Charset.forName("UTF-8")));
                super.onMessage(message, pattern);
            }
        };
    }

    @Autowired
    private ConcreteMessageHandler concreteMessageHandler;

    @Bean
    public MessageListenerAdapter delegateMessageListenerAdapter() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(concreteMessageHandler);
        // 使用指定反序列处理消息内容
        messageListenerAdapter.setSerializer(new GenericRedisSerializer());
        return messageListenerAdapter;
    }

    @Bean
    public MessageListenerAdapter delegateMessageListenerAdapterWithSepeMethod() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(concreteMessageHandler);
        messageListenerAdapter.setDefaultListenerMethod("handleMessageOnly");
        return messageListenerAdapter;
    }

    @Autowired
    private RedisExpiredListener redisExpiredListener;

    @Bean
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        // 简单监听名为chatrooom的channel
        container.addMessageListener(messageListenerAdapter(), new ChannelTopic("chatrooom"));

        // 监听多个channel
        container.addMessageListener(messageListenerAdapter(), Lists.newArrayList(
            new ChannelTopic("channel_1"),
            new ChannelTopic("channel_2")
        ));

        // 使用指定反序列处理订阅channel通知内容
        container.addMessageListener(delegateMessageListenerAdapter(), new ChannelTopic("delegate"));

        // 使用指定消息处理方法订阅channel通知内容
        container.addMessageListener(delegateMessageListenerAdapterWithSepeMethod(), new ChannelTopic("message"));

        // 指定过期事件的通知监听
        container.addMessageListener(redisExpiredListener, new ChannelTopic("__keyevent@0__:expired"));

        return container;
    }

    /**
     * 监听redis所有DB的key过期事件
     */
    @Bean
    @ConditionalOnMissingBean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        return new KeyExpirationEventMessageListener(listenerContainer);
    }


}
