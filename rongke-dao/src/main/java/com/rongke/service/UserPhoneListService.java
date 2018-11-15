package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.UserPhoneList;

import java.util.List;
import java.util.Map;

/**
 * @UserPhoneListService
 * @Service
 * @version : Ver 1.0
 */
public interface UserPhoneListService extends IService<UserPhoneList>{
    void saveCall(List<UserPhoneList> userPhoneLists);
    List<UserPhoneList>selectUserPhoneList(Map<String,Object> map);
    Integer selectUserPhoneNum(Map<String,Object>map);

    List<String> getPhoneListByUserId(Long userId);
}
