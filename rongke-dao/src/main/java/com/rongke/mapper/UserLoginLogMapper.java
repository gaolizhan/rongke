package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserLoginLog;

import java.util.List;
import java.util.Map;

/**
 * @UserLoginLogMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog>{


    List<UserLoginLog> selectList1(Map<String, Object> map);

    int selectNum1(Map<String, Object> map);

}
