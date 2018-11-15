package com.rongke.web.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.mapper.UserMapper;
import com.rongke.model.Admin;
import com.rongke.model.Channel;
import com.rongke.model.User;
import com.rongke.service.AdminService;
import com.rongke.service.ChannelService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class PasswordRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userDao;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ChannelService channelService;

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
     *//*
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        //用户名
        User user = userDao.selectByUserName(userName);
        Admin admin = new Admin();
        //手机
        if (user == null) {
            user = userDao.selectByPhone(userName);
        }
        if (user == null) {
            EntityWrapper<Admin> wrapper = new EntityWrapper<>();
            wrapper.eq("user_name",userName);
            wrapper.eq("status",1);
            admin = adminService.selectOne(wrapper);
        }
        if (user != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
            return authcInfo;
        } else if (admin != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName, admin.getPassword(), getName());
            return authcInfo;
        }else{
            return null;
        }
    }*/
    /**
     * 验证当前登录的用户
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        //用户名
        //手机
        User user = userDao.selectByPhone(userName);
        Admin admin = new Admin();
        Channel channel = new Channel();
        if (user == null) {
            EntityWrapper<Admin> wrapper = new EntityWrapper<>();
            wrapper.eq("user_name",userName);
            wrapper.eq("status",1);
            admin = adminService.selectOne(wrapper);
        }
        if (admin == null) {
            EntityWrapper<Channel> wrapper = new EntityWrapper<>();
            wrapper.eq("login_name",userName);
            wrapper.eq("status",1);
            channel = channelService.selectOne(wrapper);
        }
        if (user != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
            return authcInfo;
        } else if (admin != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName, admin.getPassword(), getName());
            return authcInfo;
        }else if (channel != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName, channel.getPassword(), getName());
            return authcInfo;
        }else{
            return null;
        }
    }

}	
