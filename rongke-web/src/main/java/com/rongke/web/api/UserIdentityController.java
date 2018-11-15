package com.rongke.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.RiskRuleNo;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.model.UserIdentity;
import com.rongke.model.YouDunLog;
import com.rongke.service.*;
import com.rongke.utils.Base64Picture;
import com.rongke.web.apix.NameAndBankAuth;
import com.rongke.web.apix.UDNotifyResultProcessor;
import com.rongke.web.config.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @version : Ver 1.0
 * @UserIdentityController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/userIdentity")
@Transactional
@CrossOrigin
public class UserIdentityController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    public Config config;
    @Autowired
    public UserZhimaService userZhimaService;
    @Autowired
    private YouDunLogService youDunLogService;
    @Autowired
    private UserRiskService userRiskService;

    /**
     * @param userIdentity
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserIdentity(@RequestBody UserIdentity userIdentity) throws IOException {
        log.debug("添加");
        User user = userService.findLoginUser();
        EntityWrapper<UserIdentity> ew = new EntityWrapper();
        ew.eq("user_id", user.getId());
        UserIdentity oldUserIdentity = userIdentityService.selectOne(ew);
        oldUserIdentity.setIdentityFront(userIdentity.getIdentityFront());
        oldUserIdentity.setIdentityNum(userIdentity.getIdentityNum());
        oldUserIdentity.setUserName(userIdentity.getUserName());
        oldUserIdentity.setIdentityBack(userIdentity.getIdentityBack());
        oldUserIdentity.setAddress(userIdentity.getAddress());
        oldUserIdentity.setAddressDetails(userIdentity.getAddressDetails());
        oldUserIdentity.setQqNum(userIdentity.getQqNum());
        userIdentity.setStatus(1);
        userIdentityService.updateById(oldUserIdentity);
        //修改认证状态
        EntityWrapper<UserAuth> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        UserAuth userAuth = userAuthService.selectOne(wrapper);
        userAuth.setIdentityAuth(1);
        userAuthService.updateById(userAuth);
        user.setUserName(userIdentity.getUserName());
        userService.updateById(user);
        return JsonResp.ok();
    }

    /**
     * @param userIdentity
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserIdentity(@RequestBody UserIdentity userIdentity) {
        log.debug("修改");
        userIdentityService.updateById(userIdentity);
        return JsonResp.ok(userIdentity);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserIdentity(Long id) {
        log.debug("查找");
        UserIdentity userIdentity = userIdentityService.selectById(id);
        return JsonResp.ok(userIdentity);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize, String realName, Integer status, String phone) {
        log.debug("分页查询认证信息");

        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("phone", phone);
        map.put("realName", realName);
        map.put("status", status);
        Integer total = userIdentityService.selectCount(map);
        List<UserIdentity> list = userIdentityService.selectByPage(map);
        PageDto pageDto = new PageDto(pageNo, pageSize, list, total);

        return JsonResp.ok(pageDto);
    }

    /**
     * 根据id查询身份证认证信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectOneIdentity", method = RequestMethod.GET)
    public JsonResp selectOneIdentity(Long id) {

        UserIdentity userIdentity = userIdentityService.selectById(id);
        User user = userService.selectById(userIdentity.getUserId());
        userIdentity.setUser(user);

        return JsonResp.ok(userIdentity);
    }

    /**
     * 根据id查询身份证认证信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectOneDetailsByUserId", method = RequestMethod.GET)
    public JsonResp selectOneDetailsByUserId(String id) {

        List<UserIdentity> userIdentities = userIdentityService.selectOneDetailsByUserId(id);
        if(userIdentities != null && userIdentities.size()>0){
            return JsonResp.ok(userIdentities.get(0));
        }
        //  User user=userService.selectById(userIdentity.getUserId());
        //userIdentity.setUser(user);
        return JsonResp.toFail("未查询到用户信息!");
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @实名认证（同盾）
     */
    @RequestMapping(value = "/nameAuth", method = RequestMethod.GET)
    public JsonResp nameAuth(String name, String idCard) {
        User loginUser = userService.findLoginUser();
        if(loginUser.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，无法继续认证");
        }
        log.debug("实名认证（同盾）");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_number", idCard);
        params.put("account_name", name);
        //params.put("biz_code", "nbsqand");
        JSONObject jsonObject = NameAndBankAuth.invoke(params);
        Object result = jsonObject.getJSONObject("result_desc").getJSONObject("AUTHENTICATION_INFOQUERY").getJSONObject("RealNameCheck").get("realname_consistence");
        if (null == result) {
            String error = jsonObject.getJSONObject("result_desc").getJSONObject("AUTHENTICATION_INFOQUERY").getJSONObject("RealNameCheck").get("error_info").toString();
            return JsonResp.fa(error);
        } else if (result.toString().equals("0")) {
            //将真实姓名放入User表中
            User user = userService.findLoginUser();
            user.setUserName(name);
            userService.updateById(user);
            return JsonResp.ok("认证成功");
        } else {
            return JsonResp.fa("认证失败！");
        }
    }

    /**
     * 接收实名认证异步通知
     *
     * @param
     * @return 返回值JsonResp
     * @实名认证
     */
    @RequestMapping(value = "/userIdentityBack", method = RequestMethod.POST)
    public void process(HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        final String sym = request.getRequestURL().toString().split("/api/")[0];
        final String pathWeb = request.getServletContext().getRealPath("/");
        final JSONObject reqObject = UDNotifyResultProcessor.getRequestJson(request);
//        log.info("+++++++++++++++++123333333333333333333333333333333333333" + reqObject.toString());
        JSONObject respJson = new JSONObject();
        //验签
        String sign = reqObject.getString("sign");
        String sign_time = reqObject.getString("sign_time");
        String partner_order_id = reqObject.getString("partner_order_id");
        System.out.println("sign：" + sign);
        String signMD5 = UDNotifyResultProcessor.getMD5Sign(UDNotifyResultProcessor.PUB_KEY, partner_order_id, sign_time, UDNotifyResultProcessor.SECURITY_KEY);
        System.out.println("signMD5：" + signMD5);
        if (!sign.equals(signMD5)) {
            System.out.println("1234567890"+UDNotifyResultProcessor.PUB_KEY);
            System.out.println("1234567890"+UDNotifyResultProcessor.SECURITY_KEY);
            System.err.println("异步通知签名错误");
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            System.out.print("收到商户异步通知");
            respJson.put("code", "1");
            respJson.put("message", "收到通知");
            //TODO 异步执行商户自己的业务逻辑(以免阻塞返回导致通知多次)
            Thread asyncThread = new Thread("asyncDataHandlerThread") {
                public void run() {
                    System.out.println("异步执行商户自己的业务逻辑...");
                    try {
                        String auth_result = reqObject.getString("auth_result");
                        if (null != auth_result && auth_result.equals("T")) {
                            EntityWrapper<YouDunLog> entityWrapper = new EntityWrapper<>();
                            entityWrapper.eq("identity_order_no", reqObject.getString("partner_order_id"));
                            YouDunLog youDunLog = youDunLogService.selectOne(entityWrapper);
                            UserIdentity userIdentity = new UserIdentity();
                            userIdentity.setUserId(youDunLog.getUserId());
                            //userIdentity.setAddressDetails(reqObject.getString("address"));
                            userIdentity.setUserName(reqObject.getString("id_name"));
                            userIdentity.setIdentityNum(reqObject.getString("id_number"));
                            userIdentity.setStatus(1);
                            //得到解密后的base64码上传
                            String identityFrontBase64 = reqObject.getString("idcard_front_photo");
                            String identityBackBase64 = reqObject.getString("idcard_back_photo");
                            String faceUrlBase64 = reqObject.getString("living_photo");
                            for (int i = 0; i < 3; i++) {
                                String fileName = UUID.randomUUID().toString();
                                Base64Picture base64Picture = new Base64Picture();
                                if (i == 0) {
                                    String uPath = pathWeb + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    String savePath = sym + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    base64Picture.GenerateImage(identityFrontBase64, uPath);
                                    userIdentity.setIdentityFront(savePath);
                                } else if (i == 1) {
                                    String uPath = pathWeb + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    String savePath = sym + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    base64Picture.GenerateImage(identityBackBase64, uPath);
                                    userIdentity.setIdentityBack(savePath);
                                } else {
                                    String uPath = pathWeb + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    String savePath = sym + "/ATTACHMENT/facephoto/" + fileName + ".jpg";
                                    base64Picture.GenerateImage(faceUrlBase64, uPath);
                                    userIdentity.setFaceUrl(savePath);
                                }
                            }
                            userIdentityService.insert(userIdentity);
                            EntityWrapper<UserAuth> wrapper = new EntityWrapper<>();
                            wrapper.eq("user_id", youDunLog.getUserId());
                            UserAuth userAuth = userAuthService.selectOne(wrapper);
                            userAuth.setIdentityAuth(1);
                            userAuthService.updateById(userAuth);
                            User user = userService.selectById(userAuth.getUserId());
                            user.setUserName(userIdentity.getUserName());
                            userService.updateById(user);

                            //风控流程之身份认证
                            try {
                                String riskResult = userRiskService.dealIdentityRisk(user.getId());
                                if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
                                    //失败，将认证状态修改为4.认证失败
                                    user.setAuthStatus(4);
                                    userService.updateById(user);

                                    userAuth.setIdentityAuth(3);
                                    userAuthService.updateById(userAuth);
                                }
                            }catch (Exception e){
                                log.error(e);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            asyncThread.start();
        }
        System.out.println("返回结果：" + respJson.toJSONString());
        //返回结果
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getOutputStream().write(respJson.toJSONString().getBytes("UTF-8"));
    }
}
