package com.rongke.mapper;

import com.rongke.model.LoanOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @LoanOrderMapper
 * @Mapper
 */
public interface LoanOrderMapper extends BaseMapper<LoanOrder> {
    List<LoanOrder> selectApplyLoanList(Map<String, Object> map);

    int selectApplyLoanListNum(Map<String, Object> map);

    int selectToPassMoneyListNum(Map<String, Object> map);

    List<LoanOrder> selectToPassMoneyList(Map<String, Object> map);

    List<LoanOrder> selectFailApplyList(Map<String, Object> map);

    int selectFailApplyListNum(Map<String, Object> map);

    List<LoanOrder> selectAllPassMoneyList(Map<String, Object> map);

    int selectAllPassMoneyListNum(Map<String, Object> map);

    List<LoanOrder> selectNormalRepaymentList(Map<String, Object> map);

    int selectNormalRepaymentListNum(Map<String, Object> map);

    double selectPeopleSum(Map<String, Object> map);

    double selectMoneySum(Map<String, Object> map);

    double selectPeopleSum1(Map<String, Object> map);

    Integer selectMoneySum1(Map<String, Object> map);

    double selectPeopleSum2(Map<String, Object> map);

    double selectMoneySum2(Map<String, Object> map);

    List<LoanOrder> selectBlackList(Map<String, Object> map);

    int selectBlackListNum(Map<String, Object> map);

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

    List<LoanOrder> selectOverDueList(Map<String, Object> map);

    int selectOverDueListNum(Map<String, Object> map);

    List<LoanOrder> overDuePaymentList(Map<String, Object> map);

    int overDuePaymentListNum(Map<String, Object> map);

    /**
     * 分页查询坏账列表
     *
     * @param map
     * @return
     */
    List<LoanOrder> selectBadDebtList(Map<String, Object> map);

    int selectBadDebtListNum(Map<String, Object> map);

    /**
     * 分页查询正常待还款列表
     *
     * @param map
     * @return
     */
    List<LoanOrder> selectNormalToPayList(Map<String, Object> map);

    int selectNormalToPayListNum(Map<String, Object> map);

    int selectFailToPassMoneyListNum(Map<String, Object> map);

    /**
     * 查询待打款失败列表
     *
     * @param map
     * @return
     */

    List<LoanOrder> selectFailToPassMoneyList(Map<String, Object> map);

    List<LoanOrder> selectThisOrder();

    BigDecimal sumAllWateMoney();

    List<LoanOrder> selectApplyByTime(String startTime, String endTime);

    List<LoanOrder> selectApplyStatus(Long id);

    List<LoanOrder> channelSelectOrder(Map<String, Object> map);

    Map<String, Object> channelSelectLoanOrderTotalAndMoney(Map<String, Object> map);

    List<LoanOrder> orderListChannel(Map<String, Object> map);

    List<LoanOrder> adminSelectLoanOrder(Map<String, Object> map);

    Map<String, Object> adminSelectLoanOrderTotalAndMoney(Map<String, Object> map);

    List<LoanOrder> userSelectLoanOrder(Map<String, Object> map);

    Map<String, Object> userSelectLoanOrderTotalAndMoney(Map<String, Object> map);

    List<LoanOrder> selectPassingList(Map<String, Object> map);

    Integer selectPassingNum(Map<String, Object> map);

    List<LoanOrder> selectMyAllPassingOrders(Map<String, Object> map);

    Map<String,Object> selectMyAllPassingOrdersNum(Map<String, Object> map);

    List<LoanOrder> selectAllOrders();

    List<LoanOrder> selectByOrderStatus(Integer orderStatus);

    List<Integer> selectPassLoanUserCount();

    List<LoanOrder> selectExpireOrderForExpireDate(List<Integer> expireDateList);

    int getOverdueCount(Long userId);
}
