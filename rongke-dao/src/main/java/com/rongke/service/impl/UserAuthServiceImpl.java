package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserAuthMapper;
import com.rongke.model.UserAuth;
import com.rongke.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserAuthServiceImpl
 * @ServiceImpl
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {
    @Autowired
    private UserAuthMapper userAuthMapper;

    public List<UserAuth> selectAll(Map<String,Object>map){
         return userAuthMapper.selectAll(map);
    }


}
