package com.rongke.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.mapper.LoanOrderMapper;
import com.rongke.model.LoanOrder;
import com.rongke.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @LoanOrderServiceImpl
 * @ServiceImpl
 */
@Service
public class LoanOrderServiceImpl extends ServiceImpl<LoanOrderMapper, LoanOrder> implements LoanOrderService {
    @Autowired
    private LoanOrderMapper loanOrderMapper;
    /**
     * @查询申请贷款列表
     */
    public List<LoanOrder> selectApplyLoanList(Map<String,Object> map){
        return loanOrderMapper.selectApplyLoanList(map);
    }


   /**
     * 查找申请贷款订单数量
     * @param map
     * @return
     */
   public int selectApplyLoanListNum(Map<String, Object> map){
       return  loanOrderMapper.selectApplyLoanListNum(map);
   }

    /**
     * 查找待打款订单数量
     * @param map
     * @return
     */
   public int selectToPassMoneyListNum(Map<String, Object> map){
       return  loanOrderMapper.selectToPassMoneyListNum(map);
   }

    /**
     * 查找所有的待打款订单
     * @param map
     * @return
     */
   public  List<LoanOrder>  selectToPassMoneyList(Map<String, Object> map){
         return loanOrderMapper.selectToPassMoneyList(map);
   }


    /**
     * 查找打款失败订单数量
     * @param map
     * @return
     */
    public int selectFailToPassMoneyListNum(Map<String, Object> map){
        return  loanOrderMapper.selectFailToPassMoneyListNum(map);
    }

    /**
     * 查找所有的打款失败订单
     * @param map
     * @return
     */
    public  List<LoanOrder>  selectFailToPassMoneyList(Map<String, Object> map){
        return loanOrderMapper.selectFailToPassMoneyList(map);
    }

    /**
     * 查询申请失败列表数量
     * @param map
     * @return
     */
   public int selectFailApplyListNum(Map<String, Object> map){
       return loanOrderMapper.selectFailApplyListNum(map);
   }


    /**
     * 查询申请失败列表
     * @param map
     * @return
     */
    public List<LoanOrder> selectFailApplyList(Map<String, Object> map){

        return loanOrderMapper.selectFailApplyList(map);
    }


    /**
     * 查询所有的打款列表
     * @param map
     * @return
     */
    public List<LoanOrder> selectAllPassMoneyList(Map<String, Object> map){
        return  loanOrderMapper.selectAllPassMoneyList(map);
    }

    public int selectAllPassMoneyListNum(Map<String, Object> map){
        return loanOrderMapper.selectAllPassMoneyListNum(map);
    }


    public  List<LoanOrder> selectNormalRepaymentList(Map<String, Object> map){
        return loanOrderMapper.selectNormalRepaymentList(map);
    }
    public int selectNormalRepaymentListNum(Map<String, Object> map){
        return loanOrderMapper.selectNormalRepaymentListNum(map);
    }


    public double selectPeopleSum(Map<String, Object> map){
        return loanOrderMapper.selectPeopleSum(map);
    }

    public double selectMoneySum(Map<String, Object> map){
        return loanOrderMapper.selectMoneySum(map);
    }

    public double selectPeopleSum1(Map<String, Object> map){
        return loanOrderMapper.selectPeopleSum1(map);
    }

    public Integer selectMoneySum1(Map<String, Object> map){
        return loanOrderMapper.selectMoneySum1(map);
    }

    public double selectPeopleSum2(Map<String, Object> map){  return loanOrderMapper.selectPeopleSum2(map);  }

    public double selectMoneySum2(Map<String, Object> map){
        return loanOrderMapper.selectMoneySum2(map);
    }

    public String selectPeopleSum3(Map<String, Object> map){ return loanOrderMapper.selectPeopleSum3(map); }

    public String selectMoneySum3(Map<String, Object> map){ return loanOrderMapper.selectMoneySum3(map);    }

