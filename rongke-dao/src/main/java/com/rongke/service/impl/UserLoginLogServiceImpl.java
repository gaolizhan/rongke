package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserLoginLogMapper;
import com.rongke.model.UserLoginLog;
import com.rongke.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserLoginLogServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {
    @Autowired
     private UserLoginLogMapper userLoginLogMapper;

    public List<UserLoginLog> selectList1(Map<String, Object> map){
        return userLoginLogMapper.selectList1(map);
    }

    /**
     * 查询申请失败列表数量
     * @param map
     * @return
     */
    public int selectNum1(Map<String, Object> map){
        return userLoginLogMapper.selectNum1(map);
    }
}
