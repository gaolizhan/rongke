package com.rongke.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rongke.model.Contract;
import com.rongke.model.EmergContact;
import com.rongke.model.PreApplyWithCreditVO;
import com.rongke.model.Present;
import com.rongke.model.RiskDataDTO;
import com.rongke.model.UserBasicMsg;
import com.rongke.model.UserIdentity;
import com.rongke.model.UserPhone;
import com.rongke.util.SignUtils;
import com.rongke.utils.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jodd.JoddDefault.encoding;

public class PaixuApiUtil {

    private final static String  MERCHANTNO = "510030";
    private final static String  APPKEY =  "10013";
    private final static String  PRODUCTID =  "10060";
    private final static String  URL = "http://risk.rank-tech.com/risk/loanApplyWithData";

    /**
     * 模拟请求
     *
     * @param
     * @param
     * @param
     * @return
     * @throws
     * @throws IOException
     */
    public static String preloan(JSONObject jsonObject) throws ParseException, IOException {

        UserIdentity userIdentity = jsonObject.getObject("userIdentity",UserIdentity.class);
        UserBasicMsg userBasicMsg = jsonObject.getObject("userBasicMsg",UserBasicMsg.class);
        UserPhone userPhone = jsonObject.getObject("userPhone",UserPhone.class);
        JSONArray list_userPhoneList = jsonObject.getJSONArray("list_userPhoneList");
        JSONObject rawInfo = jsonObject.getJSONObject("rawInfo");
        JSONObject rawreport = jsonObject.getJSONObject("rawReport");

        Present present =  new Present();
        present.setProvince(userBasicMsg.getProvince());
        present.setCity(userBasicMsg.getCity());
        present.setLiveAddr(userBasicMsg.getAddressDetails());

        List<EmergContact>  emergContacts =  new ArrayList<>(2);
        EmergContact  emergContactOne = new EmergContact();
        emergContactOne.setName(userBasicMsg.getLinkPersonNameOne());
        emergContactOne.setPhone(userBasicMsg.getLinkPersonPhoneOne());
        emergContactOne.setRelation(userBasicMsg.getLinkPersonRelationOne());
        EmergContact  emergContactTwo = new EmergContact();
        emergContactTwo.setName(userBasicMsg.getLinkPersonNameTwo());
        emergContactTwo.setPhone(userBasicMsg.getLinkPersonPhoneTwo());
        emergContactTwo.setRelation(userBasicMsg.getLinkPersonRelationTwo());
        emergContacts.add(emergContactOne);
        emergContacts.add(emergContactTwo);

        List<Contract> contracts = list_userPhoneList.stream().map(object->{
            JSONObject upList = (JSONObject)object;
            Contract contract  = new Contract();
            contract.setContact_name(upList.getString("name"));
            contract.setContact_phone(upList.getString("phone"));
            SimpleDateFormat sf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            contract.setUpdate_time(sf.format(upList.getDate("uptDatetime")));
            return contract;
        }).collect(Collectors.toList());

        RiskDataDTO riskDataDTO = new RiskDataDTO();
        riskDataDTO.setIdNum(userIdentity.getIdentityNum());
        riskDataDTO.setPresent(present);
        riskDataDTO.setContact(contracts);
        riskDataDTO.setEmergency_contacts(emergContacts);
        riskDataDTO.setMobile(userPhone.getPhone());
        riskDataDTO.setRawInfo(rawInfo);
        riskDataDTO.setRawReport(rawreport);
        SimpleDateFormat sf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        riskDataDTO.setApply_time(sf.format(new Date()));

        PreApplyWithCreditVO   preApplyWithCreditVO  = new PreApplyWithCreditVO();
        preApplyWithCreditVO.setCardNum(userIdentity.getIdentityNum());
        preApplyWithCreditVO.setMobile(userPhone.getPhone());
        preApplyWithCreditVO.setUserName(userIdentity.getUserName());
        preApplyWithCreditVO.setTimeStamp(sf.format(new Date()));
        preApplyWithCreditVO.setRiskData(riskDataDTO);
        preApplyWithCreditVO.setOrderId("" + System.currentTimeMillis() + RandomUtils.randomString(10));
        preApplyWithCreditVO.setProductId(PRODUCTID);
        preApplyWithCreditVO.setAppKey(APPKEY);
        preApplyWithCreditVO.setMerchantId(MERCHANTNO);
        preApplyWithCreditVO.setSign(SignUtils.getSign(preApplyWithCreditVO));


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);

        StringEntity sendentity = new StringEntity(JSON.toJSONString(preApplyWithCreditVO), "utf-8");//解决中文乱码问题
        sendentity.setContentEncoding("UTF-8");
        sendentity.setContentType("application/json");
        httpPost.setEntity(sendentity);
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String body = null;
        if (entity != null) {
            body = EntityUtils.toString(entity, encoding);

        }
        EntityUtils.consume(entity);
        response.close();
        return body;
    }

}
