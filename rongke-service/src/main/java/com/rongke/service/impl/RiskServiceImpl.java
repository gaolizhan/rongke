package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.model.LoanOrder;
import com.rongke.model.User;
import com.rongke.model.UserBasicMsg;
import com.rongke.model.UserIdentity;
import com.rongke.model.UserPhone;
import com.rongke.model.UserPhoneList;
import com.rongke.model.UserRisk;
import com.rongke.other.PaixuApiUtil;
import com.rongke.service.LoanOrderService;
import com.rongke.service.RiskService;
import com.rongke.service.UserBasicMsgService;
import com.rongke.service.UserIdentityService;
import com.rongke.service.UserPhoneListService;
import com.rongke.service.UserPhoneService;
import com.rongke.service.UserRiskService;
import com.rongke.service.UserService;
import com.rongke.utils.api.MoxieApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RiskServiceImpl implements RiskService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserRiskService userRiskService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPhoneService userPhoneService;
//    @Autowired
//    private UserTongdunfenService userTongdunfenService;
//    @Autowired
//    private UserZhimaService userZhimaService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserBasicMsgService userBasicMsgService;
    @Autowired
    private UserPhoneListService userPhoneListService;
//    @Autowired
//    private SystemConfigService systemConfigService;

    @Override
    public void runRiskRule(String loanOrderId) {
        log.info("<--------runRiskRule------->");
        System.out.println(loanOrderId);
        LoanOrder loanOrder = this.loanOrderService.selectById(loanOrderId);
        User user = this.userService.selectById(loanOrder.getUserId());

        UserRisk userRisk = new UserRisk();
        userRisk.setUserId(user.getId());
        userRisk.setLoanOrderId(Long.valueOf(loanOrderId));
        userRisk.setCreateTime(new Date());
        BigDecimal  finalScore  = dealWithPaixu(userRisk);
        if(finalScore  == null){
            log.info("<--------获取分控失败,请重试------->");
            return;
        }
        log.info("<--------finalScore------->:"+finalScore);
        loanOrder.setTdScore(finalScore+"");
        loanOrderService.updateById(loanOrder);

        //
////        this.dealWithZhima(userRisk, user);
//        this.dealWithOtherRule(userRisk);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("idNo", userPhone.getIdentityCode());
//        jsonObject.put("idName", userPhone.getRealName());
//        jsonObject.put("mobileNo", userPhone.getPhone());
//        jsonObject.put("idType", "1");
//        String result = null;
//        try {
//            result = YiXinApix.preloan(jsonObject);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject resultJson = JSONObject.parseObject(result);
//
//        if ("success".equals(resultJson.getString("memo"))) {
//            this.dealWithPreLoan(userRisk, resultJson);
//
//            JSONObject innerResult = resultJson.getJSONObject("result");
//            JSONArray ristIterms = innerResult.getJSONArray("ristIterms");
//            JSONObject preloanValidation = innerResult.getJSONObject("preloanValidation");
//            String finalScore = preloanValidation.getString("finalScore");
//            List<UserTongdunfen> list = new ArrayList<>();
//            for (int i = 0; i < ristIterms.size(); i++) {
//                UserTongdunfen userTongdunfen = new UserTongdunfen();
//                userTongdunfen.setUserId(user.getId());
//                userTongdunfen.setScore(ristIterms.getJSONObject(i).getString("riskLevel"));
//                userTongdunfen.setOrderNumber(loanOrder.getOrderNumber());
//                userTongdunfen.setRiskName(ristIterms.getJSONObject(i).getString("itemName"));
//                list.add(userTongdunfen);
//            }
//            if (list.size() != 0) {
//                userTongdunfenService.insertBatch(list);
//            }
//            //拿到后台设置同盾的分数
//            EntityWrapper<SystemConfig> entityWrapper = new EntityWrapper<>();
//            entityWrapper.eq("config_key", "tongdunfenshu");
//            SystemConfig systemConfig = systemConfigService.selectOne(entityWrapper);
//            String tongdunscore = systemConfig.getConfigValue();
//            Integer tdS = Integer.valueOf(tongdunscore);
//            if (tdS < Integer.valueOf(loanOrder.getTdScore())) {
//                loanOrder.setOrderStatus(7);
//                loanOrderService.updateById(loanOrder);
//                user.setStatus(4);
//                user.setIsPay(0);
//                userService.updateById(user);
//            } else {
//                loanOrderService.updateById(loanOrder);
//            }

//        } else {
//        }
//        this.dealWithTeledata(userRisk, user, userPhone);

        this.userRiskService.insert(userRisk);
    }

