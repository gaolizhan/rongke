package com.rongke.service;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserZhima;

import java.util.List;
import java.util.Map;

/**
 * @UserZhimaService
 * @芝麻信用Service
 * @version : Ver 1.0
 */
public interface UserZhimaService extends IService<UserZhima>{

    List<Map<String,Object>> zhimaList(Pagination page, Map<String, Object> map);
}
