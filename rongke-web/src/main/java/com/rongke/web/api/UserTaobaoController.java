package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.model.*;
import com.rongke.service.*;
import com.rongke.web.apix.TaoBaoAuth;
import com.rongke.web.apix.TongdunYYSAuth;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @UserTaobaoController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userTaobao")
@Transactional
@CrossOrigin
public class UserTaobaoController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserTaobaoService userTaobaoService;
    @Autowired
    private UserTaobaoAddressService userTaobaoAddressService;
    @Autowired
    private UserTabaoGoodsService userTabaoGoodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private TabaoLogService tabaoLogService;
    @Autowired
    private YYSLogService yysLogService;
    @Autowired
    private UserPhoneRecordService userPhoneRecordService;
    @Autowired
    private ZhifubaoLogService zhifubaoLogService;
    @Autowired
    private UserZhifubaoService userZhifubaoService;
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private  UserBasicMsgService userBasicMsgService;
    @Autowired
    private UserPhoneListService userPhoneListService;


    /**
     * @param userTaobao
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserTaobao(@RequestBody UserTaobao userTaobao) {
        log.debug("添加");
        userTaobaoService.insert(userTaobao);
        return JsonResp.ok(userTaobao);
    }

    /**
     * @param userTaobao
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserTaobao(@RequestBody UserTaobao userTaobao) {
        log.debug("修改");
        userTaobaoService.updateById(userTaobao);
        return JsonResp.ok(userTaobao);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserTaobao(Long id) {
        log.debug("查找");
        UserTaobao userTaobao = userTaobaoService.selectById(id);
        return JsonResp.ok(userTaobao);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @淘宝认证中
     */
    @RequestMapping(value = "/taobaoLoading", method = RequestMethod.GET)
    public JsonResp taobaoLoading(String task_id) {
        log.debug("淘宝认证中");
        User user = userService.findLoginUser();
        EntityWrapper<UserAuth> ew = new EntityWrapper();
        ew.eq("user_id", user.getId());

        //淘宝日志表
        TabaoLog tabaoLog = new TabaoLog();
        tabaoLog.setTaskId(task_id);
        tabaoLog.setUserId(user.getId());
        tabaoLogService.insert(tabaoLog);

        UserAuth userAuth = userAuthService.selectOne(ew);
        userAuth.setTaobaoAuth(1);
        userAuthService.updateById(userAuth);

        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @淘宝数据和运营商异步回调
     */
    @RequestMapping(value = "/getTaobaoAuth", method = RequestMethod.POST)
    synchronized public void getTaobaoAuth(String notify_data, HttpServletResponse response)throws IOException {
        Map<String,Object>map=new HashMap<>();
        map.put("message","回掉处理成功");
        map.put("code",0);

        log.debug("淘宝数据和运营商异步回调");
        log.info("返回码" + notify_data);
        JSONObject data = JSONObject.parseObject(notify_data);
        if (data.get("code").toString().equals("2012")) {

        } else if (data.get("code").toString().equals("0")) {
            Map<String, Object> params = new HashMap<String, Object>();
            log.info("task_id++++++++++++++" + data.get("task_id").toString());
            String task_id = data.get("task_id").toString();
            //判断类型（运营商还是淘宝）
            log.info("运营商还是淘宝" + data.getJSONObject("data").get("channel_code").toString());
            if (data.getJSONObject("data").get("channel_code").toString().equals("100000")) {
                EntityWrapper ew = new EntityWrapper();
                ew.eq("task_id", task_id);
                YYSLog yysLog = yysLogService.selectOne(ew);
                JSONObject resultJO = TongdunYYSAuth.getAll(task_id);
                if (resultJO != null) {
                    JSONObject taskData = resultJO.getJSONObject("data").getJSONObject("task_data");
                    if (taskData != null) {
                        //个人信息
                        JSONObject account_info = taskData.getJSONObject("account_info");
                        EntityWrapper<UserPhone> ewPhone = new EntityWrapper();
                        ewPhone.eq("user_id", yysLog.getUserId());
                        UserPhone userPhone = userPhoneService.selectOne(ewPhone);
                        if (null != userPhone) {
                            userPhone.setRealName(resultJO.getJSONObject("data").get("real_name").toString());
                            userPhone.setIdentityCode(resultJO.getJSONObject("data").get("identity_code").toString());
                            userPhone.setAccountBalance(account_info.getString("account_balance"));
                            userPhone.setMobileStatus(account_info.getString("mobile_status"));
                            userPhone.setNetAgeo(account_info.getString("net_age"));
                            userPhone.setNetTime(account_info.getString("net_time"));
                            userPhone.setCreditLevel(account_info.getString("credit_level"));
                        }
                        userPhoneService.updateById(userPhone);



                        //魔方数据
                        JSONObject mfResult = TongdunYYSAuth.getYYSMoFangAll(task_id);
                        if (mfResult.getString("code").toString().equals("0")){
                            String decompressed=gunzip(mfResult.getString("data"));
                            JSONObject jsonObject=JSON.parseObject(decompressed);
                            jsonObject.getJSONArray("travel_track_analysis_per_city");
                            JSONArray all_contact_detail = jsonObject.getJSONArray("all_contact_detail");
                            if (all_contact_detail.size()>0){
                                EntityWrapper<UserPhoneList>userPhoneListEntityWrapper=new EntityWrapper<>();
                                userPhoneListEntityWrapper.eq("user_id",yysLog.getUserId());
                                List<UserPhoneList>userPhoneListList=userPhoneListService.selectList(userPhoneListEntityWrapper);

                                for (UserPhoneList userPhoneList:userPhoneListList){
                                    for (int i = 0; i <all_contact_detail.size() ; i++){
                                        JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                                        if (jsonObject1.getString("contact_number").equals(userPhoneList.getPhone())){
                                            if (jsonObject1.getString("contact_area")!=null) {
                                                userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                                            }
                                            userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                                            userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                                            userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                                            userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                                            userPhoneListService.updateById(userPhoneList);
                                            all_contact_detail.remove(i);
                                        }
                                    }
                                }

                            }

                            List<UserPhoneList>userPhoneLists=new ArrayList<>();
                            for (int i = 0; i <all_contact_detail.size() ; i++) {
                                JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                                UserPhoneList userPhoneList=new UserPhoneList();
                                if (jsonObject1.getString("contact_area")!=null) {
                                    userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                                }
                                userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                                userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                                userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                                userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                                userPhoneList.setPhone(jsonObject1.getString("contact_number"));
                                userPhoneList.setUserId(yysLog.getUserId());
                                userPhoneList.setName("未知用户");
                                userPhoneLists.add(userPhoneList);
                                userPhoneListService.insert(userPhoneList);

                            }
                            // userPhoneListService.saveCall(userPhoneLists);


                        }






                        //通话记录
                        EntityWrapper<UserPhoneRecord>entityWrapper=new EntityWrapper<>();
                        entityWrapper.eq("user_id",yysLog.getUserId());
                       List<UserPhoneRecord>userPhoneRecordList=userPhoneRecordService.selectList(entityWrapper);
                       if (userPhoneRecordList==null||userPhoneRecordList.size()<100) {

                           String callInfo = taskData.getString("call_info");
                           List<String> ciList = JSON.parseArray(callInfo, String.class);
                           for (String a : ciList) {
                               List<UserPhoneRecord> userPhoneRecords = new ArrayList<>();
                               String callRecord = JSON.parseObject(a).getString("call_record");
                               List<String> callList = JSON.parseArray(callRecord, String.class);
                               for (String b : callList) {
                                   JSONObject cMap = JSON.parseObject(b);
                                   UserPhoneRecord userPhoneRecord = new UserPhoneRecord();
                                   userPhoneRecord.setStartTime(cMap.getString("call_start_time"));
                                   userPhoneRecord.setCommPlac(cMap.getString("call_address"));
                                   userPhoneRecord.setCallType(cMap.getString("call_type_name"));
                                   userPhoneRecord.setPhoneNo(cMap.getString("call_other_number"));
                                   userPhoneRecord.setConnTimes(cMap.getString("call_time"));
                                   userPhoneRecord.setCommMode(cMap.getString("call_land_type"));
                                   userPhoneRecord.setCommFee(cMap.getString("call_cost"));
                                   userPhoneRecord.setUserId(yysLog.getUserId());
                                   userPhoneRecords.add(userPhoneRecord);
                               }
//                            userPhoneRecordService.insertBatch(userPhoneRecords);
                               userPhoneRecordService.saveCall(userPhoneRecords);
                           }
                       }
                        response.setCharacterEncoding("UTF-8");
                        response.getOutputStream().write("{\"message\":\"回调处理成功\",\"code\":0 }".getBytes());
                    }
                }
            } else if (data.getJSONObject("data").get("channel_code").toString().equals("000003")) {
                //找到userid

                EntityWrapper<TabaoLog> ewTaoLog = new EntityWrapper();
                ewTaoLog.eq("task_id", task_id);
                TabaoLog tabaoLog = tabaoLogService.selectOne(ewTaoLog);

                params.put("task_id", task_id);
                JSONObject jsonObject = TaoBaoAuth.invoke(params);
                JSONObject base_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("base_info");
                EntityWrapper<UserTaobao>userTaobaoEntityWrapper=new EntityWrapper<>();
                userTaobaoEntityWrapper.eq("user_id",tabaoLog.getUserId());
                UserTaobao userTaobao1=userTaobaoService.selectOne(userTaobaoEntityWrapper);
                if (userTaobao1==null) {

                    UserTaobao userTaobao = new UserTaobao();
                    userTaobao.setUserId(tabaoLog.getUserId());
                    if (!base_info.isEmpty()) {
                        userTaobao.setGender(base_info.get("gender").toString());
                        userTaobao.setMobile(base_info.get("mobile").toString());
                        userTaobao.setName(base_info.get("name").toString());
                        userTaobao.setNickName(base_info.get("nick_name").toString());
                    }
                    userTaobaoService.insert(userTaobao);
                }
                //地址

                EntityWrapper<UserTaobaoAddress>userTaobaoAddressEntityWrapper=new EntityWrapper<>();
                userTaobaoAddressEntityWrapper.eq("user_id",tabaoLog.getUserId());
                List<UserTaobaoAddress>userTaobaoAddressList1=userTaobaoAddressService.selectList(userTaobaoAddressEntityWrapper);
                if (userTaobaoAddressList1.size()<1||userTaobaoAddressList1==null) {
                    JSONArray receiver_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("receiver_list");
                    if (!receiver_list.isEmpty()) {
                        List<UserTaobaoAddress> userTaobaoAddressList = new ArrayList();
                        for (int i = 0; i < receiver_list.size(); i++) {
                            UserTaobaoAddress userTaobaoAddress = new UserTaobaoAddress();
                            userTaobaoAddress.setUserId(tabaoLog.getUserId());
                            userTaobaoAddress.setAddress(receiver_list.getJSONObject(i).get("area").toString() + "|" + receiver_list.getJSONObject(i).get("address").toString());
                            userTaobaoAddressList.add(userTaobaoAddress);
                        }
                        userTaobaoAddressService.insertBatch(userTaobaoAddressList);
                    }
                }
                //购买记录
                EntityWrapper<UserTabaoGoods>userTabaoGoodsEntityWrapper=new EntityWrapper<>();
                userTabaoGoodsEntityWrapper.eq("user_id",tabaoLog.getUserId());
                List<UserTabaoGoods>userTabaoGoodsList1=userTabaoGoodsService.selectList(userTabaoGoodsEntityWrapper);
                if (userTabaoGoodsList1==null||userTabaoGoodsList1.size()<5) {
                    JSONArray order_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("order_list");
                    if (!order_list.isEmpty()) {
                        List<UserTabaoGoods> userTabaoGoodsList = new ArrayList();
                        for (int i = 0; i < order_list.size(); i++) {
                            UserTabaoGoods userTabaoGoods = new UserTabaoGoods();
                            userTabaoGoods.setUserId(tabaoLog.getUserId());
                            if (order_list.getJSONObject(i).get("order_shop") != null && !"".equals(order_list.getJSONObject(i).get("order_shop"))) {
                                userTabaoGoods.setOrderShop(order_list.getJSONObject(i).get("order_shop").toString());
                            }
                            userTabaoGoods.setOrderAmount(order_list.getJSONObject(i).get("order_amount").toString());
                            JSONArray product_list = order_list.getJSONObject(i).getJSONArray("product_list");
                            if (product_list.size() == 1) {
                                userTabaoGoods.setProductName(product_list.getJSONObject(0).get("product_name").toString());
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (int j = 0; j < product_list.size() - 1; j++) {
                                    sb.append(product_list.getJSONObject(j).get("product_name") + ",");
                                }
                                userTabaoGoods.setProductName(sb.toString());
                            }
                            userTabaoGoods.setOrderTime(order_list.getJSONObject(i).get("order_time").toString());
                            userTabaoGoods.setOrderStatus(order_list.getJSONObject(i).get("order_status").toString());
                            if (order_list.getJSONObject(i).get("receiver_addr") != null && !"".equals(order_list.getJSONObject(i).get("receiver_addr"))) {
                                userTabaoGoods.setReceiverAddr(order_list.getJSONObject(i).get("receiver_addr").toString());
                            }
                            userTabaoGoods.setReceiverName(order_list.getJSONObject(i).get("receiver_name").toString());
                            userTabaoGoodsList.add(userTabaoGoods);
                        }
                        userTabaoGoodsService.insertBatch(userTabaoGoodsList);
                    }
                }

                if (jsonObject.getJSONObject("data")!=null&&jsonObject.getJSONObject("data").getJSONObject("task_data")!=null&&jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("account_info")!=null){


                    JSONObject account_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("account_info");
                    System.out.println("输出支付宝信息：" + account_info.toString());
                    EntityWrapper<UserZhifubao> entityWrapper1 = new EntityWrapper<>();
                    entityWrapper1.eq("user_id", tabaoLog.getUserId());
                    UserZhifubao userZhifubao = userZhifubaoService.selectOne(entityWrapper1);
                    if (!account_info.isEmpty()&&userZhifubao!=null) {
                        if (account_info.get("account_balance")!= null&&!"".equals(account_info.get("account_balance"))) {
                            userZhifubao.setAssetsYuE(account_info.get("account_balance").toString());
                        }
                        if (account_info.get("financial_account_balance") != null&&!"".equals(account_info.get("financial_account_balance"))) {
                            userZhifubao.setAssetsYuEbao(account_info.get("financial_account_balance").toString());
                        }
                        if (account_info.get("credit_quota") != null&&!"".equals(account_info.get("credit_quota"))) {
                            userZhifubao.setHuabeiQuota(account_info.get("credit_quota").toString());
                        }
                        if (account_info.get("credit_quota") != null && account_info.get("consume_quota") != null) {
                            Integer yue = Integer.valueOf(account_info.get("credit_quota").toString()) - Integer.valueOf(account_info.get("consume_quota").toString());
                            userZhifubao.setHuabeiBalance(yue.toString());
                        }

                        if (account_info.get("jiebei_quota") != null&&!"".equals(account_info.get("jiebei_quota"))) {
                            userZhifubao.setJiebeiQuota(account_info.get("jiebei_quota").toString());
                        }
                        if (account_info.get("zhima_point") != null&&!"".equals(account_info.get("zhima_point"))) {
                            userZhifubao.setZhimaPoint(account_info.get("zhima_point").toString());
                        }
                        userZhifubaoService.updateById(userZhifubao);
                    }


            }

            //改变认证状态
                EntityWrapper<UserAuth> ew = new EntityWrapper();
                ew.eq("user_id", tabaoLog.getUserId());
                UserAuth userAuth = userAuthService.selectOne(ew);
                userAuth.setTaobaoAuth(1);
                userAuthService.updateById(userAuth);
            }
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write("{\"message\":\"回调处理成功\",\"code\":0 }".getBytes());

        } else {
            //返回错误数据码时将用认证状态改为0
            //找到userid
            String task_id = data.get("task_id").toString();
            log.info("task+++++++___________" + task_id);
            EntityWrapper<TabaoLog> ewTaoLog = new EntityWrapper();
            ewTaoLog.eq("task_id", task_id);
            TabaoLog tabaoLog = tabaoLogService.selectOne(ewTaoLog);

            EntityWrapper<ZhifubaoLog> ewZFBLog = new EntityWrapper();
            ewZFBLog.eq("task_id", task_id);
            ZhifubaoLog zhifubaoLog = zhifubaoLogService.selectOne(ewZFBLog);
            if (null != tabaoLog) {
                EntityWrapper<UserAuth> ew = new EntityWrapper();
                ew.eq("user_id", tabaoLog.getUserId());
                UserAuth userAuth = userAuthService.selectOne(ew);
                if (userAuth.getTaobaoAuth() != 1) {
                    userAuth.setTaobaoAuth(0);
                    userAuthService.updateById(userAuth);
                }
            } else if (null != zhifubaoLog) {
                //改变认证状态
                EntityWrapper<UserAuth> ew = new EntityWrapper();
                ew.eq("user_id", zhifubaoLog.getUserId());
                UserAuth userAuth = userAuthService.selectOne(ew);
                if (userAuth.getZhifubaoAuth() != 1) {
                    userAuth.setZhifubaoAuth(0);
                    userAuthService.updateById(userAuth);
                }
            } else {
                EntityWrapper<YYSLog> ewYYSLog = new EntityWrapper();
                ewYYSLog.eq("task_id", task_id);
                YYSLog yysLog = yysLogService.selectOne(ewYYSLog);
                EntityWrapper<UserAuth> ew = new EntityWrapper();
                ew.eq("user_id", yysLog.getUserId());
                UserAuth userAuth = userAuthService.selectOne(ew);
                if (userAuth.getPhoneAuth() != 1) {
                    userAuth.setPhoneAuth(0);
                    log.info("回调回调回调会叫+++++++++++++++++++++++回调回调回调会叫");
                    userAuthService.updateById(userAuth);
                }
            }

        }
    }

    @RequestMapping(value = "/tesetReturn",method = RequestMethod.GET)
     public String tesetReturn(){
        EntityWrapper<TabaoLog>entityWrapper=new EntityWrapper<>();
        List<TabaoLog>tabaoLogList=tabaoLogService.selectList(entityWrapper);
        for (TabaoLog tabaoLog1:tabaoLogList) {
            String taskId = tabaoLog1.getTaskId();
            Map<String, Object> map = new HashMap<>();
            map.put("message", "回掉处理成功");
            map.put("code", 0);

            EntityWrapper<TabaoLog> ewTaoLog = new EntityWrapper();
            ewTaoLog.eq("task_id", taskId);
            TabaoLog tabaoLog = tabaoLogService.selectOne(ewTaoLog);
            Map<String, Object> p = new HashMap<>();
            p.put("task_id", taskId);
            JSONObject jsonObject = TaoBaoAuth.invoke(p);
           /* JSONObject base_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("base_info");
            UserTaobao userTaobao = new UserTaobao();
            userTaobao.setUserId(tabaoLog.getUserId());
            if (!base_info.isEmpty()) {
                userTaobao.setGender(base_info.get("gender").toString());
                userTaobao.setMobile(base_info.get("mobile").toString());
                userTaobao.setName(base_info.get("name").toString());
                userTaobao.setNickName(base_info.get("nick_name").toString());
            }
            userTaobaoService.insert(userTaobao);*/
          /*  //地址
            JSONArray receiver_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("receiver_list");
            if (!receiver_list.isEmpty()) {
                List<UserTaobaoAddress> userTaobaoAddressList = new ArrayList();
                for (int i = 0; i < receiver_list.size(); i++) {
                    UserTaobaoAddress userTaobaoAddress = new UserTaobaoAddress();
                    userTaobaoAddress.setUserId(tabaoLog.getUserId());
                    userTaobaoAddress.setAddress(receiver_list.getJSONObject(i).get("area").toString() + "|" + receiver_list.getJSONObject(i).get("address").toString());
                    userTaobaoAddressList.add(userTaobaoAddress);
                }
                userTaobaoAddressService.insertBatch(userTaobaoAddressList);
            }*/
          /*  //购买记录
            JSONArray order_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("order_list");
            if (!order_list.isEmpty()) {
                List<UserTabaoGoods> userTabaoGoodsList = new ArrayList();
                for (int i = 0; i < order_list.size(); i++) {
                    System.out.println(i);
                    UserTabaoGoods userTabaoGoods = new UserTabaoGoods();
                    userTabaoGoods.setUserId(tabaoLog.getUserId());
                    if (order_list.getJSONObject(i).get("order_shop") != null && !"".equals(order_list.getJSONObject(i).get("order_shop"))) {
                        userTabaoGoods.setOrderShop(order_list.getJSONObject(i).get("order_shop").toString());
                    }
                    userTabaoGoods.setOrderAmount(order_list.getJSONObject(i).get("order_amount").toString());
                    JSONArray product_list = order_list.getJSONObject(i).getJSONArray("product_list");
                    if (product_list.size() == 1) {
                        userTabaoGoods.setProductName(product_list.getJSONObject(0).get("product_name").toString());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < product_list.size() - 1; j++) {
                            sb.append(product_list.getJSONObject(j).get("product_name") + ",");
                        }
                        userTabaoGoods.setProductName(sb.toString());
                    }
                    userTabaoGoods.setOrderTime(order_list.getJSONObject(i).get("order_time").toString());
                    userTabaoGoods.setOrderStatus(order_list.getJSONObject(i).get("order_status").toString());
                    if (order_list.getJSONObject(i).get("receiver_addr") != null && !"".equals(order_list.getJSONObject(i).get("receiver_addr"))) {
                        userTabaoGoods.setReceiverAddr(order_list.getJSONObject(i).get("receiver_addr").toString());
                    }
                    userTabaoGoods.setReceiverName(order_list.getJSONObject(i).get("receiver_name").toString());
                    userTabaoGoodsList.add(userTabaoGoods);
                }
                userTabaoGoodsService.insertBatch(userTabaoGoodsList);
            }*/

          if (jsonObject.getJSONObject("data")!=null&&jsonObject.getJSONObject("data").getJSONObject("task_data")!=null&&jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("account_info")!=null){


            JSONObject account_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("account_info");
            System.out.println("输出支付宝信息：" + account_info.toString());
            EntityWrapper<UserZhifubao> entityWrapper1 = new EntityWrapper<>();
            entityWrapper1.eq("user_id", tabaoLog.getUserId());
            UserZhifubao userZhifubao = userZhifubaoService.selectOne(entityWrapper1);
            if (!account_info.isEmpty()&&userZhifubao!=null) {
                if (account_info.get("account_balance")!= null&&!"".equals(account_info.get("account_balance"))) {
                    userZhifubao.setAssetsYuE(account_info.get("account_balance").toString());
                }
                if (account_info.get("financial_account_balance") != null&&!"".equals(account_info.get("financial_account_balance"))) {
                    userZhifubao.setAssetsYuEbao(account_info.get("financial_account_balance").toString());
                }
                if (account_info.get("credit_quota") != null&&!"".equals(account_info.get("credit_quota"))) {
                    userZhifubao.setHuabeiQuota(account_info.get("credit_quota").toString());
                }
                if (account_info.get("credit_quota") != null && account_info.get("consume_quota") != null) {
                    Integer yue = Integer.valueOf(account_info.get("credit_quota").toString()) - Integer.valueOf(account_info.get("consume_quota").toString());
                    userZhifubao.setHuabeiBalance(yue.toString());
                }

                if (account_info.get("jiebei_quota") != null&&!"".equals(account_info.get("jiebei_quota"))) {
                    userZhifubao.setJiebeiQuota(account_info.get("jiebei_quota").toString());
                }
                if (account_info.get("zhima_point") != null&&!"".equals(account_info.get("zhima_point"))) {
                    userZhifubao.setZhimaPoint(account_info.get("zhima_point").toString());
                }
                userZhifubaoService.updateById(userZhifubao);
            }

        }
        }

       return "" ;
}







    @RequestMapping(value = "/tesetReturnOne",method = RequestMethod.GET)
    public String tesetReturnOne(String taskId){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "回掉处理成功");
            map.put("code", 0);

            EntityWrapper<TabaoLog> ewTaoLog = new EntityWrapper();
            ewTaoLog.eq("task_id", taskId);
            TabaoLog tabaoLog = tabaoLogService.selectOne(ewTaoLog);
            Map<String, Object> p = new HashMap<>();
            p.put("task_id", taskId);
            JSONObject jsonObject = TaoBaoAuth.invoke(p);
            JSONObject base_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("base_info");
            UserTaobao userTaobao = new UserTaobao();
            userTaobao.setUserId(tabaoLog.getUserId());
            if (!base_info.isEmpty()) {
                userTaobao.setGender(base_info.get("gender").toString());
                userTaobao.setMobile(base_info.get("mobile").toString());
                userTaobao.setName(base_info.get("name").toString());
                userTaobao.setNickName(base_info.get("nick_name").toString());
            }
            userTaobaoService.insert(userTaobao);
            //地址
            JSONArray receiver_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("receiver_list");
            if (!receiver_list.isEmpty()) {
                List<UserTaobaoAddress> userTaobaoAddressList = new ArrayList();
                for (int i = 0; i < receiver_list.size(); i++) {
                    UserTaobaoAddress userTaobaoAddress = new UserTaobaoAddress();
                    userTaobaoAddress.setUserId(tabaoLog.getUserId());
                    userTaobaoAddress.setAddress(receiver_list.getJSONObject(i).get("area").toString() + "|" + receiver_list.getJSONObject(i).get("address").toString());
                    userTaobaoAddressList.add(userTaobaoAddress);
                }
                userTaobaoAddressService.insertBatch(userTaobaoAddressList);
            }
            //购买记录
            JSONArray order_list = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONArray("order_list");
            if (!order_list.isEmpty()) {
                List<UserTabaoGoods> userTabaoGoodsList = new ArrayList();
                for (int i = 0; i < order_list.size(); i++) {
                    System.out.println(i);
                    UserTabaoGoods userTabaoGoods = new UserTabaoGoods();
                    userTabaoGoods.setUserId(tabaoLog.getUserId());
                    if (order_list.getJSONObject(i).get("order_shop") != null && !"".equals(order_list.getJSONObject(i).get("order_shop"))) {
                        userTabaoGoods.setOrderShop(order_list.getJSONObject(i).get("order_shop").toString());
                    }
                    userTabaoGoods.setOrderAmount(order_list.getJSONObject(i).get("order_amount").toString());
                    JSONArray product_list = order_list.getJSONObject(i).getJSONArray("product_list");
                    if (product_list.size() == 1) {
                        userTabaoGoods.setProductName(product_list.getJSONObject(0).get("product_name").toString());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < product_list.size() - 1; j++) {
                            sb.append(product_list.getJSONObject(j).get("product_name") + ",");
                        }
                        userTabaoGoods.setProductName(sb.toString());
                    }
                    userTabaoGoods.setOrderTime(order_list.getJSONObject(i).get("order_time").toString());
                    userTabaoGoods.setOrderStatus(order_list.getJSONObject(i).get("order_status").toString());
                    if (order_list.getJSONObject(i).get("receiver_addr") != null && !"".equals(order_list.getJSONObject(i).get("receiver_addr"))) {
                        userTabaoGoods.setReceiverAddr(order_list.getJSONObject(i).get("receiver_addr").toString());
                    }
                    userTabaoGoods.setReceiverName(order_list.getJSONObject(i).get("receiver_name").toString());
                    userTabaoGoodsList.add(userTabaoGoods);
                }
                userTabaoGoodsService.insertBatch(userTabaoGoodsList);
            }

            JSONObject account_info = jsonObject.getJSONObject("data").getJSONObject("task_data").getJSONObject("account_info");
            System.out.println("输出支付宝信息：" + account_info.toString());
            EntityWrapper<UserZhifubao> entityWrapper1 = new EntityWrapper<>();
            entityWrapper1.eq("user_id", tabaoLog.getUserId());
            UserZhifubao userZhifubao = userZhifubaoService.selectOne(entityWrapper1);
            if (!account_info.isEmpty()) {
                if (account_info.get("account_balance") != null) {
                    userZhifubao.setAssetsYuE(account_info.get("account_balance").toString());
                }
                if (account_info.get("financial_account_balance") != null) {
                    userZhifubao.setAssetsYuEbao(account_info.get("financial_account_balance").toString());
                }
                if (account_info.get("credit_quota") != null) {
                    userZhifubao.setHuabeiQuota(account_info.get("credit_quota").toString());
                }
                if (account_info.get("credit_quota") != null && account_info.get("consume_quota") != null) {
                    Integer yue = Integer.valueOf(account_info.get("credit_quota").toString()) - Integer.valueOf(account_info.get("consume_quota").toString());
                    userZhifubao.setHuabeiBalance(yue.toString());
                }

                if (account_info.get("jiebei_quota") != null) {
                    userZhifubao.setJiebeiQuota(account_info.get("jiebei_quota").toString());
                }
                if (account_info.get("zhima_point") != null) {
                    userZhifubao.setZhimaPoint(account_info.get("zhima_point").toString());
                }
                userZhifubaoService.updateById(userZhifubao);
            }



        return "" ;
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @分页查询淘宝认证用户
     */
    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JsonResp findByPage(Page page, String name, String nickName) {
        log.debug("分页查询淘宝认证用户");
        EntityWrapper<UserTaobao> ew = new EntityWrapper();
        if (!StringUtils.isEmpty(name)) {
            ew.eq("name", name);
        }
        if (!StringUtils.isEmpty(nickName)) {
            ew.eq("nick_name", nickName);
        }
        ew.orderBy("gmt_datetime", false);
        Page userTaobaoList = userTaobaoService.selectPage(page, ew);
        return JsonResp.ok(userTaobaoList);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @test
     */



    @RequestMapping(value = "/testReturn", method = RequestMethod.GET)
    public JsonResp testReturn(String task_id) {
        EntityWrapper<YYSLog>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("task_id",task_id);
        YYSLog tabaoLog=yysLogService.selectOne(entityWrapper);
        EntityWrapper<UserBasicMsg>entityWrapper1=new EntityWrapper<>();
        entityWrapper1.eq("user_id",tabaoLog.getUserId());
        UserBasicMsg userBasicMsg=userBasicMsgService.selectOne(entityWrapper1);



        //魔方数据
        JSONObject mfResult = TongdunYYSAuth.getYYSMoFangAll(task_id);
        if (mfResult.getString("code").toString().equals("0")){
            String decompressed=gunzip(mfResult.getString("data"));
            JSONObject jsonObject=JSON.parseObject(decompressed);
            jsonObject.getJSONArray("travel_track_analysis_per_city");
            JSONArray all_contact_detail = jsonObject.getJSONArray("all_contact_detail");
            if (all_contact_detail.size()>0){
                EntityWrapper<UserPhoneList>userPhoneListEntityWrapper=new EntityWrapper<>();
                userPhoneListEntityWrapper.eq("user_id",tabaoLog.getUserId());
                List<UserPhoneList>userPhoneListList=userPhoneListService.selectList(userPhoneListEntityWrapper);

                for (UserPhoneList userPhoneList:userPhoneListList){
                    for (int i = 0; i <all_contact_detail.size() ; i++){
                        JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                        if (jsonObject1.getString("contact_number").equals(userPhoneList.getPhone())){
                            if (jsonObject1.getString("contact_area")!=null) {
                                userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                            }
                            userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                            userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                            userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                            userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                            userPhoneListService.updateById(userPhoneList);
                         //   all_contact_detail.remove(i);
                        }
                    }
                }

            }
/*
            List<UserPhoneList>userPhoneLists=new ArrayList<>();
            for (int i = 0; i <all_contact_detail.size() ; i++) {
                JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                UserPhoneList userPhoneList=new UserPhoneList();
                if (jsonObject1.getString("contact_area")!=null) {
                    userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                }
                userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                userPhoneList.setPhone(jsonObject1.getString("contact_number"));
                userPhoneList.setUserId(tabaoLog.getUserId());
                userPhoneList.setName("未知用户");
                userPhoneLists.add(userPhoneList);
                userPhoneListService.insert(userPhoneList);

            }*/
            // userPhoneListService.saveCall(userPhoneLists);


        }






        return JsonResp.ok();
    }




    @RequestMapping(value = "/returnPhoneList",method = RequestMethod.GET)
    public JsonResp returnPhoneList(String taskId) {
        //魔方数据
        JSONObject mfResult = TongdunYYSAuth.getYYSMoFangAll(taskId);
        if (mfResult.getString("code").toString().equals("0")) {
            String decompressed = gunzip(mfResult.getString("data"));
            JSONObject jsonObject = JSON.parseObject(decompressed);
            jsonObject.getJSONArray("travel_track_analysis_per_city");
            JSONArray all_contact_detail = jsonObject.getJSONArray("all_contact_detail");
            if (all_contact_detail.size() > 0) {
                return JsonResp.ok(all_contact_detail);
            }
        }
        return JsonResp.ok();
    }


    /**
     * 解压魔方返回数据
     * @param compressedStr
     * @return
     */
    public  String gunzip(String compressedStr){

        ByteArrayOutputStream out= new ByteArrayOutputStream();
        ByteArrayInputStream in=null;
        GZIPInputStream ginzip=null;
        byte[] compressed=null;
        String decompressed = null;
        try {
            // 对返回数据BASE64解码
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in=new ByteArrayInputStream(compressed);
            ginzip=new GZIPInputStream(in);
            // 解码后对数据gzip解压缩
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            // 最后对数据进行utf-8转码
            decompressed=out.toString("utf-8");
        } catch (IOException e) {

        } finally {

        }
        return decompressed;
    }



    @RequestMapping(value = "/testReturnBack", method = RequestMethod.GET)
    public JsonResp testReturnBack(String task_id) {
        EntityWrapper<YYSLog>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("task_id",task_id);
        YYSLog tabaoLog=yysLogService.selectOne(entityWrapper);
        EntityWrapper<UserBasicMsg>entityWrapper1=new EntityWrapper<>();
        entityWrapper1.eq("user_id",tabaoLog.getUserId());
        UserBasicMsg userBasicMsg=userBasicMsgService.selectOne(entityWrapper1);



        //魔方数据
        JSONObject mfResult = TongdunYYSAuth.getYYSMoFangAll(task_id);
        if (mfResult.getString("code").toString().equals("0")){
            String decompressed=gunzip(mfResult.getString("data"));
            JSONObject jsonObject=JSON.parseObject(decompressed);
            jsonObject.getJSONArray("travel_track_analysis_per_city");
            JSONArray all_contact_detail = jsonObject.getJSONArray("all_contact_detail");
            if (all_contact_detail.size()>0){
                EntityWrapper<UserPhoneList>userPhoneListEntityWrapper=new EntityWrapper<>();
                userPhoneListEntityWrapper.eq("user_id",tabaoLog.getUserId());
                List<UserPhoneList>userPhoneListList=userPhoneListService.selectList(userPhoneListEntityWrapper);

               for (UserPhoneList userPhoneList:userPhoneListList){
                   for (int i = 0; i <all_contact_detail.size() ; i++){
                       JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                       if (jsonObject1.getString("contact_number").equals(userPhoneList.getPhone())){
                           if (jsonObject1.getString("contact_area")!=null) {
                               userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                           }
                           userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                           userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                           userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                           userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                           userPhoneListService.updateById(userPhoneList);
                           all_contact_detail.remove(i);
                       }
                   }
               }

            }

            List<UserPhoneList>userPhoneLists=new ArrayList<>();
            for (int i = 0; i <all_contact_detail.size() ; i++) {
                JSONObject jsonObject1 = all_contact_detail.getJSONObject(i);
                UserPhoneList userPhoneList=new UserPhoneList();
                if (jsonObject1.getString("contact_area")!=null) {
                    userPhoneList.setBelongArea(jsonObject1.getString("contact_area").toString());
                }
                userPhoneList.setCallTimes(Integer.valueOf(jsonObject1.getString("call_count_6month")));
                userPhoneList.setCallTime(jsonObject1.getString("call_time_6month").toString());
                userPhoneList.setPhone(jsonObject1.getString("contact_number"));
                userPhoneList.setCallCountActive(jsonObject1.getString("call_count_active_6month").toString());
                userPhoneList.setCallCountPassive(jsonObject1.getString("call_count_passive_6month").toString());
                userPhoneList.setUserId(tabaoLog.getUserId());
                userPhoneList.setName("未知用户");
                userPhoneLists.add(userPhoneList);
                userPhoneListService.insert(userPhoneList);

            }
            // userPhoneListService.saveCall(userPhoneLists);


        }





        return JsonResp.ok();
    }


}