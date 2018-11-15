package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserTaobaoAddressMapper;
import com.rongke.model.User;
import com.rongke.model.UserTaobaoAddress;
import com.rongke.service.UserTaobaoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserTaobaoAddressServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserTaobaoAddressServiceImpl extends ServiceImpl<UserTaobaoAddressMapper, UserTaobaoAddress> implements UserTaobaoAddressService {

    @Autowired
    private UserTaobaoAddressMapper userTaobaoAddressMapper;

    public List<UserTaobaoAddress> selectByPage(Map<String,Object> map){
        return userTaobaoAddressMapper.selectByPage(map);
    }
    public Integer selectCount(Map<String,Object> map){
        return userTaobaoAddressMapper.selectCount(map);
    }
    public List<User> selectUser(Map<String,Object> map){
        return userTaobaoAddressMapper.selectUser(map);
    }
}
