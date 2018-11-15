package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.Admin;

import java.util.List;
import java.util.Map;

/**
 * @AdminService
 * @管理员Service
 * @version : Ver 1.0
 */
public interface AdminService extends IService<Admin>{
    List<Admin> selectPage(Map<String,Object> map);
}
