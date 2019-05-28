package com.yunlsp.saas.service.api.portal.auth;

import com.yunlsp.saas.service.api.portal.annotation.Authentication;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 许路路
 */
public class AuthHelper {

    /**
     * 是否有权限注解
     *
     * @param annotations
     * @return
     */
    public static Authentication isHasAuthAnnotation(Annotation[] annotations) {
        if (annotations == null && annotations.length <= 0) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof Authentication) {
                Authentication authAnnotation = (Authentication) annotation;
                return authAnnotation;
            }
        }
        return null;
    }

    public static Authentication getAuthAnnotation(Class<?> clazz, Method method) {
        Annotation[] annotations = method.getAnnotations();
        Authentication auth = isHasAuthAnnotation(annotations);
        if (auth == null) {
            annotations = clazz.getAnnotations();
            auth = isHasAuthAnnotation(annotations);
        }
        return auth;
    }

    /**
     * 判断是否有注解
     *
     * @param annotations
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T isHasAnnotation(Annotation[] annotations, Class<T> clazz) {
        if (annotations == null && annotations.length <= 0) {
            return null;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(clazz)) {
                T authAnnotation = (T) annotation;
                return authAnnotation;
            }
        }
        return null;
    }

}
