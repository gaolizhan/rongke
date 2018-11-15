package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.ChannelMapper;
import com.rongke.model.Channel;
import com.rongke.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ChannelServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    /**
     * @根据条件查找
     * @param channel
     * @return
     */
    @Override
    public List<Channel> selectByCondition(Channel channel) {
        return channelMapper.selectByCondition(channel);
    }


    @Override
    public List<Channel> selectAllChannelInfo() {
        List<Channel> channels = channelMapper.selectAll();
        for (Channel channel : channels) {
            int loanUserCount = channelMapper.selectLoanUserCount(channel.getId());
            channel.setLoanUserCount(loanUserCount);
        }
        return channels;
    }
}
