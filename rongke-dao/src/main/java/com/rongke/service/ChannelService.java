package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.Channel;

import java.util.List;

/**
 * @ChannelService
 * @Service
 * @version : Ver 1.0
 */
public interface ChannelService extends IService<Channel>{

    List<Channel> selectByCondition(Channel channel);

    List<Channel> selectAllChannelInfo();
}
