package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;

import com.rongke.commons.PageDto;


import com.rongke.model.AppFeedback;
import com.rongke.model.User;
import com.rongke.service.AppFeedbackService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @version : Ver 1.0
 * @AppFeedbackController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/appFeedback")
@Transactional
@CrossOrigin
public class AppFeedbackController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AppFeedbackService appFeedbackService;
    @Autowired
    private UserService userService;

    /**
     * @param appFeedback
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAppFeedback(@RequestBody AppFeedback appFeedback) {
        log.debug("添加");
        User user = userService.findLoginUser();
        appFeedback.setUserId(user.getId());
        appFeedbackService.insert(appFeedback);
        return JsonResp.ok(appFeedback);
    }

    /**
     * @param appFeedback
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAppFeedback(@RequestBody AppFeedback appFeedback) {
        log.debug("修改");
        appFeedbackService.updateById(appFeedback);
        return JsonResp.ok(appFeedback);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAppFeedback(Long id) {
        log.debug("查找");
        AppFeedback appFeedback = appFeedbackService.selectById(id);
        return JsonResp.ok(appFeedback);
    }


    /**
     * @param page,type,phoneNumber
     * @return 返回值JsonResp
     * @查询所有订单
     */
    @RequestMapping(value = "/selectAllFeedBackList", method = RequestMethod.GET)
    public JsonResp selectAppFeedbackList(Page page, String type, String phoneNumber) {
        EntityWrapper<AppFeedback> appFeesdbackWrapper = new EntityWrapper<>();
        if (type != null && !"".equals(type)) {
            appFeesdbackWrapper.eq("type", type);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            appFeesdbackWrapper.eq("phone", phoneNumber);
        }

        List<AppFeedback> appFeedbackList = appFeedbackService.selectList(appFeesdbackWrapper);


        for (AppFeedback appFeedback : appFeedbackList) {
            EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
            userEntityWrapper.eq("id", appFeedback.getUserId());
            User user = userService.selectOne(userEntityWrapper);
            if (user != null) {
                appFeedback.setUser(user);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), appFeedbackList, appFeedbackList.size()));

    }

    /**
     * 删除一条订单记录
     *
     * @param id
     * @return
     */

    @RequestMapping(value = "/deleteOneAppFeedBackList", method = RequestMethod.GET)
    public JsonResp deleteOneOrderList(String id) {

        boolean bo = appFeedbackService.deleteById(id);

        return JsonResp.ok(bo);
    }


}
