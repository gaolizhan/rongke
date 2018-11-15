package com.rongke.web.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.ConstantUtil;
import com.rongke.commons.JsonResp;
import com.rongke.enums.FileType;
import com.rongke.model.SystemConfig;
import com.rongke.model.UserRisk;
import com.rongke.redis.CacheUtil;
import com.rongke.service.SystemConfigService;
import com.rongke.service.UserRiskService;
import com.rongke.utils.Base64Picture;
import com.rongke.utils.RandomUtils;
import com.rongke.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @version : Ver 1.0
 * @SysConfigController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/sysConfig")
@Transactional
@CrossOrigin
public class SysConfigController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SystemConfigService sysConfigService;
    @Autowired
    private UserRiskService userRiskService;
    @Autowired
    private CacheUtil cacheUtil;

    /**
     * @param
     * @return 返回值JsonResp
     * @修改qq
     */
    @RequestMapping(value = "/updateQQ", method = RequestMethod.GET)
    public JsonResp updateQQ(String qqOne, String qqTwo, String qqThree) {
        log.debug("查找");
        EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
        ew1.eq("config_key", "qqOne");
        SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
        systemConfig1.setConfigValue(qqOne);
        sysConfigService.updateById(systemConfig1);

        EntityWrapper<SystemConfig> ew2 = new EntityWrapper();
        ew2.eq("config_key", "qqTwo");
        SystemConfig systemConfig2 = sysConfigService.selectOne(ew2);
        systemConfig2.setConfigValue(qqTwo);
        sysConfigService.updateById(systemConfig2);

        EntityWrapper<SystemConfig> ew3 = new EntityWrapper();
        ew3.eq("config_key", "qqThree");
        SystemConfig systemConfig3 = sysConfigService.selectOne(ew3);
        systemConfig3.setConfigValue(qqThree);
        sysConfigService.updateById(systemConfig3);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改续期金额
     */
    @RequestMapping(value = "/updateXuqiMoney", method = RequestMethod.GET)
    public JsonResp updateXuqiMoney(String xuqiMoney) {
        log.debug("修改续期金额");
        EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
        ew1.eq("config_key", "xuqiMoney");
        SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
        systemConfig1.setConfigValue(xuqiMoney);
        sysConfigService.updateById(systemConfig1);
        return JsonResp.ok();
    }




    /**
     * @param
     * @return 返回值JsonResp
     * @是否自动审核及放款
     */
    @RequestMapping(value = "/updateAutoPay", method = RequestMethod.GET)
    public JsonResp updateAutoPay(String isAuto) {
        log.debug("修改自动审核及放款逻辑");
        if(StringUtil.isEmpty(isAuto) ||  !"true".equals(isAuto)){
            isAuto = "false";
            cacheUtil.put("isAuto","false");
        }else{
            cacheUtil.put("isAuto","true");
        }
        EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
        ew1.eq("config_key", "isAuto");
        SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
        systemConfig1.setConfigValue(isAuto);
        sysConfigService.updateById(systemConfig1);
        return JsonResp.ok();
    }

    /**
     * 是否
     */
    @RequestMapping(value = "/updateTongdun", method = RequestMethod.GET)
    public JsonResp updateTongdun(String tongdunfenshu) {
        log.debug("修改同盾分数");
        EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
        ew1.eq("config_key", "tongdunfenshu");
        SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
        systemConfig1.setConfigValue(tongdunfenshu);
        sysConfigService.updateById(systemConfig1);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改同盾分数
     */
    @RequestMapping(value = "/updateMoney", method = RequestMethod.GET)
    public JsonResp updateMoney(String money) {
        log.debug("修改同盾分数");
        EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
        ew1.eq("config_key", "money");
        SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
        systemConfig1.setConfigValue(money);
        sysConfigService.updateById(systemConfig1);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @修改参数
     */
    @RequestMapping(value = "/updateParam", method = RequestMethod.GET)
    public JsonResp updateParam(String param, String type) {
        log.debug("修改同盾分数");
        EntityWrapper<SystemConfig> ew = new EntityWrapper();
        ew.eq("config_key", type);
        SystemConfig systemConfig = sysConfigService.selectOne(ew);
        systemConfig.setConfigValue(param);
        sysConfigService.updateById(systemConfig);
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @所有参数显示
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public JsonResp selectAll() {
        log.debug("所有参数显示");
        EntityWrapper<SystemConfig> ew = new EntityWrapper();
        List<SystemConfig> list = sysConfigService.selectList(ew);
        return JsonResp.ok(list);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @客服qq号
     */
    @RequestMapping(value = "/selectQQThree", method = RequestMethod.GET)
    public JsonResp selectQQThree() {
        log.debug("所有参数显示");
        EntityWrapper<SystemConfig> ew = new EntityWrapper();
        ew.eq("config_key", "qqThree");
        SystemConfig systemConfig = sysConfigService.selectOne(ew);
        return JsonResp.ok(systemConfig.getConfigValue());
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @客服qq号
     */
    @RequestMapping(value = "/selectCustomServiceQq", method = RequestMethod.GET)
    public JsonResp selectCustomServiceQq() {
        log.debug("所有参数显示");
        EntityWrapper<SystemConfig> ew = new EntityWrapper();
        ew.eq("config_key", "customServiceQq");
        SystemConfig systemConfig = sysConfigService.selectOne(ew);
        return JsonResp.ok(systemConfig.getConfigValue());
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @还款后qq
     */
    @RequestMapping(value = "/selectQQTwo", method = RequestMethod.GET)
    public JsonResp selectQQTwo() {
        log.debug("所有参数显示");
        EntityWrapper<SystemConfig> ew = new EntityWrapper();
        ew.eq("config_key", "qqTwo");
        SystemConfig systemConfig = sysConfigService.selectOne(ew);
        return JsonResp.ok(systemConfig.getConfigValue());
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addSysConfig(@RequestBody JSONArray jsonArray) {
        log.debug("添加");
        List<String> keylist = new ArrayList<>();
        List<SystemConfig> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            SystemConfig sysConfig = new SystemConfig();
            Set<Map.Entry<String, Object>> entrySet = jsonArray.getJSONObject(i).entrySet();
            Iterator iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> me = (Map.Entry<String, Object>) iterator.next();
                sysConfig.setId(1l);
                sysConfig.setConfigKey(me.getKey());
                sysConfig.setConfigValue(String.valueOf(me.getValue()));
                sysConfig.setCreateTime(new Date());
                list.add(sysConfig);
                keylist.add(me.getKey());
            }
        }
        if (!keylist.isEmpty()) {
            EntityWrapper<SystemConfig> ew = new EntityWrapper<>();
            ew.in("config_key", keylist);
            sysConfigService.delete(ew);
        }
        sysConfigService.insertBatch(list);
        return JsonResp.ok();
    }

    /**
     * @param basejson
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public JsonResp uploadPic(@RequestBody JSONObject basejson) {
        log.debug("上传base64图片");
        FileType fileType = FileType.OTHER;
        String base64 = basejson.getString("base64");
        // 文件存储目录
        String filePath = fileType.getAbsolutePath();
        File rootFolder = new File(filePath);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        String realFileName = UUID.randomUUID().toString()
                .replaceAll("-", "");
        String originalFileName = RandomUtils.randomString(6) + ".jpg";
        int index = originalFileName.lastIndexOf('.');
        realFileName += originalFileName.substring(index);
        String path = filePath + File.separator + realFileName;
        Base64Picture.GenerateImage(base64, path);
        String fileUrl = fileType.getFileUrl() + "/" + realFileName;

        return JsonResp.ok("", fileUrl);
    }

    /**
     * @param sysConfig
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateSysConfig(@RequestBody SystemConfig sysConfig) {
        log.debug("修改");
        sysConfigService.updateById(sysConfig);
        return JsonResp.ok(sysConfig);
    }

    /**
     * @return 返回值JsonResp
     * @获取还款方式列表
     */
    @RequestMapping(value = "/selectSysConfig", method = RequestMethod.GET)
    public JsonResp selectSysConfig() {
        log.debug("获取还款方式列表");
        List<SystemConfig> sysConfigList = sysConfigService.selectList(null);
        return JsonResp.ok(sysConfigList);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @更新字典设置
     */
    @RequestMapping(value = "/updateAll", method = RequestMethod.POST)
    public JsonResp updateAll(@RequestBody Map<String, Object> map) {
        log.debug("更新字典设置");
        List<SystemConfig> list = new ArrayList<>();
        for (String key : map.keySet()) {
            SystemConfig sysConfig = new SystemConfig();
            sysConfig.setConfigKey(key);
            sysConfig.setConfigValue(map.get(key).toString());
            list.add(sysConfig);
        }
        sysConfigService.updateListByConfigKey(list);
        return JsonResp.ok();
    }

    /**
     * app查询支付方式
     *
     * @return
     */
    @RequestMapping(value = "/selectPayInfo", method = RequestMethod.GET)
    public JsonResp selectPayInfo() {
        EntityWrapper<SystemConfig> entityWrapper = new EntityWrapper<>();
        List<SystemConfig> systemConfigs = sysConfigService.selectList(entityWrapper);
        Map<String, Object> map = new HashMap<>();
        for (SystemConfig systemConfig : systemConfigs) {
            if (systemConfig.getId() < 32) {
                map.put(systemConfig.getConfigKey(), systemConfig.getConfigValue());
            }
        }
        return JsonResp.ok(map);
    }

    /**
     * app查询支付方式
     *
     * @return
     */
    @RequestMapping(value = "/selectPayInfo1", method = RequestMethod.GET)
    public JsonResp selectPayInfo1() {
        EntityWrapper<SystemConfig> entityWrapper = new EntityWrapper<>();
        List<SystemConfig> systemConfigs = sysConfigService.selectList(entityWrapper);
        Map<String, Object> map = new HashMap<>();
        for (SystemConfig systemConfig : systemConfigs) {
            if (systemConfig.getId() > 31) {
                if (systemConfig.getConfigKey().equals("alipay1")) {
                    map.put("alipay", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("alipayCode1")) {
                    map.put("alipayCode", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("debitCardName1")) {
                    map.put("debitCardName", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("customServiceQq1")) {
                    map.put("customServiceQq", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("debitCardNo1")) {
                    map.put("debitCardNo", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("servicePhone1")) {
                    map.put("servicePhone", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("wechatAccount1")) {
                    map.put("wechatAccount", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("wechatCode1")) {
                    map.put("wechatCode", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("bankName1")) {
                    map.put("bankName", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("alipayUser1")) {
                    map.put("alipayUser", systemConfig.getConfigValue());
                } else if (systemConfig.getConfigKey().equals("wechatOfficial1")) {
                    map.put("wechatOfficial", systemConfig.getConfigValue());
                }
            }
        }
        return JsonResp.ok(map);
    }

    /**
     * 设置后台获取验证码的号码
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/setAllPhone", method = RequestMethod.POST)
    public JsonResp setAllPhone(@RequestBody JSONObject jsonObject) {
        List<String> phoneList = new ArrayList<>();
        String phone1 = jsonObject.getString("phone1");
        String phone2 = jsonObject.getString("phone2");
        String phone3 = jsonObject.getString("phone3");
        String phone4 = jsonObject.getString("phone4");
        String phone5 = jsonObject.getString("phone5");
        String phone6 = jsonObject.getString("phone6");
        String phone7 = jsonObject.getString("phone7");
        String phone8 = jsonObject.getString("phone8");
        String phone9 = jsonObject.getString("phone9");
        String phone10 = jsonObject.getString("phone10");
        phoneList.add(phone1);
        phoneList.add(phone2);
        phoneList.add(phone3);
        phoneList.add(phone4);
        phoneList.add(phone5);
        phoneList.add(phone6);
        phoneList.add(phone7);
        phoneList.add(phone8);
        phoneList.add(phone9);
        phoneList.add(phone10);

        List<String> phones = new ArrayList<>();
        phones.add("phone1");
        phones.add("phone2");
        phones.add("phone3");
        phones.add("phone4");
        phones.add("phone5");
        phones.add("phone6");
        phones.add("phone7");
        phones.add("phone8");
        phones.add("phone9");
        phones.add("phone10");

        for (int i = 0; i < phoneList.size(); i++) {
            EntityWrapper<SystemConfig> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("config_key", phones.get(i));
            SystemConfig systemConfig = sysConfigService.selectOne(entityWrapper);
            systemConfig.setConfigValue(phoneList.get(i));
            sysConfigService.updateById(systemConfig);
        }
        return JsonResp.ok();
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @重新运营风控规则
     */
    @RequestMapping(value = "/reRunRiskRule", method = RequestMethod.GET)
    public JsonResp reRunRiskRule(String startTime, String endTime) {
        log.debug("重新运营风控规则");

        EntityWrapper<UserRisk> ew = new EntityWrapper();
        if (StringUtil.isNotEmpty(startTime)) {
            ew.ge("create_time", startTime);
        }
        if (StringUtil.isNotEmpty(endTime)) {
            ew.le("create_time", endTime);
        }
        List<UserRisk> list = this.userRiskService.selectList(ew);
        if (list != null) {
            Set<String> set = new HashSet<>();
            for (UserRisk ur : list) {
                set.add(ur.getLoanOrderId().toString());
            }
            for (String loanOrderId : set) {
                this.cacheUtil.leftPush(ConstantUtil.RISK_TASK_LOAN_ORDER_ID, loanOrderId);
            }
        }

        return JsonResp.ok();
    }
}
