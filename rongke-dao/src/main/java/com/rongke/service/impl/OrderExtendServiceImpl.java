package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.OrderExtendMapper;
import com.rongke.model.OrderExtend;
import com.rongke.service.OrderExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @OrderExtendServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class OrderExtendServiceImpl extends ServiceImpl<OrderExtendMapper, OrderExtend> implements OrderExtendService {
    @Autowired
    private OrderExtendMapper orderExtendMapper;

    @Override
    public List<OrderExtend> getPage(HashMap<String, Object> paramMap) {
        List<OrderExtend> list =orderExtendMapper.getPage(paramMap);
        return list;
    }

    @Override
    public int getPageCount(HashMap<String, Object> paramMap) {
        return orderExtendMapper.getPageCount(paramMap);
    }

    @Override
    public String getExtendTotalAmount(HashMap<String, Object> paramMap) {
        return orderExtendMapper.getExtendTotalAmount(paramMap);
    }
}
