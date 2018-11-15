package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.PushMsgRecord;

import java.util.List;
import java.util.Map;

/**
 * @PushMsgRecordService
 * @Service
 * @version : Ver 1.0
 */
public interface PushMsgRecordService extends IService<PushMsgRecord>{
    List<Map> selectListByUser(Long userId);
}
