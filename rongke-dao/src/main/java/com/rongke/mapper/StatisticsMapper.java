package com.rongke.mapper;


import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @version : Ver 1.0
 * @AgreementMapper
 * @Mapper
 */
@Repository
public interface StatisticsMapper {
    BigDecimal getLenderAmount();
}
