package com.xkcoding.cache.redis.listener;

import com.xkcoding.cache.redis.config.event.ChannelEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 项目名称：
 * 类名称：ConcreateMessageListenerAdapter
 * 类描述：
 * @author liubo
 * 创建时间：2019/12/19 18:29
 */
@Slf4j
@Component
public class ConcreteMessageHandler {

    /**
     * 解析消息为对象
     * @param channelEvent 消息对象
     * @param channel 订阅的频道
     */
    public void handleMessage(ChannelEvent channelEvent, String channel) {
        channelEvent.setChannel(channel);
        log.info("handleMessage(channelEvent={})", channelEvent);
    }

    /**
     * 只返回消息体
     * @param message 消息
     */
    public void handleMessageOnly(String message) {
        log.info("handleMessageOnly(message={})", message);
    }

}
