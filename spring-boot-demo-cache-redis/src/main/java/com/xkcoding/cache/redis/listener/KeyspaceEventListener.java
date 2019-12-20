package com.xkcoding.cache.redis.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 项目名称：
 * 类名称：KeyspaceEventListener
 * 类描述：监听redis所有的key操作
 * @author liubo
 * 创建时间：2019/12/20 10:34
 */
@Slf4j
@Component
public class KeyspaceEventListener extends KeyspaceEventMessageListener {

    public KeyspaceEventListener(
            RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);

        // 默认监听所有的key操作事件：__keyevent@*
        // 当redis没有设置notify-keyspace-events时，可以修改KeyspaceEventMessageListener默认的EA 为 E$
//        setKeyspaceNotificationsConfigParameter("E$");
    }

    @Override
    protected void doHandleMessage(Message message) {
        log.info("doHandleMessage >> channel={}, event={}, eventKey={}",
                new String(message.getChannel()), StrUtil.subAfter(new String(message.getChannel()), ":", true), new String(message.getBody()));
    }
}