    public Integer selectPeopleSum6(Map<String, Object> map){ return loanOrderMapper.selectPeopleSum6(map); }

    public double selectMoneySum6(Map<String, Object> map){ return loanOrderMapper.selectMoneySum6(map); }

    public double selectPeopleSum7(Map<String, Object> map){ return loanOrderMapper.selectPeopleSum7(map); }

    public double selectMoneySum7(Map<String, Object> map){ return loanOrderMapper.selectMoneySum7(map); }

    public double selectPeopleSum8(Map<String, Object> map){ return loanOrderMapper.selectPeopleSum8(map); }

    public double selectMoneySum8(Map<String, Object> map){ return loanOrderMapper.selectMoneySum8(map); }

    public double selectPeopleSum9(Map<String, Object> map){ return loanOrderMapper.selectPeopleSum9(map); }

    public double selectMoneySum9(Map<String, Object> map){ return loanOrderMapper.selectMoneySum9(map); }


    /**
     * 黑名单查询
     * @param map
     * @return
     */

    public List<LoanOrder> selectBlackList(Map<String, Object> map){
        return  loanOrderMapper.selectBlackList(map);
    }

    public int selectBlackListNum(Map<String, Object> map){
        return loanOrderMapper.selectBlackListNum(map);
    }


   public List<LoanOrder> selectOverDueList(Map<String, Object> map){
    return loanOrderMapper.selectOverDueList(map);
   }


  public  int selectOverDueListNum(Map<String, Object> map){
      return loanOrderMapper.selectOverDueListNum(map);
  }

    public List<LoanOrder> overDuePaymentList(Map<String, Object> map){
        return loanOrderMapper.overDuePaymentList(map);
    }


    public  int overDuePaymentListNum(Map<String, Object> map){
        return loanOrderMapper.overDuePaymentListNum(map);
    }

    /**
     * 分页查询坏账列表
     * @param map
     * @return
     */
    public List<LoanOrder> selectBadDebtList(Map<String, Object> map){
        return  loanOrderMapper.selectBadDebtList(map);
    }


    public int selectBadDebtListNum(Map<String, Object> map){
        return loanOrderMapper.selectBadDebtListNum(map);
    }



    public List<LoanOrder> selectNormalToPayList(Map<String, Object> map){
        return  loanOrderMapper.selectNormalToPayList(map);
    }


    public int selectNormalToPayListNum(Map<String, Object> map){
        return loanOrderMapper.selectNormalToPayListNum(map);
    }


    public List<LoanOrder> selectThisOrder(){
        return loanOrderMapper.selectThisOrder();
    };

    public BigDecimal sumAllWateMoney(){
        return loanOrderMapper.sumAllWateMoney();
    }

    @Override
    public List<LoanOrder> selectApplyByTime(String startTime, String endTime) {
        return loanOrderMapper.selectApplyByTime(startTime,endTime);
    }

    @Override
    public List<LoanOrder> selectApplyStatus(Long id) {
        return loanOrderMapper.selectApplyStatus(id);
    }


    /**
     *渠道商查询自己会员订单
     * @param
     * @return
     */
    public List<LoanOrder> channelSelectOrder(Integer status,String phone,String channelId ,String gmtDatetime,Integer currentPage){
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        map.put("phone",phone);
        map.put("gmtDatetime",gmtDatetime);
        if(!StringUtils.isEmpty(gmtDatetime)){
            map.put("gmtDatetime1",gmtDatetime.split("~")[0]);
            map.put("gmtDatetime2",gmtDatetime.split("~")[1]);
        }
        map.put("channelId",channelId);
        map.put("pageNo",(currentPage-1)*10);
        map.put("pageSize",10);
        return loanOrderMapper.channelSelectOrder(map);
    }
    /**
     *渠道商查询自己会员订单(总数)
     * @param
     * @return
     */
    public Map<String,Object> channelSelectLoanOrderTotalAndMoney(Integer status,String phone,String channelId ,String gmtDatetime){
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        map.put("phone",phone);
        map.put("gmtDatetime",gmtDatetime);
        map.put("channelId",channelId);
        return loanOrderMapper.channelSelectLoanOrderTotalAndMoney(map);
    };
    /**
     *时间删选渠道商会员订单
     * @param map
     * @return
     */
    public List<LoanOrder> orderListChannel(Map<String, Object> map){
        return loanOrderMapper.orderListChannel(map);
    };

