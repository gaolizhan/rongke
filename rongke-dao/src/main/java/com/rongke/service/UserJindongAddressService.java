package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.User;
import com.rongke.model.UserJindongAddress;

import java.util.List;
import java.util.Map;

/**
 * @UserJindongAddressService
 * @Service
 * @version : Ver 1.0
 */
public interface UserJindongAddressService extends IService<UserJindongAddress>{
    Integer selectCount(Map<String,Object> map);
    List<User> selectUser(Map<String,Object> map);
}
