package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.TextTemplate;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.model.UserMessage;
import com.rongke.service.TextTemplateService;
import com.rongke.service.UserAuthService;
import com.rongke.service.UserMessageService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version : Ver 1.0
 * @UserMessageController
 * @用户推送消息Controller
 */
@RestController
@RequestMapping(value = "/api/userMessage")
@Transactional
@CrossOrigin
public class UserMessageController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private UserService userService;
    @Autowired
    private TextTemplateService textTemplateService;
    @Autowired
    private UserAuthService userAuthService;

    /**
     * @param userMessage
     * @return 返回值JsonResp
     * @添加用户推送消息
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserMessage(@RequestBody UserMessage userMessage) {
        log.debug("添加用户推送消息");
        userMessageService.insert(userMessage);
        return JsonResp.ok(userMessage);
    }

    /**
     * @param userMessage
     * @return 返回值JsonResp
     * @修改用户推送消息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserMessage(@RequestBody UserMessage userMessage) {
        log.debug("修改用户推送消息");
        userMessageService.updateById(userMessage);
        return JsonResp.ok(userMessage);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找用户推送消息
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserMessage(Long id) {
        log.debug("查找用户推送消息");
        UserMessage userMessage = userMessageService.selectById(id);

        UserService userService = null;
        userService.selectById(1);

        return JsonResp.ok(userMessage);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找用户推送消息
     */
    @RequestMapping(value = "/selectOne1", method = RequestMethod.GET)
    public JsonResp selectUserMessage1(Long id) {
        log.debug("查找用户推送消息");
        UserService userService = null;
        userService.selectById(1);

        return JsonResp.ok();
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @查找用户推送消息
     */
    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    public JsonResp selectByUserId() {
        log.debug("查找用户推送消息");
        User user = userService.findLoginUser();
        EntityWrapper<UserMessage> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        wrapper.orderBy("gmt_datetime", true);
        List<UserMessage> list = userMessageService.selectList(wrapper);
        return JsonResp.ok(list);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @极光推送给所有用户
     */
    @RequestMapping(value = "/sendMsgToAll", method = RequestMethod.GET)
    public JsonResp sendMsgToAll(Long id) {
        log.debug("极光推送给所有用户");
        TextTemplate textTemplate = textTemplateService.selectById(id);
        //记录储存
        EntityWrapper<User> ewUser = new EntityWrapper();
        ewUser.eq("status", 1);
        List<User> userList = userService.selectList(ewUser);
        List<UserMessage> userMessages = new ArrayList();
        for (User u : userList) {
            UserMessage userMessage = new UserMessage();
            userMessage.setUserId(u.getId());
            userMessage.setText(textTemplate.getText());
            userMessage.setTitle(textTemplate.getTitle());
            userMessages.add(userMessage);
        }
        userMessageService.insertBatch(userMessages);
        return JsonResp.ok("推送成功");
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @极光推送给特定用户
     */
    @RequestMapping(value = "/sendMsgToSome", method = RequestMethod.GET)
    public JsonResp sendMsgToSome(Long id, String userId[]) {
        log.debug("极光推送给特定用户");
        List<String> userIds = Arrays.asList(userId);
        TextTemplate textTemplate  = textTemplateService.selectById(id);
        //记录储存
        List<UserMessage> list  = new ArrayList();
        for (int i = 0; i < userIds.size(); i++) {
            UserMessage userMessage = new UserMessage();
            userMessage.setUserId(Long.parseLong(userIds.get(i)));
            userMessage.setText(textTemplate.getText());
            userMessage.setTitle(textTemplate.getTitle());
            list.add(userMessage);
        }
        userMessageService.insertBatch(list);
        return JsonResp.ok("推送成功");
    }

    /**
     * 前端查询我的全部消息
     * @return
     */
    @RequestMapping(value = "/selectMyMessage",method = RequestMethod.GET)
    public JsonResp selectMyMessage(){
      User user=userService.findLoginUser();
      EntityWrapper<UserMessage>entityWrapper=new EntityWrapper<>();
      entityWrapper.eq("user_id",user.getId());
      entityWrapper.orderBy("gmt_datetime",false);
      List<UserMessage>userMessageList=userMessageService.selectList(entityWrapper);

      return JsonResp.ok(userMessageList);
    }

    /**
     * 前端查询我的最新消息
     * @return
     */
    @RequestMapping(value = "/myNewMessage",method = RequestMethod.GET)
    public JsonResp myNewMessage(){
        User user=userService.findLoginUser();
        EntityWrapper<UserMessage>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",user.getId());
        entityWrapper.orderBy("gmt_datetime",false);
        entityWrapper.eq("status",1);
        List<UserMessage>userMessageList=userMessageService.selectList(entityWrapper);
        for (UserMessage userMessage:userMessageList){
            userMessage.setStatus(2);
            userMessageService.updateById(userMessage);
        }
        return JsonResp.ok(userMessageList);

    }


    /**
     * 前端查询我的最新消息
     * @return
     */
    @RequestMapping(value = "/applyFailMessage",method = RequestMethod.GET)
    public JsonResp applyFailMessage(){
        User user=userService.findLoginUser();
        EntityWrapper<UserAuth>userAuthEntityWrapper=new EntityWrapper<>();
        userAuthEntityWrapper.eq("user_id",user.getId());
        UserAuth userAuth=userAuthService.selectOne(userAuthEntityWrapper);
        EntityWrapper<UserMessage>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",user.getId());
        entityWrapper.orderBy("gmt_datetime",false);

        List<UserMessage>userMessageList=userMessageService.selectList(entityWrapper);
        if (userMessageList.size()<1&&userAuth.getZhifubaoAuth()==0){
            UserMessage userMessage=new UserMessage();
            userMessage.setUserId(user.getId());
            userMessage.setText("请先完成认证！");
          //  userMessageService.insert(userMessage);
            userMessageList.add(userMessage);
        }else if (userMessageList.size()<1&&userAuth.getZhifubaoAuth()==1){
            UserMessage userMessage=new UserMessage();
            userMessage.setUserId(user.getId());
            userMessage.setText("正在进行额度审核，请稍等！");
         //   userMessageService.insert(userMessage);
            userMessageList.add(userMessage);
        }
        return JsonResp.ok(userMessageList.get(0));

    }





}
