package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.PushMsg;
import com.rongke.model.PushMsgRecord;
import com.rongke.model.User;
import com.rongke.service.PushMsgRecordService;
import com.rongke.service.PushMsgService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @PushMsgController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/pushMsg")
@Transactional
@CrossOrigin
public class PushMsgController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PushMsgService pushMsgService;
    @Autowired
    private UserService userService;
    @Autowired
    private PushMsgRecordService pushMsgRecordService;

    /**
     * @添加
     * @param pushMsg
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addPushMsg(@RequestBody PushMsg pushMsg){
        log.debug("添加");
        pushMsgService.insert(pushMsg);
        return JsonResp.ok(pushMsg);

    }

    /**
     * @修改
     * @param pushMsg
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updatePushMsg(@RequestBody PushMsg pushMsg){
        log.debug("修改");
        pushMsgService.updateById(pushMsg);
        return JsonResp.ok(pushMsg);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectPushMsg(Long id){
        log.debug("查找");
        PushMsg pushMsg = pushMsgService.selectById(id);
        return JsonResp.ok(pushMsg);
    }

    /**
     * @极光推送给所有用户
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/sendMsgToAll", method = RequestMethod.GET)
    public JsonResp sendMsgToAll(Long id){
        log.debug("极光推送给所有用户");
        PushMsg pushMsg = pushMsgService.selectById(id);
/*        String linkUrl = pushMsg.getLinkUrl();
        String content = linkUrl+pushMsg.getContent();*/
        if(JpushClientUtil.sendToAllIos(pushMsg.getTitle(),pushMsg.getContent(),pushMsg.getTitle(),"","")==1&&JpushClientUtil.sendToAllAndroid(pushMsg.getTitle(),pushMsg.getTitle(),pushMsg.getContent(),"")==1){
            pushMsg.setStatus(1);
            pushMsgService.updateById(pushMsg);
            //记录储存
            EntityWrapper<User> ewUser = new EntityWrapper();
            ewUser.eq("status",1);
            List<User> userList = userService.selectList(ewUser);
            List<PushMsgRecord> pushMsgRecordList = new ArrayList();
            for(User u:userList){
                PushMsgRecord pushMsgRecord = new PushMsgRecord();
                pushMsgRecord.setUserId(u.getId());
                pushMsgRecord.setPushMsgId(pushMsg.getId());
                pushMsgRecordList.add(pushMsgRecord);
            }
            pushMsgRecordService.insertBatch(pushMsgRecordList);
            return JsonResp.ok("推送成功");
        }else{
            return JsonResp.fa("推送失败");
        }
    }

    /**
     * @极光推送给特定用户
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/sendMsgToSome", method = RequestMethod.GET)
    public JsonResp sendMsgToSome(Long id,String userId[]){
        log.debug("极光推送给特定用户");
        List<String> userIds= Arrays.asList(userId);

        PushMsg pushMsg = pushMsgService.selectById(id);
/*        String linkUrl = pushMsg.getLinkUrl();
        String content = linkUrl+pushMsg.getContent();*/
        if(JpushClientUtil.sendToAliasId(userIds,pushMsg.getTitle(),pushMsg.getTitle(),pushMsg.getContent(),"")==1){
            pushMsg.setStatus(1);
            pushMsgService.updateById(pushMsg);
            //记录储存
            List<PushMsgRecord> pushMsgRecordList = new ArrayList();
            for(int i=0;i<userIds.size();i++){
                PushMsgRecord pushMsgRecord = new PushMsgRecord();
                pushMsgRecord.setUserId(Long.parseLong(userIds.get(i)));
                pushMsgRecord.setPushMsgId(pushMsg.getId());
                pushMsgRecordList.add(pushMsgRecord);
            }
            pushMsgRecordService.insertBatch(pushMsgRecordList);
            return JsonResp.ok("推送成功");
        }else{
            return JsonResp.fa("推送失败");
        }
    }

    /**
     * 查询所有的信息推送模板
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectPage",method = RequestMethod.GET)
    public JsonResp selectPage(Integer pageNo, Integer pageSize){
        EntityWrapper<PushMsg> pushMsgEntityWrapper=new EntityWrapper<>();
        pushMsgEntityWrapper.orderBy("gmt_datetime",false);
        Page page=pushMsgService.selectPage(new Page(pageNo,pageSize),pushMsgEntityWrapper);
       // Page page=pushMsgService.selectPage(new Page(pageNo,pageSize),);
        return  JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));

    }

@RequestMapping(value = "deleteOne",method = RequestMethod.GET)
    public JsonResp deleteOne(Long id){
      Boolean bool=pushMsgService.deleteById(id);
        return  JsonResp.ok(bool);
    }


}
