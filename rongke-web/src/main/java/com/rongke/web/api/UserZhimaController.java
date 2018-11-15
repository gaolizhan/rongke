package com.rongke.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.enums.RiskRuleNo;
import com.rongke.model.*;
import com.rongke.service.*;
import com.rongke.utils.OrderUtils;
import com.rongke.utils.StringUtil;
import com.rongke.web.apix.YiXinApix;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @version : Ver 1.0
 * @UserZhimaController
 * @芝麻信用Controller
 */
@RestController
@RequestMapping(value = "/api/userZhima")
@Transactional
@CrossOrigin
public class UserZhimaController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserZhimaService userZhimaService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserRiskService userRiskService;

    /**
     * @param userZhima
     * @return 返回值JsonResp
     * @添加芝麻信用
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserZhima(@RequestBody UserZhima userZhima) {
        log.debug("添加芝麻信用");
        userZhimaService.insert(userZhima);
        return JsonResp.ok(userZhima);
    }

    /**
     * @param userZhima
     * @return 返回值JsonResp
     * @修改芝麻信用
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserZhima(@RequestBody UserZhima userZhima) {
        log.debug("修改芝麻信用");
        userZhimaService.updateById(userZhima);
        return JsonResp.ok(userZhima);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找芝麻信用
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserZhima(Long id) {
        log.debug("查找芝麻信用");
        UserZhima userZhima = userZhimaService.selectById(id);
        return JsonResp.ok(userZhima);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id删除芝麻信用
     */
    @RequestMapping(value = "/delUserZhima", method = RequestMethod.GET)
    public JsonResp delUserZhima(Long id) {
        log.debug("删除芝麻信用");
        userZhimaService.deleteById(id);
        return JsonResp.ok();
    }

    /**
     * @param page
     * @return 返回值JsonResp
     * @根据条件查找芝麻信用
     */
    @RequestMapping(value = "/zhimaList", method = RequestMethod.POST)
    public JsonResp zhimaList(@RequestBody Page page) {
        log.debug("根据条件查找芝麻信用");
        List<Map<String, Object>> list = userZhimaService.zhimaList(page, page.getCondition());
        page.setRecords(list);
        return JsonResp.ok(page);
    }

    /**
     * @param userId
     * @return 返回值JsonResp
     * @根据userId查找芝麻信用
     */
    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    public JsonResp selectByUserId(Long userId) {
        log.debug("查找芝麻信用");
        EntityWrapper<UserZhima> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        UserZhima userZhima = userZhimaService.selectOne(ew);
        return JsonResp.ok(userZhima);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @芝麻认证第一步
     */
    @RequestMapping(value = "/zhimaAuthFirst", method = RequestMethod.GET)
    public JsonResp zhimaAuthFirst() {
        log.debug("芝麻认证第一步");
        User user = userService.findLoginUser();
        String orderId = OrderUtils.getOrderNo() + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
        EntityWrapper<UserZhima> ew = new EntityWrapper<>();
        ew.eq("user_id", user.getId());
        UserZhima userZhima = userZhimaService.selectOne(ew);
        userZhima.setTransactionId(orderId);
        userZhimaService.updateById(userZhima);
        userZhima.setOpenId(orderId);
        EntityWrapper<UserPhone> ewPhone = new EntityWrapper<>();
        ewPhone.eq("user_id", user.getId());
        ewPhone.eq("status", 1);
        UserPhone userPhone = userPhoneService.selectOne(ewPhone);
        Map map = new HashMap();
        map.put("phone", userPhone.getPhone());
        map.put("name", userPhone.getRealName());
        map.put("idCard", userPhone.getIdentityCode());
        map.put("orderId", orderId);
        return JsonResp.ok(map);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @芝麻授权回调
     */
    @RequestMapping(value = "/zhimaNotify", method = RequestMethod.POST)
    public JsonResp zhimaNotify(@RequestBody JSONObject jsonObject) throws IOException {
        log.debug("芝麻授权回调");
        log.error("-----------------------------------进入芝麻授权回调----");
        String status = jsonObject.getString("status");
        if (status.equals("success")) {
            String order_id = jsonObject.getString("order_id");
            //查询结果
            String result1 = YiXinApix.zhimaScoreSecond(order_id);
            JSONObject result = null;
            if (result1 != null) {
                result = JSONObject.parseObject(result1);
            } else {
                return JsonResp.fa("授权失败！");
            }
            if (result.getString("memo").equals("success")) {
                String zhimascore = result.getJSONObject("result").getString("zhimaScore");
                EntityWrapper<UserZhima> ewZhima = new EntityWrapper<>();
                ewZhima.eq("transaction_id", order_id);
                UserZhima userZhima = userZhimaService.selectOne(ewZhima);
                userZhima.setOpenId(order_id);
                userZhima.setScore(Integer.parseInt(zhimascore));
                userZhimaService.updateById(userZhima);
                //改变认证表
                EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
                ewAuth.eq("user_id", userZhima.getUserId());
                UserAuth userAuth = userAuthService.selectOne(ewAuth);
                userAuth.setZhimaAuth(1);
                userAuthService.updateById(userAuth);
                //风控流程之芝麻认证
                try {
                    User user = userService.selectById(userZhima.getUserId());
                    String riskResult = userRiskService.dealZhimaRisk(user.getId());
                    if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
                        //失败，将认证状态修改为4.认证失败
                        user.setAuthStatus(4);
                        userService.updateById(user);

                        userAuth.setZhimaAuth(3);
                        userAuthService.updateById(userAuth);
                    }
                }catch (Exception e){
                    log.error(e);
                }
                return JsonResp.ok(zhimascore);
            } else {
                return JsonResp.fa("授权失败！");
            }
        } else {
            return JsonResp.fa("授权失败！");
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @芝麻认证
     */
    @RequestMapping(value = "/findzhimaAuthPre", method = RequestMethod.GET)
    public JsonResp findzhimaAuthPre() throws IOException {
        log.debug("查找芝麻信用");
        User user = userService.findLoginUser();
        if(user.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，禁止认证!!");
        }
        EntityWrapper<UserZhima> ew = new EntityWrapper<>();
        ew.eq("user_id", user.getId());
        UserZhima userZhima = userZhimaService.selectOne(ew);
        EntityWrapper<UserIdentity> ewIdent = new EntityWrapper<>();
        ewIdent.eq("user_id", user.getId());
        UserIdentity userIdentity = userIdentityService.selectOne(ewIdent);
        if (userZhima == null) {
            UserZhima newUserZhima = new UserZhima();
            newUserZhima.setUserId(user.getId());
            userZhimaService.insert(newUserZhima);
            return JsonResp.fa("继续下一步");
        } else {
            JSONObject jsonObject = new JSONObject();
            //params.put("id_number", "341024199409289737");
            //params.put("account_name", "汪航");
            String orderId = OrderUtils.getOrderNo() + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
            jsonObject.put("idNo", userIdentity.getIdentityNum());
            jsonObject.put("idName", user.getUserName());
            jsonObject.put("phoneNo", user.getPhone());
            jsonObject.put("orderId", orderId);
            jsonObject.put("redirect", "1");
            jsonObject.put("customUrl", "1");
            jsonObject.put("channel", "H5");
            jsonObject.put("userId", "1");
//            jsonObject.put("partner_code", "qtEBjzc+a0AZOa1JpIBCaKv/AIho84B0");
            jsonObject.put("partner_code", YiXinApix.PARTNER_CODE);
            String result = YiXinApix.zhimaScoreFirst(jsonObject);
            if (StringUtil.isEmpty(result)) {
                return JsonResp.fa("失败");
            } else {
                if (result.contains("zhimaScore")) {
                    /*int last = result.lastIndexOf("zhimaScore");
                    String[] array = result.substring(last,result.length()-1).split("&");
                    String zhimaScore = array[1].replaceAll("[a-zA-Z]","" ).replaceAll(":","").replaceAll(";","").replaceAll(",","");*/
                    String result1 = YiXinApix.zhimaScoreSecond(userZhima.getTransactionId());
                    JSONObject jsonObject1 = JSONObject.parseObject(result1);
                    userZhima.setScore(Integer.parseInt(jsonObject1.getJSONObject("result").getString("zhimaScore")));
                    userZhimaService.updateById(userZhima);
                    EntityWrapper<UserAuth> ewAutth = new EntityWrapper<>();
                    ewAutth.eq("user_id", user.getId());
                    UserAuth userAuth = userAuthService.selectOne(ewAutth);
                    userAuth.setZhimaAuth(1);
                    userAuthService.updateById(userAuth);
                    return JsonResp.ok();
                } else {
                    return JsonResp.fa("失败");
                }
            }
        }
    }

    /**
     * @param userId
     * @return 返回值JsonResp
     * @查询用户单个芝麻分
     */
    @RequestMapping(value = "/selectZhimaByUserId", method = RequestMethod.GET)
    public JsonResp selectZhimaByUserId(Long userId) throws IOException {
        log.debug("查询用户单个芝麻分");
        EntityWrapper<UserZhima> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        UserZhima userZhima = userZhimaService.selectOne(ew);

        if (userZhima.getScore() == null || userZhima.getScore() == 0) {
            String transactionId = userZhima.getTransactionId();
            String result1 = YiXinApix.zhimaScoreSecond(transactionId);
            JSONObject result = null;
            if (result1 != null) {
                result = JSONObject.parseObject(result1);
                if (result.getString("memo").equals("success")) {
                    String zhimascore = result.getJSONObject("result").getString("zhimaScore");
                    userZhima.setOpenId(transactionId);
                    userZhima.setScore(Integer.parseInt(zhimascore));
                    userZhimaService.updateById(userZhima);
                    //改变认证表
                    EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
                    ewAuth.eq("user_id", userZhima.getUserId());
                    UserAuth userAuth = userAuthService.selectOne(ewAuth);
                    userAuth.setZhimaAuth(1);
                    userAuthService.updateById(userAuth);
                }
            }
        }
        return JsonResp.ok(userZhima.getScore());
    }


}
