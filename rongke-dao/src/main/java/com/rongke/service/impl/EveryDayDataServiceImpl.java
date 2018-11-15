package com.rongke.service.impl;

import com.rongke.mapper.ChannelMapper;
import com.rongke.mapper.EveryDayDataMapper;
import com.rongke.model.Channel;
import com.rongke.model.EveryDayData;
import com.rongke.service.EveryDayDataService;
import com.rongke.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zero
 * @date: 2018-11-06
 * @time: 19:45
 */
@Service
public class EveryDayDataServiceImpl implements EveryDayDataService {

    @Autowired
    private EveryDayDataMapper everyDayDataMapper;

    @Autowired public ChannelMapper channelMapper;

    @Override
    public void addEveryDayData(int type,long channelId) {


        EveryDayData everyDayData=everyDayDataMapper.getTodayEveryDayData(type,channelId);
        if(everyDayData!=null&&everyDayData.getId()!=0){
            //扣量逻辑
            Channel c=new Channel();
            c.setId(channelId);
            List<Channel> list = channelMapper.selectByCondition(c);
            if(list!=null&&list.size()==1){
                Channel channel=list.get(0);
                int decrementRate=channel.getDecrementRate();
                decrementRate=decrementRate>100?100:decrementRate;
                decrementRate=decrementRate<0?0:decrementRate;
                int ran = (int)(Math.random()*100+1);
                //扣量
                if(decrementRate>0 && ran<=decrementRate){
                    everyDayDataMapper.updateEveryDayDataBydecrementNum(everyDayData);
                }
                else{
                    everyDayDataMapper.updateEveryDayData(everyDayData);
                }
            }


        }
        else{
            everyDayData=new EveryDayData();
            everyDayData.setType(type);
            everyDayData.setDateTime(DateUtils.nowDateSimple());
            everyDayData.setChannelId(channelId);
            everyDayDataMapper.addEveryDayData(everyDayData);

        }
    }
}
