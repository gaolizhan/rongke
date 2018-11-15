package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AdminMapper;
import com.rongke.model.Admin;
import com.rongke.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @AdminServiceImpl
 * @管理员ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> selectPage(Map<String,Object> map){
        return adminMapper.selectPage(map);
    }
}
