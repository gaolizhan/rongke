package com.rongke.web.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.constant.MsgTemplate;
import com.rongke.commons.constant.PushTemplate;
import com.rongke.model.LoanOrder;
import com.rongke.model.OrderExtend;
import com.rongke.model.PersonRecord;
import com.rongke.model.User;
import com.rongke.service.ChannelService;
import com.rongke.service.LoanOrderService;
import com.rongke.service.OrderExtendService;
import com.rongke.service.PersonRecordService;
import com.rongke.service.UserService;
import com.rongke.utils.DateUtils;
import com.rongke.web.api.LoanOrderController;
import com.rongke.web.api.UserController;
import com.rongke.web.lianpay.bean.BankCardPayBean;
import com.rongke.web.lianpay.util.SignUtil;
import com.rongke.web.util.YunpianSmsUtil;
import com.rongke.yibaoApi.RemittanceApi;
import com.rongke.yibaoApi.RepaymentApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 这个Controller需放到服务器上运行调试 异步回调接口 
 * 用于接收连连付款状态 只有生成连连支付单的订单才会有异步回调通知，
 * 没生成连连支付单的订单连连是不会提交到银行渠道走付款流程
 * 
 * 用spring mvc获取需要配置下面 或者参考另一个文件夹的异步回调demo从http request 数据流中获取
 * <mvc:annotation-driven> <mvc:message-converters register-defaults="false">
 * <!-- 避免IE执行AJAX时,返回JSON出现下载文件 --> <bean id="fastJsonHttpMessageConverter"
 * class=
 * "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter "
 * > <property name="supportedMediaTypes"> <list>
 * <value>text/html;charset=UTF-8</value>
 * <value>application/json;charset=UTF-8</value>
 * <value>text/json;charset=UTF-8</value> </list> </property> </bean>
 * </mvc:message-converters> </mvc:annotation-driven>
 * 
 * @author lihp
 * @date 2017-3-17 上午09:55:30
 */

@Controller
@RequestMapping(value="/api/lianpay")
public class ImmediatePayNotifyController {

