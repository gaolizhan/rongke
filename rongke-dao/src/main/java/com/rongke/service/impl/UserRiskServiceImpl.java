package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.rongke.commons.JsonResp;
import com.rongke.enums.RiskRuleNo;
import com.rongke.enums.SysConfigConst;
import com.rongke.mapper.UserRiskMapper;
import com.rongke.model.*;
import com.rongke.service.*;
import com.rongke.utils.StringUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.rongke.utils.api.MoxieApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jigang
 * @since 2018-08-24
 */
@Service
@Transactional
public class UserRiskServiceImpl extends ServiceImpl<UserRiskMapper, UserRisk> implements UserRiskService {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private UserTongdunfenService userTongdunfenService;
    @Autowired
    private UserZhimaService userZhimaService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserBasicMsgService userBasicMsgService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UserPhoneListService userPhoneListService;
    @Autowired
    private UserRiskRecordService userRiskRecordService;
    @Autowired
    private UserRiskRuleService userRiskRuleService;
    @Autowired
    private UserBlackService userBlackService;

    private ThreadLocal<String> riskFalgTL = new ThreadLocal<String>();
    private ThreadLocal<Long> userIdTL = new ThreadLocal<Long>();
    private ThreadLocal<Map<String,UserRiskRule>> ruleMapTL = new ThreadLocal<Map<String,UserRiskRule>>();


