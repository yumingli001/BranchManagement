package com.yunlsp.saas.service.api.portal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要权限认证注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Authentication {

    /**
     * true 需要权限认证
     *
     * @return
     */
    boolean limit() default true;

    /**
     * 权限标识
     *
     * @return
     */
    String[] permissions() default {};

}
