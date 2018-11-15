package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.dto.MxDTO;
import com.rongke.mapper.UserPhoneMapper;
import com.rongke.model.UserAuth;
import com.rongke.model.UserPhone;
import com.rongke.service.UserAuthService;
import com.rongke.service.UserPhoneService;
import com.rongke.service.UserRiskService;
import com.rongke.service.UserService;
import com.rongke.utils.StringUtil;
import com.rongke.utils.api.MoxieApi;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @UserPhoneServiceImpl
 * @ServiceImpl
 * @version : Ver 1.0
 */
@Service
public class UserPhoneServiceImpl extends ServiceImpl<UserPhoneMapper, UserPhone> implements UserPhoneService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserRiskService userRiskService;
    @Autowired
    private UserPhoneMapper userPhoneMapper;
    //报告拉取成功
    private static final int REPORT_STATUS_SUCCESS = 1;
    //报告拉取失败
    private static final int REPORT_STATUS_FAIL = 0;
    //账单拉取成功
    private static final int BILL_STATUS_SUCCESS = 1;
    //账单拉取失败
    private static final int BILL_STATUS_FAIL = 0;
    //认证成功
    private static final int AUTH_STATUS_SUCCESS = 1;

    public List<UserPhone> selectByPage(Map<String,Object> map){

        List<UserPhone> list = userPhoneMapper.selectByPage(map);

        return list;
    }

    public Integer selectCount(Map<String,Object> map){
        Integer count = userPhoneMapper.selectCount(map);
        return count;
    }

    @Override
    public void mxBillNoticeWorker(MxDTO dto, String realPath) {
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", dto.getUser_id());
        entityWrapper.eq("status", 2);
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if(userPhone ==  null){
            log.error("认证错误,该用户不存在或状态不对"+ dto.getUser_id());
            return;
        }
        try {
            String result = MoxieApi.getMxData(userPhone.getPhone(), userPhone.getTaskId(),realPath);
            //判断获取成功还是失败
            JSONObject resultJSON = (JSONObject) JSON.parse(result);
            String resultCode = String.valueOf(resultJSON.get("status"));
            log.info("账单回调通知 resultCode:"+resultCode);
            if(!StringUtils.equals("null",resultCode)){
                log.info("账单回调通知失败 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
                return;
            }
            int reportStatus = userPhone.getReport_status();
            log.info("账单回调通知修改 reportStatus:"+reportStatus);
            userPhone.setBill_status(BILL_STATUS_SUCCESS);
            if(1 == reportStatus){
                userPhone.setStatus(AUTH_STATUS_SUCCESS);
            }
            boolean efforRows = userPhoneService.updateById(userPhone);
            log.info("账单回调通知修改 efforRows:"+efforRows);
            if(efforRows){
                log.info("账单回调通知修改成功 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
            }else{
                log.info("账单回调通知修改失败 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
                return;
            }
            //user_phone:status 认证状态
            setPhoneAuth(dto, userPhone);
        } catch (IOException e) {
            log.info("账单回调通知异常 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
            log.info(e);
        }
    }

    @Deprecated
    @Override
    public void mxCallbackRealWoker(MxDTO dto, String realPath) {
        // user phone 状态设置为 1 ： 已认证
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", dto.getUser_id());
        entityWrapper.eq("status", 2);
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if(userPhone ==  null){
            log.error("认证错误,该用户不存在或状态不对"+ dto.getUser_id());
            return;
        }
//        if(!dto.getTask_id().equals(userPhone.getTaskId())){
//            log.error("认证错误,该用户任务id不对:已存在任务id"+userPhone.getTaskId()+",错误的task_id:"+ dto.getUser_id());
//            return;
//        }
        userPhone.setTaskId(dto.getTask_id());
        userPhone.setStatus(1);
        userPhone.setMessage(dto.getMessage());
        userPhoneService.updateById(userPhone);
        // user auth 将运营商认证状态设置为 1 ： 已认证
        EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
        ewAuth.eq("user_id", dto.getUser_id());
        UserAuth userAuth = userAuthService.selectOne(ewAuth);
        userAuth.setPhoneAuth(1);
        userAuthService.updateById(userAuth);
        try {
            MoxieApi.getMxData(userPhone.getPhone(), userPhone.getTaskId(),realPath);
            MoxieApi.getMxReportData(userPhone.getPhone(), userPhone.getRealName(), userPhone.getIdentityCode(), userPhone.getTaskId(),realPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //风控流程之运营商认证
//        try {
//            User user = userService.selectById(dto.getUser_id());
//            String riskResult = userRiskService.dealOperatorRisk(user.getId(),realPath);
//            if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
//                //失败，将认证状态修改为4.认证失败
//                user.setAuthStatus(4);
//                userService.updateById(user);
//
//                userAuth.setPhoneAuth(3);
//                userAuthService.updateById(userAuth);
//            }
//        }catch (Exception e){
//            log.error(e);
//        }
    }

    @Override
    public void mxReportWorker(MxDTO dto, String realPath) {
        EntityWrapper<UserPhone> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id", dto.getUser_id());
        entityWrapper.eq("status", 2);
        UserPhone userPhone = userPhoneService.selectOne(entityWrapper);
        if(userPhone ==  null){
            log.error("认证错误,该用户不存在或状态不对"+ dto.getUser_id());
            return;
        }
        try {
            String result = MoxieApi.getMxReportData(userPhone.getPhone(), userPhone.getRealName(), userPhone.getIdentityCode(), userPhone.getTaskId(),realPath);
            //资信报告暂不判断返回状态
//            JSONObject resultJSON = (JSONObject) JSON.parse(result);
//            String resultCode = String.valueOf(resultJSON.get("status"));
//            if(StringUtil.isNotEmpty(resultCode)){
//                log.info("报告回调通知失败 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
//                return;
//            }
            int billStatus = userPhone.getBill_status();
            log.info("报告回调修改 billStatus:"+billStatus);
            userPhone.setReport_status(REPORT_STATUS_SUCCESS);
            if(1 == billStatus){
                userPhone.setStatus(AUTH_STATUS_SUCCESS);
            }
            boolean efforRows = userPhoneService.updateById(userPhone);
            log.info("报告回调修改 efforRows:"+efforRows);
            if(efforRows){
                log.info("报告回调修改成功 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
            }else{
                log.info("报告回调修改失败 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
                return;
            }
            //user_phone:status 认证状态
            setPhoneAuth(dto, userPhone);
        } catch (IOException e) {
            log.info("报告回调通知异常 user_id:"+dto.getUser_id()+"taskId:"+dto.getTask_id());
            log.info(e);
        }
    }

    private void setPhoneAuth(MxDTO dto, UserPhone userPhone) {
        int userPhoneStatus = userPhone.getStatus();
        if(1 == userPhoneStatus){
            // user auth 将运营商认证状态设置为 1 ： 已认证
            EntityWrapper<UserAuth> ewAuth = new EntityWrapper<>();
            ewAuth.eq("user_id", dto.getUser_id());
            UserAuth userAuth = userAuthService.selectOne(ewAuth);
            userAuth.setPhoneAuth(1);
            userAuthService.updateById(userAuth);
        }
    }


    public static void main(String[] args) {
        Person person = new Person();
        person.setName("test");
        person.setAge("1111");
        person.setSexy("male");
        String str = JSON.toJSONString(person);
        JSONObject resultJSON = (JSONObject) JSON.parse(str);
        System.out.println(resultJSON.get("name"));
    }

    public static class Person{

        private String name;
        private String age;
        private String sexy;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSexy() {
            return sexy;
        }

        public void setSexy(String sexy) {
            this.sexy = sexy;
        }
    }

}
