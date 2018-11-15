package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.OrderExtend;

import java.util.HashMap;
import java.util.List;

/**
 * @OrderExtendMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface OrderExtendMapper extends BaseMapper<OrderExtend>{

    List<OrderExtend> getPage(HashMap<String, Object> paramMap);

    int getPageCount(HashMap<String, Object> paramMap);

    String getExtendTotalAmount(HashMap<String,Object> paramMap);
}
