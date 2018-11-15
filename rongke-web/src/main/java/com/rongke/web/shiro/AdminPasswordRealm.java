package com.rongke.web.shiro;

import com.rongke.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员验证
 */
@Transactional
public class AdminPasswordRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userDao;


    /**
     * 为当前登录的用户授予权限
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //String phone = (String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorizationInfo;
    }

    /**
     * 验证当前登录的用户
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
/*        String userName = (String) token.getPrincipal();
        User user = userDao.selectByUserName(userName);
        if (user != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
            return authcInfo;
        } else {
            return null;
        }*/
        return null;
    }
}	
