package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.User;
import com.rongke.model.UserJindongAddress;

import java.util.List;
import java.util.Map;

/**
 * @UserJindongAddressMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface UserJindongAddressMapper extends BaseMapper<UserJindongAddress>{
    Integer selectCount(Map<String,Object> map);
    List<User> selectUser(Map<String,Object> map);

}
