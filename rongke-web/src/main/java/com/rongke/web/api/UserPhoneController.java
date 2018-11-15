package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.dto.MxDTO;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.model.UserIdentity;
import com.rongke.model.UserPhone;
import com.rongke.service.*;
import com.rongke.utils.StringUtil;
import com.rongke.utils.api.MoxieApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserPhoneController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/userPhone")
@CrossOrigin
public class UserPhoneController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserIdentityService userIdentityService;
//    @Autowired
//    private ProcessCallbackResultsService processCallbackResultsService;
    @Autowired
    private ProcssMXBillNoticeResultService procssMXBillNoticeResultService;
    @Autowired
    private ProcssMXReportResultService procssMXReportResultService;
    /**
     * @param userPhone
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserPhone(@RequestBody UserPhone userPhone) {
        log.debug("添加");
        userPhoneService.insert(userPhone);
        return JsonResp.ok(userPhone);
    }

    /**
     * @param userPhone
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserPhone(@RequestBody UserPhone userPhone) {
        log.debug("修改");
        userPhoneService.updateById(userPhone);
        return JsonResp.ok(userPhone);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserPhone(Long id) {
        log.debug("查找");
        EntityWrapper<UserPhone> ew = new EntityWrapper();
        ew.eq("user_id", id);
        UserPhone userPhone = userPhoneService.selectOne(ew);
        return JsonResp.ok(userPhone);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @分页查询手机认证
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize, String phone, Integer status, String realName) {
        log.debug("分页查询手机认证");
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("phone", phone);
        map.put("realName", realName);
        map.put("status", status);
        Integer count = userPhoneService.selectCount(map);
        List<UserPhone> list = userPhoneService.selectByPage(map);

        return JsonResp.ok(new PageDto(pageNo, pageSize, list, count));
    }

    /**
     * 查询task_id
     *
     * @return
     */
    @RequestMapping(value = "/selectPhoneTaskId", method = RequestMethod.GET)
    public JsonResp selectPhoneTaskId(Long userId) {
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", userId);
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        return JsonResp.ok(userPhone);
    }

    /**
     * 运营商前置
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/phonePre", method = RequestMethod.GET)
    public JsonResp phonePre() {
        User user = userService.findLoginUser();
        if(user.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，禁止认证!!");
        }
        EntityWrapper<UserIdentity> ewIdent = new EntityWrapper<>();
        ewIdent.eq("user_id", user.getId());
        UserIdentity userIdentity = userIdentityService.selectOne(ewIdent);
        //创建phone
        EntityWrapper<UserPhone> ewPhone = new EntityWrapper<>();
        ewPhone.eq("user_id", user.getId());
        ewPhone.eq("status", 0);
        UserPhone userPhone = userPhoneService.selectOne(ewPhone);
        if (userPhone == null) {
            UserPhone newuserPhone = new UserPhone();
            newuserPhone.setRealName(userIdentity.getUserName());
            newuserPhone.setStatus(0);
            newuserPhone.setUserId(user.getId());
            newuserPhone.setPhone(user.getPhone());
            newuserPhone.setIdentityCode(userIdentity.getIdentityNum());
            userPhoneService.insert(newuserPhone);
        }

        String url = "https://api.51datakey.com/h5/importV3/index.html#/carrier?apiKey=" + MoxieApi.API_KEY + "&userId=" + user.getId() + "&backUrl=" + MoxieApi.BACK_URL +
                "&carrier_phone=" + user.getPhone() + "&carrier_idcard=" + userIdentity.getIdentityNum() + "&carrier_name=" + userIdentity.getUserName();
        Map map = new HashMap();
        map.put("url", url);
        return JsonResp.ok(map);
    }

    /**
     * 运营商报告查询状态
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/taskstatus", method = RequestMethod.GET)
    public JsonResp taskstatus() throws IOException {
        //运营商报告请求
        log.info("<--------运营商报告请求-------->");
        User user = userService.findLoginUser();
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", user.getId());
//        entityWrapper.eq("status", 0);
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if (userPhone == null) {
            return JsonResp.ok();
        }

        if (userPhone.getStatus() == 0 || userPhone.getStatus() == 1) {
            return JsonResp.ok();
        }

        EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
        ewAuth.eq("user_id", user.getId());
        UserAuth userAuth = userAuthService.selectOne(ewAuth);

        String result = MoxieApi.getMxReportStatus(userPhone.getPhone(), userPhone.getTaskId());
        if (result == null) {
            return JsonResp.ok("查询失败,请联系客服");
        }

        JSONObject json = JSONObject.parseObject(result);
        int code = json.getIntValue("code");
        log.info("<--------code-------->:"+code);
        String taskId = json.getString("task_id");

        if (200 == code) {
            // user phone 状态设置为 1 ： 已认证
            userPhone.setStatus(1);
            userPhone.setTaskId(taskId);
            this.userPhoneService.updateById(userPhone);
            // user auth 将运营商认证状态设置为 1 ： 已认证
            userAuth.setPhoneAuth(1);
            this.userAuthService.updateById(userAuth);
            return JsonResp.ok();
        } else if (0 == code) {
            // user phone 状态设置为 2 ： 认证中
            userPhone.setStatus(2);
            userPhone.setTaskId(taskId);
            this.userPhoneService.updateById(userPhone);
            // user auth 将运营商认证状态设置为 2 ： 认证中
            userAuth.setPhoneAuth(2);
            this.userAuthService.updateById(userAuth);
            return JsonResp.fa("查询中");
        } else if (-1 == code) {
            // user phone 状态设置为 2 ： 认证中
            userPhone.setStatus(0);
            userPhone.setTaskId(null);
            this.userPhoneService.updateById(userPhone);
            // user auth 将运营商认证状态设置为 2 ： 认证中
            userAuth.setPhoneAuth(0);
            this.userAuthService.updateById(userAuth);
            return JsonResp.ok("查询失败");
        }
        return JsonResp.ok("查询失败,请联系客服");
    }

    /**
     * 魔蝎任务创建通知
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/notifyByMxTaskLogin", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public JsonResp notifyByMxTaskLogin(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
        log.info("-------------- 魔蝎任务登陆通知 --------------");
        log.info("-------- mobile : " + dto.getMobile());
        log.info("-------- user_id : " + dto.getUser_id());
        log.info("-------- task_id : " + dto.getTask_id());
        log.info("-------- timestamp : " + dto.getTimestamp());
        log.info("-------- result : " + dto.getResult());
        log.info("-------- message : " + dto.getMessage());
        log.info("-------------- notifyByMxTaskLogin step0 --------------");
        if ("true".equals(dto.getResult()) || StringUtil.isEmpty(dto.getResult())||"null".equals(dto.getResult())) {
            // user phone 状态设置为 2 ： 认证中，并保存taskid
            EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_id", dto.getUser_id());
            entityWrapper.eq("status", 0);
            UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
            userPhone.setTaskId(dto.getTask_id());
            userPhone.setStatus(2);
            log.info("-------------- notifyByMxTaskLogin step1 --------------");
            this.userPhoneService.updateById(userPhone);
            log.info("-------------- notifyByMxTaskLogin step2 --------------");
            // user auth 将运营商认证状态设置为 2 ： 认证中
            EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
            ewAuth.eq("user_id", dto.getUser_id());
            log.info("-------------- notifyByMxTaskLogin step3 --------------");
            UserAuth userAuth = userAuthService.selectOne(ewAuth);
            userAuth.setPhoneAuth(2);
            this.userAuthService.updateById(userAuth);
            log.info("-------------- notifyByMxTaskLogin step4 --------------");
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        log.info("-------------- notifyByMxTaskLogin step5 --------------");
        return JsonResp.ok();
    }

    /**
     * 魔蝎任务结果通知
     *
     * @return
     */
