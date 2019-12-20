package com.xkcoding.cache.redis.config.serializer;

import cn.hutool.json.JSONUtil;
import com.xkcoding.cache.redis.config.event.ChannelEvent;
import java.nio.charset.Charset;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 项目名称：
 * 类名称：GenericRedisSerializer
 * 类描述：
 * @author liubo
 * 创建时间：2019/12/19 18:16
 */
public class GenericRedisSerializer implements RedisSerializer<ChannelEvent> {

    @Override
    public byte[] serialize(ChannelEvent t) throws SerializationException {
        return new byte[0];
    }

    @Override
    public ChannelEvent deserialize(byte[] bytes) throws SerializationException {
        return JSONUtil.toBean(new String(bytes, Charset.defaultCharset()), ChannelEvent.class);
    }
}
