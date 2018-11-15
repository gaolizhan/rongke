package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.RollingNewsMapper;
import com.rongke.model.RollingNews;
import com.rongke.service.RollingNewsService;
import org.springframework.stereotype.Service;

/**
 * @RollingNewsServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class RollingNewsServiceImpl extends ServiceImpl<RollingNewsMapper, RollingNews> implements RollingNewsService {
}
