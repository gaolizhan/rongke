package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserLoginLog;

import java.util.List;
import java.util.Map;

/**
 * @UserLoginLogService
 * @Service
 * @version : Ver 1.0
 */
public interface UserLoginLogService extends IService<UserLoginLog>{
    List<UserLoginLog> selectList1(Map<String, Object> map);
    int selectNum1(Map<String, Object> map);
}
