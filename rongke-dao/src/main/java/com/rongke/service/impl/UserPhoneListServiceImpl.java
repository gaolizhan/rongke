package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.UserPhoneListMapper;
import com.rongke.model.UserPhoneList;
import com.rongke.service.UserPhoneListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @UserPhoneListServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserPhoneListServiceImpl extends ServiceImpl<UserPhoneListMapper, UserPhoneList> implements UserPhoneListService {
    @Autowired
    private UserPhoneListMapper userPhoneListMapper;
    public  void saveCall(List<UserPhoneList> userPhoneLists){
         userPhoneListMapper.saveCall(userPhoneLists);
    }

   public List<UserPhoneList>selectUserPhoneList(Map<String,Object> map){
        return userPhoneListMapper.selectUserPhoneList(map);
   }
    public Integer selectUserPhoneNum(Map<String,Object>map){
        return userPhoneListMapper.selectUserPhoneNum(map);
    }

    @Override
    public List<String> getPhoneListByUserId(Long userId) {
        return userPhoneListMapper.getPhoneListByUserId(userId);
    }
}
