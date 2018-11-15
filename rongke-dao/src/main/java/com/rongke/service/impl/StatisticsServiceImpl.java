package com.rongke.service.impl;

import com.rongke.dto.IndexDTO;
import com.rongke.mapper.StatisticsMapper;
import com.rongke.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Override
    public IndexDTO statisticsHomeData() {
        return null;
    }
}
