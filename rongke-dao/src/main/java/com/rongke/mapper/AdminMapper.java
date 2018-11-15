package com.rongke.mapper;

import com.rongke.model.Admin;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @AdminMapper
 * @管理员Mapper
 * @version : Ver 1.0
 */
public interface AdminMapper extends BaseMapper<Admin>{
    List<Admin> selectPage(Map<String,Object> map);
}
