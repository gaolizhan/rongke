package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserRisk;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jigang
 * @since 2018-08-24
 */
public interface UserRiskService extends IService<UserRisk> {
    String dealRegisterRisk(Long userId);

    String dealBasicInfoRisk(Long userId);

    String dealOperatorRisk(Long userId,String realPath);

    String dealIdentityRisk(Long userId);

    String dealZhimaRisk(Long userId);

    String dealBankRisk(Long userId);
}
