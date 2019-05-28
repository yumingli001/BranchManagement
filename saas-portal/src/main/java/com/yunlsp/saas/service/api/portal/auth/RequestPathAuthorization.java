package com.yunlsp.saas.service.api.portal.auth;

import com.yunlsp.saas.service.api.model.Permission;
import com.yunlsp.saas.service.api.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限验证实现--通过请求路径匹配
 *
 * @author 许路路
 */
public class RequestPathAuthorization implements IAuthorization {

    private static Logger logger = LoggerFactory.getLogger("RequestPathAuthorization");

    private IUrlMatch urlMatch = new AntUrlMatch();

    @Override
    public boolean hasAuth(HttpServletRequest request, Object currentUser, Object handler) {
        if (!(currentUser instanceof UserModel) || !(handler instanceof HandlerMethod)) {
            return false;
        }
        UserModel _current_user = (UserModel) currentUser;
        //管理员有全部权限
        if (_current_user.isAdmin()) {
            return true;
        }
        String reqPath = request.getServletPath();
        //获取用户的权限信息
        List<Permission> userPerms = new ArrayList<>();
        if (userPerms != null && !userPerms.isEmpty()) {
            return userPerms.stream().map(item -> item.getPermUrl()).anyMatch(item -> urlMatch.match(item, reqPath));
        }
        logger.debug("用户{}没有访问{}的权限", _current_user.getUserId(), reqPath);
        return false;
    }

    public void setUrlMatch(IUrlMatch urlMatch) {
        this.urlMatch = urlMatch;
    }
}
