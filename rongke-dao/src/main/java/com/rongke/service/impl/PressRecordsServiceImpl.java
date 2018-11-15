package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.PressRecordsMapper;
import com.rongke.model.PressRecords;
import com.rongke.service.PressRecordsService;
import org.springframework.stereotype.Service;

/**
 * @PressRecordsServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class PressRecordsServiceImpl extends ServiceImpl<PressRecordsMapper, PressRecords> implements PressRecordsService {
}
