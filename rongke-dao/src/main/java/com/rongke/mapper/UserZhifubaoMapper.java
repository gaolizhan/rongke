package com.rongke.mapper;

import com.rongke.model.UserZhifubao;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @UserZhifubaoMapper
 * @Mapper
 * @version : Ver 1.0
 */
public interface UserZhifubaoMapper extends BaseMapper<UserZhifubao>{
   List<UserZhifubao>selectAll(Map<String,Object>map);
   Integer selectCountNum(Map<String,Object>map);
   Integer selectMyCountNum(Map<String,Object>map);
   List<UserZhifubao>selectMyAll(Map<String,Object>map);
   Integer selectMyTemporaryNum(Map<String,Object>map);
   List<UserZhifubao> selectMyTemporaryAll(Map<String,Object> map);
   List<UserZhifubao> selectAllTemporary(Map<String,Object> map);
   Integer selectAllTemporaryNum(Map<String,Object>map);

}
