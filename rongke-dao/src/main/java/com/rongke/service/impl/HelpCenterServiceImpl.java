package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.HelpCenterMapper;
import com.rongke.model.HelpCenter;
import com.rongke.service.HelpCenterService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @HelpCenterServiceImpl
 * @ServiceImpl
 */
@Service
public class HelpCenterServiceImpl extends ServiceImpl<HelpCenterMapper, HelpCenter> implements HelpCenterService {
}
