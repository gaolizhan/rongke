package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.annotation.SourceAuthority;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.RiskRuleNo;
import com.rongke.enums.SourceType;
import com.rongke.model.*;
import com.rongke.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @UserBasicMsgController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/userBasicMsg")
@Transactional
@CrossOrigin
public class UserBasicMsgController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserBasicMsgService userBasicMsgService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPhoneListService userPhoneListService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private PersonRecordService personRecordService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private UserRiskService userRiskService;

    /**
     * @param userBasicMsg
     * @return 返回值JsonResp
     * @添加
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserBasicMsg(@RequestBody UserBasicMsg userBasicMsg) {
        log.debug("添加");
        User user = userService.findLoginUser();
        if(user.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，禁止认证!!");
        }
        userBasicMsg.setUserId(user.getId());

        userBasicMsgService.insert(userBasicMsg);
        //改变认证表状态
        EntityWrapper<UserAuth> ewAuth = new EntityWrapper();
        ewAuth.eq("user_id",user.getId());
        UserAuth userAuth =userAuthService.selectOne(ewAuth);
        //直接设置认证成功
        userAuth.setBaiscAuth(1);
        userAuthService.updateById(userAuth);

        UserBank userBank=new UserBank();
        userBank.setUserId(user.getId());
        userBankService.insert(userBank);

        //风控流程之基本信息认证
        try {
            String riskResult = userRiskService.dealBasicInfoRisk(user.getId());
            if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
                //失败，将认证状态修改为4.认证失败
                user.setAuthStatus(4);
                userService.updateById(user);

                userAuth.setBaiscAuth(3);
                userAuthService.updateById(userAuth);
            }
        }catch (Exception e){
            log.error(e);
        }
        return JsonResp.ok("提交成功");
    }

    /**
     * @param userBasicMsg
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json; charset=utf-8")
    public JsonResp updateUserBasicMsg(@RequestBody UserBasicMsg userBasicMsg) {
        log.debug("修改");
        userBasicMsgService.updateById(userBasicMsg);
        return JsonResp.ok(userBasicMsg);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @当前用户绑定的银行卡
     */
    @RequestMapping(value = "/selectBasicMsgByLoginUser", method = RequestMethod.GET)
    public JsonResp selectUserBank() {
        log.debug("当前用户绑定的银行卡");
        User user = userService.findLoginUser();
        EntityWrapper<UserBasicMsg> ew = new EntityWrapper();
        ew.eq("user_id", user.getId());
        UserBasicMsg userBasicMsg = userBasicMsgService.selectOne(ew);
        return JsonResp.ok(userBasicMsg);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @存储用户通讯录
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/saveUserPhoneList", method = RequestMethod.POST)
    public JsonResp saveUserPhoneList(@RequestBody List<UserPhoneList> userPhoneLists) {
        log.debug("存储用户通讯录");
        User user = userService.findLoginUser();
        //List<UserPhoneList> userPhoneListList = userPhoneLists.getUserPhoneLists();

        for (UserPhoneList u : userPhoneLists) {
            u.setUserId(user.getId());
        }
        userPhoneListService.insertBatch(userPhoneLists);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize, String phone, Integer status, String realName) {
        log.debug("分页查询认证信息");
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("phone", phone);
        map.put("realName", realName);
        map.put("status", status);
        Integer total = userBasicMsgService.selectCount(map);
        List<UserBasicMsg> userBasicMsgs = userBasicMsgService.selectByPage(map);
        PageDto pageDto = new PageDto(pageNo, pageSize, userBasicMsgs, total);

        return JsonResp.ok(pageDto);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/selectOneDetailsByUserId", method = RequestMethod.GET)
    public JsonResp selectOne(String id) {
        log.debug("查询认证信息详情");
        List<UserBasicMsg> userBasicMsgList = userBasicMsgService.selectDetailsByUserId(id);

        UserBasicMsg userBasicMsg = new UserBasicMsg();
        if(userBasicMsgList.size()>0){
            userBasicMsg = userBasicMsgList.get(0);
        }
        return JsonResp.ok(userBasicMsg);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/selectOneDetails", method = RequestMethod.GET)
    public JsonResp findByPage(String id) {
        log.debug("查询认证信息详情");

        UserBasicMsg userBasicMsg = userBasicMsgService.selectOneDetails(id);

        return JsonResp.ok(userBasicMsg);
    }
    /**
     * 更改认证信息状态
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
    public JsonResp updateStatus(Long userId, Integer status, Long id) {
        log.debug("更改认证信息状态");
        UserBasicMsg userBasicMsg = userBasicMsgService.selectById(id);
        userBasicMsg.setStatus(status);
        userBasicMsgService.updateById(userBasicMsg);
        EntityWrapper<UserAuth> wrapper = new EntityWrapper();
        wrapper.eq("user_id", userId);
        UserAuth userAuth = userAuthService.selectOne(wrapper);
        if (status == 1) {
            userAuth.setBaiscAuth(1);
        } else if (status == 2) {
            userAuth.setBaiscAuth(0);
        }
        userAuthService.updateById(userAuth);
        return JsonResp.ok();
    }
}
