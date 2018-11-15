package com.rongke.mapper;

import com.rongke.model.UserPhone;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserPhoneMapper
 * @Mapper
 */
public interface UserPhoneMapper extends BaseMapper<UserPhone> {
    List<UserPhone> selectByPage(Map<String, Object> map);

    Integer selectCount(Map<String, Object> map);
}