    /**
     *各管理员查询订单
     * @param
     * @return
     */
    public List<LoanOrder> adminSelectLoanOrder(Integer status,String phone,String channelName ,String gmtDatetime,Integer currentPage){
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        map.put("phone",phone);
        map.put("gmtDatetime",gmtDatetime);
        map.put("channelName",channelName);
        map.put("pageNo",(currentPage-1)*10);
        map.put("pageSize",10);
        List<LoanOrder> list = loanOrderMapper.adminSelectLoanOrder(map);
        return list;
    }

    /**
     *各管理员查询订单总数,总放款额，总分成利润
     * @param
     * @return
     */
    public Map<String,Object> adminSelectLoanOrderTotalAndMoney(Integer status,String phone,String channelName ,String gmtDatetime){
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        map.put("phone",phone);
        map.put("gmtDatetime",gmtDatetime);
        map.put("channelName",channelName);
        Map<String,Object> map1 = loanOrderMapper.adminSelectLoanOrderTotalAndMoney(map);
        return map1;
    }

    /**
     *各管理员查询订单
     * @param
     * @return
     */
    public List<LoanOrder> userSelectLoanOrder(Integer status,String phone,String gmtDatetime,Integer currentPage,Integer payStatus,Integer renewalStatus,Integer moneyBackStatus,String realPayTime,String limitPayTime,String name,Integer giveStatus,String extendSign){
        Map<String,Object> map = new HashMap<>();
        map.put("extendSign",extendSign);
        map.put("status",status);
        map.put("phone",phone);
        map.put("pageNo",(currentPage-1)*10);
        map.put("pageSize",50);
        map.put("payStatus",payStatus);
        map.put("renewalStatus",renewalStatus);
        map.put("moneyBackStatus",moneyBackStatus);
        map.put("name",name);
        String gmtDatetime1 = "";
        String gmtDatetime2 = "";
        String realPayTime1 = "";
        String realPayTime2 = "";
        String limitPayTime1 = "";
        String limitPayTime2 = "";
        if (gmtDatetime != null && !gmtDatetime.equals("")) {
            String[] array = gmtDatetime.split("~");
            gmtDatetime1 = array[0];
            gmtDatetime2 = array[1];
            //map.put("orderByDate",1);
        }
//        else{
//            map.put("orderByDate",2);
//        }
        if (realPayTime != null && !realPayTime.equals("")) {
            String[] array = realPayTime.split("~");
            realPayTime1 = array[0];
            realPayTime2 = array[1];
        }
        if (limitPayTime != null && !limitPayTime.equals("")) {
            String[] array = limitPayTime.split("~");
            limitPayTime1 = array[0];
            limitPayTime2 = array[1];
        }
        map.put("orderByDate",1);
        map.put("gmtDatetime1",gmtDatetime1);
        map.put("gmtDatetime2",gmtDatetime2);
        map.put("realPayTime1",realPayTime1);
        map.put("realPayTime2",realPayTime2);
        map.put("limitPayTime1",limitPayTime1);
        map.put("limitPayTime2",limitPayTime2);
        map.put("gmtDatetime",gmtDatetime);
        map.put("realPayTime",realPayTime);
        map.put("limitPayTime",limitPayTime);
        map.put("giveStatus",giveStatus);

        List<LoanOrder> list = loanOrderMapper.userSelectLoanOrder(map);
        return list;
    }

