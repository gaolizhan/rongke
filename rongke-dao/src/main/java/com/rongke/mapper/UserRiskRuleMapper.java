package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserRiskRule;

public interface UserRiskRuleMapper extends BaseMapper<UserRiskRule> {
    int deleteByPrimaryKey(String id);

    int insertSelective(UserRiskRule record);

    UserRiskRule selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRiskRule record);

    int updateByPrimaryKey(UserRiskRule record);
}