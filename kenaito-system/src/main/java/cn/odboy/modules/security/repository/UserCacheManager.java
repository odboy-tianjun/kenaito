package cn.odboy.modules.security.repository;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.modules.security.config.LoginProperties;
import cn.odboy.modules.security.domain.JwtUserDto;
import cn.odboy.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户缓存管理
 **/
@Component
public class UserCacheManager {

    @Resource
    private RedisUtil redisUtil;
    @Value("${login.user-cache.idle-time}")
    private long idleTime;

    /**
     * 返回用户缓存
     *
     * @param userName 用户名
     * @return JwtUserDto
     */
    public JwtUserDto getUserCache(String userName) {
        if (StringUtil.isNotEmpty(userName)) {
            // 获取数据
            Object obj = redisUtil.get(LoginProperties.CACHE_KEY + userName);
            if (obj != null) {
                return (JwtUserDto) obj;
            }
        }
        return null;
    }

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    public void addUserCache(String userName, JwtUserDto user) {
        if (StringUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期
            long time = idleTime + RandomUtil.randomInt(900, 1800);
            redisUtil.set(LoginProperties.CACHE_KEY + userName, user, time);
        }
    }

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    public void cleanUserCache(String userName) {
        if (StringUtil.isNotEmpty(userName)) {
            // 清除数据
            redisUtil.del(LoginProperties.CACHE_KEY + userName);
        }
    }
}
