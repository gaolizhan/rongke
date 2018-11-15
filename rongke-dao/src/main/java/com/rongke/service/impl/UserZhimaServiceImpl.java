package com.rongke.service.impl;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserZhimaMapper;
import com.rongke.model.UserZhima;
import com.rongke.service.UserZhimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserZhimaServiceImpl
 * @芝麻信用ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserZhimaServiceImpl extends ServiceImpl<UserZhimaMapper, UserZhima> implements UserZhimaService {

    @Autowired
    private UserZhimaMapper userZhimaMapper;


    @Override
    public List<Map<String, Object>> zhimaList(Pagination page, Map<String, Object> map) {
        return userZhimaMapper.zhimaList(page,map);
    }
}
