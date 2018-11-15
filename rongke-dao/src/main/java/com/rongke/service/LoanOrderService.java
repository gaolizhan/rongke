package com.rongke.service;

import com.baomidou.mybatisplus.service.IService;
import com.rongke.model.LoanOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @LoanOrderService
 * @Service
 */
public interface LoanOrderService extends IService<LoanOrder> {
    /**
     * @查找申请贷款列表
     */
    List<LoanOrder>  selectApplyLoanList(Map<String,Object> map);




    /**
     * 查找申请贷款订单数量
     * @param map
     * @return
     */
    int selectApplyLoanListNum(Map<String, Object> map);

    /**
     * 查找待打款订单数量
     * @param map
     * @return
     */
    int selectToPassMoneyListNum(Map<String, Object> map);


    /**
     * 查询待打款列表
     * @param map
     * @return
     */

    List<LoanOrder> selectToPassMoneyList(Map<String, Object> map);




    /**
     * 查询打款失败数量
     * @param map
     * @return
     */
    int selectFailToPassMoneyListNum(Map<String, Object> map);


    /**
     * 查询待打款失败列表
     * @param map
     * @return
     */

    List<LoanOrder> selectFailToPassMoneyList(Map<String, Object> map);



    /**
     * 查询申请失败列表
     * @param map
     * @return
     */

    List<LoanOrder> selectFailApplyList(Map<String, Object> map);



    /**
     * 查询申请失败列表数量
     * @param map
     * @return
     */

    int selectFailApplyListNum(Map<String, Object> map);

    /**
     * 查询所有的放款列表
     * @param map
     * @return
     */
    List<LoanOrder> selectAllPassMoneyList(Map<String, Object> map);


    /**
     * 查询放款数量
     * @param map
     * @return
     */
    int selectAllPassMoneyListNum(Map<String, Object> map);




    List<LoanOrder> selectNormalRepaymentList(Map<String, Object> map);

    int selectNormalRepaymentListNum(Map<String, Object> map);



    double selectPeopleSum(Map<String, Object> map);

    double selectMoneySum(Map<String, Object> map);

    double selectPeopleSum1(Map<String, Object> map);

    Integer selectMoneySum1(Map<String, Object> map);

    double selectPeopleSum2(Map<String, Object> map);

    double selectMoneySum2(Map<String, Object> map);

    String selectPeopleSum3(Map<String, Object> map);

    String selectMoneySum3(Map<String, Object> map);

    Integer selectPeopleSum6(Map<String, Object> map);

    double selectMoneySum6(Map<String, Object> map);


    double selectPeopleSum7(Map<String, Object> map);

    double selectMoneySum7(Map<String, Object> map);



    double selectPeopleSum8(Map<String, Object> map);

    double selectMoneySum8(Map<String, Object> map);


    double selectPeopleSum9(Map<String, Object> map);

    double selectMoneySum9(Map<String, Object> map);





    /**
     * 黑名单查询
     * @param map
     * @return
     */
    List<LoanOrder> selectBlackList(Map<String, Object> map);

    int selectBlackListNum(Map<String, Object> map);




    /**
     * 分页查询逾期列表
     * @param map
     * @return
     */
    List<LoanOrder> selectOverDueList(Map<String, Object> map);


    int selectOverDueListNum(Map<String, Object> map);


    /**
     * 分页查询逾期结清列表
     * @param map
     * @return
     */
    List<LoanOrder> overDuePaymentList(Map<String, Object> map);


    int overDuePaymentListNum(Map<String, Object> map);

    /**
     * 分页查询坏账列表
     * @param map
     * @return
     */
    List<LoanOrder> selectBadDebtList(Map<String, Object> map);


    int selectBadDebtListNum(Map<String, Object> map);




    /**
     * 分页查询正常待还款列表
     * @param map
     * @return
     */
    List<LoanOrder> selectNormalToPayList(Map<String, Object> map);


    int selectNormalToPayListNum(Map<String, Object> map);


    List<LoanOrder> selectThisOrder();


    BigDecimal sumAllWateMoney();

    List<LoanOrder> selectApplyByTime(String startTime,String endTime);

    List<LoanOrder> selectApplyStatus(Long id);

    /**
     *渠道商查询自己会员订单
     * @param
     * @return
     */
    List<LoanOrder> channelSelectOrder(Integer status,String phone,String channelId ,String gmtDatetime,Integer currentPage);
    Map<String, Object> channelSelectLoanOrderTotalAndMoney(Integer status,String phone,String channelId ,String gmtDatetime);
    /**
     *时间删选渠道商会员订单
     * @param map
     * @return
     */
    List<LoanOrder> orderListChannel(Map<String, Object> map);
    /**
     *各管理员(分页)查询订单
     * @param
     * @return
     */
    List<LoanOrder> adminSelectLoanOrder(Integer status,String phone,String channelName ,String gmtDatetime,Integer currentPage);

    /**
     *各管理员查询订单总数,总放款额，总分成利润
     * @param
     * @return
     */
    Map<String,Object> adminSelectLoanOrderTotalAndMoney(Integer status,String phone,String channelName ,String gmtDatetime);

    /**
     *各管理员(分页)查询订单
     * @param
     * @return
     */
    List<LoanOrder> userSelectLoanOrder(Integer status,String phone,String gmtDatetime,Integer currentPage,Integer payStatus,Integer renewalStatus,Integer moneyBackStatus,String realPayTime,String limitPayTime,String name,Integer giveStatus,String extendSign);

    /**
     *各管理员(分页)查询订单
     * @param
     * @return
     */
    List<LoanOrder> userSelectLoanOrder(Map<String,Object> map);

    /**
     *各管理员查询订单总数,总放款额
     * @param
     * @return
     */
    Map<String,Object> userSelectLoanOrderTotalAndMoney(Integer status,String phone,String gmtDatetime,Integer payStatus,Integer renewalStatus,Integer moneyBackStatus,String realPayTime,String limitPayTime,String name,Integer giveStatus,String extendSign);

    List<LoanOrder> selectPassingList(Map<String, Object> map);
    Integer selectPassingNum(Map<String, Object> map);


    List<LoanOrder>selectMyAllPassingOrders(Map<String,Object>map);
    Map<String,Object> selectMyAllPassingOrdersNum(Map<String,Object>map);


    List<LoanOrder>selectAllOrders();

    List<LoanOrder> selectByOrderStatus(Integer orderStatus);

    List<Integer> selectPassLoanUserCount();


    List<LoanOrder> selectExpireOrderForExpireDate(List<Integer> expireDateList);

    int getOverdueCount(Long userId);
}
