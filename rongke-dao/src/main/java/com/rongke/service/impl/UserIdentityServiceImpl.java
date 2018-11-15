package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserIdentityMapper;
import com.rongke.model.UserIdentity;
import com.rongke.service.UserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserIdentityServiceImpl
 * @ServiceImpl
 */
@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityMapper, UserIdentity> implements UserIdentityService {

    @Autowired
    private UserIdentityMapper userIdentityMapper;

    public List<UserIdentity> selectByPage(Map<String,Object> map){

        List<UserIdentity> list = userIdentityMapper.selectByPage(map);

        return list;
    }

    public Integer selectCount(Map<String,Object> map){

        Integer count = userIdentityMapper.selectCount(map);
        return count;
    }
    public List<UserIdentity> selectOneDetailsByUserId(String id){

        return userIdentityMapper.selectOneDetailsByUserId(id);
    }


}
