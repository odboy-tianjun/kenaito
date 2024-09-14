package cn.odboy.infra.redis;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.util.annotation.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 重写序列化器
 *
 * @date 2024-04-11
 */
public class StringRedisSerializer implements RedisSerializer<Object> {
    private final Charset charset;

    StringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    private StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public @Nullable byte[] serialize(Object object) {
        String string = JSON.toJSONString(object);
        if (org.apache.commons.lang3.StringUtils.isBlank(string)) {
            return null;
        }
        string = string.replace("\"", "");
        return string.getBytes(charset);
    }
}
