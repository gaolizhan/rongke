package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.PayOrderConfirmMapper;
import com.rongke.model.PayOrderConfirm;
import com.rongke.service.PayOrderConfirmService;
import org.springframework.stereotype.Service;

/**
 * @PayOrderConfirmServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class PayOrderConfirmServiceImpl extends ServiceImpl<PayOrderConfirmMapper, PayOrderConfirm> implements PayOrderConfirmService {
}
