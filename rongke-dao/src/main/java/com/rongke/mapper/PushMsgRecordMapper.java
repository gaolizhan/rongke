package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.PushMsgRecord;

import java.util.List;
import java.util.Map;

/**
 * @PushMsgRecordMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface PushMsgRecordMapper extends BaseMapper<PushMsgRecord>{
    List<Map> selectListByUser(Long userId);

}
