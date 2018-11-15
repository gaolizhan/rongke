package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.PayFeedbackMapper;
import com.rongke.model.PayFeedback;
import com.rongke.service.PayFeedbackService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @PayFeedbackServiceImpl
 * @ServiceImpl
 */
@Service
public class PayFeedbackServiceImpl extends ServiceImpl<PayFeedbackMapper, PayFeedback> implements PayFeedbackService {
}
