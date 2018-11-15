package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.YouDunLog;

import java.util.List;

/**
 * @YouDunLogService
 * @Service
 * @version : Ver 1.0
 */
public interface YouDunLogService extends IService<YouDunLog>{

    List<YouDunLog> selectByCondition(YouDunLog youDunLog);
}
