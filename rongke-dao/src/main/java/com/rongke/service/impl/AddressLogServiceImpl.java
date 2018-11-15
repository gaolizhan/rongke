package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.AddressLogMapper;
import com.rongke.model.AddressLog;
import com.rongke.service.AddressLogService;
import org.springframework.stereotype.Service;

/**
 * @version : Ver 1.0
 * @AddressLogServiceImpl
 * @地址信息ServiceImpl
 */
@Service
public class AddressLogServiceImpl extends ServiceImpl<AddressLogMapper, AddressLog> implements AddressLogService {
}
