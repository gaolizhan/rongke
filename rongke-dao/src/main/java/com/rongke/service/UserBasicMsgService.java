package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserBasicMsg;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserBasicMsgService
 * @Service
 */
public interface UserBasicMsgService extends IService<UserBasicMsg> {
    List<UserBasicMsg> selectByPage(Map<String,Object> map);
    UserBasicMsg selectOneDetails(String id);
    Integer selectCount(Map<String,Object> map);
    UserBasicMsg selectOneDetailsByUserId(String id);

    List<UserBasicMsg> selectDetailsById(String id);

    List<UserBasicMsg> selectDetailsByUserId(String id);
}
