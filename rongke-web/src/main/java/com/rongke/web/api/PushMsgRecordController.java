package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.model.PushMsgRecord;
import com.rongke.model.User;
import com.rongke.service.PushMsgRecordService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @PushMsgRecordController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/pushMsgRecord")
@Transactional
@CrossOrigin
public class PushMsgRecordController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PushMsgRecordService pushMsgRecordService;
    @Autowired
    private UserService userService;

    /**
     * @添加
     * @param pushMsgRecord
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addPushMsgRecord(@RequestBody PushMsgRecord pushMsgRecord){
        log.debug("添加");
        pushMsgRecordService.insert(pushMsgRecord);
        return JsonResp.ok(pushMsgRecord);
    }

    /**
     * @修改
     * @param pushMsgRecord
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updatePushMsgRecord(@RequestBody PushMsgRecord pushMsgRecord){
        log.debug("修改");
        pushMsgRecordService.updateById(pushMsgRecord);
        return JsonResp.ok(pushMsgRecord);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectPushMsgRecord(Long id){
        log.debug("查找");
        PushMsgRecord pushMsgRecord = pushMsgRecordService.selectById(id);
        return JsonResp.ok(pushMsgRecord);
    }


    /**
     * @当前登录用户的推送信息列表
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectListByUser", method = RequestMethod.GET)
    public JsonResp selectListByUser(){
        log.debug("当前登录用户的推送信息列表");
        User user = userService.findLoginUser();
        List<Map> mapList = pushMsgRecordService.selectListByUser(user.getId());
        return JsonResp.ok(mapList);
    }

}
