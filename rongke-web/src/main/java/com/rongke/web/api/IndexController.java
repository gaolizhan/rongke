package com.rongke.web.api;

/**
 * Created by bin on 2017/8/18.
 */

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.mapper.IndexMapper;
import com.rongke.model.LoanOrder;
import com.rongke.model.PersonRecord;
import com.rongke.service.LoanOrderService;
import com.rongke.service.PersonRecordService;
import com.rongke.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version : Ver 1.0
 * @AgreementController
 * @Controller(首页数据统计)
 */
@RestController
@RequestMapping(value = "/api/index")
@Transactional
@CrossOrigin
public class IndexController {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private IndexMapper indexMapper;
    @Autowired
    private PersonRecordService personRecordService;
    @Autowired
    private LoanOrderService loanOrderService;

    /**
     * @param
     * @return 返回值JsonResp
     * @报表统计
     */
    @RequestMapping(value = "/statementSelect", method = RequestMethod.GET)
    public JsonResp statementSelect(){
        log.debug("报表统计");
        List<LoanOrder> loanOrderList = loanOrderService.selectThisOrder();
        if (!loanOrderList.isEmpty()){
            LoanOrder loanOrder = loanOrderList.get(0);
            String condition = "{\"frms_ware_category\":\"2013\",\"user_info_mercht_userno\":\""+loanOrder.getUserId()+"\",\"user_info_dt_register\":\""+DateUtils.dateSimple(loanOrder.getUser().getGmtDatetime()).toString()+"\",\"user_info_bind_phone\":\""+loanOrder.getUser().getPhone()+"\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_full_name\":\""+loanOrder.getUser().getUserName()+"\",\"user_info_id_no\":\""+loanOrder.getUser().getIdNo()+"\"}";
            String a = DateUtils.dateSimple(loanOrderList.get(0).getUser().getGmtDatetime()).toString();
        }
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        map.put("time",format.format(date));
        PersonRecord personRecord = personRecordService.selectById(1);
        Integer userAddCount = indexMapper.userAddCount(map);
        Integer orderPassUserCount = indexMapper.orderPassUserCount(map);
        String moneyOutAll = indexMapper.moneyOutAll(map);
        String moneyBackAll = indexMapper.moneyBackAll(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("userAddCount",userAddCount);
        map1.put("orderPassUserCount",orderPassUserCount);
        map1.put("moneyBackAll",moneyBackAll);
        map1.put("moneyOutAll",moneyOutAll);
        map1.put("personRecord",personRecord);

        return JsonResp.ok(map1);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @报表统计
     */
    @RequestMapping(value = "/findMoneyStatistics", method = RequestMethod.GET)
    public JsonResp findMoneyStatistics(String year){
        log.debug("报表统计");
        EntityWrapper<LoanOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.like("give_time",year);
        EntityWrapper<LoanOrder> entityWrapper1 = new EntityWrapper<>();
        entityWrapper1.like("real_pay_time",year);
        List<LoanOrder> list = loanOrderService.selectList(entityWrapper);
        List<LoanOrder> list1 = loanOrderService.selectList(entityWrapper1);
        List<BigDecimal> outMoneys = new ArrayList<>();
        List<BigDecimal> backMoneys = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String year1 = year + "-0" + (i + 1);
            BigDecimal outMoney = new BigDecimal("0");
            BigDecimal backMoney = new BigDecimal("0");
            if (i >= 9) {
                year1 = year + "-" + (i + 1) + "";
            }
            for (LoanOrder loanOrder:list) {
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(loanOrder.getGiveTime());
                if(date.contains(year1)){
                    outMoney = outMoney.add(loanOrder.getRealMoney());
                }
            }
            for (LoanOrder loanOrder:list1) {
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (loanOrder.getGiveTime() == null) {
                    int a = 1;
                }
                String date = format.format(loanOrder.getRealPayTime());
                if(date.contains(year1)){
                    backMoney = backMoney.add(loanOrder.getRealPayMoney());
                }
            }
            outMoneys.add(outMoney);
            backMoneys.add(backMoney);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("outMoneys",outMoneys);
        map.put("backMoneys",backMoneys);
        return JsonResp.ok(map);

    }


}
