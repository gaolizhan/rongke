package com.rongke.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.annotation.SourceAuthority;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.SourceType;
import com.rongke.model.*;
import com.rongke.service.*;
import com.rongke.utils.DateUtils;
import com.rongke.utils.OrderUtils;
import com.rongke.utils.RandomUtils;
import com.rongke.web.lianpay.FenqiPayment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @OrderExtendController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/orderExtend")
@Transactional
@CrossOrigin
public class OrderExtendController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private OrderExtendService orderExtendService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private ParamSettingService paramSettingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBankService userBankService;

    /**
     * @添加
     * @param orderExtend
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addOrderExtend(@RequestBody OrderExtend orderExtend){
        log.debug("添加");
        orderExtendService.insert(orderExtend);
        return JsonResp.ok(orderExtend);
    }

    /**
     * @修改
     * @param orderExtend
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateOrderExtend(@RequestBody OrderExtend orderExtend){
        log.debug("修改");
        orderExtendService.updateById(orderExtend);
        return JsonResp.ok(orderExtend);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectOrderExtend(Long id){
        log.debug("查找");
        OrderExtend orderExtend = orderExtendService.selectById(id);
        return JsonResp.ok(orderExtend);
    }

    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * @续期界面信息
     * @param
     * @return 返回值JsonResp
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/extendMsg", method = RequestMethod.GET)
    public JsonResp extendMsg(Long id,Integer limitDays){
        log.debug("续期界面信息");
        LoanOrder loanOrder = loanOrderService.selectById(id);

            //计算费用
            EntityWrapper<ParamSetting> ew = new EntityWrapper<>();
            ew.eq("limit_days", limitDays);
            ew.eq("status", 1);
            ParamSetting paramSetting = paramSettingService.selectOne(ew);
            BigDecimal interestMoney = loanOrder.getBorrowMoney().multiply(BigDecimal.valueOf(paramSetting.getInterestPercent()*0.001));
            BigDecimal placeServeMoney = loanOrder.getBorrowMoney().multiply(BigDecimal.valueOf(paramSetting.getPlaceServePercent()*0.01));
            BigDecimal msgAuthMoney = loanOrder.getBorrowMoney().multiply(BigDecimal.valueOf(paramSetting.getMsgAuthPercent()*0.01));
            BigDecimal riskServeMoney = loanOrder.getBorrowMoney().multiply(BigDecimal.valueOf(paramSetting.getRiskServePercent()*0.01));
            BigDecimal riskPlanMoney = loanOrder.getBorrowMoney().multiply(BigDecimal.valueOf(paramSetting.getRiskPlanPercent()*0.01));
            BigDecimal allWasteMoney = interestMoney.add(placeServeMoney).add(msgAuthMoney).add(riskServeMoney).add(riskPlanMoney);

            EntityWrapper<SystemConfig>entityWrapper=new EntityWrapper<>();//续期手续费
            entityWrapper.eq("config_key","xuqiMoney");
            SystemConfig systemConfig=systemConfigService.selectOne(entityWrapper);

            // 获取手续费费率
            BigDecimal xuqiMoneyRate=new BigDecimal(systemConfig.getConfigValue());
            // 计算手续费
            BigDecimal xuqiMoney = loanOrder.getNeedPayMoney().multiply(xuqiMoneyRate).divide(new BigDecimal(100)).setScale(2);
            // 手续费小于1 默认设置为1
            BigDecimal defaultMoney  = new BigDecimal(1);
            if(xuqiMoney == null || xuqiMoney.compareTo(defaultMoney) < 0 ){
                xuqiMoney  = defaultMoney;
            }

            Map map = new HashMap();
            if(loanOrder!=null){
                map.put("limitPayTime",DateUtils.dayAdd(limitDays-1+loanOrder.getOverdueDays(),loanOrder.getLimitPayTime()));
                map.put("allWasteMoney",allWasteMoney);
                map.put("needPayMoney",loanOrder.getNeedPayMoney());
                map.put("interestMoney",interestMoney);
                map.put("overdueMoney",loanOrder.getOverdueMoney());
                //allWasteMoney.add(loanOrder.getOverdueMoney()).add(xuqiMoney)
                //map.put("extendMoney",0.10);
				map.put("extendMoney", allWasteMoney.add(loanOrder.getOverdueMoney()).add(xuqiMoney));
				map.put("xuqiMoney",xuqiMoney);
            }
            return JsonResp.ok(map);

    }

    /**
     * @续期支付界面信息
     * @param
     * @return 返回值JsonResp
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/extendPayMsg", method = RequestMethod.GET)
    public JsonResp extendPayMsg(Long id){
        log.debug("续期支付界面信息");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        Map map =new HashMap();
        map.put("bankNum",loanOrder.getBankCardNum());
        map.put("bankName",loanOrder.getBankName());
        map.put("orderId",loanOrder.getId());
        return JsonResp.ok(map);
    }


    /**
     * @获取续期签约信息
     * @return 返回值JsonResp
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/getSignMsg", method = RequestMethod.GET)
    public JsonResp getSignMsg(String orderId,String money,Integer limitDays,String payPwd)  {
        log.debug("获取续期签约信息");
        User user = userService.findLoginUser();
        //User user = userService.selectById(96);
//        if(user.getPayPwd().equals(payPwd)){
        if(true){
            LoanOrder loanOrder = loanOrderService.selectById(orderId);
            EntityWrapper<UserBank> ewBank = new EntityWrapper();
            ewBank.eq("user_id",user.getId());
            UserBank userBank = userBankService.selectOne(ewBank);
            if(loanOrder!=null){
                Map map = new HashMap();
                String orderNo = "XQ"+ OrderUtils.getOrderNo();
                map.put("no_order",orderNo);
                map.put("money",money);
                map.put("bankNum",userBank.getBankcardno());
                map.put("idCard",userBank.getIdcardno());
                map.put("name",user.getUserName());
                map.put("orderId",loanOrder.getId());
                map.put("userId",user.getUuid());
                //续期表储存
                OrderExtend orderExtend = new OrderExtend();
                orderExtend.setOrderId(loanOrder.getId());
                orderExtend.setExtendDays(limitDays);
                orderExtend.setExtendMoney(new BigDecimal(money));
                orderExtend.setExtendLianlianNum(orderNo);
                orderExtendService.insert(orderExtend);
                map.put("extendId",orderExtend.getId());
                //风控参数
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String condition = "{\"frms_ware_category\":\"2010\",\"user_info_mercht_userno\":\""+user.getUuid()+"\",\"user_info_dt_register\":\""+dataFormat.format(user.getGmtDatetime())+"\",\"user_info_bind_phone\":\""+user.getPhone()+"\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_full_name\":\""+userBank.getName()+"\",\"user_info_id_no\":\""+userBank.getIdcardno()+"\"}";
                map.put("risk_item",condition);
                return JsonResp.ok(map);
            }else{
                return JsonResp.fa("该用户当前无还款订单");
            }
        }else{
            return JsonResp.fa("支付密码不正确");
        }

    }

    /**
     * @签约授权
     * @return 返回值JsonResp
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value="/getSignPass", method = RequestMethod.GET)
    public JsonResp getSignPass(String orderId,String extendId) throws ParseException {
        log.debug("签约授权");
        String  repayment_no = RandomUtils.randomString(7);//还款计划编号
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        User user = userService.selectById(loanOrder.getUserId());
        //存储续约所需信息
        OrderExtend orderExtend = orderExtendService.selectById(extendId);
        orderExtend.setRepaymentNo(repayment_no);
        orderExtendService.updateById(orderExtend);
        //授权
        String resJson = FenqiPayment.agreenoauthapply(loanOrder.getNoAgree(),repayment_no,orderExtend.getExtendMoney().toString(), DateUtils.dateSimple(loanOrder.getLimitPayTime()).toString(),user.getId().toString());
        JSONObject jsonObject = JSONObject.parseObject(resJson);
        if(jsonObject.get("ret_code").toString().equals("0000")){
            return JsonResp.ok(jsonObject.get("ret_msg").toString());
        }else{
            return JsonResp.fa(jsonObject.get("ret_msg").toString());
        }

    }


    @RequestMapping(value="/selectOrderExtendList", method = RequestMethod.GET)
    public JsonResp selectOrderExtend(Page page, String phone, String userName) throws ParseException {
        //EntityWrapper<OrderExtend> userEntityWrapper = new EntityWrapper<>();
        HashMap<String, Object> paramMap = new HashMap<>();

        paramMap.put("pageSize",page.getSize());
        paramMap.put("pageNo",(page.getCurrent()-1)*page.getSize());
        paramMap.put("phone",phone);
        paramMap.put("userName",userName);

        List<OrderExtend> orderExtendList = orderExtendService.getPage(paramMap);
        int count = orderExtendService.getPageCount(paramMap);
        String extendTotalAmount = orderExtendService.getExtendTotalAmount(paramMap);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageDto",new PageDto(page.getCurrent(), page.getSize(), orderExtendList, count));
        resultMap.put("extendTotalAmount",extendTotalAmount);
        return JsonResp.ok(resultMap);
    }


}
