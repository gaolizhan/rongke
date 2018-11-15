package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AdminCatalogMapper;
import com.rongke.model.AdminCatalog;
import com.rongke.service.AdminCatalogService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @AdminCatalogServiceImpl
 * @ServiceImpl
 */
@Service
public class AdminCatalogServiceImpl extends ServiceImpl<AdminCatalogMapper, AdminCatalog> implements AdminCatalogService {
}
