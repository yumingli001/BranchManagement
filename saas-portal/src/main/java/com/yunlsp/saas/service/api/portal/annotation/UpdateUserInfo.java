package com.yunlsp.saas.service.api.portal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 更新用户信息注解
 * 方法标注此注解  调用后会更新sso中的用户信息为最新信息
 * 注：要使此注解起作用 方法必须被sso拦截器拦截
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UpdateUserInfo {
}
