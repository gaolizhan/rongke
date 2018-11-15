package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.OrderExtend;

import java.util.HashMap;
import java.util.List;

/**
 * @OrderExtendService
 * @Service
 * @version : Ver 1.0
 */
public interface OrderExtendService extends IService<OrderExtend>{
    List<OrderExtend> getPage(HashMap<String, Object> paramMap);

    int getPageCount(HashMap<String, Object> paramMap);

    String getExtendTotalAmount(HashMap<String,Object> paramMap);
}
