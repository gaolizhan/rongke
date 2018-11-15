package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.UserTabaoGoods;
import com.rongke.service.UserTabaoGoodsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @UserTabaoGoodsController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userTabaoGoods")
@Transactional
@CrossOrigin
public class UserTabaoGoodsController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserTabaoGoodsService userTabaoGoodsService;

    /**
     * @添加
     * @param userTabaoGoods
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserTabaoGoods(@RequestBody UserTabaoGoods userTabaoGoods){
        log.debug("添加");
        userTabaoGoodsService.insert(userTabaoGoods);
        return JsonResp.ok(userTabaoGoods);
    }

    /**
     * @修改
     * @param userTabaoGoods
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserTabaoGoods(@RequestBody UserTabaoGoods userTabaoGoods){
        log.debug("修改");
        userTabaoGoodsService.updateById(userTabaoGoods);
        return JsonResp.ok(userTabaoGoods);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserTabaoGoods(Long id){
        log.debug("查找");
        UserTabaoGoods userTabaoGoods = userTabaoGoodsService.selectById(id);
        return JsonResp.ok(userTabaoGoods);
    }

    /**
     * @分页查询淘宝认证购买记录
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByGoodsPage", method = RequestMethod.GET)
    public JsonResp findByAddressPage(Integer pageNo, Integer pageSize,Long userId){
        log.debug("分页查询淘宝认证购买记录");
        EntityWrapper<UserTabaoGoods> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        Page page = userTabaoGoodsService.selectPage(new Page(pageNo,pageSize), wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));
    }


}
