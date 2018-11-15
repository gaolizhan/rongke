package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.ParamSettingMapper;
import com.rongke.model.ParamSetting;
import com.rongke.service.ParamSettingService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @ParamSettingServiceImpl
 * @ServiceImpl
 */
@Service
public class ParamSettingServiceImpl extends ServiceImpl<ParamSettingMapper, ParamSetting> implements ParamSettingService {
}