	private static final Logger logger = LoggerFactory.getLogger(ImmediatePayNotifyController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private LoanOrderService loanOrderService;
	@Autowired
	private UserController userController;
	@Autowired
	private LoanOrderController loanOrderController;
	@Autowired
	private OrderExtendService orderExtendService;
	@Autowired
	private PersonRecordService personRecordService;




	@Autowired
	private ChannelService channelService;


	/**
	 * 实时支付平台异步通知更新(打款)
	 *
	 * @param
	 * @return
	 */
	/**
	 * 实时支付平台异步通知更新(打款)
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/tradepayapi/receiveNotify",
			method = RequestMethod.POST)
	public void receiveNotify(HttpServletRequest request,HttpServletResponse response)throws java.text.ParseException {
		logger.error("易宝打款===============================================================================================");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = RemittanceApi.remittanceNotifyCreVerify(request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		if ("S".equals(jsonObject.getString("status"))) {
			// TODO 商户更新订单为成功，处理自己的业务逻辑
			logger.info("商户更新订单为成功，处理自己的业务逻辑");
			String orderId = jsonObject.getString("orderId");
         /*   EntityWrapper<LoanOrder>entityWrapper=new EntityWrapper<>();
            entityWrapper.eq("order_number",orderId);
            LoanOrder loanOrder=loanOrderService.selectOne(entityWrapper);*/
            userController.payOrder(new Long(orderId));

				ServletOutputStream out = null;
				try {
					out = response.getOutputStream();
					out.print("S");
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}




	/**
	 *
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/renzhenPay",
			method = RequestMethod.POST)
	@ResponseBody
	public synchronized String renzhenPay(HttpServletRequest req, HttpServletResponse resp,HttpServletResponse response) throws Exception {
		logger.debug("绑卡支付回调————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
		//易宝绑卡支付回调返回参数解密
		Map<String, String> params = RepaymentApi.decodeParams(req);

		if ("PAY_SUCCESS".equals(params.get("status"))) {
			String requestno = params.get("requestno");
			EntityWrapper<LoanOrder>entityWrapper=new EntityWrapper<>();
			entityWrapper.eq("request_no",requestno);
			LoanOrder loanOrder1=loanOrderService.selectOne(entityWrapper);
			User user = userService.selectById(loanOrder1.getUserId());
			//判断是续期还是还款

			if (loanOrder1 != null) {

			if (loanOrder1.getType() == 1) {//续期

				//userController.xuqiOrder(loanOrder.getId(),loanOrder.getNeedPayMoney());

				LoanOrder loanOrder = loanOrderService.selectById(loanOrder1.getId());
				OrderExtend orderExtend = new OrderExtend();
				orderExtend.setExtendMoney(loanOrder.getNeedPayMoney());
				orderExtend.setOrderId(Long.valueOf(loanOrder.getId()));
				orderExtend.setStatus(1);
				orderExtend.setExtendDays(loanOrder.getLimitDays());
				orderExtendService.insert(orderExtend);
				loanOrder.setOrderStatus(3);
				loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(loanOrder.getNeedPayMoney()));
				loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(loanOrder.getOverdueMoney()));
				loanOrder.setWateMoney(loanOrder.getWateMoney().subtract(loanOrder.getOverdueMoney()));
				//时间
				loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays()-1+loanOrder.getOverdueDays(),loanOrder.getLimitPayTime()));
				loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays()-1+loanOrder.getOverdueDays(),loanOrder.getOverdueTime()));
				//清除逾期的金额天数
				loanOrder.setOverdueDays(0);
				loanOrder.setOverdueMoney(new BigDecimal(0));
				loanOrder.setAllowDays(0);
				loanOrder.setAllowMoney(new BigDecimal(0));
				loanOrderService.updateById(loanOrder);
				logger.debug("续期成功");
			} else {//还款
              // userController.repayOrder(loanOrder.getId());

            	logger.debug("结清订单");
				LoanOrder loanOrder = loanOrderService.selectById(loanOrder1.getId());
				User user1 = userService.selectById(loanOrder.getUserId());
				if (user1.getIsPay()==0){
					return "已还款!";
				}
				loanOrder.setOrderStatus(6);
				try {
					if(DateUtils.YYMMDDDate(new Date()).getTime()>loanOrder.getOverdueTime().getTime()){
						loanOrder.setPayStatus(2);//超出容限期还款
						user.setMoney(loanOrder.getBorrowMoney());
					}else{
						loanOrder.setPayStatus(1);//正常还款
						user.setMoney(loanOrder.getBorrowMoney());
						//user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()).add(BigDecimal.valueOf(500)));
					}
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(loanOrder.getBorrowMoney()));
				loanOrder.setRealPayTime(new Date());
				loanOrderService.updateById(loanOrder);

				user.setIsPay(0);
				user.setIsOld(1);
				userService.updateById(user);

				//统计
				EntityWrapper<PersonRecord> ew = new EntityWrapper();
				PersonRecord personRecord = personRecordService.selectOne(ew);
				personRecord.setOverOrderCount(personRecord.getOverOrderCount()+1);
				personRecordService.updateById(personRecord);
				//发送账单已处理短信，和推送
				try {
					logger.info("账单处理完成，发送提醒消息"+ user.getId());
					YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_EXAMINE_PASS,user.getPhone(),null);
					List<String> userIds = new ArrayList<>();
					userIds.add(user.getId().toString());
					int status = JpushClientUtil.sendToAliasId(userIds,
							PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, MsgTemplate.PAYMENT_SUCCESS_SMS, "");
				}catch (Exception e){
					logger.error("发送还款成功消息异常",e);
				}

				logger.debug("还款成功");
			   }
			}
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                out.print("success");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
		return "{\"success\"}";
	}




	/**
	 * 分期支付平台异步通知更新
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sign",
			method = RequestMethod.POST)
	@ResponseBody
	public String sign(@RequestBody BankCardPayBean bankCardPayBean) {
		// 用商户自己的私钥加签
		String sign = SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(bankCardPayBean)));
		return sign;
	}

}
