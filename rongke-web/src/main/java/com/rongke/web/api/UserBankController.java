package com.rongke.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.RiskRuleNo;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.model.UserBank;
import com.rongke.model.UserIdentity;
import com.rongke.service.UserAuthService;
import com.rongke.service.UserBankService;
import com.rongke.service.UserIdentityService;
import com.rongke.service.UserRiskService;
import com.rongke.service.UserService;
import com.rongke.utils.RandomUtils;
import com.rongke.web.payutil.helipay.action.QuickPayApi;
import com.rongke.web.payutil.helipay.vo.request.AgreementBindCardValidateCodeVo;
import com.rongke.web.payutil.helipay.vo.request.QuickPayBindCardVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version : Ver 1.0
 * @UserBankController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/userBank")
@Transactional
@CrossOrigin
public class UserBankController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserRiskService userRiskService;

    /**
     * @param userBank
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserBank(@RequestBody UserBank userBank) {
        log.debug("添加");
        userBankService.insert(userBank);
        return JsonResp.ok(userBank);
    }

    /**
     * @param userBank
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserBank(@RequestBody UserBank userBank) {
        log.debug("修改");
        userBankService.updateById(userBank);
        return JsonResp.ok(userBank);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改银行卡前置
     */
    @RequestMapping(value = "/updateBankBefore", method = RequestMethod.GET)
    public JsonResp updateBankBefore() {
        log.debug("查找");
        User user = userService.findLoginUser();
        EntityWrapper<UserAuth> ew = new EntityWrapper();
        ew.eq("user_id",user.getId());
        UserAuth userAuth = userAuthService.selectOne(ew);
        if(userAuth.getBankAuth()==0){
            return JsonResp.fa("请先去认证银行卡！");
        }else{
            return JsonResp.ok();
        }
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserBank(Long id) {
        log.debug("查找");
        UserBank userBank = userBankService.selectById(id);
        return JsonResp.ok(userBank);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @当前用户绑定的银行卡
     */
    @RequestMapping(value = "/selectBankByLoginUser", method = RequestMethod.GET)
    public JsonResp selectUserBank() {
        log.debug("当前用户绑定的银行卡");
        User user = userService.findLoginUser();
        EntityWrapper<UserBank> ew = new EntityWrapper();
        ew.eq("user_id",user.getId());
        UserBank userBank = userBankService.selectOne(ew);
        return JsonResp.ok(userBank);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Integer pageNo, Integer pageSize,String realName){
        log.debug("分页查找银行卡认证信息");

        Page page = new Page(pageNo,pageSize);
        EntityWrapper<UserBank> entityWrapper = new EntityWrapper<>();
        if (!realName.equals("")) {
            entityWrapper.like("name",realName);
        }
        Page page1 = userBankService.selectPage(page,entityWrapper);

        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }

    /**
     * 根据user_id查询银行卡认证详情
     */
    @RequestMapping(value = "/selectByUserId",method = RequestMethod.GET)
    public JsonResp selectByUserId(String id){
      EntityWrapper<UserBank>userBankEntityWrapper=new EntityWrapper<>();
        userBankEntityWrapper.eq("user_id",id);
        UserBank userBank=userBankService.selectOne(userBankEntityWrapper);
        return  JsonResp.ok(userBank);


    }

//    /**
//     * @return 返回值JsonResp
//     * @合利宝鉴权银行卡  验证验证码
//     */
//    @RequestMapping(value = "/bankCardAuthConfirm", method = RequestMethod.GET)
//    public JsonResp bankCardAuthConfirm(String idcardno,String code, String requestno,String phone,String bankcardno,String name,String year,String month,String cvv2) throws Exception {
//        log.debug("鉴权绑卡短信确认----------------------------------------------------------------------------------------------");
//        User user = userService.findLoginUser();
//        System.out.println("订单号："+requestno);
//
//        QuickPayBindCardVo quickPayBindCardVo = new QuickPayBindCardVo();
//        quickPayBindCardVo.setP3_userId(user.getId()+"");
//        quickPayBindCardVo.setP4_orderId(requestno);
//        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
//        quickPayBindCardVo.setP5_timestamp(format.format(new Date()));
//        quickPayBindCardVo.setP6_payerName(name);
//        quickPayBindCardVo.setP7_idCardType("IDCARD");
//        quickPayBindCardVo.setP8_idCardNo(idcardno);
//        quickPayBindCardVo.setP9_cardNo(bankcardno);
//        quickPayBindCardVo.setP10_year(year);
//        quickPayBindCardVo.setP11_month(month);
//        quickPayBindCardVo.setP12_cvv2(cvv2);
//        quickPayBindCardVo.setP13_phone(phone);
//        quickPayBindCardVo.setP14_validateCode(code);
//        JSONObject result = QuickPayApi.quickPayBindCard(quickPayBindCardVo);
//        if ("0000".equals(result.get("rt2_retCode"))&&
//            "SUCCESS".equals(result.get("rt7_bindStatus"))) {
//            EntityWrapper<UserBank> wrapper = new EntityWrapper<>();
//            wrapper.eq("user_id", user.getId());
//            UserBank userBank = userBankService.selectOne(wrapper);
//            userBank.setBankName(String.valueOf(result.get("bankId")));
//            userBank.setBankPhone(phone);
//            userBank.setBankcardno(bankcardno);
//            userBank.setIdcardno(idcardno);
//            userBankService.updateById(userBank);
//
//            EntityWrapper<UserAuth> wrapper1 = new EntityWrapper<>();
//            wrapper1.eq("user_id", user.getId());
//            UserAuth userAuth = userAuthService.selectOne(wrapper1);
//            userAuth.setBankAuth(1);
//            //            authentication.setAuthStatus(1);
//            userAuthService.updateById(userAuth);
//            return JsonResp.ok(result);
//        }
//        return JsonResp.fa("短信校验异常");
//    }
//
//
//    /**
//     * @return 返回值JsonResp
//     * @合利宝鉴权银行卡  发送验证码
//     */
//    @RequestMapping(value = "/bankCardAuth", method = RequestMethod.GET)
//    public JsonResp bankCardAuth( String bankcardno, String name, String idcardno, String phone,String year,String month,String cvv2) throws Exception {
//        User user = userService.findLoginUser();
//        EntityWrapper<UserBank> wrapper = new EntityWrapper<>();
//        wrapper.eq("user_id", user.getId());
//        UserBank userBank = userBankService.selectOne(wrapper);
//        EntityWrapper<UserIdentity>entityWrapper=new EntityWrapper<>();
//        entityWrapper.eq("user_id",user.getId());
//        UserIdentity userIdentity=userIdentityService.selectOne(entityWrapper);
//        if (!idcardno.equals(userIdentity.getIdentityNum())){
//            return JsonResp.fa("请使用本人银行卡");
//        }
//        user.setUserName(userIdentity.getUserName());
//        userService.updateById(user);
//        userBank.setName(name);
//        userBankService.updateById(userBank);
//        AgreementBindCardValidateCodeVo agreementBindCardValidateCodeVo = new  AgreementBindCardValidateCodeVo();
//        agreementBindCardValidateCodeVo.setP3_userId(user.getId()+"");
//        agreementBindCardValidateCodeVo.setP4_orderId(System.currentTimeMillis() + RandomUtils.randomString(10));
//        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
//        agreementBindCardValidateCodeVo.setP5_timestamp(format.format(new Date()));
//        agreementBindCardValidateCodeVo.setP6_cardNo(bankcardno);
//        agreementBindCardValidateCodeVo.setP7_phone(phone);
//        agreementBindCardValidateCodeVo.setP8_idCardNo(idcardno);
//        agreementBindCardValidateCodeVo.setP9_idCardType("IDCARD");
//        agreementBindCardValidateCodeVo.setP10_payerName(name);
//        agreementBindCardValidateCodeVo.setP12_year(year);
//        agreementBindCardValidateCodeVo.setP13_month(month);
//        agreementBindCardValidateCodeVo.setP14_cvv2(cvv2);
//        JSONObject result = QuickPayApi.agreementBindCardValidateCode(agreementBindCardValidateCodeVo);
//        if ("0000".equals(result.get("rt2_retCode"))) {
//            return JsonResp.ok(result);
//        } else {
//            return JsonResp.fa(result.get("rt3_retMsg").toString());
//        }
//    }


    /**
     * @param
     * @return 返回值JsonResp
     * @易宝支付绑卡
     */
    @RequestMapping(value = "/bindingCard", method = RequestMethod.GET)
    public JsonResp bindingCard(String bankCardNo,String phone,String idCardNo,String userName,String year,String month,String  cvv2 ) {
        log.debug("合利宝支付绑卡");
        User user = userService.findLoginUser();
        System.out.print("银行卡认证用户id"+user.getId());
        if(user.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，禁止认证!!");
        }
        //判断认证银行卡与身份认证是否一致
        EntityWrapper<UserIdentity> identityEntityWrapper = new EntityWrapper<>();
        identityEntityWrapper.eq("user_id",user.getId());
        System.out.print("银行卡认证用户id"+user.getId());
        UserIdentity userIdentity = userIdentityService.selectOne(identityEntityWrapper);
        if (!idCardNo.equals(userIdentity.getIdentityNum())) {
            return JsonResp.fa("银行卡、身份证认证信息不一致");
        }
        AgreementBindCardValidateCodeVo agreementBindCardValidateCodeVo = new  AgreementBindCardValidateCodeVo();
        agreementBindCardValidateCodeVo.setP3_userId(user.getId()+"");
        agreementBindCardValidateCodeVo.setP4_orderId(System.currentTimeMillis() + RandomUtils.randomString(10));
        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
        agreementBindCardValidateCodeVo.setP5_timestamp(format.format(new Date()));
        agreementBindCardValidateCodeVo.setP6_cardNo(bankCardNo);
        agreementBindCardValidateCodeVo.setP7_phone(phone);
        agreementBindCardValidateCodeVo.setP8_idCardNo(idCardNo);
        agreementBindCardValidateCodeVo.setP9_idCardType("IDCARD");
        agreementBindCardValidateCodeVo.setP10_payerName(userName);
        agreementBindCardValidateCodeVo.setP12_year(year);
        agreementBindCardValidateCodeVo.setP13_month(month);
        agreementBindCardValidateCodeVo.setP14_cvv2(cvv2);
        agreementBindCardValidateCodeVo.setSignatureType("MD5WITHRSA");
        JSONObject result = QuickPayApi.agreementBindCardValidateCode(agreementBindCardValidateCodeVo);
        log.error("合利宝支付绑卡发送验证码========================"+result);
        if ("0000".equals(result.get("rt2_retCode"))) {
            //判断之前是否已认证
            EntityWrapper<UserBank> ewBank = new EntityWrapper();
            ewBank.eq("user_id", user.getId());
            UserBank oldUserBank = userBankService.selectOne(ewBank);
            //暂定只能绑定一张银行卡
            if(oldUserBank==null){
                UserBank userBank = new UserBank();
                userBank.setName(userName);
                userBank.setUserId(user.getId());
                userBank.setIdcardno(idCardNo);
                userBank.setBankPhone(phone);
                userBank.setBankcardno(bankCardNo);
                userBank.setGmtDatetime(new Date());
                userBank.setCardtype(result.getString("rt8_bankId"));
                userBankService.insert(userBank);
            }else{
                oldUserBank.setName(userName);
                oldUserBank.setIdcardno(idCardNo);
                oldUserBank.setBankPhone(phone);
                oldUserBank.setGmtDatetime(new Date());
                oldUserBank.setBankcardno(bankCardNo);
                oldUserBank.setCardtype(result.getString("rt8_bankId"));
                userBankService.updateById(oldUserBank);
            }
            return JsonResp.ok(result);
        }else{
            return JsonResp.fa(result.getString("rt3_retMsg"));
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @易宝支付绑卡发送验证码
     */
    @RequestMapping(value = "/sendBindingCardMsg", method = RequestMethod.GET)
    public JsonResp sendBindingCardMsg(String code,String requestno,String bankCardNo,String phone,String idCardNo,String userName,String year,String month,String  cvv2) {
        User user = userService.findLoginUser();
        if(user.getAuthStatus() == 4){
            return JsonResp.toFail("认证失败，禁止认证!!");
        }

        QuickPayBindCardVo quickPayBindCardVo = new QuickPayBindCardVo();
        quickPayBindCardVo.setP3_userId(user.getId()+"");
        quickPayBindCardVo.setP4_orderId(requestno);
        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
        quickPayBindCardVo.setP5_timestamp(format.format(new Date()));
        quickPayBindCardVo.setP6_payerName(userName);
        quickPayBindCardVo.setP7_idCardType("IDCARD");
        quickPayBindCardVo.setP8_idCardNo(idCardNo);
        quickPayBindCardVo.setP9_cardNo(bankCardNo);
        quickPayBindCardVo.setP10_year(year);
        quickPayBindCardVo.setP11_month(month);
        quickPayBindCardVo.setP12_cvv2(cvv2);
        quickPayBindCardVo.setP13_phone(phone);
        quickPayBindCardVo.setP14_validateCode(code);
        quickPayBindCardVo.setSignatureType("MD5WITHRSA");
        JSONObject result = QuickPayApi.quickPayBindCard(quickPayBindCardVo);
        log.error("合利宝验证验证码结果========================"+result);
        if ("0000".equals(result.get("rt2_retCode"))&&
            "SUCCESS".equals(result.get("rt7_bindStatus"))) {
            //修改用户认证状态
            EntityWrapper<UserAuth> ew = new EntityWrapper();
            ew.eq("user_id",user.getId());
            UserAuth userAuth = userAuthService.selectOne(ew);
            userAuth.setBankAuth(1);
            userAuthService.updateById(userAuth);
            user.setAuthStatus(1);
            userService.updateById(user);

            EntityWrapper<UserBank> ewBank = new EntityWrapper();
            ewBank.eq("user_id", user.getId());
            UserBank userBank = userBankService.selectOne(ewBank);
            userBank.setBindId(result.getString("rt10_bindId"));
            userBank.setCardtype(result.getString("rt8_bankId"));
            userBankService.updateById(userBank);

            //风控流程之银行卡认证
            try {
                String riskResult = userRiskService.dealBankRisk(user.getId());
                if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
                    //失败，将认证状态修改为4.认证失败
                    user.setAuthStatus(4);
                    userService.updateById(user);
                    //将该失败项状态改为3，认证失败
                    userAuth.setBankAuth(3);
                    userAuthService.updateById(userAuth);
                }
            }catch (Exception e){
                log.error(e);
            }
            return JsonResp.ok(result);
        }else{
            return JsonResp.fa(result.get("rt3_retMsg").toString());
        }
    }



}
