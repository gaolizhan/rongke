package com.rongke.mapper;

import com.rongke.model.UserPhoneList;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserPhoneRecord;

import java.util.List;
import java.util.Map;

/**
 * @UserPhoneListMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface UserPhoneListMapper extends BaseMapper<UserPhoneList>{
    void saveCall(List<UserPhoneList> userPhoneLists);

    List<UserPhoneList>selectUserPhoneList(Map<String,Object>map);
    Integer selectUserPhoneNum(Map<String,Object>map);

    List<String> getPhoneListByUserId(Long userId);
}
