package com.rongke.mapper;

import java.util.Map;

/**
 * Created by bin on 2017/8/18.
 */

public interface IndexMapper {

    Integer userAddCount(Map<String,Object> map);

    Integer userAllCount(Map<String,Object> map);

    Integer outMoneyUserCount(Map<String,Object> map);

    Integer badOrderUserCount(Map<String,Object> map);

    Integer orderPassUserCount(Map<String,Object> map);

    String moneyOutAll(Map<String,Object> map);

    String moneyBackAll(Map<String,Object> map);

    Integer userAddCountChannel(Map map);

    String orderOutMoneyChannel(Map map);
    String applyOrderMoneyChannel(Map map);
    String orderChannelProfitChannel(Map map);


}
