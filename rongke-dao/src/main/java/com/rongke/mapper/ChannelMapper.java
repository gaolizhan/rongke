package com.rongke.mapper;

import com.rongke.model.Channel;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * @ChannelMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface ChannelMapper extends BaseMapper<Channel>{

    List<Channel>  selectByCondition(Channel channel);

    int selectLoanUserCount(Long channelId);

    List<Channel> selectAll();



}
