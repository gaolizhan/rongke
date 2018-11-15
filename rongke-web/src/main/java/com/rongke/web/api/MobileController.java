package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.*;
import com.rongke.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @MobileController
 * @controller
 * @version : Ver 1.0
 */

@RestController
@RequestMapping(value="/api/mobileAuthentication")
@Transactional
@CrossOrigin
public class MobileController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private MobileService mobileService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private UserBasicMsgService userBasicMsgService;

    @Autowired
    private UserService userService;

//    @RequestMapping(value="/submit", method = RequestMethod.POST)
//    public JsonResp submit(HttpServletRequest request){
//        Map<String, String> resultMap = new HashMap<String, String>();
//        try {
//            String mobile = request.getParameter("mobile");
//            String cycle = systemConfigService.selectValueByKey("yongxun_cycle") ;
//            String userId = "";
//            //走永讯，判断是否进行同盾验证
//            Map<String,String> mapYongXun = mobileService.yongxunRun(mobile, cycle);
//            Integer count = Integer.valueOf(mapYongXun.get("count"));
//            //查询永讯配置
//            Integer yxCount = Integer.valueOf(systemConfigService.selectValueByKey("yongxun_count"));
//            if(count <= yxCount){
//                EntityWrapper ew=new EntityWrapper();
//                ew.eq("user_id",userId);
//                LoanOrder lo = loanOrderService.selectOne(ew);
//                //走同盾
//                Map<String, String> paramsMap = new HashMap<String,String>();
//                paramsMap.put("loan_amount",String.valueOf(lo.getBorrowMoney()));
//                paramsMap.put("limit_days",String.valueOf(lo.getLimitDays()));
//                paramsMap.put("loan_term_unit","DAY");
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                String res = simpleDateFormat.format(lo.getGmtDatetime());
//                paramsMap.put("loan_date",res);
//                UserBank ub = userBankService.selectOne(ew);
//                paramsMap.put("name",ub.getName());
//                paramsMap.put("id_number",ub.getName());
//                paramsMap.put("mobile",ub.getBankPhone());
//                paramsMap.put("card_number",ub.getBankcardno());
//                UserBasicMsg ubm = userBasicMsgService.selectOne(ew);
//                paramsMap.put("work_phone",ubm.getWorkPhone());
//                paramsMap.put("diploma",ubm.getStudy());
//                paramsMap.put("marriage",ubm.getMarry());
//                paramsMap.put("registered_address",ubm.getProvince()+ubm.getCity()+ubm.getCounty()+ubm.getAddressDetails());
//                paramsMap.put("company_name",ubm.getWorkCompany());
//                paramsMap.put("company_address",ubm.getWorkPlace());
//                paramsMap.put("contact1_name",ubm.getLinkPersonNameOne());
//                paramsMap.put("contact1_mobile",ubm.getLinkPersonPhoneOne());
//                paramsMap.put("contact1_relation",ubm.getLinkPersonRelationOne());
//                paramsMap.put("contact2_name",ubm.getLinkPersonNameTwo());
//                paramsMap.put("contact2_mobile",ubm.getLinkPersonPhoneTwo());
//                paramsMap.put("contact2_relation",ubm.getLinkPersonRelationTwo());
//                User user = userService.selectOne(ew);
//                paramsMap.put("token_id",user.getToken());
//                TongdunLog  tdl = mobileService.TongDunRun(userId,lo,paramsMap);
//                if(tdl != null){
//                    Boolean b = mobileService.insert(tdl);
//                    if (b = true){log.debug("保存成功");}
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            log.debug(" 手机运营商认证发生系统错误");
//        }finally {
//            return  JsonResp.ok(resultMap);
//        }
//    }
    @Autowired
    private TongdunAuditService tongdunAuditService;

    @Autowired
    private UserIdentityService userIdentityService;
    @RequestMapping(value="/query", method = RequestMethod.GET)
    public JsonResp tongdunAuditQuery(String userId){
        Map<String,Object> restMap = new HashMap<>();
        try{
            EntityWrapper ew = new EntityWrapper();
            ew.eq("user_id",userId);
            TongdunAudit tongdunAudit = tongdunAuditService.selectOne(ew);
            if(tongdunAudit != null){
                restMap.put("td_data1",JSON.parseObject(tongdunAudit.getRiskItems()));
            }
            EntityWrapper ew2 = new EntityWrapper();
            ew2.eq("id",userId);
            User user = userService.selectOne(ew2);
            JSONObject jo = new JSONObject();
            if(user != null){
                jo.put("mobile",user.getPhone());
            }
            UserIdentity userIdentity = userIdentityService.selectOne(ew);
            if(userIdentity != null){
                jo.put("name",userIdentity.getUserName());
                jo.put("id_number",userIdentity.getIdentityNum());
            }
            restMap.put("person_info1",jo);
        }catch (Exception e){
            e.printStackTrace();
            restMap.put("msg","系统错误");
        }finally {
            return JsonResp.ok(restMap);
        }
    }

    @RequestMapping(value="/query2", method = RequestMethod.GET)
    public JsonResp tongdunAuditQuery2(String userId){
        List<Object> list = new ArrayList<>();
        try{
            EntityWrapper ew = new EntityWrapper();
            ew.eq("user_id",userId);
            TongdunAudit tongdunAudit = tongdunAuditService.selectOne(ew);
            String ri = tongdunAudit.getRiskItems();
            List<String> riList = JSON.parseArray(ri,String.class);
            if(riList != null){
                for(String a : riList) {
                    JSONObject aJO = JSON.parseObject(a);
                    if("10".equals(aJO.getString("item_id"))){
                        String nhd = aJO.getJSONObject("item_detail").getString("namelist_hit_details");
                        List<String> nhdList = JSON.parseArray(nhd,String.class);
                        for(String b:nhdList){
                            String cd = JSON.parseObject(b).getString("court_details");
                            List<String> cdl =  JSON.parseArray(cd,String.class);
                            for (String c:cdl){
                                JSONObject cJO = JSON.parseObject(c);
                                Map<String,String> map = new HashMap<>();
                                map.put("fraud_type",cJO.getString("fraud_type"));
                                map.put("id_number",cJO.getString("id_number"));
                                map.put("name",cJO.getString("name"));
                                map.put("age",cJO.getString("age"));
                                map.put("gender",cJO.getString("gender"));
                                map.put("court_name",cJO.getString("court_name"));
                                map.put("province",cJO.getString("province"));
                                map.put("filing_time",cJO.getString("filing_time"));
                                map.put("execution_department",cJO.getString("execution_department"));
                                map.put("duty",cJO.getString("duty"));
                                map.put("situation",cJO.getString("situation"));
                                map.put("discredit_detail",cJO.getString("discredit_detail"));
                                map.put("execution_base",cJO.getString("execution_base"));
                                map.put("case_number",cJO.getString("case_number"));
                                list.add(map);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            return JsonResp.ok(list);
        }
    }

    @RequestMapping(value="/yongxunChange", method = RequestMethod.GET)
    public JsonResp yongxunChange(String count,String cycle){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(count)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","yongxun_count");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("yongxun_count");
                sc.setConfigValue(count);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("yongxun_count");
                sc.setConfigValue(count);
                systemConfigService.insert(sc);
            }
        }
        if(StringUtils.isNotEmpty(cycle)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","yongxun_cycle");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("yongxun_cycle");
                sc.setConfigValue(cycle);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("yongxun_cycle");
                sc.setConfigValue(cycle);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/tongdunChange", method = RequestMethod.GET)
    public JsonResp tongdunChange(String tongdunfen){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(tongdunfen)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","tongdun_score");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("tongdun_score");
                sc.setConfigValue(tongdunfen);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("tongdun_score");
                sc.setConfigValue(tongdunfen);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/jiekuanChange", method = RequestMethod.GET)
    public JsonResp jiekuanChange(String everydayResetTime,String everydayMaxLoan){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(everydayResetTime)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","everydayResetTime");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("everydayResetTime");
                sc.setConfigValue(everydayResetTime);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("everydayResetTime");
                sc.setConfigValue(everydayResetTime);
                systemConfigService.insert(sc);
            }
        }
        if(StringUtils.isNotEmpty(everydayMaxLoan)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","everydayMaxLoan");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("everydayMaxLoan");
                sc.setConfigValue(everydayMaxLoan);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("everydayMaxLoan");
                sc.setConfigValue(everydayMaxLoan);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/shengriChange", method = RequestMethod.GET)
    public JsonResp shengriChange(String shengriyouhui){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(shengriyouhui)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","shengriyouhui");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("shengriyouhui");
                sc.setConfigValue(shengriyouhui);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("shengriyouhui");
                sc.setConfigValue(shengriyouhui);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/daikuanChange", method = RequestMethod.GET)
    public JsonResp daikuanChange(String dabiaocishu){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(dabiaocishu)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","dabiaocishu");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("dabiaocishu");
                sc.setConfigValue(dabiaocishu);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("dabiaocishu");
                sc.setConfigValue(dabiaocishu);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/cuishouChange", method = RequestMethod.GET)
    public JsonResp cuishouChange(String cuishoutichen){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(cuishoutichen)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","cuishoutichen");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("cuishoutichen");
                sc.setConfigValue(cuishoutichen);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("cuishoutichen");
                sc.setConfigValue(cuishoutichen);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value="/xuqiChange", method = RequestMethod.GET)
    public JsonResp xuqiChange(String xuqi){
        SystemConfig sc = null;
        if(StringUtils.isNotEmpty(xuqi)){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("config_key","xuqi");
            sc = systemConfigService.selectOne(ew);
            if(sc != null){
                sc = new SystemConfig();
                sc.setConfigKey("xuqi");
                sc.setConfigValue(xuqi);
                systemConfigService.updataByKey(sc);
            }else{
                sc = new SystemConfig();
                sc.setConfigKey("xuqi");
                sc.setConfigValue(xuqi);
                systemConfigService.insert(sc);
            }
        }
        return JsonResp.ok("success");
    }
    @RequestMapping(value="/selectAll", method = RequestMethod.GET)
    public JsonResp selectAll(String cuishoutichen){
        Map map  = new HashMap();
        map.put("yongxun_count",systemConfigService.selectValueByKey("yongxun_count"));
        map.put("yongxun_cycle",systemConfigService.selectValueByKey("yongxun_cycle"));
        map.put("tongdun_score",systemConfigService.selectValueByKey("tongdun_score"));
        map.put("everydayResetTime",systemConfigService.selectValueByKey("everydayResetTime"));
        map.put("everydayMaxLoan",systemConfigService.selectValueByKey("everydayMaxLoan"));
        map.put("dabiaocishu",systemConfigService.selectValueByKey("dabiaocishu"));
        map.put("shengriyouhui",systemConfigService.selectValueByKey("shengriyouhui"));
        map.put("cuishoutichen",systemConfigService.selectValueByKey("cuishoutichen"));
        map.put("xuqi",systemConfigService.selectValueByKey("xuqi"));
        return JsonResp.ok(map);
    }

}
