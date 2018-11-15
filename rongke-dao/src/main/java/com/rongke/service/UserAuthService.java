package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserAuth;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserAuthService
 * @Service
 */
public interface UserAuthService extends IService<UserAuth> {
    List<UserAuth> selectAll(Map<String,Object> map);
}
