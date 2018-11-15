package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserAddressMapper;
import com.rongke.model.UserAddress;
import com.rongke.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/15.
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public boolean setDefault(Long addressId,Long userId) {
        //将改用户所有地址设置为非默认
        userAddressMapper.updateDefaultSignToFalse(userId);

        //将对应地址设置为默认
        UserAddress userAddress = new UserAddress();
        userAddress.setAddressId(addressId);
        userAddress.setDefaultSign(true);
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
        return false;
    }
}
