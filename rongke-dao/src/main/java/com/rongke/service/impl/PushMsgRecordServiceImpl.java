package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.PushMsgRecordMapper;
import com.rongke.model.PushMsgRecord;
import com.rongke.service.PushMsgRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @PushMsgRecordServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class PushMsgRecordServiceImpl extends ServiceImpl<PushMsgRecordMapper, PushMsgRecord> implements PushMsgRecordService {
    @Autowired
    private PushMsgRecordMapper pushMsgRecordDao;
    public List<Map> selectListByUser(Long userId){
        return pushMsgRecordDao.selectListByUser(userId);
    }
}
