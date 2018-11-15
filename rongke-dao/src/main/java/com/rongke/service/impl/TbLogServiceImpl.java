package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.TbLogMapper;
import com.rongke.model.TbLog;
import com.rongke.service.TbLogService;
import org.springframework.stereotype.Service;

/**
 * @TbLogServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class TbLogServiceImpl extends ServiceImpl<TbLogMapper, TbLog> implements TbLogService {
}
