package com.rongke.mapper;

import com.alibaba.fastjson.JSONObject;
import com.rongke.model.Channel;
import com.rongke.model.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserMapper
 * @用户基本信息Mapper
 */
public interface UserMapper extends BaseMapper<User> {
    User selectByUserName(String userName);

    User selectByPhone(String phone);

    User selectByToken(String token);

    Boolean updateUserList(Map<String,Object> map);

    Integer selectCanLinquNum();

    List<User> selectLinquUser();

    String getChannleNameByUser(Long id);

    List<User> getBlackUserList();

    List<String> getAllPhone();

    Integer selectUserRegisterCountByBetweenDate(Map paramMap);

    Integer getCountUserData(JSONObject parameter);
}
