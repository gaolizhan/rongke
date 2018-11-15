package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.model.HelpCenter;
import com.rongke.service.HelpCenterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version : Ver 1.0
 * @HelpCenterController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/helpCenter")
@Transactional
@CrossOrigin
public class HelpCenterController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private HelpCenterService helpCenterService;

    /**
     * @param helpCenter
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addNews(@RequestBody HelpCenter helpCenter) {
        log.debug("添加");
         Boolean bool=helpCenterService.insert(helpCenter);
        return JsonResp.ok(bool);
    }

    /**
     * @param helpCenter
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateHelpCenter(@RequestBody HelpCenter helpCenter) {
        log.debug("修改");
        helpCenterService.updateById(helpCenter);
        return JsonResp.ok(helpCenter);
    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonResp delete(Long id) {
        log.debug("修改");
       Boolean bool= helpCenterService.deleteById(id);
        return JsonResp.ok(bool);
    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectHelpCenter(Long id) {
        log.debug("查找");
        HelpCenter helpCenter = helpCenterService.selectById(id);
        return JsonResp.ok(helpCenter);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @帮助中心列表
     */
    @RequestMapping(value = "/selectPage1", method = RequestMethod.GET)
    public JsonResp selectPage(Page page) {
        log.debug("帮助中心列表");
        EntityWrapper<HelpCenter> helpCenterEntityWrapper=new EntityWrapper<>();
        helpCenterEntityWrapper.eq("type",1);
        Page helpCenterList = helpCenterService.selectPage(page,helpCenterEntityWrapper);
        return JsonResp.ok(helpCenterList);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @帮助中心列表
     */
    @RequestMapping(value = "/selectPage2", method = RequestMethod.GET)
    public JsonResp selectPage2(Page page) {
        log.debug("新闻中心列表");
        EntityWrapper<HelpCenter> helpCenterEntityWrapper=new EntityWrapper<>();
        helpCenterEntityWrapper.eq("type",2);
        Page helpCenterList = helpCenterService.selectPage(page,helpCenterEntityWrapper);
        return JsonResp.ok(helpCenterList);
    }


}
