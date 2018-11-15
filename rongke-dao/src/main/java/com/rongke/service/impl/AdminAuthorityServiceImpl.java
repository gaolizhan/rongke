package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AdminAuthorityMapper;
import com.rongke.model.AdminAuthority;
import com.rongke.service.AdminAuthorityService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @AdminAuthorityServiceImpl
 * @ServiceImpl
 */
@Service
public class AdminAuthorityServiceImpl extends ServiceImpl<AdminAuthorityMapper, AdminAuthority> implements AdminAuthorityService {
}
