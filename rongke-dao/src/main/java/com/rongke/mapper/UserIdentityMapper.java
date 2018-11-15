package com.rongke.mapper;

import com.rongke.model.UserIdentity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserIdentityMapper
 * @Mapper
 */
public interface UserIdentityMapper extends BaseMapper<UserIdentity> {

    List<UserIdentity> selectByPage(Map<String,Object> map);
    Integer selectCount(Map<String,Object> map);

    List<UserIdentity> selectOneDetailsByUserId(String id);

}
