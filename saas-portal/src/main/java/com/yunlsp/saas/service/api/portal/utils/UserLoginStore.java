package com.yunlsp.saas.service.api.portal.utils;

import com.alibaba.fastjson.JSON;
import com.yunlsp.saas.service.api.model.Permission;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * local login store
 *
 * @author 2018-04-02 20:03:11
 */
public class UserLoginStore {

    /**
     * 默认过期时间,单位/秒, 60*60*1=1H,
     */
    private static final int DEFAULT_EXPIRE_TIME = 60 * 60 * 5;

    /**
     * 记住我过期时间,单位/秒, 60*60*24*7=172800, 7天
     */
    public static final int REMEMBER_ME_EXPIRE_TIME = 60 * 60 * 7 * 24;

    /**
     * get 获取当前的登陆用户信息
     *
     * @param sessionId
     * @return
     */
    public static <T extends Serializable> T getLoginUserInfo(String sessionId, Class<T> clazz) {
        String redisKey = redisKey(sessionId);
        String res = JedisUtil.getStringValue(redisKey);
        T userModel = null;
        if (StringUtils.isNotBlank(res)) {
            userModel = JSON.parseObject(res, clazz);
        }
        if (userModel != null) {
            Long expireSurplus = JedisUtil.expireSurplus(redisKey);
            //小于一半时间有请求 则时间延长到默认过期时间
            if (expireSurplus != null && expireSurplus < DEFAULT_EXPIRE_TIME / 2) {
                JedisUtil.expire(redisKey, DEFAULT_EXPIRE_TIME);
            }
            return userModel;
        }
        return null;
    }

    /**
     * 获取redis中的用户权限信息
     *
     * @param userId
     * @return
     */
    public static List<Permission> getUserPerms(String userId) {
        String permKey = permRedisKey(userId);
        String jsonStr = JedisUtil.getStringValue(permKey);
        return JSON.parseArray(jsonStr, Permission.class);

    }

    /**
     * remove 删除redis中的用户信息
     *
     * @param sessionId
     */
    public static void remove(String sessionId) {
        String redisKey = redisKey(sessionId);
        JedisUtil.del(redisKey);
    }

    /**
     * put 用户信息  到  redis
     *
     * @param sessionId
     * @param user
     */
    public static void put(String sessionId, UserModel user, boolean isRemember) {
        String redisKey = redisKey(sessionId);
        if (isRemember) {
            JedisUtil.setStringValue(redisKey, JSON.toJSONString(user), REMEMBER_ME_EXPIRE_TIME);
        } else {
            JedisUtil.setStringValue(redisKey, JSON.toJSONString(user), DEFAULT_EXPIRE_TIME);
        }
    }

    /**
     * put 用户的权限信息到redis
     *
     * @param userId
     * @param perms
     */
    public static void putPerm(String userId, List<Permission> perms) {
        String redisKey = permRedisKey(userId);
        JedisUtil.setStringValue(redisKey, JSON.toJSONString(perms));
    }

    private static String redisKey(String sessionId) {
        return Conf.SSO_SESSIONID.concat("#").concat(sessionId);
    }

    private static String permRedisKey(String userId) {
        return Conf.USER_PERMISSION_KEY.concat("#").concat(userId);
    }

}
