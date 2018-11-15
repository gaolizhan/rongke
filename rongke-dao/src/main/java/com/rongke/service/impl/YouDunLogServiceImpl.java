package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.YouDunLogMapper;
import com.rongke.model.YouDunLog;
import com.rongke.service.YouDunLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @YouDunLogServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class YouDunLogServiceImpl extends ServiceImpl<YouDunLogMapper, YouDunLog> implements YouDunLogService {

    @Autowired
    private YouDunLogMapper youDunLogMapper;

    /**
     * @根据条件查找
     * @param youDunLog
     * @return
     */
    @Override
    public List<YouDunLog> selectByCondition(YouDunLog youDunLog) {
        return youDunLogMapper.selectByCondition(youDunLog);
    }
}