    @Override
    public String dealRegisterRisk(Long userId){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_REGISTER_SIGN,RiskRuleNo.RUlE_GROUP_REGISTER)){
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_REGISTER));
            this.insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_001);
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }

    }
    @Override
    public String dealBasicInfoRisk(Long userId){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_BASIC_SIGN,RiskRuleNo.RUlE_GROUP_BASIC)){
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_BASIC));
            // 手机通讯录个数（去重且只包含手机号）
            EntityWrapper<UserPhoneList> ew = new EntityWrapper();
            ew.eq("user_id",userId);
            List<UserPhoneList> userPhoneList = userPhoneListService.selectList(ew);
            int addressListCount = 0;
            if (userPhoneList != null && userPhoneList.size() > 0) {
                Map map = new HashMap();
                for (UserPhoneList upl : userPhoneList) {
                    if (StringUtil.isEmpty(upl.getPhone())) {
                        continue;
                    } else {
                        map.put(upl.getPhone().trim(), null);
                    }
                }
                addressListCount = map.entrySet().size();
            }
            this.insertRiskRecord(addressListCount,RiskRuleNo.RULE_NO_002);

            //黑名单校验 名称号码校验
            User user = userService.selectById(userId);
            EntityWrapper<UserBlack> bankEw = new EntityWrapper();
            bankEw.eq("real_name",user.getUserName());
            bankEw.eq("phone",user.getPhone());
            List<UserBlack> userBlacks = userBlackService.selectList(bankEw);
            if(userBlacks!= null && userBlacks.size()>0){
                this.insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_003);
            }else{
                this.insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_003);
            }
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }
    }
    @Override
    public String dealOperatorRisk(Long userId,String realPath){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_OPERATOR_SIGN,RiskRuleNo.RUlE_GROUP_OPERATOR)){
            //获得当前的规则组
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_OPERATOR));

            EntityWrapper<UserIdentity> ewId = new EntityWrapper<>();
            ewId.eq("user_id",userId);
            List<UserIdentity> userIdentities = userIdentityService.selectList(ewId);
            UserIdentity userIdentitie = userIdentities.get(0);

            User user = this.userService.selectById(userId);
            EntityWrapper<UserPhone> ewPhone = new EntityWrapper<>();
            ewPhone.eq("user_id", user.getId());
            ewPhone.eq("status", 1);
            UserPhone userPhone = userPhoneService.selectList(ewPhone).get(0);
            String body_response = "";
            try {
                body_response = MoxieApi.getMxReportHtml(userPhone.getMessage());
                if (!StringUtils.isEmpty(body_response)) {
                    Document document = Jsoup.parse(body_response);
                    this.dealWithDocument(document,user,userIdentitie,realPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }
    }

    @Override
    public String dealIdentityRisk(Long userId){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_IDENTITY_SIGN,RiskRuleNo.RUlE_GROUP_IDENTITY)){
            //获得当前的规则组
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_IDENTITY));
            EntityWrapper<UserIdentity> ew = new EntityWrapper();
            ew.eq("user_id", userId);
            List<UserIdentity> userIdentities = userIdentityService.selectList(ew);
            UserIdentity userIdentity = userIdentities.get(0);

            if (userIdentity != null && StringUtil.isNotEmpty(userIdentity.getIdentityNum())) {
                //校验年龄
                Integer age = this.calculateRealAgeByIdCardNum(userIdentity.getIdentityNum());
                this.insertRiskRecord(age,RiskRuleNo.RULE_NO_030);
                this.insertRiskRecord(age,RiskRuleNo.RULE_NO_031);
                //内部黑名单校验
                EntityWrapper<UserBlack> bankEw = new EntityWrapper();
                bankEw.eq("id_card",userIdentity.getIdentityNum());
                List<UserBlack> userBlacks = userBlackService.selectList(bankEw);

                if(userBlacks != null && userBlacks.size()>0){
                    this.insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_032);
                }else{
                    this.insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_032);
                }
            }
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }

    }

    @Override
    public String dealZhimaRisk(Long userId){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_ZHIMA_SIGN,RiskRuleNo.RUlE_GROUP_ZHIMA)){
            //获得当前的规则组
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_ZHIMA));

            EntityWrapper<UserZhima> ew = new EntityWrapper<>();
            ew.eq("user_id", userId);
            UserZhima userZhima = userZhimaService.selectOne(ew);
            this.insertRiskRecord(userZhima.getScore(),RiskRuleNo.RULE_NO_033);
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }
    }

    @Override
    public String dealBankRisk(Long userId){
        this.userIdTL.set(userId);
        this.riskFalgTL.set(RiskRuleNo.RISK_SUCCESS);
        if(executeCheck(SysConfigConst.RISK_BANK_SIGN,RiskRuleNo.RUlE_GROUP_BANK)){
            //获得当前的规则组
            this.ruleMapTL.set(getRiskRule(RiskRuleNo.RUlE_GROUP_BANK));

            User user = userService.selectById(userId);

            EntityWrapper<UserIdentity> ewId = new EntityWrapper<>();
            ewId.eq("user_id",userId);
            List<UserIdentity> userIdentities = userIdentityService.selectList(ewId);
            UserIdentity userIdentitie = userIdentities.get(0);
            if(userIdentitie.getUserName().equals(user.getUserName())){
                this.insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_034);
            }else{
                this.insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_034);
            }
            this.insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_035);
            return riskFalgTL.get();
        }else{
            return RiskRuleNo.RISK_NEGLECT;
        }
    }

    /**
     * 通过规则组获取规则
     * @param ruleGroup
     * @return
     */
    private Map<String,UserRiskRule> getRiskRule(String ruleGroup){
        EntityWrapper<UserRiskRule> ew = new EntityWrapper();
        ew.eq("rule_group",ruleGroup);
        List<UserRiskRule> userRiskRuleList = userRiskRuleService.selectList(ew);

        HashMap<String, UserRiskRule> map = new HashMap<>();
        for (UserRiskRule userRiskRule : userRiskRuleList) {
            map.put(userRiskRule.getId(),userRiskRule);
        }
        return map;
    }

    /**
     * 添加规则执行记录
     * @param value
     * @param ruleNO
     * @return
     */
    private boolean insertRiskRecord(Integer value,String ruleNO){
        if(value != null){
            Map<String, UserRiskRule> stringUserRiskRuleMap = this.ruleMapTL.get();
            UserRiskRule userRiskRule = stringUserRiskRuleMap.get(ruleNO);
            String status = checkRule(value, userRiskRule);
            UserRiskRecord userRiskRecord = new UserRiskRecord();
            userRiskRecord.setUserId(this.userIdTL.get());
            userRiskRecord.setCreateTime(new Date());
            userRiskRecord.setDescription(MessageFormat.format(userRiskRule.getDescribe(),userRiskRule.getRuleParam()));
            userRiskRecord.setRiskStatus(status);
            userRiskRecord.setValue(value+"");
            userRiskRecord.setUserRiskRuleId(userRiskRule.getId());
            userRiskRecord.setRuleGroup(userRiskRule.getRuleGroup());
            userRiskRecord.setCoerceSign(userRiskRule.getCoerceSign());
            return  userRiskRecordService.insert(userRiskRecord);
        }
        return Boolean.FALSE;
    }

    /**
     * 检验规则执行结果
     * @param value
     * @param userRiskRule
     * @return
     */
    private String checkRule(Integer value,UserRiskRule userRiskRule){
        String status = RiskRuleNo.RUlE_PASS;
        String judgeSign = userRiskRule.getJudgeSign();
        if("eq".equals(judgeSign)){
            if(value == userRiskRule.getRuleParam()){
                status = RiskRuleNo.RUlE_ACCURACY;
                if(userRiskRule.getCoerceSign()){
                    this.riskFalgTL.set(RiskRuleNo.RISK_FAIL);
                }
            }
        }else if("lt".equals(judgeSign)){
            if(value < userRiskRule.getRuleParam()){
                status = RiskRuleNo.RUlE_ACCURACY;
                if(userRiskRule.getCoerceSign()){
                    this.riskFalgTL.set(RiskRuleNo.RISK_FAIL);
                }
            }
        }else if("gt".equals(judgeSign)){
            if(value > userRiskRule.getRuleParam()){
                status = RiskRuleNo.RUlE_ACCURACY;
                if(userRiskRule.getCoerceSign()){
                    this.riskFalgTL.set(RiskRuleNo.RISK_FAIL);
                }
            }
        }else if("lteq".equals(judgeSign)){
            if(value <= userRiskRule.getRuleParam()){
                status = RiskRuleNo.RUlE_ACCURACY;
                if(userRiskRule.getCoerceSign()){
                    this.riskFalgTL.set(RiskRuleNo.RISK_FAIL);
                }
            }
        }else if("gteq".equals(judgeSign)){
            if(value >= userRiskRule.getRuleParam()){
                status = RiskRuleNo.RUlE_ACCURACY;
                if(userRiskRule.getCoerceSign()){
                    this.riskFalgTL.set(RiskRuleNo.RISK_FAIL);
                }
            }
        }
        return status;
    }

    private void dealWithDocument(Document document, User user,UserIdentity userIdentitie,String realPath) {
        System.out.println("------------------- dealWithDocument -------------------");
        Element ele_basic = document.getElementsByTag("table").get(0); // 基本信息

        Element ele_sj = document.getElementsByTag("table").get(5); // 社交分析摘要
        Element ele_thsj = document.getElementsByTag("table").get(19); // 通话社交
        Element ele_fx = document.getElementsByTag("table").get(26); // 风险状况
        Element ele_cx = document.getElementsByTag("table").get(1); // 用户信息检测(联系人数据)
        Element ele_thhy = document.getElementsByTag("table").get(30); //  通话活跃
        Element ele_black = document.getElementsByTag("table").get(2); // 黑名单

        //身份证号与运营商数据匹配
        try {
            String replace = ele_basic.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text().trim().replace("身份证号From客户：", "");
            if (userIdentitie.getIdentityNum().equals(ele_basic.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text().trim().replace("身份证号From客户：", ""))) {
                insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_004);
            } else {
                insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_004);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 姓名是否与运营商数据匹配
        try {
            String replace = ele_basic.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text().trim().replace("姓名：", "");
            if (user.getUserName().equals(ele_basic.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text().trim().replace("姓名：", ""))) {
                insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_005);
            } else {
                insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_005);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Element ele_mx = document.getElementsByTag("table").get(12); // 魔蝎数据
            Elements ele_mxs = ele_mx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_mx : ele_mxs) {
                if ("申请人姓名+身份证号码是否出现在法院黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_006);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_006);
                    }
                    continue;
                }
                if ("申请人姓名+身份证号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_007);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_007);
                    }
                    continue;
                }
                if ("申请人姓名+手机号码是否出现在金融机构黑名单".equals(e_mx.getElementsByTag("td").get(0).text().trim())) {
                    if ("否".equals(e_mx.getElementsByTag("td").get(1).text().trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_008);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_008);
                    }
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 号码使用时间 ＜6个月
        try {
            int inTime = 0;
            inTime = Integer.valueOf(ele_basic.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text().trim().replace("入网时长：", "").replace("月", "").trim());
            insertRiskRecord(inTime,RiskRuleNo.RULE_NO_009);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Elements ele_sjs = ele_sj.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_sj : ele_sjs) {
                if ("互通电话的号码数目".equals(e_sj.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_sj.getElementsByTag("td").get(1).text().trim()),RiskRuleNo.RULE_NO_010);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Elements ele_thhys = ele_thhy.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_thhy : ele_thhys) {
                if ("通话次数".equals(e_thhy.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_thhy.getElementsByTag("td").get(1).text().trim()),RiskRuleNo.RULE_NO_011);

                    insertRiskRecord(Integer.valueOf(e_thhy.getElementsByTag("td").get(2).text().trim()),RiskRuleNo.RULE_NO_012);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Elements ele_fxs = ele_fx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_fx : ele_fxs) {
                if ("与法院通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim()),RiskRuleNo.RULE_NO_013);
                    continue;
                }
                if ("与律师通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_fx.getElementsByTag("td").get(3).text().trim()),RiskRuleNo.RULE_NO_014);
                    continue;
                }

                // 与110近6个月联系次数 >= 1
                if ("与110通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim()),RiskRuleNo.RULE_NO_023);
                    continue;
                }

                // 与催收公司近6个月联系次数 >= 1
                if ("与催收公司通话次数".equals(e_fx.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_fx.getElementsByTag("td").get(2).text().trim()),RiskRuleNo.RULE_NO_024);
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Elements ele_cxs = ele_cx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_cx : ele_cxs) {
                //查询机构数
                if ("查询过该用户的相关企业数量".equals(e_cx.getElementsByTag("td").get(1).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_cx.getElementsByTag("td").get(2).text().trim()),RiskRuleNo.RULE_NO_015);
                    continue;
                }
                //注册机构数
                if ("电话号码注册过的相关企业数量".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_cx.getElementsByTag("td").get(1).text().trim()),RiskRuleNo.RULE_NO_016);
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Elements ele_blacks = ele_black.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_black : ele_blacks) {
                if ("黑中介分数".equals(e_black.getElementsByTag("td").get(1).text().trim())) {
                    insertRiskRecord(Integer.valueOf(e_black.getElementsByTag("td").get(2).text().trim().replace("(分数范围0-100，40分以下为高危人群）", "").trim()),RiskRuleNo.RULE_NO_017);
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 籍贯 内蒙古自治区及新疆
        try {
            String doc_native = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(0).text().trim().replace("籍贯：", "");
            if (doc_native.contains("内蒙古") || doc_native.contains("新疆") || doc_native.contains("西藏")) {
                insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_018);
            } else {
                insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_018);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 号码归属地 内蒙古自治区及新疆
        try {
            String doc_phoneFrom = ele_basic.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text().trim().replace("手机号码归属地：", "");
            if (doc_phoneFrom.contains("内蒙古") || doc_phoneFrom.contains("新疆") || doc_phoneFrom.contains("西藏")) {
                insertRiskRecord(RiskRuleNo.EQ_ACCURACY,RiskRuleNo.RULE_NO_019);
            } else {
                insertRiskRecord(RiskRuleNo.EQ_PASS,RiskRuleNo.RULE_NO_019);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Elements ele_thhys = ele_thhy.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_thhy : ele_thhys) {
                if ("关机天数".equals(e_thhy.getElementsByTag("td").get(0).text().trim())) {
                    // 近30天关机天数 >= 3
                    insertRiskRecord(Integer.valueOf(e_thhy.getElementsByTag("td").get(1).text().trim()),RiskRuleNo.RULE_NO_020);

                    // 近60天关机天数 >= 9
                    insertRiskRecord(Integer.valueOf(e_thhy.getElementsByTag("td").get(2).text().trim()),RiskRuleNo.RULE_NO_021);

                    // 近90天关机天数 >= 18
                    insertRiskRecord(Integer.valueOf(e_thhy.getElementsByTag("td").get(3).text().trim()),RiskRuleNo.RULE_NO_022);
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 号码归属地 内蒙古自治区及新疆
        try {
            Elements ele_cxs = ele_cx.getElementsByTag("tbody").get(0).getElementsByTag("tr");
            for (Element e_cx : ele_cxs) {
                if ("身份证组合过的其他姓名".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
                    String idComName = e_cx.getElementsByTag("td").get(1).text();
                    if (idComName == null || "".equals(idComName.trim()) || "0".equals(idComName.trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS, RiskRuleNo.RULE_NO_025);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY, RiskRuleNo.RULE_NO_025);
                    }
                }
                if ("身份证组合过其他电话".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
                    String idComPhone = e_cx.getElementsByTag("td").get(1).text();
                    if (idComPhone == null || "".equals(idComPhone.trim()) || "0".equals(idComPhone.trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS, RiskRuleNo.RULE_NO_026);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY, RiskRuleNo.RULE_NO_026);
                    }
                }
                if ("电话号码组合过其他身份证".equals(e_cx.getElementsByTag("td").get(0).text().trim())) {
                    String phoneComId = e_cx.getElementsByTag("td").get(1).text();
                    if (phoneComId == null || "".equals(phoneComId.trim()) || "0".equals(phoneComId.trim())) {
                        insertRiskRecord(RiskRuleNo.EQ_PASS, RiskRuleNo.RULE_NO_028);
                    } else {
                        insertRiskRecord(RiskRuleNo.EQ_ACCURACY, RiskRuleNo.RULE_NO_028);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            EntityWrapper<UserPhone> ewPhone = new EntityWrapper<>();
            ewPhone.eq("user_id", user.getId());
            ewPhone.eq("status", 1);
            UserPhone userPhone = userPhoneService.selectList(ewPhone).get(0);
            String mxData = MoxieApi.getMxData(userPhone.getPhone(), userPhone.getTaskId(),realPath);
            System.out.println(mxData);
            JSONObject baseObject = JSON.parseObject(mxData);
            JSONArray callsArray = baseObject.getJSONArray("calls");
            ArrayList<String> peerNumberList = new ArrayList<>();

            for (int i=0;i<callsArray.size();i++) {
                JSONObject jsonObject = callsArray.getJSONObject(i);
                JSONArray items = jsonObject.getJSONArray("items");
                for (int j=0;j<items.size();j++) {
                    JSONObject itemObject = items.getJSONObject(j);
                    String peerNumber = itemObject.getString("peer_number");
                    peerNumberList.add(peerNumber);
                }
            }
            int applicantCallCount = 0;
            int contactCallCount = 0;
            List<String> applicantPhoneList = userService.getAllPhone();
            List<String> contactPhoneList = userPhoneListService.getPhoneListByUserId(this.userIdTL.get());
            for (String phone : peerNumberList) {
                if(applicantPhoneList.contains(phone)){
                    applicantCallCount++;
                }
                if(contactPhoneList.contains(phone)){
                    contactCallCount++;
                }
            }
            insertRiskRecord(applicantCallCount, RiskRuleNo.RULE_NO_029);
            insertRiskRecord(contactCallCount, RiskRuleNo.RULE_NO_027);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //计算027   029
        System.out.println("--------------------------------------------");
    }

    /**
     * 根据身份证号计算周岁
     *
     * @param idCardNum
     * @return
     */
    private Integer calculateRealAgeByIdCardNum(String idCardNum) {
        Integer age = 0;
        // 先截取到字符串中的年、月、日
        int selectYear = Integer.parseInt(idCardNum.substring(6, 10));
        int selectMonth = Integer.parseInt(idCardNum.substring(10, 12));
        int selectDay = Integer.parseInt(idCardNum.substring(12, 14));
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear - 1;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        age = yearMinus;
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }

        return age;
    }

    public Boolean executeCheck(String systemConfigKey,String riskGroup){
        String executeSign = systemConfigService.selectValueByKey(systemConfigKey);
        if(SysConfigConst.RISK_EXECUTE_SIGN.equals(executeSign)) {
            EntityWrapper<UserRiskRecord> urrew = new EntityWrapper();
            urrew.eq("rule_group", riskGroup);
            urrew.eq("user_id", this.userIdTL.get());
            List<UserRiskRecord> userRiskRecords = userRiskRecordService.selectList(urrew);
            if (userRiskRecords == null || userRiskRecords.size() == 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
