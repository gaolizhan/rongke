package com.rongke.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.rongke.commons.JsonResp;
import com.rongke.model.Admin;
import com.rongke.model.Channel;
import com.rongke.model.User;

import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserService
 * @用户基本信息Service
 */
public interface UserService extends IService<User> {
    JsonResp loginByPhone(String phone, String password, String phoneSign);

    void logout();

    User findLoginUser();

    JsonResp loginByUserName(String phone, String password);

    Admin getThisLogin();

    JsonResp channelLogin(String userName,String password);

    Channel  getCurentChannel();

    Boolean updateUserList(Map<String,Object>map);


    Integer selectCanLinquNum();

    List<User>selectLinquUser();

    String getChannleNameByUser(Long id);

   List<User> getBlackUserList();

    List<String> getAllPhone();

    Integer selectUserRegisterCountByBetweenDate(String startTime,String endTime,Long channelId);

    JsonResp getChannelStatistics(String startTime,String endTime,String channelId);
}
