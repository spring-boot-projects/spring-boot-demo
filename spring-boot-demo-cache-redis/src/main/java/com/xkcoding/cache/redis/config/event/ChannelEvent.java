package com.xkcoding.cache.redis.config.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 项目名称：
 * 类名称：ChannelEvent
 * 类描述：
 * @author liubo
 * 创建时间：2019/12/19 18:20
 */
@Getter
@Setter
@Builder
@ToString
public class ChannelEvent {

    /* 订阅频道 */
    private String channel;

    /* 通知事件 */
    private String event;

    /* 通知消息内容 */
    private String message;

}
