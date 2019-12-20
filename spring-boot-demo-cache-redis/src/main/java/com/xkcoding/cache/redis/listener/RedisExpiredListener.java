package com.xkcoding.cache.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 项目名称：
 * 类名称：RedisExpiredListener
 * 类描述：自定义Redis过期监听器，指定监听某个事件类型。例如："__keyevent@0__:expired"
 * @author liubo
 * 创建时间：2019/12/20 10:22
 */
@Slf4j
@Component
public class RedisExpiredListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        DefaultMessage defaultMessage = (DefaultMessage) message;
        log.info("onMessage >> channel={}, expiredKey={}",
                new String(defaultMessage.getChannel()), new String(defaultMessage.getBody()));
    }
}