    /**
     *各管理员查询订单
     * @param
     * @return
     */
    public List<LoanOrder> userSelectLoanOrder(Map<String,Object> map){
        List<LoanOrder> list = loanOrderMapper.userSelectLoanOrder(map);
        return list;
    }

    /**
     *各管理员查询订单总数,总放款额，总分成利润
     * @param
     * @return
     */
    public Map<String,Object> userSelectLoanOrderTotalAndMoney(Integer status,String phone,String gmtDatetime,Integer payStatus,Integer renewalStatus,Integer moneyBackStatus,String realPayTime,String limitPayTime,String name,Integer giveStatus,String extendSign){
        Map<String,Object> map = new HashMap<>();
        map.put("extendSign",extendSign);
        map.put("status",status);
        map.put("phone",phone);
        map.put("gmtDatetime",gmtDatetime);
        map.put("payStatus",payStatus);
        map.put("renewalStatus",renewalStatus);
        map.put("moneyBackStatus",moneyBackStatus);
        map.put("name",name);
        map.put("giveStatus",giveStatus);
        String gmtDatetime1 = "";
        String gmtDatetime2 = "";
        String realPayTime1 = "";
        String realPayTime2 = "";
        String limitPayTime1 = "";
        String limitPayTime2 = "";
        if (gmtDatetime != null && !gmtDatetime.equals("")) {
            String[] array = gmtDatetime.split("~");
            gmtDatetime1 = array[0];
            gmtDatetime2 = array[1];
        }
        if (realPayTime != null && !realPayTime.equals("")) {
            String[] array = realPayTime.split("~");
            realPayTime1 = array[0];
            realPayTime2 = array[1];
        }
        if (limitPayTime != null && !limitPayTime.equals("")) {
            String[] array = limitPayTime.split("~");
            limitPayTime1 = array[0];
            limitPayTime2 = array[1];
        }
        map.put("gmtDatetime1",gmtDatetime1);
        map.put("gmtDatetime2",gmtDatetime2);
        map.put("realPayTime1",realPayTime1);
        map.put("realPayTime2",realPayTime2);
        map.put("limitPayTime1",limitPayTime1);
        map.put("limitPayTime2",limitPayTime2);
        map.put("gmtDatetime",gmtDatetime);
        map.put("realPayTime",realPayTime);
        map.put("limitPayTime",limitPayTime);
        map.put("giveStatus",giveStatus);
        Map<String,Object> map1 = loanOrderMapper.userSelectLoanOrderTotalAndMoney(map);
        return map1;
    }



    public List<LoanOrder> selectPassingList(Map<String, Object> map){
        return loanOrderMapper.selectPassingList(map);
    }

    public Integer selectPassingNum(Map<String, Object> map){
        return loanOrderMapper.selectPassingNum(map);
    }

    public  List<LoanOrder>selectMyAllPassingOrders(Map<String,Object>map){
        return loanOrderMapper.selectMyAllPassingOrders(map);
    }

    public Map<String,Object> selectMyAllPassingOrdersNum(Map<String,Object>map){
        return loanOrderMapper.selectMyAllPassingOrdersNum(map);
    }

    public List<LoanOrder>selectAllOrders(){
        return loanOrderMapper.selectAllOrders();
    }

    @Override
    public List<LoanOrder> selectByOrderStatus(Integer orderStatus) {
        return loanOrderMapper.selectByOrderStatus(orderStatus);
    }

    @Override
    public List<Integer> selectPassLoanUserCount() {
        return loanOrderMapper.selectPassLoanUserCount();
    }

    @Override
    public List<LoanOrder> selectExpireOrderForExpireDate(List<Integer> expireDateList) {
        return loanOrderMapper.selectExpireOrderForExpireDate(expireDateList);
    }
    @Override
    public int getOverdueCount(Long userId){
        return loanOrderMapper.getOverdueCount(userId);
    }
}
