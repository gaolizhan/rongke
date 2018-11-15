package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AgreementMapper;
import com.rongke.model.Agreement;
import com.rongke.service.AgreementService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @AgreementServiceImpl
 * @ServiceImpl
 */
@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements AgreementService {
}
