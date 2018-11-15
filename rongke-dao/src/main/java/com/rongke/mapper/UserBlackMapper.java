package com.rongke.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.rongke.model.UserBlack;

public interface UserBlackMapper extends BaseMapper<UserBlack> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(UserBlack record);

    UserBlack selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBlack record);

    int updateByPrimaryKey(UserBlack record);
}