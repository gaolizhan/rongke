package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserIdentity;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserIdentityService
 * @Service
 */
public interface UserIdentityService extends IService<UserIdentity> {
    List<UserIdentity> selectByPage(Map<String,Object> map);
    Integer selectCount(Map<String,Object> map);
    List<UserIdentity> selectOneDetailsByUserId(String id);




}
