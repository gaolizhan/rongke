package com.rongke.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface RiskService {
    public void runRiskRule(String loadOrderId);
}
