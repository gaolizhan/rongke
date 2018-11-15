package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserZhifubaoMapper;
import com.rongke.model.UserZhifubao;
import com.rongke.service.UserZhifubaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserZhifubaoServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserZhifubaoServiceImpl extends ServiceImpl<UserZhifubaoMapper, UserZhifubao> implements UserZhifubaoService {
    @Autowired
    private UserZhifubaoMapper userZhifubaoMapper;


    public List<UserZhifubao> selectAll(Map<String,Object> map){
        return userZhifubaoMapper.selectAll(map);
    }



    public List<UserZhifubao> selectMyAll(Map<String,Object> map){
        return userZhifubaoMapper.selectMyAll(map);
    }


    public Integer selectCountNum(Map<String,Object>map){
        return userZhifubaoMapper.selectCountNum(map);
    }
    public Integer selectMyCountNum(Map<String,Object>map){
        return userZhifubaoMapper.selectMyCountNum(map);
    }

    public List<UserZhifubao> selectMyTemporaryAll(Map<String,Object> map){
        return userZhifubaoMapper.selectMyTemporaryAll(map);
    }
     public Integer selectMyTemporaryNum(Map<String,Object>map){
        return userZhifubaoMapper.selectMyTemporaryNum(map);
     }



    public List<UserZhifubao> selectAllTemporary(Map<String,Object> map){
        return userZhifubaoMapper.selectAllTemporary(map);
    }
    public Integer selectAllTemporaryNum(Map<String,Object>map){
        return userZhifubaoMapper.selectAllTemporaryNum(map);
    }





}
