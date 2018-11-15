package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.TbBaseInfo;
import com.rongke.model.TbLog;
import com.rongke.model.TbReceiver;
import com.rongke.model.User;
import com.rongke.service.TbBaseInfoService;
import com.rongke.service.TbLogService;
import com.rongke.service.TbReceiverService;
import com.rongke.service.UserService;
import com.rongke.web.apix.TongDunSDKAuth;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tdSDKCallBack")
@Transactional
@CrossOrigin
public class TongDunSDKController {
    private Logger log = Logger.getLogger(this.getClass());
    /**
     * 同盾淘宝回调
     * @param notify_event
     * @param notify_type
     * @param notify_time
     * @param sign
     * @param notify_data
     */
    @Autowired
    private TbLogService tbLogService;
    @Autowired
    private TbBaseInfoService tbBaseInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private TbReceiverService tbReceiverService;
    @RequestMapping(value = "/tdSDKCallBack", method = RequestMethod.POST)
    public void tdTbSDKCallBack(String notify_data){
        try{
            log.debug("回调入参："+notify_data);
            JSONObject notify_dataJO = JSON.parseObject(notify_data);
            String taskId = notify_dataJO.getString("task_id");
            String code1 = notify_dataJO.getString("code");
            if("0".equals(code1)){
                JSONObject resultJO = TongDunSDKAuth.TongDunSDKQuery(taskId);
                if(resultJO != null){
                    Integer code = resultJO.getInteger("code");
                    if(code == 0){
                        JSONObject dataJO = resultJO.getJSONObject("data");
                        if(dataJO != null){
                            JSONObject taskDataJO = dataJO.getJSONObject("task_data");
                            if(taskDataJO != null){
                                TbLog tbLog = new TbLog();
                                JSONObject baseInfoJO = taskDataJO.getJSONObject("base_info");
                                if (baseInfoJO != null){
                                    EntityWrapper ew = new EntityWrapper();
                                    ew.eq("task_id",taskId);
                                    tbLog = tbLogService.selectOne(ew);
                                    TbBaseInfo tbBaseInfo = new TbBaseInfo();
                                    tbBaseInfo.setUserName(baseInfoJO.getString("user_name"));
                                    tbBaseInfo.setEmail(baseInfoJO.getString("email"));
                                    tbBaseInfo.setUserLevel(baseInfoJO.getString("user_level"));
                                    tbBaseInfo.setNickName(baseInfoJO.getString("nick_name"));
                                    tbBaseInfo.setName(baseInfoJO.getString("name"));
                                    tbBaseInfo.setGender(baseInfoJO.getString("gender"));
                                    tbBaseInfo.setMobile(baseInfoJO.getString("mobile"));
                                    tbBaseInfo.setRealName(baseInfoJO.getString("real_name"));
                                    tbBaseInfo.setIdentityCode(baseInfoJO.getString("identity_code"));
                                    tbBaseInfo.setVipCount(baseInfoJO.getString("vip_count"));
                                    tbBaseInfo.setUserId(tbLog.getUserId());
                                    tbBaseInfoService.insert(tbBaseInfo);
                                }
                                String receiver = taskDataJO.getString("receiver_list");
                                if (receiver != null){
                                    List<JSONObject> list = JSON.parseArray(receiver,JSONObject.class);
                                    if(list.size()>0){
                                        for(JSONObject j:list){
                                            TbReceiver tbReceiver = new TbReceiver();
                                            tbReceiver.setUserId(tbLog.getUserId());
                                            tbReceiver.setName(j.getString("name"));
                                            tbReceiver.setArea(j.getString("area"));
                                            tbReceiver.setAddress(j.getString("address"));
                                            tbReceiver.setDefaultArea(j.getString("default"));
                                            tbReceiver.setMobile(j.getString("mobile"));
                                            tbReceiver.setTelephone(j.getString("telephone"));
                                            tbReceiver.setZipCode(j.getString("zip_count"));
                                            tbReceiverService.insert(tbReceiver);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
