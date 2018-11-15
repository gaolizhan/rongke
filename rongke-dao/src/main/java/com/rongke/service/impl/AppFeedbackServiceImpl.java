package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AppFeedbackMapper;
import com.rongke.model.AppFeedback;
import com.rongke.service.AppFeedbackService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @AppFeedbackServiceImpl
 * @ServiceImpl
 */
@Service
public class AppFeedbackServiceImpl extends ServiceImpl<AppFeedbackMapper, AppFeedback> implements AppFeedbackService {
}
