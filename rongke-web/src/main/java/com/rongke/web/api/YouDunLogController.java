package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.model.User;
import com.rongke.model.YouDunLog;
import com.rongke.service.UserService;
import com.rongke.service.YouDunLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @YouDunLogController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/youDunLog")
@Transactional
@CrossOrigin
public class YouDunLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private YouDunLogService youDunLogService;
    @Autowired
    private UserService userService;

    /**
     * @添加
     * @param youDunLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addYouDunLog(@RequestBody YouDunLog youDunLog){
        log.debug("添加");
        User user = userService.findLoginUser();
        youDunLog.setCreatedTime(new Date());
        youDunLog.setUserId(user.getId());
        youDunLogService.insert(youDunLog);
        return JsonResp.ok(youDunLog);
    }

    /**
     * @修改
     * @param youDunLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateYouDunLog(@RequestBody YouDunLog youDunLog){
        log.debug("修改");
        youDunLogService.updateById(youDunLog);
        return JsonResp.ok(youDunLog);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectYouDunLog(Long id){
        log.debug("查找");
        YouDunLog youDunLog = youDunLogService.selectById(id);
        return JsonResp.ok(youDunLog);
    }



}
