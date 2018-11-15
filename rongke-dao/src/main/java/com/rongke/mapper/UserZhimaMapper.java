package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.rongke.model.UserZhima;

import java.util.List;
import java.util.Map;

/**
 * @UserZhimaMapper
 * @芝麻信用Mapper
 * @version : Ver 1.0
 */
public interface UserZhimaMapper extends BaseMapper<UserZhima>{


    List<Map<String,Object>> zhimaList(Pagination page, Map<String, Object> map);

}
