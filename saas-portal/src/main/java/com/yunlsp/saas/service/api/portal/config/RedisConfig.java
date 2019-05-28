package com.yunlsp.saas.service.api.portal.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.yunlsp.saas.service.api.portal.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import redis.clients.util.JedisURIHelper;

import java.net.URI;

/**
 * @author 许路路
 */
@Configuration
public class RedisConfig implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @NacosValue("${sso.redis.address:redis://:RedisAdmin@192.168.2.75:6379/3}")
    private String ssoRedisAddress;

    @Override
    public void afterPropertiesSet() {
        // redis init
        JedisUtil.init(ssoRedisAddress);
        URI uri = URI.create(ssoRedisAddress);
        if (JedisURIHelper.isValid(uri)) {
            logger.info("redis初始化..........address:" + uri.getHost());
        }
    }
}
