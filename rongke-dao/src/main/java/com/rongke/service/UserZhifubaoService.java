package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserZhifubao;

import java.util.List;
import java.util.Map;

/**
 * @UserZhifubaoService
 * @Service
 * @version : Ver 1.0
 */
public interface UserZhifubaoService extends IService<UserZhifubao>{

    List<UserZhifubao> selectAll(Map<String,Object> map);
    Integer selectCountNum(Map<String,Object>map);
    Integer selectMyCountNum(Map<String,Object>map);
    List<UserZhifubao> selectMyAll(Map<String,Object> map);
    List<UserZhifubao> selectMyTemporaryAll(Map<String,Object> map);
    Integer selectMyTemporaryNum(Map<String,Object>map);
    List<UserZhifubao> selectAllTemporary(Map<String,Object> map);
    Integer selectAllTemporaryNum(Map<String,Object>map);

}
