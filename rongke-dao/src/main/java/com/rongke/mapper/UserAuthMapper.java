package com.rongke.mapper;

import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserAuthMapper
 * @Mapper
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {
    List<UserAuth> selectAll(Map<String,Object>map);

}
