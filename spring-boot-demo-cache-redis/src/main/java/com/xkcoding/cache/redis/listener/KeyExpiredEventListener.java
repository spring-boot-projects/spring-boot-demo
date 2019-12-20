package com.xkcoding.cache.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

/**
 * 项目名称：
 * 类名称：KeyExpiredEventListener
 * 类描述：监听redis所有DB的key过期事件
 * @author liubo
 * 创建时间：2019/12/20 10:48
 */
@Slf4j
@Component
public class KeyExpiredEventListener implements ApplicationListener<RedisKeyExpiredEvent> {

    @Override
    public void onApplicationEvent(RedisKeyExpiredEvent event) {
        log.info("onApplicationEvent >> channel={}, event={}", event.getChannel(), event);
    }
}
