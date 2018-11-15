package com.rongke.mapper;

import com.rongke.model.YouDunLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * @YouDunLogMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface YouDunLogMapper extends BaseMapper<YouDunLog>{

    List<YouDunLog>  selectByCondition(YouDunLog youDunLog);

}
