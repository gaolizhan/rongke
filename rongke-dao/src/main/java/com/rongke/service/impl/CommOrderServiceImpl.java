package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.CommOrderMapper;
import com.rongke.model.CommOrder;
import com.rongke.service.CommOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/15.
 */
@Service
public class CommOrderServiceImpl extends ServiceImpl<CommOrderMapper, CommOrder>  implements CommOrderService{

    @Autowired
    private CommOrderMapper commOrderMapper;
}
