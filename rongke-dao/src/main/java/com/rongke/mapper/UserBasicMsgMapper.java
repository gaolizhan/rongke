package com.rongke.mapper;

import com.rongke.model.UserBasicMsg;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserBasicMsgMapper
 * @Mapper
 */
public interface UserBasicMsgMapper extends BaseMapper<UserBasicMsg> {

    List<UserBasicMsg> selectByPage(Map<String,Object> map);

    UserBasicMsg selectOneDetails(String id);

    Integer selectCount(Map<String,Object> map);

    UserBasicMsg selectOneDetailsByUserId(String id);


    List<UserBasicMsg> selectDetailsById(String id);

    List<UserBasicMsg> selectDetailsByUserId(String id);
}
