package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserJindongAddressMapper;
import com.rongke.model.User;
import com.rongke.model.UserJindongAddress;
import com.rongke.service.UserJindongAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserJindongAddressServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserJindongAddressServiceImpl extends ServiceImpl<UserJindongAddressMapper, UserJindongAddress> implements UserJindongAddressService {

    @Autowired
    private UserJindongAddressMapper userJindongAddressMapper;

    public Integer selectCount(Map<String,Object> map){
        return userJindongAddressMapper.selectCount(map);
    };
    public List<User> selectUser(Map<String,Object> map){
        return userJindongAddressMapper.selectUser(map);
    };
}
