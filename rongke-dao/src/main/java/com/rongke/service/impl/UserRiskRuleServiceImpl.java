package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserRiskRuleMapper;
import com.rongke.model.UserRiskRule;
import com.rongke.service.UserRiskRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRiskRuleServiceImpl extends ServiceImpl<UserRiskRuleMapper, UserRiskRule> implements UserRiskRuleService {
}