//    /**
//     * 处理其他规则
//     *
//     * @param userRisk
//     * @return
//     */
//    private UserRisk dealWithOtherRule(UserRisk userRisk) {
//        EntityWrapper<UserIdentity> ew_id = new EntityWrapper<>();
//        ew_id.eq("user_id", userRisk.getUserId());
//        UserIdentity userIdentity = this.userIdentityService.selectOne(ew_id);
//
//        EntityWrapper<UserBasicMsg> ew_msg = new EntityWrapper<>();
//        ew_msg.eq("user_id", userRisk.getUserId());
//        UserBasicMsg userBasicMsg = this.userBasicMsgService.selectOne(ew_msg);
//
//        EntityWrapper<UserPhoneList> ew_pl = new EntityWrapper<>();
//        ew_pl.eq("user_id", userRisk.getUserId());
//        List<UserPhoneList> list_userPhoneList = this.userPhoneListService.selectList(ew_pl);
//
//        // 手机通讯录个数（去重且只包含手机号） <= 50
//        if (list_userPhoneList != null && list_userPhoneList.size() > 0) {
//            Map map = new HashMap();
//            for (UserPhoneList upl : list_userPhoneList) {
//                if (StringUtil.isEmpty(upl.getPhone())) {
//                    continue;
//                } else {
//                    map.put(upl.getPhone().trim(), null);
//                }
//            }
//            if (map.entrySet().size() <= 50) {
//                userRisk.setRule0070(Boolean.TRUE);
//            } else {
//                userRisk.setRule0070(Boolean.FALSE);
//            }
//        } else {
//            userRisk.setRule0070(Boolean.TRUE);
//        }
//
//        if (userIdentity != null && StringUtil.isNotEmpty(userIdentity.getIdentityNum())) {
//            Integer age = this.calculateRealAgeByIdCardNum(userIdentity.getIdentityNum());
//            // 年龄 <20周岁或>45周岁
//            if (age < 20 || age > 40) {
//                userRisk.setRule0021(Boolean.TRUE);
//            } else {
//                userRisk.setRule0021(Boolean.FALSE);
//            }
//
//            // 年龄+婚姻状况 >40岁且离异
//            if (StringUtil.isNotEmpty(userBasicMsg.getMarry()) && "离异".equals(userBasicMsg.getMarry()) && age > 40) {
//                userRisk.setRule0028(Boolean.TRUE);
//            } else {
//                userRisk.setRule0028(Boolean.FALSE);
//            }
//        }
//
//        return userRisk;
//    }