//    @RequestMapping(value = "/notifyByMxTaskReportResult", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
//    public JsonResp notifyByMxTaskReportResult(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
//        log.info("-------------- 魔蝎任务报告结果通知 --------------");
//        log.info("-------- mobile : " + dto.getMobile());
//        log.info("-------- name : " + dto.getName());
//        log.info("-------- idcard : " + dto.getIdcard());
//        log.info("-------- user_id : " + dto.getUser_id());
//        log.info("-------- task_id : " + dto.getTask_id());
//        log.info("-------- timestamp : " + dto.getTimestamp());
////        log.info("-------- result : " + dto.getResult());
//        log.info("-------- message : " + dto.getMessage());
//        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
//        entityWrapper.eq("user_id", dto.getUser_id());
//        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
//        if(userPhone != null){
//            userPhone.setMxDto(dto.toString());
//            userPhoneService.updateById(userPhone);
//            if ("true".equals(dto.getResult())) {
//                processCallbackResultsService.MxTaskReportProcessor(dto);
//            }
//        }
//        response.setStatus(HttpServletResponse.SC_CREATED);
//        return JsonResp.ok();
//    }

    /**
     * 魔蝎任务创建通知
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/notifyByMxTaskCreate", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public JsonResp notifyByMxTaskCreate(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
        log.info("-------------- notifyByMxTaskCreate --------------");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return JsonResp.ok();
    }


    /**
     * 魔蝎任务授权登录结果
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/notifyByMxTaskAuthLoginResult", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public JsonResp notifyByMxTaskAuthLoginResult(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
        log.info("-------------- notifyByMxTaskAuthLoginResult --------------");
        log.info("-------- mobile : " + dto.getMobile());
        log.info("-------- user_id : " + dto.getUser_id());
        log.info("-------- task_id : " + dto.getTask_id());
        log.info("-------- timestamp : " + dto.getTimestamp());
        log.info("-------- result : " + dto.getResult());
        log.info("-------- message : " + dto.getMessage());
        if ("true".equals(dto.getResult()) || StringUtil.isEmpty(dto.getResult())||"null".equals(dto.getResult())) {
            // user phone 状态设置为 2 ： 认证中，并保存taskid
            EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_id", dto.getUser_id());
            entityWrapper.eq("status", 0);
            UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
            userPhone.setTaskId(dto.getTask_id());
            userPhone.setStatus(2);
            boolean effrows = this.userPhoneService.updateById(userPhone);
            if(effrows){
                log.info("保存taskId|修改状态为认证中 SUCCESS");
            }else{
                log.info("保存taskId|修改状态为认证中 FAIL");
            }
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        return JsonResp.ok();
    }


    /**
     * 魔蝎账单通知
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/notifyByBillNotice", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public JsonResp notifyByBillNotice(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
        log.info("-------------- notifyByBillNotice --------------");
        log.info("-------- mobile : " + dto.getMobile());
        log.info("-------- user_id : " + dto.getUser_id());
        log.info("-------- task_id : " + dto.getTask_id());
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", dto.getUser_id());
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if(userPhone != null){
//            userPhone.setMxDto(dto.toString());
//            userPhoneService.updateById(userPhone);
//            if ("true".equals(dto.getResult())) {
//                processCallbackResultsService.MxTaskReportProcessor(dto);
//            }
            procssMXBillNoticeResultService.mxBillNoticeResultProcess(dto);
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        return JsonResp.ok();
    }


    /**
     * 魔蝎资信报告通知
     *
     * @return
     */
    @Transactional
    @RequestMapping(value = "/notifyByMxTaskReportResult", method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public JsonResp notifyByMxTaskReportResult(HttpServletResponse response, @RequestBody MxDTO dto) throws IOException {
        log.info("-------------- notifyByMxTaskReportResult --------------");
        log.info("-------- mobile : " + dto.getMobile());
        log.info("-------- user_id : " + dto.getUser_id());
        log.info("-------- task_id : " + dto.getTask_id());
        log.info("-------- timestamp : " + dto.getTimestamp());
        log.info("-------- result : " + dto.getResult());
        log.info("-------- message : " + dto.getMessage());
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", dto.getUser_id());
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if(userPhone != null){
            userPhone.setMxDto(dto.toString());
            userPhoneService.updateById(userPhone);
            if ("true".equals(dto.getResult())) {
//                processCallbackResultsService.MxTaskReportProcessor(dto);
                procssMXReportResultService.mxBillReportResultProcess(dto);
            }
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        return JsonResp.ok();
    }


}
