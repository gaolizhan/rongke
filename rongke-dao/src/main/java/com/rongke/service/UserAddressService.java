package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserAddress;

/**
 * Created by Administrator on 2018/8/15.
 */
public interface UserAddressService extends IService<UserAddress> {
    boolean setDefault(Long userAddressId,Long userId);
}
