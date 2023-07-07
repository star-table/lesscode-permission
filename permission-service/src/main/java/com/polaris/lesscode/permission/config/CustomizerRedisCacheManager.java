package com.polaris.lesscode.permission.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * 自定义Redis缓存管理
 *
 * @author roamer
 * @version v1.0
 * @date 2020-08-31 10:30
 */
@Slf4j
public class CustomizerRedisCacheManager extends RedisCacheManager {

    /**
     * 过期时间分隔符，可以用其他符号
     */
    private static final String SEPARATOR = "#";

    public CustomizerRedisCacheManager(RedisCacheWriter cacheWriter,
                                       RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        String[] array = name.split(SEPARATOR);
        name = array[0];
        // 解析TTL
        if (array.length > 1) {
            long ttl = Long.parseLong(array[1]);
            // 注意单位是秒
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
