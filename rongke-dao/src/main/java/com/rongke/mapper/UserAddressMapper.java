package com.rongke.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserAddress;
import org.springframework.stereotype.Repository;

public interface UserAddressMapper extends BaseMapper<UserAddress> {
    int updateByPrimaryKeySelective(UserAddress record);

    int updateDefaultSignToFalse(Long userId);
}