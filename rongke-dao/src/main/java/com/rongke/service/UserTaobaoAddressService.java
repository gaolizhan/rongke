package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.User;
import com.rongke.model.UserTaobaoAddress;

import java.util.List;
import java.util.Map;

/**
 * @UserTaobaoAddressService
 * @Service
 * @version : Ver 1.0
 */
public interface UserTaobaoAddressService extends IService<UserTaobaoAddress>{
    List<UserTaobaoAddress> selectByPage(Map<String,Object> map);
    Integer selectCount(Map<String,Object> map);
    List<User> selectUser(Map<String,Object> map);
}