//    /**
//     * 处理芝麻分风控规则
//     *
//     * @param userRisk
//     * @return
//     */
//    private UserRisk dealWithZhima(UserRisk userRisk, User user) {
//        EntityWrapper<UserZhima> ew_zm = new EntityWrapper<>();
//        ew_zm.eq("user_id", userRisk.getUserId());
//        UserZhima userZhima = this.userZhimaService.selectOne(ew_zm);
//
//        if (userZhima != null && userZhima.getScore() != null && userZhima.getScore() > 0) {
//            // 芝麻分 < 500
//            if (userZhima.getScore() < 580) {
//                userRisk.setRule0020(Boolean.TRUE);
//            } else {
//                userRisk.setRule0020(Boolean.FALSE);
//            }
//
//            Calendar gmtDate = Calendar.getInstance();
//            gmtDate.setTime(user.getGmtDatetime());
//            int hour = gmtDate.get(Calendar.HOUR_OF_DAY);
//
//            // 注册时间=23、0点，550=<芝麻分<600
//            if (hour >= 23 && userZhima.getScore() >= 550 && userZhima.getScore() < 600) {
//                userRisk.setRule0024(Boolean.TRUE);
//            } else {
//                userRisk.setRule0024(Boolean.FALSE);
//            }
//
//            // 1<=注册时间<=6，550=<芝麻分<610
//            if (hour >= 1 && hour <= 6 && userZhima.getScore() >= 550 && userZhima.getScore() < 610) {
//                userRisk.setRule0025(Boolean.TRUE);
//            } else {
//                userRisk.setRule0025(Boolean.FALSE);
//            }
//
//            // 1<=注册时间<=6，550=<芝麻分<610
//            if (hour >= 3 && hour <= 4 && userZhima.getScore() >= 610 && userZhima.getScore() < 660) {
//                userRisk.setRule0026(Boolean.TRUE);
//            } else {
//                userRisk.setRule0026(Boolean.FALSE);
//            }
//        }
//        return userRisk;
//    }

    /**
     * 处理排序科技风控规则
     *
     * @param userRisk
     * @return
     */
    private BigDecimal dealWithPaixu(UserRisk userRisk) {
        log.info("<-----------dealWithPaixu----------> userRisk:"+ JSON.toJSONString(userRisk));
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath = httpServletRequest.getServletContext().getRealPath("/");
        log.info("<-----------dealWithPaixu----------> realPath:"+ realPath);
        EntityWrapper<UserIdentity> ew_id = new EntityWrapper<>();
        ew_id.eq("user_id", userRisk.getUserId());
        UserIdentity userIdentity = this.userIdentityService.selectOne(ew_id);

        EntityWrapper<UserBasicMsg> ew_msg = new EntityWrapper<>();
        ew_msg.eq("user_id", userRisk.getUserId());
        UserBasicMsg userBasicMsg = this.userBasicMsgService.selectOne(ew_msg);

        EntityWrapper<UserPhone> ewPhone = new EntityWrapper<>();
        ewPhone.eq("user_id", userRisk.getUserId());
        ewPhone.eq("status", 1);
        UserPhone userPhone = userPhoneService.selectOne(ewPhone);

        EntityWrapper<UserPhoneList> ew_pl = new EntityWrapper<>();
        ew_pl.eq("user_id", userRisk.getUserId());
        List<UserPhoneList> list_userPhoneList = this.userPhoneListService.selectList(ew_pl);

        BigDecimal finalScore = new BigDecimal(0);

        try {
            log.info("<-----------dealWithPaixu----------> mxData");
            String mxData  = MoxieApi.getMxData(userPhone.getPhone(), userPhone.getTaskId(),realPath);
            String mxDataReport = MoxieApi.getMxReportData(userPhone.getPhone(), userPhone.getRealName(),userIdentity.getIdentityNum(),userPhone.getTaskId(),realPath);
            log.info("<-----------dealWithPaixu----------> mxDataReport");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userIdentity", userIdentity);
            jsonObject.put("userBasicMsg", userBasicMsg);
            jsonObject.put("userPhone", userPhone);
            jsonObject.put("list_userPhoneList", list_userPhoneList);
            jsonObject.put("rawInfo", JSONObject.parseObject(mxData));
            jsonObject.put("rawReport", JSONObject.parseObject(mxDataReport));
            String result = PaixuApiUtil.preloan(jsonObject);
            log.info("<-----------排序返回的信息---------->" + result);
            JSONObject resultJson = JSONObject.parseObject(result);

            if ("000000".equals(resultJson.getString("rspCode"))) {
                finalScore = resultJson.getBigDecimal("score");
                return finalScore;
            }else{
                log.info("<-----------dealWithPaixu 获取分数失败---------->");
            }

        } catch (Exception e) {
            log.info("<-----------获取风控分数失败异常---------->");
            log.info(JSON.toJSON(e));
            log.info(e.getMessage());
            log.info(JSON.toJSONString(e.getStackTrace()));
            log.info(e.getCause());
//            throw new RuntimeException("获取风控分数失败");
//            throw new Exception(e);
        }

        return null;
    }

