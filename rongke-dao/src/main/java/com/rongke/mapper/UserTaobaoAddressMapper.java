package com.rongke.mapper;

import com.rongke.model.User;
import com.rongke.model.UserTaobaoAddress;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @UserTaobaoAddressMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface UserTaobaoAddressMapper extends BaseMapper<UserTaobaoAddress>{
    List<UserTaobaoAddress> selectByPage(Map<String,Object> map);
    Integer selectCount(Map<String,Object> map);
    List<User> selectUser(Map<String,Object> map);


}
