package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.RoleMapper;
import com.rongke.model.Role;
import com.rongke.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @RoleServiceImpl
 * @角色ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