//    /**
//     * 处理贷前审核风控
//     *
//     * @param userRisk
//     * @param json
//     * @return
//     */
//    private UserRisk dealWithPreLoan(UserRisk userRisk, JSONObject json) {
//        JSONObject json_result = json.getJSONObject("result");
//        JSONArray json_arr_ristItems = json_result.getJSONArray("ristIterms");
//        JSONObject json_preloan = json_result.getJSONObject("preloanValidation");
//
//        userRisk.setRule0043(Boolean.FALSE);
//        userRisk.setRule0044(Boolean.FALSE);
//        userRisk.setRule0045(Boolean.FALSE);
//        userRisk.setRule0046(Boolean.FALSE);
//        userRisk.setRule0047(Boolean.FALSE);
//        userRisk.setRule0048(Boolean.FALSE);
//        userRisk.setRule0049(Boolean.FALSE);
//        userRisk.setRule0050(Boolean.FALSE);
//        userRisk.setRule0051(Boolean.FALSE);
//        userRisk.setRule0052(Boolean.FALSE);
//        userRisk.setRule0053(Boolean.FALSE);
//        userRisk.setRule0054(Boolean.FALSE);
//        userRisk.setRule0055(Boolean.FALSE);
//        userRisk.setRule0056(Boolean.FALSE);
//        userRisk.setRule0057(Boolean.FALSE);
//        userRisk.setRule0058(Boolean.FALSE);
//        userRisk.setRule0059(Boolean.FALSE);
//        userRisk.setRule0060(Boolean.FALSE);
//        userRisk.setRule0061(Boolean.FALSE);
//
//        try {
//            int score = json_preloan.getIntValue("finalScore"); // 风险分 同盾
//            if (score >= 100) {
//                userRisk.setRule0046(Boolean.TRUE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            userRisk.setRule0046(null);
//        }
//
//        for (int i = 0; i < json_arr_ristItems.size(); i++) {
//            JSONObject json_ristItem = json_arr_ristItems.getJSONObject(i);
//            // 身份证命中高风险关注名单
//            if ("24374864".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0047(Boolean.TRUE);
//                continue;
//            }
//            // 手机号命中高风险关注名单
//            if ("24374934".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0048(Boolean.TRUE);
//                continue;
//            }
//            // 手机号命中虚假号码库
//            if ("24373944".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0049(Boolean.TRUE);
//                continue;
//            }
//            // 手机号命中通信小号库
//            if ("24374084".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0050(Boolean.TRUE);
//                continue;
//            }
//            // 手机号命中诈骗骚扰库
//            if ("24374224".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0051(Boolean.TRUE);
//                continue;
//            }
//            // 身份证命中法院失信名单
//            if ("24374194".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0052(Boolean.TRUE);
//                continue;
//            }
//            // 身份证命中犯罪通缉名单
//            if ("24374254".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0053(Boolean.TRUE);
//                continue;
//            }
//            // 身份证命中法院执行名单
//            if ("24374474".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0054(Boolean.TRUE);
//                continue;
//            }
//            // 身份证命中信贷逾期名单
//            if ("24374734".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0055(Boolean.TRUE);
//                continue;
//            }
//            // 身份证_姓名命中信贷逾期模糊名单
//            if ("24375134".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0056(Boolean.TRUE);
//                continue;
//            }
//            // 身份证_姓名命中法院失信模糊名单
//            if ("24375154".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0057(Boolean.TRUE);
//                continue;
//            }
//            // 身份证_姓名命中法院执行模糊名单
//            if ("24375174".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0058(Boolean.TRUE);
//                continue;
//            }
//            // 3个月内申请信息关联多个身份证数量
//            if ("24375314".equals(json_ristItem.getString("itemId"))) {
//                try {
//                    int count = JSONObject.parseObject(json_ristItem.getString("itemDetail"))
//                            .getJSONArray("frequency_detail_list").getJSONObject(0).getJSONArray("data").size();
//                    if (count > 3) {
//                        userRisk.setRule0059(Boolean.TRUE);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    userRisk.setRule0059(null);
//                }
//                continue;
//            }
//            // 3个月内身份证作为联系人身份证出现的次数大于等于2
//            if ("24375334".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0060(Boolean.TRUE);
//                continue;
//            }
//            // 7天内身份证使用过多设备进行申请
//            if ("24375454".equals(json_ristItem.getString("itemId"))) {
//                userRisk.setRule0061(Boolean.TRUE);
//                continue;
//            }
//            // 7天内申请人在多个平台申请借款
//            if ("24375464".equals(json_ristItem.getString("itemId"))) {
//
//            }
//            // 1个月内申请人在多个平台申请借款
//            if ("24375604".equals(json_ristItem.getString("itemId"))) {
//                try {
//                    int count = JSONObject.parseObject(json_ristItem.getString("itemDetail")).getIntValue("platform_count");
//                    if (count > 24) {
//                        userRisk.setRule0043(Boolean.TRUE);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    userRisk.setRule0043(null);
//                }
//                continue;
//            }
//            // 3个月内申请人在多个平台申请借款
//            if ("24375614".equals(json_ristItem.getString("itemId"))) {
//                try {
//                    int count = JSONObject.parseObject(json_ristItem.getString("itemDetail")).getIntValue("platform_count");
//                    if (count > 39) {
//                        userRisk.setRule0044(Boolean.TRUE);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    userRisk.setRule0044(null);
//                }
//                continue;
//            }
//            // 12个月内申请人在多个平台申请借款
//            if ("24375634".equals(json_ristItem.getString("itemId"))) {
//                try {
//                    int count = JSONObject.parseObject(json_ristItem.getString("itemDetail")).getIntValue("platform_count");
//                    if (count > 80) {
//                        userRisk.setRule0045(Boolean.TRUE);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    userRisk.setRule0045(null);
//                }
//                continue;
//            }
//        }
//
//        return userRisk;
//    }
//
//    /**
//     * 根据身份证号计算周岁
//     *
//     * @param idCardNum
//     * @return
//     */
//    private Integer calculateRealAgeByIdCardNum(String idCardNum) {
//        Integer age = 0;
//        // 先截取到字符串中的年、月、日
//        int selectYear = Integer.parseInt(idCardNum.substring(6, 10));
//        int selectMonth = Integer.parseInt(idCardNum.substring(10, 12));
//        int selectDay = Integer.parseInt(idCardNum.substring(12, 14));
//        // 得到当前时间的年、月、日
//        Calendar cal = Calendar.getInstance();
//        int yearNow = cal.get(Calendar.YEAR);
//        int monthNow = cal.get(Calendar.MONTH) + 1;
//        int dayNow = cal.get(Calendar.DATE);
//
//        // 用当前年月日减去生日年月日
//        int yearMinus = yearNow - selectYear - 1;
//        int monthMinus = monthNow - selectMonth;
//        int dayMinus = dayNow - selectDay;
//
//        age = yearMinus;
//        if (yearMinus < 0) {// 选了未来的年份
//            age = 0;
//        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
//            if (monthMinus < 0) {// 选了未来的月份
//                age = 0;
//            } else if (monthMinus == 0) {// 同月份的
//                if (dayMinus < 0) {// 选了未来的日期
//                    age = 0;
//                } else if (dayMinus >= 0) {
//                    age = 1;
//                }
//            } else if (monthMinus > 0) {
//                age = 1;
//            }
//        } else if (yearMinus > 0) {
//            if (monthMinus < 0) {// 当前月>生日月
//            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
//                if (dayMinus < 0) {
//                } else if (dayMinus >= 0) {
//                    age = age + 1;
//                }
//            } else if (monthMinus > 0) {
//                age = age + 1;
//            }
//        }
//
//        return age;
//    }
//
//    /**
//     * 处理魔蝎数据
//     *
//     * @param userRisk
//     * @param user
//     * @return
//     */
//    private UserRisk dealWithTeledata(UserRisk userRisk, User user, UserPhone userPhone) {
//        String body_response = "";
//        try {
//            if (StringUtil.isEmpty(userPhone.getMessage())) {
//                body_response = YiXinApix.mxReport(userPhone.getTaskId());
//            } else {
//                body_response = MoxieApi.getMxReportHtml(userPhone.getMessage());
//            }
//
//            if (!StringUtils.isEmpty(body_response)) {
//                Document document = Jsoup.parse(body_response);
//                userRisk = this.dealWithDocument(document, userRisk, user);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return userRisk;
//    }
//
//    /**
//     * 处理魔蝎报告html
//     *
//     * @param document
//     * @param userRisk
//     * @param user
//     * @return
//     */
//    private UserRisk dealWithDocument(Document document, UserRisk userRisk, User user) {
//        System.out.println("------------------- dealWithDocument -------------------");
//        Element ele_basic = document.getElementsByTag("table").get(0); // 基本信息
//        Element ele_mx = document.getElementsByTag("table").get(12); // 魔蝎数据
//        Element ele_sj = document.getElementsByTag("table").get(5); // 社交分析摘要
//        Element ele_thsj = document.getElementsByTag("table").get(19); // 通话社交
//        Element ele_fx = document.getElementsByTag("table").get(26); // 风险状况
//        Element ele_cx = document.getElementsByTag("table").get(1); // 用户信息检测(联系人数据)
//        Element ele_thhy = document.getElementsByTag("table").get(30); //  通话活跃
//        Element ele_black = document.getElementsByTag("table").get(3); // 黑名单
//
//        int inTime = 0; // 入网时长
//
//        // 号码使用时间 ＜6个月
//        try {
//            inTime = Integer.valueOf(ele_basic.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text().trim().replace("入网时长：", "").replace("月", "").trim());
//            if (inTime < 6) {
//                userRisk.setRule0034(Boolean.TRUE);
//            } else {
//                userRisk.setRule0034(Boolean.FALSE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // 姓名是否与运营商数据匹配
//        try {
//            if (user.getUserName().equals(ele_basic.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text().trim().replace("姓名：", ""))) {
//                userRisk.setRule0030(Boolean.FALSE);
//            } else {
//                userRisk.setRule0030(Boolean.TRUE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // 籍贯 内蒙古自治区及新疆
//        try {
//            String doc_native = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(0).text().trim().replace("籍贯：", "");
//            if (doc_native.contains("内蒙古") || doc_native.contains("新疆")) {
//                userRisk.setRule0063(Boolean.TRUE);
//            } else {
//                userRisk.setRule0063(Boolean.FALSE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        // 号码归属地 内蒙古自治区及新疆
//        try {
//            String doc_phoneFrom = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text().trim().replace("手机号码归属地：", "");
//            if (doc_phoneFrom.contains("内蒙古") || doc_phoneFrom.contains("新疆")) {
//                userRisk.setRule0064(Boolean.TRUE);
//            } else {
//                userRisk.setRule0064(Boolean.FALSE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_mxs = ele_mx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_mx : ele_mxs) {
//                if ("申请人姓名+身份证号码是否出现在法院黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
//                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
//                        userRisk.setRule0031(Boolean.FALSE);
//                    } else {
//                        userRisk.setRule0031(Boolean.TRUE);
//                    }
//                    continue;
//                }
//                if ("申请人姓名+身份证号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
//                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
//                        userRisk.setRule0032(Boolean.FALSE);
//                    } else {
//                        userRisk.setRule0032(Boolean.TRUE);
//                    }
//                    continue;
//                }
//                if ("申请人姓名+手机号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
//                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
//                        userRisk.setRule0033(Boolean.FALSE);
//                    } else {
//                        userRisk.setRule0033(Boolean.TRUE);
//                    }
//                    continue;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_sjs = ele_sj.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_sj : ele_sjs) {
//                if ("互通电话的号码数目".equals(e_sj.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_sj.getElementsByTag("td").get(1).text().trim()) < 10) {
//                        userRisk.setRule0035(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0035(Boolean.FALSE);
//                    }
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_thsjs = ele_thsj.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_sj : ele_thsjs) {
//                if ("通话号码数目".equals(e_sj.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_sj.getElementsByTag("td").get(1).text().trim()) < 3) {
//                        userRisk.setRule0036(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0036(Boolean.FALSE);
//                    }
//
//                    if (Integer.valueOf(e_sj.getElementsByTag("td").get(2).text().trim()) < 3) {
//                        userRisk.setRule0037(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0037(Boolean.FALSE);
//                    }
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_fxs = ele_fx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_fx : ele_fxs) {
//                if ("与法院通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim()) >= 1) {
//                        userRisk.setRule0038(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0038(Boolean.FALSE);
//                    }
//                    continue;
//                }
//                if ("与律师通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim()) >= 1) {
//                        userRisk.setRule0039(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0039(Boolean.FALSE);
//                    }
//                    continue;
//                }
//
//                // 与110近6个月联系次数 >= 1
//                if ("与110通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim()) >= 1) {
//                        userRisk.setRule0068(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0068(Boolean.FALSE);
//                    }
//                    continue;
//                }
//
//                // 与催收公司近6个月联系次数 >= 1
//                if ("与催收公司通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim()) >= 1) {
//                        userRisk.setRule0069(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0069(Boolean.FALSE);
//                    }
//                    continue;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_blacks = ele_black.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_black : ele_blacks) {
//                if ("黑中介分数".equals(e_black.getElementsByTag("td").get(1).text().trim())) {
//                    if (Integer.valueOf(e_black.getElementsByTag("td").get(2).text().trim().replace("(分数范围0-100，40分以下为高危人群）", "").trim()) <= 40) {
//                        userRisk.setRule0062(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0062(Boolean.FALSE);
//                    }
//                    continue;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_thhys = ele_thhy.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_thhy : ele_thhys) {
//                if ("关机天数".equals(e_thhy.getElementsByTag("td").get(0).text().trim())) {
//                    // 近30天关机天数 >= 3
//                    if (Integer.valueOf(e_thhy.getElementsByTag("td").get(1).text().trim()) >= 3) {
//                        userRisk.setRule0065(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0065(Boolean.FALSE);
//                    }
//
//                    // 近60天关机天数 >= 9
//                    if (Integer.valueOf(e_thhy.getElementsByTag("td").get(2).text().trim()) >= 9) {
//                        userRisk.setRule0066(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0066(Boolean.FALSE);
//                    }
//
//                    // 近90天关机天数 >= 18
//                    if (Integer.valueOf(e_thhy.getElementsByTag("td").get(3).text().trim()) >= 18) {
//                        userRisk.setRule0067(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0067(Boolean.FALSE);
//                    }
//
//                    continue;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            Elements ele_cxs = ele_cx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//            for (Element e_cx : ele_cxs) {
//                if ("查询过该用户的相关企业数量".equals(e_cx.getElementsByTag("td").get(1).text().trim())) {
//                    if (Integer.valueOf(e_cx.getElementsByTag("td").get(2).text().trim()) >= 100) {
//                        userRisk.setRule0040(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0040(Boolean.FALSE);
//                    }
//                    continue;
//                }
//                if ("电话号码注册过的相关企业数量".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
//                    if (Integer.valueOf(e_cx.getElementsByTag("td").get(1).text().trim()) >= 100) {
//                        userRisk.setRule0041(Boolean.TRUE);
//                    } else {
//                        userRisk.setRule0041(Boolean.FALSE);
//                    }
//                    continue;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        System.out.println("--------------------------------------------");
//
//        return userRisk;
//    }
}
