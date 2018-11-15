package com.rongke.mapper;

import com.alibaba.fastjson.JSONObject;
import com.rongke.model.EveryDayData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: zero
 * @date: 2018-11-06
 * @time: 19:31
 */
@Repository
public interface EveryDayDataMapper {

    int addEveryDayData(EveryDayData everyDayData);
    int updateEveryDayData(EveryDayData everyDayData);

    //扣量
    int updateEveryDayDataBydecrementNum(EveryDayData everyDayData);



    EveryDayData getTodayEveryDayData(int type,long channelId);

    JSONObject getTimeEveryDayData(JSONObject parameter);
}
