package com.yunlsp.saas.service.api.portal.constant;

/**
 * Conf
 *
 * @author rodxu
 * @date 2018/8/31
 */
public class Conf {

    /**
     * redirect url, for client
     */
    public static final String REDIRECT_URL = "redirect_url";

    /**
     * sso sessionid, between browser and sso-server
     */
    public static final String SSO_SESSIONID = "yun_saas_sessionid";

    /**
     * sso user model
     */
    public static final String SSO_USER = "sso_user";

    /**
     * sso server address
     */
    public static final String SSO_SERVER = "sso_server";

    /**
     * login url
     */
    public static final String SSO_LOGIN = "login";

    /**
     * 没有权限页面路径
     */
    public static final String NO_AUTHORITY = "noAuthority";

    /**
     * sso exclued path
     */
    public static final String SSO_EXCLUDED_PATH = "excluded_path";

    /**
     * logout url
     */
    public static final String SSO_LOGOUT = "logout";

    /**
     * temp token  to get sessionId
     */
    public static final String SSO_TEMP_TOKEN = "token";

    /**
     * sso user permission redis key
     */
    public static final String USER_PERMISSION_KEY = "user_permission";

    /**
     * login path attribute
     */
    public static final String LOGIN_PATH_ATTR = "request_login_path";

    /**
     * filter logout path
     */
    public static final String SSO_LOGOUT_PATH = "logoutPath";

    /**
     * openId cookie key
     */
    public static final String OPENID_KEY = "open_id";


    /**
     * 图片验证码session key
     */
    public static final String IMAGE_CODE_SESSION_KEY = "image_auth_code";

    /**
     * 短信验证码session key
     */
    public static final String MESSAGE_CODE_SESSION_KEY = "message_auth_code";

    /**
     * 短信验证码验证成功唯一标识
     */
    public static final String MESSAGE_CODE_SUCCCESS_IDENTIFY = "message_auth_success_identify";

    /**
     * 注册短信模板
     */
    public static final String REGISTER_MESSAGE_CONTENT = "您的短信验证码是:%s,十分钟内有效";

    /**
     * 初始密码短信模板
     */
    public static final String ORIGINAL_PASSWORD_MESSAGE_CONTENT = "您已成功注册,登录账号【%s】,初始密码【%s】,请您及时登录修改密码";

    /**
     * redis key
     *
     * 登录时密码输入错误的次数
     */
    public static final String PSW_INPUT_ERROR_COUNT_KEY = "login:password:error:";

    /**
     * redis key
     *
     * 登录黑名单
     */
    public static final String LOGIN_USER_BLACKLIST_KEY = "login:blacklist:user:";

}
