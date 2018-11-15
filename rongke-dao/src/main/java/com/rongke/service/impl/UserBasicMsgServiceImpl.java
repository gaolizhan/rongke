package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserBasicMsgMapper;
import com.rongke.model.UserBasicMsg;
import com.rongke.service.UserBasicMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserBasicMsgServiceImpl
 * @ServiceImpl
 */
@Service
public class UserBasicMsgServiceImpl extends ServiceImpl<UserBasicMsgMapper, UserBasicMsg> implements UserBasicMsgService {

    @Autowired
    private UserBasicMsgMapper userBasicMsgMapper;

    public List<UserBasicMsg> selectByPage(Map<String,Object> map){

        List<UserBasicMsg> userBasicMsgs = userBasicMsgMapper.selectByPage(map);

        return userBasicMsgs;
    }

    public UserBasicMsg selectOneDetails(String id){

        UserBasicMsg userBasicMsg = userBasicMsgMapper.selectOneDetails(id);

        return userBasicMsg;
    }

    public Integer selectCount(Map<String,Object> map){

        return userBasicMsgMapper.selectCount(map);
    }

    public UserBasicMsg selectOneDetailsByUserId(String id){

        UserBasicMsg userBasicMsg = userBasicMsgMapper.selectOneDetailsByUserId(id);

        return userBasicMsg;
    }

    @Override
    public List<UserBasicMsg> selectDetailsById(String id) {
        return userBasicMsgMapper.selectDetailsById(id);
    }

    @Override
    public List<UserBasicMsg> selectDetailsByUserId(String id) {
        return userBasicMsgMapper.selectDetailsByUserId(id);
    }

}
