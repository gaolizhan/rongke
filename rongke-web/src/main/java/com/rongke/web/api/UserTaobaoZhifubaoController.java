package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.mapper.UserTaobaoZhifubaoMapper;
import com.rongke.model.UserTaobaoZhifubao;
import com.rongke.service.UserTaobaoZhifubaoService;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.PageDto;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @UserTaobaoZhifubaoController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userTaobaoZhifubao")
@Transactional
@CrossOrigin
public class UserTaobaoZhifubaoController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserTaobaoZhifubaoService userTaobaoZhifubaoService;

    /**
     * @添加
     * @param userTaobaoZhifubao
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserTaobaoZhifubao(@RequestBody UserTaobaoZhifubao userTaobaoZhifubao){
        log.debug("添加");
        userTaobaoZhifubaoService.insert(userTaobaoZhifubao);
        return JsonResp.ok(userTaobaoZhifubao);
    }

    /**
     * @修改
     * @param userTaobaoZhifubao
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserTaobaoZhifubao(@RequestBody UserTaobaoZhifubao userTaobaoZhifubao){
        log.debug("修改");
        userTaobaoZhifubaoService.updateById(userTaobaoZhifubao);
        return JsonResp.ok(userTaobaoZhifubao);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserTaobaoZhifubao(Long id){
        log.debug("查找");
        UserTaobaoZhifubao userTaobaoZhifubao = userTaobaoZhifubaoService.selectById(id);
        return JsonResp.ok(userTaobaoZhifubao);
    }



    @RequestMapping(value="/findByUserPage", method = RequestMethod.GET)
    public JsonResp findByUserPage(Long userId,Integer pageNo,Integer pageSize ){
        log.debug("查询指定用户通讯录");
        EntityWrapper<UserTaobaoZhifubao> entityWrapper = new EntityWrapper<>();
        Page page = new Page(pageNo,pageSize);
        entityWrapper.eq("user_id",userId);
        Page page1 = userTaobaoZhifubaoService.selectPage(page,entityWrapper);

        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }

}
