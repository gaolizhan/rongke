package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rongke.annotation.SourceAuthority;
import com.rongke.annotation.SystemLog;
import com.rongke.commons.ConstantUtil;
import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.enums.EveryDayDataType;
import com.rongke.enums.FileType1;
import com.rongke.enums.SourceType;
import com.rongke.model.*;
import com.rongke.redis.CacheUtil;
import com.rongke.service.*;
import com.rongke.utils.CollectionUtils;
import com.rongke.utils.DateUtils;
import com.rongke.utils.OrderUtils;
import com.rongke.utils.StringUtil;
import com.rongke.web.config.Config;
import com.rongke.web.payutil.helipay.action.TransferApi;
import com.rongke.web.service.ConstraintPaymentService;
import com.rongke.web.util.YunpianSmsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version : Ver 1.0
 * @LoanOrderController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/loanOrder")
@Transactional
@CrossOrigin
public class LoanOrderController {
    private            Logger              log = Logger.getLogger(this.getClass());
    @Autowired private LoanOrderService    loanOrderService;
    @Autowired private RiskService         riskService;
    @Autowired private UserService         userService;
    @Autowired private UserBankService     userBankService;
    @Autowired private ParamSettingService paramSettingService;
    @Autowired private UserCouponService   userCouponService;
    @Autowired private CouponService       couponService;
    @Autowired private AgreementService    agreementService;
    @Autowired public  Config              config;
    @Autowired public  AdminService        adminService;
    @Autowired public  MsgModelService     msgModelService;

    @Autowired private UserBlackService userBlackService;
    @Autowired private CacheUtil        cacheUtil;

    //    @Autowired
    //    public SmsUtil smsUtil;
    @Autowired public  PersonRecordService            personRecordService;
    @Autowired public  SystemConfigService            sysConfigService;
    @Autowired public  UserZhimaService               userZhimaService;
    @Autowired public  UserPhoneService               userPhoneService;
    @Autowired public  UserTongdunfenService          userTongdunfenService;
    @Autowired public  ChannelService                 channelService;
    @Autowired private ConstraintPaymentRecordService constraintPaymentRecordService;
    @Autowired private ConstraintPaymentService       constraintPaymentService;

    @Autowired
    private EveryDayDataService everyDayDataService;
    /**
     * @param loanOrder
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST) public JsonResp addLoanOrder(
            @RequestBody LoanOrder loanOrder) {
        log.debug("添加");
        loanOrderService.insert(loanOrder);
        return JsonResp.ok(loanOrder);
    }

    /**
     * @param loanOrder
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST) public JsonResp updateLoanOrder(
            @RequestBody LoanOrder loanOrder) {
        log.debug("修改");
        loanOrderService.updateById(loanOrder);
        return JsonResp.ok(loanOrder);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectLoanOrder(Long id) {
        log.debug("查找");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        return JsonResp.ok(loanOrder);
    }

    @RequestMapping(value = "/selectApplyStatus", method = RequestMethod.GET)
    public JsonResp selectApplyStatus(HttpServletRequest request) {
        Map map = new HashMap();
        User user = userService.findLoginUser();
        List<LoanOrder> l = loanOrderService.selectApplyStatus(user.getId());
        if (l.size() != 0) {
            Integer s = l.get(0).getOrderStatus();
            map.put("status", s);
            if (user.getStatus() == 4) {
                Date startdate = l.get(0).getGmtDatetime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startdate);
                cal.add(Calendar.DATE, 1);
                map.put("refuseTime", (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(cal.getTime()));
            }
        }
        return JsonResp.ok(map);
    }

    @RequestMapping(value = "/selectApplyNum", method = RequestMethod.GET)
    public JsonResp selectApplyNum() {
        Map map = new HashMap();
        User user = userService.findLoginUser();
        EntityWrapper ew = new EntityWrapper();
        ew.eq("user_id", user.getId());
        ew.eq("pay_status", "1");
        List<LoanOrder> l = loanOrderService.selectList(ew);
        Integer nums = Integer.valueOf(systemConfigService.selectValueByKey("dabiaocishu"));
        if (l.size() <= nums) {
            map.put("success", "0");
            return JsonResp.ok(map);
        } else {
            map.put("success", "1");
            return JsonResp.ok(map);
        }
    }

    private static final String agreementThirdUrl = "https://yironghua-admin.oss-cn-hangzhou.aliyuncs.com/agreement/a7deedcb-a3df-4fd0-9804-42c9da83585f.pdf";

    /**
     * @param
     * @return 返回值JsonResp
     * @生成未申请订单
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/addNewOrder", method = RequestMethod.GET) public JsonResp addNewOrder(
            BigDecimal borrowMoney, Integer limitDays, HttpServletRequest request)
            throws IOException, DocumentException, ParseException {

/*        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        String everydayResetTime = systemConfigService.selectValueByKey("everydayResetTime");
        date += " "+everydayResetTime+":00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = sdf.parse(date);
        Long rTime = date2.getTime();
        Long nTime = new Date().getTime();
        String endTime = "";
        if(rTime>nTime){
            endTime = sdf.format(nTime - 86400000);
        }else{
            endTime = sdf.format(nTime + 86400000);
        }*/
        //        List<LoanOrder> l = loanOrderService.selectApplyByTime(date,endTime);
        //        if(l.size()>200){
        //            return JsonResp.fa("今日申请已达到上限");
        //        }

        log.debug("生成订单");
        User user = userService.findLoginUser();
        //清除之前申请的状态为0的订单
        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper();
        ewOrder.eq("order_status", 0);
        List<LoanOrder> loanOrderList = loanOrderService.selectList(ewOrder);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                loanOrderService.deleteById(loanOrder);
            }
        }

        if (user.getIsPay() == 0) {
            EntityWrapper<LoanOrder> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_id", user.getId());
            List<LoanOrder> orderList = loanOrderService.selectList(entityWrapper);
            if (orderList.size() > 0) {
                for (LoanOrder loanOrder : orderList) {
                    if (loanOrder.getOrderStatus() == 1 || loanOrder.getOrderStatus() == 2
                        || loanOrder.getOrderStatus() == 3 || loanOrder.getOrderStatus() == 4
                        || loanOrder.getOrderStatus() == 5 || loanOrder.getOrderStatus() == 8) {
                        user.setIsPay(1);
                        userService.updateById(user);
                        return JsonResp.fa("无法申请！");
                    }
                }
            }
        }

        if (user.getAuthStatus() == 0) {
            return JsonResp.fa("请去完成所有基本认证！");
        } else if (user.getAuthStatus() == 4) {
            return JsonResp.fa("你的认证信息不符合贷款需求！");
        } else if (user.getIsPay() == 1) {
            return JsonResp.fa("你有订单正在审核或还款！");
        } else if (user.getStatus() == 2) {
            return JsonResp.fa("你的信用已不足！");
        } else if (user.getStatus() == 4) {
            return JsonResp.fa("您有订单近期内审核失败,请一个月之后再来申请！");
        } else if (user.getMoney().compareTo(borrowMoney) == -1) {
            return JsonResp.fa("你的额度不足！");
        } else {
            LoanOrder loanOrder = new LoanOrder();
            loanOrder.setUserId(user.getId());
            //银行信息
            EntityWrapper<UserBank> ew = new EntityWrapper<>();
            ew.eq("user_id", user.getId());
            UserBank userBank = userBankService.selectOne(ew);
            loanOrder.setBankCardNum(userBank.getBankcardno());
            loanOrder.setBankName(userBank.getBankName());
            //计算费用
            EntityWrapper<ParamSetting> ew2 = new EntityWrapper<>();
            ew2.eq("limit_days", limitDays);
            ew2.eq("status", 1);
            ParamSetting paramSetting = paramSettingService.selectOne(ew2);
            loanOrder.setParamSettingId(paramSetting.getId());
            loanOrder.setInterestPrecent(paramSetting.getInterestPercent());
            loanOrder.setAllowDays(paramSetting.getAllowDays());
            BigDecimal interestMoney = borrowMoney
                    .multiply(BigDecimal.valueOf(paramSetting.getInterestPercent() / 1000));
            BigDecimal placeServeMoney = borrowMoney
                    .multiply(BigDecimal.valueOf(paramSetting.getPlaceServePercent() / 100));
            BigDecimal msgAuthMoney = borrowMoney
                    .multiply(BigDecimal.valueOf(paramSetting.getMsgAuthPercent() / 100));
            BigDecimal riskServeMoney = borrowMoney
                    .multiply(BigDecimal.valueOf(paramSetting.getRiskServePercent() / 100));
            BigDecimal riskPlanMoney = borrowMoney
                    .multiply(BigDecimal.valueOf(paramSetting.getRiskPlanPercent() / 100));
            BigDecimal allWasteMoney = interestMoney.add(placeServeMoney).add(msgAuthMoney).add(riskServeMoney).add(riskPlanMoney);
            BigDecimal realMoney = borrowMoney.subtract(allWasteMoney);
            loanOrder.setBorrowMoney(borrowMoney);
            loanOrder.setLimitDays(limitDays);
            loanOrder.setRealMoney(realMoney);
            loanOrder.setWateMoney(allWasteMoney);
            loanOrder.setInterestMoney(interestMoney);
            loanOrder.setPlaceServeMoney(placeServeMoney);
            loanOrder.setMsgAuthMoney(msgAuthMoney);
            loanOrder.setRiskPlanMoney(riskPlanMoney);
            loanOrder.setRiskServeMoney(riskServeMoney);
            loanOrder.setNeedPayMoney(borrowMoney);
            loanOrder.setOrderStatus(0);
            //还款时间
            Date limitPayTime = DateUtils.dayAdd(paramSetting.getLimitDays(), new Date());
            loanOrder.setLimitPayTime(limitPayTime);
            //超出容限期时间
            Date overdueTime = DateUtils.dayAdd(paramSetting.getAllowDays(), limitPayTime);
            loanOrder.setOverdueTime(overdueTime);

            /* 生成借款和借款服务协议开始*/

            //获得合同模板
            EntityWrapper<Agreement> ewAgreement = new EntityWrapper<>();
            ewAgreement.eq("type", 1);
            String htmlstr = agreementService.selectOne(ewAgreement).getContent();
            //替换掉标签
            String orderNumber = OrderUtils.getOrderNo();
            String newHtml = htmlstr.replace("#money", borrowMoney.toString())
                    .replace("#number", orderNumber).replace("#jiafang", user.getUserName())
                    .replace("#identityNum", userBank.getIdcardno())
                    .replaceAll("#bankCardNum", userBank.getBankcardno()).replace("#date", DateUtils.dateSimple(new Date()))
                    .replace("#borrowDate", DateUtils.dateSimple(new Date()))
                    .replace("#limitDays", limitDays.toString())
                    .replace("#interestMoney", interestMoney.toString())
                    .replace("#interestMoney", interestMoney.toString())
                    .replace("#userName", userBank.getName()).replaceAll("宋体", "SimSun")
                    .replace("宋体", "SimSun").replace("<br>", "").replace("<hr>", "")
                    .replace("<img>", "").replace("<param>", "").replace("<link>", "");
            ;
            //在服务器端生成pdf的路径
            String[] paths = FileType1.getPath(FileType1.OTHER, 1, request);
            String path = paths[0] + File.separator + orderNumber + "1.pdf";
            //创建html转成pdf
            StringBuilder html = new StringBuilder();
            html.append("<html>");
            html.append("<body style='font-size:20px;font-family:SimSun;'>");
            html.append(newHtml);
            html.append("</body>");
            html.append("</html>");

            System.out.println(html.toString());
            InputStream is = new ByteArrayInputStream(html.toString().getBytes());
            OutputStream os = new FileOutputStream(path);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, os);

            document.open();

            // 将html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

            document.close();
            File saveFile = new File(path);
            String savePath = config
                    .getUploadHost(request, null, saveFile.getParentFile().getName(), saveFile.getName());
            loanOrder.setAgreementUrl(savePath);

            //借款服务协议
            EntityWrapper<Agreement> ewAgreement2 = new EntityWrapper<>();
            ewAgreement2.eq("type", 2);
            String htmlstr2 = agreementService.selectOne(ewAgreement2).getContent();
            //替换掉标签
            String newHtml2 = htmlstr2.replace("#date", DateUtils.dateSimple(new Date()))
                    .replace("#number", orderNumber).replace("#userName", user.getUserName())
                    .replace("#identityNum", userBank.getIdcardno()).replace("<br>", "")
                    .replace("<hr>", "").replace("<img>", "").replace("<param>", "")
                    .replace("<link>", "");

            //在服务器端生成pdf的路径
            String[] paths2 = FileType1.getPath(FileType1.OTHER, 1, request);
            String path2 = paths2[0] + File.separator + orderNumber + "2.pdf";
            //创建html转成pdf
            StringBuilder html2 = new StringBuilder();
            html2.append("<html>");
            html2.append("<body style='font-size:20px;font-family:SimSun;'>");
            html2.append(newHtml2);
            html2.append("</body>");
            html2.append("</html>");
            try {
                is.close();
                os.close();
            } catch (IOException e) {

            } finally {
                is.close();
                os.close();
            }

            InputStream is2 = new ByteArrayInputStream(html2.toString().getBytes());
            OutputStream os2 = new FileOutputStream(path2);

            Document document2 = new Document();
            PdfWriter writer2 = PdfWriter.getInstance(document2, os2);

            document2.open();

            // 将html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer2, document2, is2);

            document2.close();
            File saveFile2 = new File(path2);
            String savePath2 = config
                    .getUploadHost(request, null, saveFile2.getParentFile().getName(), saveFile2.getName());
            loanOrder.setAgreementTwoUrl(savePath2);
            /*生成借款和借款服务协议结束*/

            try {
                is2.close();
                os2.close();
            } catch (IOException e) {

            } finally {
                is2.close();
                os2.close();
            }

            loanOrderService.insert(loanOrder);

            return JsonResp.ok(loanOrder);
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @使用优惠卷
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/useCoupon", method = RequestMethod.GET)
    public JsonResp selectLoanOrder(Long userCouponId, Long orderId) {
        log.debug("使用优惠卷");
        UserCoupon userCoupon = userCouponService.selectById(userCouponId);//提交申请后再改状态
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        loanOrder.setUserCouponId(userCouponId);
        loanOrder.setSaveMoney(userCoupon.getSaveMoney());
        //应付金额减减免
        loanOrder.setNeedPayMoney(loanOrder.getBorrowMoney().subtract(userCoupon.getSaveMoney()));
        loanOrderService.updateById(loanOrder);
        return JsonResp.ok(loanOrder);
    }

    @Autowired private UserBasicMsgService userBasicMsgService;
    @Autowired private MobileService       mobileService;

    @Autowired private SystemConfigService systemConfigService;

    @Autowired private UserIdentityService userIdentityService;
    @Autowired private TongdunAuditService tongdunAuditService;

    /**
     * @param
     * @return 返回值JsonResp
     * @确认提交申请
     */
    //@SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.GET)
    public JsonResp confirmOrder(Long orderId) throws ParseException {
        log.debug("确认提交申请");
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        //生成订单号
        String orderNumber = OrderUtils.getOrderNo();
        loanOrder.setOrderNumber(orderNumber);
        //扣额度，改变is_pay
        User user = userService.findLoginUser();
        if (user.getStatus() == 1) {
            user.setMoney(user.getMoney().subtract(loanOrder.getBorrowMoney()));
            user.setIsPay(1);
            userService.updateById(user);
            //优惠券使用掉
            if (loanOrder.getUserCouponId() != null) {
                UserCoupon userCoupon = userCouponService.selectById(loanOrder.getUserCouponId());
                userCoupon.setStatus(2);
                userCouponService.updateById(userCoupon);
                Integer a = user.getCouponUseCount();
                user.setCouponUseCount(user.getCouponUseCount() + 1);
                userService.updateById(user);
            }
            //修改状态时间等
            loanOrder.setGmtDatetime(new Date());
            Date limitPayTime = DateUtils.dayAdd(loanOrder.getLimitDays(), new Date());
            Date overdueTime = DateUtils
                    .dayAdd(loanOrder.getLimitDays() + loanOrder.getAllowDays(), new Date());
            loanOrder.setLimitPayTime(limitPayTime);
            loanOrder.setOverdueTime(overdueTime);
            loanOrder.setOrderStatus(1);
            loanOrderService.updateById(loanOrder);
            //申请人数统计
            try {
                log.info("申请人数统计 ChannelId="+user.getChannelId());
                User orderUser = userService.selectById(loanOrder.getUserId());
                everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.APPLY_NUM.getKey(),orderUser.getChannelId()==null?0:orderUser.getChannelId());

            }catch (Exception e){
                e.printStackTrace();
            }
            this.cacheUtil.leftPush(ConstantUtil.RISK_TASK_LOAN_ORDER_ID, loanOrder.getId().toString());

            String isAuto = cacheUtil.getString("isAuto");
            //自动审核以放款
            if ("true".equals(isAuto)) {
                JsonResp<String> result = loanAudit(loanOrder.getId()+"");
                if (!result.isSuccess()) {
                    return result;
                }else{
                    return giveMoney(loanOrder.getId()+"");
                }
            }
            return JsonResp.ok(loanOrder);
        } else {
            return JsonResp.fa("暂时无法申请");
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @分页查询订单列表
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/selectAllOrderPage", method = RequestMethod.GET)
    public JsonResp selectAllOrderPage(Page page) {
        log.debug("分页查询订单列表");
        User user = userService.findLoginUser();
        //User user =  userService.selectById(29l);
        EntityWrapper<LoanOrder> ew = new EntityWrapper<>();
        ew.eq("user_id", user.getId());
        List<Integer> list = new ArrayList<Integer>() {{
            add(0);
            add(8);
        }};
        ew.notIn("order_status", list);
        Page loanOrderList = loanOrderService.selectPage(page, ew);
        return JsonResp.ok(loanOrderList);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @借款协议
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/selectAgreement", method = RequestMethod.GET)
    public JsonResp selectAgreement(Long id) {
        log.debug("借款协议");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        return JsonResp.ok(loanOrder.getAgreementUrl());
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @借款服务协议
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/selectAgreementTwo", method = RequestMethod.GET)
    public JsonResp selectAgreementTwo(Long id) {
        log.debug("借款服务协议");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        return JsonResp.ok(loanOrder.getAgreementTwoUrl());
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询申请贷款列表订单数和金额总数
     */
    @RequestMapping(value = "/countPeopleMoney2", method = RequestMethod.GET)
    public JsonResp countPeopleMoney2(String gmtDatetime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 1);
        double peoplecount = loanOrderService.selectPeopleSum1(map);
        Integer moneycount = loanOrderService.selectMoneySum1(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param page,orderStatus
     * @return 返回值JsonResp
     * @查询机审拒绝列表
     */
    @RequestMapping(value = "/selectApplyLoanList2", method = RequestMethod.GET)
    public JsonResp selectApplyLoanList2(Page page, String gmtDatetime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }

        map.put("orderStatus", 9);
        int count = loanOrderService.selectApplyLoanListNum(map);

        List<LoanOrder> loanOrderList = loanOrderService.selectApplyLoanList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param page,orderStatus
     * @return 返回值JsonResp
     * @查询申请贷款列表
     */
    @Autowired private OrderExtendService orderExtendService;

    @RequestMapping(value = "/selectApplyLoanList", method = RequestMethod.GET)
    public JsonResp selectApplyLoanList(Page page, String gmtDatetime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }

        map.put("orderStatus", 1);
        int count = loanOrderService.selectApplyLoanListNum(map);

        List<LoanOrder> loanOrderList = loanOrderService.selectApplyLoanList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }

        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    @RequestMapping(value = "/selectOldOrder", method = RequestMethod.GET)
    public JsonResp selectOldOrder(Long userId) {
        Map<String, Object> map = new HashMap<>();
        EntityWrapper ew1 = new EntityWrapper();
        System.out.print(userId);
        ew1.eq("user_id", userId);
        List<LoanOrder> uLoanOrderList = loanOrderService.selectList(ew1);
        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        BigDecimal d = new BigDecimal(0);
        if (!uLoanOrderList.isEmpty()) {
            for (LoanOrder l : uLoanOrderList) {
                if (a < l.getOverdueDays()) {
                    a = l.getOverdueDays();
                }
                if (l.getPayStatus() == 2) {
                    b++;
                }
                if (l.getOrderStatus() == 6) {
                    c++;
                }
                EntityWrapper lEW = new EntityWrapper();
                lEW.eq("order_id", l.getId());
                List<OrderExtend> orderExtendsList = orderExtendService.selectList(lEW);
                if (!orderExtendsList.isEmpty()) {
                    for (OrderExtend o : orderExtendsList) {
                        d.add(o.getExtendMoney());
                    }
                }
            }
        }
        map.put("zdyuqi", a);
        map.put("yuqics", b);
        map.put("cghuankuan", c);
        map.put("xqcishu", d);

        return JsonResp.ok(map);
    }

    private final String content = "亲爱的用户，恭喜您，您的订单审核通过，可打开APP查看详情。";

    /**
     * @param id
     * @return 返回值JsonResp
     * @同意申请，进入待打款状态
     */
    @RequestMapping(value = "/thisAgree", method = RequestMethod.GET) public JsonResp thisAgree(long id) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if (loanOrder.getOrderStatus() != 3) {
            loanOrder.setOrderStatus(2);
            loanOrder.setPassTime(new Date());
            boolean bo = loanOrderService.updateById(loanOrder);
            PersonRecord pr = personRecordService.selectById(2);
            if (bo) {
                pr.setOutOrderCount(pr.getOutOrderCount() + 1);
            }
            Boolean bo1 = personRecordService.updateById(pr);
            Boolean bool = false;
            if (bo && bo1) {
                bool = true;
            }
        }
        //发送审核成功短信
        /*EntityWrapper<MsgModel> ewMsg = new EntityWrapper();
        ewMsg.eq("type",6);
        MsgModel msgModel = msgModelService.selectOne(ewMsg);//找到短信模板
        //将信息部分替换*/
        User user = userService.selectById(loanOrder.getUserId());
  /*      String content = msgModel.getContent().replaceAll("</?[^>]+>", "").replace("#userName",user.getUserName());
        SmsTempletEnum.REMINDSEND.setTypeName(content);
        smsService.smsCodeByType(user.getPhone(), 3);*/
        //String content = user.getUserName();
        //smsUtil.smsNotification(user.getPhone(),"3748",content);
        ArrayList<String> userList = new ArrayList<>();
        userList.add(user.getId().toString());
        this.pushRepaymentMsg(userList);

        return JsonResp.ok();
    }

    public static final String PUSH_PASS_LOAN_MSG = "亲爱的用户，恭喜您，您的订单审核通过，可打开APP查看详情。";

    public static final String PUSH_PASS_LOAN_TITLE = "审核通过";

    private void pushRepaymentMsg(List<String> userIds) {
        try {
            if (userIds.size() > 0) {
                int status = JpushClientUtil
                        .sendToAliasId(userIds, PUSH_PASS_LOAN_TITLE, PUSH_PASS_LOAN_TITLE,
                                PUSH_PASS_LOAN_MSG, "");
                if (status == 1) {
                    log.info("推送成功");
                }
            }
        } catch (Exception e) {
            log.error("推送失败", e);
        }
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @拒绝申请，进入审核失败状态
     */
    @RequestMapping(value = "/thisRefuse", method = RequestMethod.GET) public JsonResp thisRefuse(
            Long id) {
        //  LoanOrder loanOrder=new LoanOrder();
        LoanOrder loanOrder = loanOrderService.selectById(id);
        loanOrder.setOrderStatus(7);
        loanOrderService.updateById(loanOrder);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 30);//计算30天后的时间
        Date date1 = c.getTime();

        User user = userService.selectById(loanOrder.getUserId());
        user.setIsPay(0);
        user.setStatus(4);
        user.setRefuseRemoveTime(date1);
        Double money = user.getMoney().doubleValue();
        Double borrowMoney = loanOrder.getBorrowMoney().doubleValue();
        Double temp = money + borrowMoney;
        // BigDecimal temp1=temp.
        user.setMoney(new BigDecimal(temp));
        boolean boo = userService.insertOrUpdate(user);
        //如果使用使用的优惠券返还
        if (loanOrder.getUserCouponId() != null) {
            UserCoupon userCoupon = userCouponService.selectById(loanOrder.getUserCouponId());
            userCoupon.setStatus(1);
            userCouponService.updateById(userCoupon);
        }
        // LoanOrder loanOrder1=loanOrderService.selectById(id);
        //User user=userService.selectById(loanOrder.getUserId());
        //user.setIsPay(0);

        return JsonResp.ok(boo);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询待打款订单数和总金额
     */
    @RequestMapping(value = "/countPeopleMoney3", method = RequestMethod.GET)
    public JsonResp countPeopleMoney3(String gmtDatetime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 2);
        double peoplecount = loanOrderService.selectPeopleSum1(map);
        double moneycount = loanOrderService.selectMoneySum1(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param page,orderStatus
     * @return 返回值JsonResp
     * @查询待打款列表
     */
    @RequestMapping(value = "/toPassMoney", method = RequestMethod.GET) public JsonResp toPassMoney(
            Page page, String passTime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (passTime != null && !"".equals(passTime)) {
            String time[] = passTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 2);
        int count = loanOrderService.selectToPassMoneyListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectToPassMoneyList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据单号查找用户评估报告
     */
    @RequestMapping(value = "/selectOneByEvaNum", method = RequestMethod.GET)
    public JsonResp selectOneByEvaNum(String orderNumber) {
        log.debug("根据单号查找用户评估报告");
        EntityWrapper<LoanOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("order_number", orderNumber);
        LoanOrder loanOrder = loanOrderService.selectOne(wrapper);
        return JsonResp.ok(loanOrder);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询所有的已经打款订单总数和总金额
     */
    @RequestMapping(value = "/countPeopleMoney4", method = RequestMethod.GET)
    public JsonResp countPeopleMoney4(String gmtDatetime, String name, String phoneNumber, String orderStatus) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();
        if (orderStatus != null && !"".equals(orderStatus)) {
            int orderStatus1 = Integer.parseInt(orderStatus);
            status.add(orderStatus1);
        } else {
            status.add(3);
            status.add(4);
            status.add(5);
            status.add(6);
            status.add(8);
        }
        map.put("status", status);
        double peoplecount = loanOrderService.selectPeopleSum2(map);
        double moneycount = loanOrderService.selectMoneySum2(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询所有的逾期订单总数和总金额
     */
    @RequestMapping(value = "/countPeopleMoney6", method = RequestMethod.GET)
    public JsonResp countPeopleMoney6(String limitPayTime, String name, String phoneNumber, String orderStatus) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();
        if (orderStatus != null && !"".equals(orderStatus)) {
            int orderStatus1 = Integer.parseInt(orderStatus);
            status.add(orderStatus1);
        } else {

            status.add(4);
            status.add(5);
        }
        map.put("status", status);
        double peoplecount = loanOrderService.selectPeopleSum6(map);
        double moneycount = loanOrderService.selectMoneySum6(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param page,orderStatus
     * @return 返回值JsonResp
     * @查询所有的已经打款信息列表
     */
    @RequestMapping(value = "/allPassMoneyList", method = RequestMethod.GET)
    public JsonResp allPassMoneyList(Page page, String gmtDatetime, String name, String phoneNumber,
                                     String orderStatus) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();
        if (orderStatus != null && !"".equals(orderStatus)) {
            int orderStatus1 = Integer.parseInt(orderStatus);
            status.add(orderStatus1);
        } else {
            status.add(3);
            status.add(4);
            status.add(5);
            status.add(6);
            status.add(8);
        }
        map.put("status", status);
        List<LoanOrder> loanOrderList = loanOrderService.selectAllPassMoneyList(map);
        int count = loanOrderService.selectAllPassMoneyListNum(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);

                Long id = loanOrder.getAuditorId();
                if (id != null && !"".equals(id)) {
                    EntityWrapper<Admin> adminEntityWrapper = new EntityWrapper<>();
                    adminEntityWrapper.eq("id", id);
                    Admin admin;
                    admin = adminService.selectOne(adminEntityWrapper);
                    loanOrder.setAdmin(admin);
                }
            }
        }

        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @单个打款
     */
    @RequestMapping(value = "/passMoney", method = RequestMethod.GET) public JsonResp passMoney(long id) {

        LoanOrder loanOrder = loanOrderService.selectById(id);
        loanOrder.setOrderStatus(3);
        loanOrder.setGiveTime(new Date());
        boolean bo = loanOrderService.updateById(loanOrder);
        PersonRecord pr = personRecordService.selectById(1);
        BigDecimal b1 = pr.getOutMoney();
        BigDecimal b2 = loanOrder.getRealMoney();
        pr.setOutMoney(b1.add(b2));
        Boolean bo1 = personRecordService.updateById(pr);
        Boolean bool = false;
        if (bo && bo1) {
            bool = true;
        }
        if (bo) {
            mobileService.tongDunMonitor(loanOrder);
        }
        return JsonResp.ok(bool);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET) public void test() {
        EntityWrapper<LoanOrder> loanOrderEntityWrapper = new EntityWrapper<>();
        loanOrderEntityWrapper.eq("id", "558");
        LoanOrder loanOrder = loanOrderService.selectOne(loanOrderEntityWrapper);
        mobileService.tongDunMonitor(loanOrder);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @一键全部打款
     */
    @RequestMapping(value = "/passAllMoney", method = RequestMethod.GET)
    public JsonResp passAllMoney() {
        EntityWrapper<LoanOrder> loanOrderEntityWrapper = new EntityWrapper<>();
        loanOrderEntityWrapper.eq("order_status", 2);
        boolean bool = false;
        List<LoanOrder> loanOrderList = loanOrderService.selectList(loanOrderEntityWrapper);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                loanOrder.setOrderStatus(3);
                loanOrder.setGiveTime(new Date());
                loanOrderService.updateById(loanOrder);
                bool = true;
                mobileService.tongDunMonitor(loanOrder);
            }
        }
        return JsonResp.ok(bool);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @查询一条详情
     */
    @RequestMapping(value = "/selectOneDetail", method = RequestMethod.GET)
    public JsonResp selectOneDetail(String id) {

        LoanOrder loanOrder = loanOrderService.selectById(id);
        User user = userService.selectById(loanOrder.getUserId());
        loanOrder.setUser(user);
        Admin admin = adminService.selectById(loanOrder.getAuditorId());
        loanOrder.setAdmin(admin);
        return JsonResp.ok(loanOrder);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @拉黑用户
     */
    @RequestMapping(value = "/stopUse", method = RequestMethod.GET) public JsonResp stopUse(Long id) {
        User user = userService.selectById(id);
        if (user.getStatus().equals(3)) {
            return JsonResp.fa("该用户已被禁用！");
        } else {
            user.setId(id);
            user.setStatus(3);
            Boolean boo = userService.updateById(user);

            return JsonResp.ok(boo);
        }
    }

    @RequestMapping(value = "/dropBadDebt", method = RequestMethod.GET)
    public JsonResp dropBadDebt(Long id) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        loanOrder.setOrderStatus(8);
        Boolean bool = loanOrderService.updateById(loanOrder);
        PersonRecord personRecord = personRecordService.selectById(1L);

        if (bool == true) {
            personRecord.setBadOrderCount(personRecord.getBadOrderCount() + 1);
            personRecordService.updateById(personRecord);
        }
        return JsonResp.ok(bool);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询打款失败订单
     */
    @RequestMapping(value = "/findPassFailPage", method = RequestMethod.GET)
    public JsonResp findPassFailPage(Page page) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());

        int count = loanOrderService.selectFailToPassMoneyListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectFailToPassMoneyList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * 打款失败后拒绝打款
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/refusePassMoney", method = RequestMethod.GET)
    public JsonResp refusePassMoney(Long id) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        loanOrder.setGiveStatus(0);

        Boolean bool = loanOrderService.updateById(loanOrder);
        return JsonResp.ok(bool);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询申请失败订单数和金额
     */
    @RequestMapping(value = "/countPeopleMoney1", method = RequestMethod.GET)
    public JsonResp countPeopleMoney1(String gmtDatetime, String name, String phoneNumber) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 7);
        double peoplecount = loanOrderService.selectPeopleSum1(map);
        double moneycount = loanOrderService.selectMoneySum1(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询申请失败列表
     */
    @RequestMapping(value = "/selectFailApplyList", method = RequestMethod.GET)
    public JsonResp selectFailApplyList(Page page, String gmtDatetime, String name, String phoneNumber) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 7);
        int count = loanOrderService.selectFailApplyListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectFailApplyList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param page
     * @param realPayTime
     * @param name
     * @param phoneNumber
     * @return 正常还款列表
     */
    @RequestMapping(value = "/normalRepaymentList", method = RequestMethod.GET)
    public JsonResp normalRepaymentList(Page page, String realPayTime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (realPayTime != null && !"".equals(realPayTime)) {
            String time[] = realPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time1 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("payStatus", 1);
        int count = loanOrderService.selectNormalRepaymentListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectNormalRepaymentList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * 计算正常还款的总人数和总金额
     *
     * @param realPayTime
     * @param name
     * @param phoneNumber
     * @param
     * @return
     */
    @RequestMapping(value = "/countPeopleMoney", method = RequestMethod.GET)
    public JsonResp countPeopleMoney(String realPayTime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (realPayTime != null && !"".equals(realPayTime)) {
            String time[] = realPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time1 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("payStatus", 1);
        double peoplecount = loanOrderService.selectPeopleSum(map);
        double moneycount = loanOrderService.selectMoneySum(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * 计算黑名单的总人数和总欠款金额
     *
     * @param
     * @param
     * @param phoneNumber
     * @param
     * @return
     */
    @RequestMapping(value = "/countPeopleMoney5", method = RequestMethod.GET)
    public JsonResp countPeopleMoney5(String userName, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (userName != null && !"".equals(userName)) {
            userEntityWrapper.like("user_name", userName);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        userEntityWrapper.eq("status", 2);
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (!userList.isEmpty()) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        String peoplecount = loanOrderService.selectPeopleSum3(map);
        String moneycount = loanOrderService.selectMoneySum3(map);
        LoanOrder loanOrder = new LoanOrder();
        if (moneycount == null) {
            moneycount = "0";
        }
        if (peoplecount == null) {
            peoplecount = "0";
        }
        loanOrder.setTotalPeople(new Double(peoplecount));
        loanOrder.setTotalMoney(new Double(moneycount));

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param page
     * @param limitPayTime
     * @param name
     * @param phoneNumber
     * @return 所有逾期列表
     */
    @RequestMapping(value = "/overDueList", method = RequestMethod.GET) public JsonResp overDueList(
            Page page, String limitPayTime, String name, String phoneNumber, String orderStatus) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();
        if (orderStatus != null && !"".equals(orderStatus)) {
            int orderStatus1 = Integer.parseInt(orderStatus);
            status.add(orderStatus1);
        } else {
            status.add(4);
            status.add(5);
        }
        map.put("status", status);

        List<LoanOrder> loanOrderList = loanOrderService.selectOverDueList(map);
        int count = loanOrderService.selectOverDueListNum(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
                Admin admin = adminService.selectById(loanOrder.getAuditorId());
                loanOrder.setAdmin(admin);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param page
     * @param realPayTime
     * @param name
     * @param phoneNumber
     * @return查詢所有逾期結清列表
     */
    @RequestMapping(value = "/overDuePaymentList", method = RequestMethod.GET)
    public JsonResp overDuePaymentList(Page page, String realPayTime, String name, String phoneNumber, Integer status) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        map.put("ids", ids);
        map.put("payStatus", 2);

        if (realPayTime != null && !"".equals(realPayTime)) {
            String time[] = realPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status1 = new ArrayList<>();
        if (status != null && !"".equals(status)) {
            status1.add(status);
        } else {
            status1.add(4);
            status1.add(5);
        }
        map.put("status1", status1);
        int count = loanOrderService.overDuePaymentListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.overDuePaymentList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询所有的逾期结清订单总数和总金额
     */
    @RequestMapping(value = "/countPeopleMoney7", method = RequestMethod.GET)
    public JsonResp countPeopleMoney7(String realPayTime, String name, String phoneNumber, Integer status) {

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("payStatus", 2);

        if (realPayTime != null && !"".equals(realPayTime)) {
            String time[] = realPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status1 = new ArrayList<>();
        if (status != null && !"".equals(status)) {
            status1.add(status);
        } else {
            status1.add(4);
            status1.add(5);
        }
        map.put("status1", status1);
        double peoplecount = loanOrderService.selectPeopleSum7(map);
        double moneycount = loanOrderService.selectMoneySum7(map);
        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param id
     * @return 拉黑用戶
     */
    @RequestMapping(value = "/joinBlackList", method = RequestMethod.GET)
    public JsonResp joinBlackList(Long id) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        User user = userService.selectById(loanOrder.getUserId());
        user.setStatus(2);
        Boolean bo = userService.updateById(user);

        PersonRecord pr = personRecordService.selectById(1);
        pr.setBlackCount(pr.getBlackCount() + 1);
        Boolean bo1 = personRecordService.updateById(pr);
        Boolean bool = false;
        if (bo && bo1) {
            bool = true;
        }
        return JsonResp.ok(bo);
    }

    /**
     * @return 查詢正常待还款
     */
    @RequestMapping(value = "/selectNormalToPayList", method = RequestMethod.GET)
    public JsonResp selectNormalToPayList(Page page, String limitPayTime, String name, String phoneNumber, String orderStatus) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 3);
        int count = loanOrderService.selectNormalToPayListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectNormalToPayList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @return 查詢正常待还款经单数和金额
     */
    @RequestMapping(value = "/countPeopleMoney9", method = RequestMethod.GET)
    public JsonResp countPeopleMoney9(String limitPayTime, String name, String phoneNumber, String orderStatus) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (ids.size() > 0) {
            map.put("ids", ids);
        }

        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 3);
        double peoplecount = loanOrderService.selectPeopleSum9(map);
        double moneycount = loanOrderService.selectMoneySum9(map);

        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * @param id
     * @return
     * @throws ParseException 到期前一天发送提醒还款短信
     */
    @RequestMapping(value = "/sendMenssage", method = RequestMethod.GET)
    public JsonResp sendMenssage(Long id) throws ParseException {
        LoanOrder loanOrder = new LoanOrder();
        if (id != null && !"".equals(id)) {
            loanOrder = loanOrderService.selectById(id);
        }
        //if (loanOrder.getLimitPayTime().getTime() == DateUtils.dayAdd(1, DateUtils.YYMMDDDate(new Date())).getTime()) {
        EntityWrapper<MsgModel> msgModelEntityWrapper = new EntityWrapper<>();
        msgModelEntityWrapper.eq("type", 2);
        // MsgModel msgModel = msgModelService.selectById(msgModelEntityWrapper);
        MsgModel msgModel = msgModelService.selectOne(msgModelEntityWrapper);
        User user = userService.selectById(loanOrder.getUserId());

        //String content = msgModel.getContent().replaceAll("</?[^>]+>", "").replace("#userName", user.getUserName()).replace("#time", DateUtils.YYMMDDDateChinese(loanOrder.getGiveTime())).replace("#money", loanOrder.getNeedPayMoney().toString());

           /* SmsTempletEnum.REMINDSEND.setTypeName(content);
            smsService.smsCodeByType(user.getPhone(), 3);*/

        String content = user.getUserName() + "##" + loanOrder.getBorrowMoney();
        //smsUtil.smsNotification(user.getPhone(),"3750",content);

        return JsonResp.ok();
    }

    /**
     * @param page
     * @param limitPayTime
     * @param name
     * @param phoneNumber
     * @param
     * @return 查询所有的逾期待催款列表
     */
    @RequestMapping(value = "/overDueToPayList", method = RequestMethod.GET)
    public JsonResp overDueToPayList(Page page, String limitPayTime, String name, String phoneNumber, String orderStatus) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();
        if (orderStatus != null && !"".equals(orderStatus)) {
            int orderStatus1 = Integer.parseInt(orderStatus);
            status.add(orderStatus1);
        } else {
            status.add(4);
            status.add(5);
        }
        map.put("status", status);

        List<LoanOrder> loanOrderList = loanOrderService.selectOverDueList(map);
        int count = loanOrderService.selectOverDueListNum(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
                Admin admin = adminService.selectById(loanOrder.getAuditorId());
                loanOrder.setAdmin(admin);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    @RequestMapping(value = "/myOverDueToPayList", method = RequestMethod.GET)
    public JsonResp myOverDueToPayList(Page page, String limitPayTime, String name, String phoneNumber, String orderStatus, Integer type) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<Long> ids = new ArrayList<>();
        if ((phoneNumber == null || "".equals(phoneNumber)) && (name == null || "".equals(name))) {

        } else {
            List<User> userList = userService.selectList(userEntityWrapper);
            if (userList.size() > 0) {
                for (User user : userList) {
                    ids.add(user.getId());
                }
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            map.put("time1", time1);
            map.put("time2", time2);
        }
        List<Integer> status = new ArrayList<>();

        Admin admin1 = userService.getThisLogin();
        if (type == 1) {
            map.put("auditorId", admin1.getId());
            if (orderStatus != null && !"".equals(orderStatus)) {
                int orderStatus1 = Integer.parseInt(orderStatus);
                status.add(orderStatus1);
            } else {
                status.add(1);
            }
        } else {
            map.put("pmId", admin1.getId());
            if (orderStatus != null && !"".equals(orderStatus)) {
                int orderStatus1 = Integer.parseInt(orderStatus);
                status.add(orderStatus1);
            } else {
                status.add(6);

                status.add(4);
                status.add(5);
                if (type == 2) {
                    status.add(3);
                }
            }
        }
        map.put("status", status);
        List<LoanOrder> loanOrderList = loanOrderService.selectOverDueList(map);
        int count = loanOrderService.selectOverDueListNum(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {

                User user1 = userService.selectById(loanOrder.getUserId());
                loanOrder.setUser(user1);
                Admin admin = adminService.selectById(loanOrder.getAuditorId());
                loanOrder.setAdmin(admin);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    @RequestMapping(value = "/selectAllPressMoneyMan", method = RequestMethod.GET)
    public JsonResp selectAllPressMoneyMan() {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("department_id", 9);
        List<Admin> list = adminService.selectList(ew);
        return JsonResp.ok(list);
    }

    @RequestMapping(value = "/selectAllPassMan", method = RequestMethod.GET)
    public JsonResp selectAllPassMan() {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("department_id", 4);
        List<Admin> list = adminService.selectList(ew);
        return JsonResp.ok(list);
    }

    @RequestMapping(value = "/setUpPressMoneyMan", method = RequestMethod.GET)
    public JsonResp setUpPressMoneyMan(String orderId, String pmId) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("id", orderId);
        LoanOrder lo = loanOrderService.selectOne(ew);
        lo.setPressMoneyMan(Long.valueOf(pmId));
        loanOrderService.updateById(lo);
        return JsonResp.ok("成功！");
    }

    @RequestMapping(value = "/setUpPassMan", method = RequestMethod.GET)
    public JsonResp setUpPassMan(String orderId, String pmId) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("id", orderId);
        LoanOrder lo = loanOrderService.selectOne(ew);
        lo.setAuditorId(Long.valueOf(pmId));
        loanOrderService.updateById(lo);
        return JsonResp.ok("成功！");
    }

    /**
     * @param page
     * @param limitPayTime
     * @param name
     * @param phoneNumber
     * @param
     * @return 查询所有的坏账列表
     */
    @RequestMapping(value = "/badDebtList", method = RequestMethod.GET) public JsonResp badDebtList(
            Page page, String limitPayTime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 8);
        int count = loanOrderService.selectBadDebtListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectBadDebtList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param limitPayTime
     * @param
     * @return 拉出所有的坏账列表
     */
    @RequestMapping(value = "/excelbadDebtList", method = RequestMethod.GET)
    public JsonResp excelbadDebtList(String limitPayTime, ModelMap modelMap) {
        EntityWrapper<LoanOrder> loanOrderEntityWrapper = new EntityWrapper<>();
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];
            loanOrderEntityWrapper.between("limit_pay_time", time1, time2);
        }
        loanOrderEntityWrapper.eq("order_status", 8);
        List<LoanOrder> loanOrderList = loanOrderService.selectList(loanOrderEntityWrapper);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
                EntityWrapper<Admin> adminEntityWrapper = new EntityWrapper<>();
                adminEntityWrapper.eq("id", loanOrder.getAuditorId());
                Admin admin = adminService.selectOne(adminEntityWrapper);
                loanOrder.setAdmin(admin);
            }
        }
        modelMap.put("list", loanOrderList);
        return JsonResp.ok(modelMap);
    }

    /**
     * 计算坏账的总人数和总金额
     *
     * @param
     * @param name
     * @param phoneNumber
     * @param
     * @return
     */
    @RequestMapping(value = "/countPeopleMoney8", method = RequestMethod.GET)
    public JsonResp countPeopleMoney8(String limitPayTime, String name, String phoneNumber) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (name != null && !"".equals(name)) {
            userEntityWrapper.like("user_name", name);
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            userEntityWrapper.like("phone", phoneNumber);
        }
        List<User> userList = userService.selectList(userEntityWrapper);
        List<Long> ids = new ArrayList<>();
        if (userList.size() > 0) {
            for (User user : userList) {
                ids.add(user.getId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        if (limitPayTime != null && !"".equals(limitPayTime)) {
            String time[] = limitPayTime.split("~");
            String time1 = "";
            String time2 = "";
            time1 = time[0];
            time2 = time[1];

            map.put("time1", time1);
            map.put("time2", time2);
        }
        map.put("orderStatus", 8);
        double peoplecount = loanOrderService.selectPeopleSum8(map);
        double moneycount = loanOrderService.selectMoneySum8(map);

        LoanOrder loanOrder = new LoanOrder();

        loanOrder.setTotalPeople(peoplecount);
        loanOrder.setTotalMoney(moneycount);

        return JsonResp.ok(loanOrder);
    }

    /**
     * 根据电话号码查询个人所有贷款订单
     *
     * @param pageNo
     * @param pageSize
     * @param phone
     * @return
     */
    @RequestMapping(value = "/selectOrderLists", method = RequestMethod.GET)
    public JsonResp selectOrderLists(Integer pageNo, Integer pageSize, String phone) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("phone", phone);
        User user = userService.selectOne(userEntityWrapper);

        EntityWrapper<LoanOrder> loanOrderEntityWrapper = new EntityWrapper<>();
        loanOrderEntityWrapper.eq("user_id", user.getId());

        Page page = loanOrderService.selectPage(new Page(pageNo, pageSize), loanOrderEntityWrapper);
        List<LoanOrder> loanOrders = page.getRecords();
        for (LoanOrder loanOrder : loanOrders) {
            loanOrder.setUser(user);
            EntityWrapper<Admin> adminEntityWrapper = new EntityWrapper<>();
            adminEntityWrapper.eq("id", loanOrder.getAuditorId());
            Admin admin = adminService.selectOne(adminEntityWrapper);
            loanOrder.setAdmin(admin);
        }
        return JsonResp.ok(new PageDto(pageNo, pageSize, page.getRecords(), page.getTotal()));
    }

    /**
     * 综合费用收益总额
     *
     * @return
     */
    @RequestMapping(value = "/allWateMoney", method = RequestMethod.GET)
    public JsonResp allWateMoney() {

        BigDecimal wateMoney = loanOrderService.sumAllWateMoney();

        return JsonResp.ok(wateMoney);
    }

    @Autowired private CollectionRecordService collectionRecordService;

    /**
     * 查看当前登录用户催收记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectJiLu", method = RequestMethod.GET) public JsonResp selectJiLu(
            String id) {
        Admin admin = userService.getThisLogin();

        EntityWrapper ew = new EntityWrapper();
        ew.eq("admin_id", admin.getId());
        ew.eq("order_id", id);

        List<CollectionRecord> cList = collectionRecordService.selectList(ew);

        return JsonResp.ok(cList);
    }

    @RequestMapping(value = "/jilubaocun", method = RequestMethod.GET)
    public JsonResp jilubaocun(String orderId, String text) {
        Admin admin = userService.getThisLogin();
        EntityWrapper ew = new EntityWrapper();
        ew.eq("order_id", orderId);
        List<CollectionRecord> crl = collectionRecordService.selectList(ew);
        for (CollectionRecord c : crl) {
            c.setAdminId(admin.getId());
            collectionRecordService.update(c, ew);
        }
        CollectionRecord cr = new CollectionRecord();
        cr.setText(text);
        cr.setAdminId(admin.getId());
        cr.setOrderId(Long.valueOf(orderId));
        collectionRecordService.insert(cr);
        return JsonResp.ok("success");
    }

    /**
     * 查询我的催收记录
     *
     * @return
     */
    @RequestMapping(value = "/allMyLoanList", method = RequestMethod.GET)
    public JsonResp allMyLoanList() {
        Admin admin = userService.getThisLogin();
        Map<Object, Object> map = new HashMap<>();
        List<CollectionRecord> crl = collectionRecordService.selectAdminLoan(admin.getId());
        List<LoanOrder> list = new ArrayList<>();
        for (CollectionRecord c : crl) {
            EntityWrapper ew = new EntityWrapper();
            ew.eq("order_id", c.getOrderId());
            LoanOrder l = loanOrderService.selectOne(ew);
            list.add(l);
        }
        map.put("list", list);
        java.math.BigDecimal sun = null;
        for (LoanOrder lo : list) {
            if (lo.getOrderStatus() == 6) {
                sun.add(lo.getNeedPayMoney());
            }
        }
        map.put("tichen", sun.doubleValue());
        return JsonResp.ok(map);
    }

    /**
     * @param orderId
     * @param money
     * @return
     */
    @RequestMapping(value = "/bufenhuankuan", method = RequestMethod.GET)
    public JsonResp bufenhuankuan(String orderId, String money) {
        try {
            java.math.BigDecimal cMoney = new BigDecimal(money);
            cMoney = cMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
            EntityWrapper ew = new EntityWrapper();
            ew.eq("id", orderId);
            LoanOrder ld = loanOrderService.selectOne(ew);
            java.math.BigDecimal oldMoney = ld.getConsultRepaymentMoney();
            cMoney = oldMoney.add(cMoney);
            ld.setConsultRepaymentMoney(cMoney);
            loanOrderService.updateById(ld);
            return JsonResp.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResp.fa("操作失败。");
        }
    }

    /**
     * @param orderId
     * @param money
     * @return
     */
    @RequestMapping(value = "/jieqinhuankuan", method = RequestMethod.GET)
    public JsonResp jieqinhuankuan(String orderId, String money) {
        try {
            java.math.BigDecimal cMoney = new BigDecimal(money);
            cMoney = cMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
            EntityWrapper ew = new EntityWrapper();
            ew.eq("id", orderId);
            LoanOrder ld = loanOrderService.selectOne(ew);
            java.math.BigDecimal oldMoney = ld.getConsultRepaymentMoney();
            cMoney = oldMoney.add(cMoney);
            ld.setConsultRepaymentMoney(cMoney);
            ld.setOrderStatus(6);
            ld.getPayStatus();
            loanOrderService.updateById(ld);
            EntityWrapper ew2 = new EntityWrapper();
            ew2.eq("id", ld.getUserId());
            User user = userService.selectOne(ew2);
            user.setStatus(2);
            userService.updateById(user);
            return JsonResp.ok();
        } catch (Exception e) {
            return JsonResp.fa("操作失败。");
        }
    }

    //拒绝地区设置
    @Autowired private SysAreaService    sysAreaService;
    @Autowired private RefuseAreaService refuseAreaService;

    @RequestMapping(value = "/selectRefuseArea", method = RequestMethod.GET)
    public JsonResp selectRefuseArea(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        Map cMap = new HashMap();
        List<SysArea> list = refuseAreaService.selectByMap(cMap);
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), list, list.size()));
    }

    @RequestMapping(value = "/addRefuseArea", method = RequestMethod.GET)
    public JsonResp addRefuseArea(String areaName) {
        List<SysArea> list = sysAreaService.selectLikeName("%" + areaName + "%");
        for (SysArea s : list) {
            EntityWrapper ew = new EntityWrapper();
            ew.eq("area_code", s.getAreaCode());
            Integer a = refuseAreaService.selectCount(ew);
            if (a == 0) {
                RefuseArea refuseArea = new RefuseArea();
                refuseArea.setAreaCode(s.getAreaCode());
                refuseArea.setAreaName(s.getAreaName());
                refuseAreaService.insert(refuseArea);
            }
        }
        return JsonResp.ok("success");
    }

    @RequestMapping(value = "/deleteRefuseArea", method = RequestMethod.GET)
    public JsonResp deleteRefuseArea(String id) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("id", id);
        refuseAreaService.delete(ew);
        return JsonResp.ok("success");
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @各管理员查询订单
     */
    @RequestMapping(value = "/adminSelectLoanOrder", method = RequestMethod.GET)
    public JsonResp adminSelectLoanOrder(Integer status, String phone, String gmtDatetime, Integer currentPage, String channelName) {
        log.debug("各管理员查询订单");
        //分页查询
        List<LoanOrder> list = loanOrderService
                .adminSelectLoanOrder(status, phone, channelName, gmtDatetime, currentPage);
        //查询总数,总放款额，总分成利润
        Map<String, Object> map = loanOrderService
                .adminSelectLoanOrderTotalAndMoney(status, phone, channelName, gmtDatetime);
        Integer total = Integer.valueOf(map.get("total").toString());
        Map<String, Object> map1 = new HashMap<>();
        map1.put("pageDto", new PageDto(currentPage, 10, list, total));
        map1.put("borrowMoney", map.get("borrowMoney").toString());
        map1.put("channelProfit", map.get("channelProfit").toString());
        return JsonResp.ok(map1);
    }

    /**
     * @param channelId(不为空说明后台查询指定 否则渠道商查询自己)
     * @return 返回值JsonResp
     * @渠道商查询订单
     */
    @RequestMapping(value = "/channelSelectLoanOrder", method = RequestMethod.GET)
    public JsonResp channelSelectLoanOrder(Integer status, String phone, String channelId, String gmtDatetime, Integer currentPage) {
        log.debug("渠道商查询订单");
        if (channelId == null) {
            channelId = userService.getCurentChannel().getId().toString();
        }
        //分页查询
        List<LoanOrder> list = loanOrderService
                .channelSelectOrder(status, phone, channelId, gmtDatetime, currentPage);
        //查询总数,总放款额，总分成利润
        Map<String, Object> map = loanOrderService
                .channelSelectLoanOrderTotalAndMoney(status, phone, channelId, gmtDatetime);
        Integer total = Integer.valueOf(map.get("total").toString());
        Map<String, Object> map1 = new HashMap<>();
        map1.put("pageDto", new PageDto(currentPage, 10, list, total));
        map1.put("borrowMoney", map.get("borrowMoney").toString());
        map1.put("channelProfit", map.get("channelProfit").toString());
        return JsonResp.ok(map1);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @后台查询订单
     */
    @RequestMapping(value = "/userSelectLoanOrder", method = RequestMethod.GET)
    public JsonResp userSelectLoanOrder(Integer status, String phone, String gmtDatetime, Integer currentPage, Integer payStatus,
                                        Integer renewalStatus, Integer moneyBackStatus, String realPayTime, String limitPayTime, String name,
                                        Integer giveStatus, String extendSign) {
        log.debug("后台查询订单");
        //分页查询
        List<LoanOrder> list = loanOrderService
                .userSelectLoanOrder(status, phone, gmtDatetime, currentPage, payStatus,
                        renewalStatus, moneyBackStatus, realPayTime, limitPayTime, name, giveStatus,
                        extendSign);
        for (LoanOrder loanOrder : list) {
            int overdueCount = loanOrderService.getOverdueCount(loanOrder.getUserId());
            boolean onceOverdueSign = overdueCount > 0 ? true : false;
            loanOrder.getUser().setOnceOverdueSign(onceOverdueSign);
            loanOrder.setChannelName(userService.getChannleNameByUser(loanOrder.getUserId()));
            if (payStatus != null) {
                if (payStatus == 2) {
                    //逾期列表做特殊处理
                    EntityWrapper<OrderExtend> ewOrderEx = new EntityWrapper<>();
                    ewOrderEx.eq("order_id", loanOrder.getId());
                    ewOrderEx.eq("status", 1);
                    List<OrderExtend> orderExtends = orderExtendService.selectList(ewOrderEx);
                    if (orderExtends != null && orderExtends.size() > 0) {
                        loanOrder.setExtendSign(Boolean.TRUE);
                    } else {
                        loanOrder.setExtendSign(Boolean.FALSE);
                    }
                }
            }
        }
        //查询总数,总放款额，总分成利润
        Map<String, Object> map = loanOrderService
                .userSelectLoanOrderTotalAndMoney(status, phone, gmtDatetime, payStatus,
                        renewalStatus, moneyBackStatus, realPayTime, limitPayTime, name, giveStatus,
                        extendSign);
        Integer total = Integer.valueOf(map.get("total").toString());
        Map<String, Object> map1 = new HashMap<>();
        map1.put("pageDto", new PageDto(currentPage, 10, list, total));
        map1.put("borrowMoney", map.get("borrowMoney").toString());
        map1.put("realPayMoney", map.get("realPayMoney").toString());
        map1.put("principalMoney", map.get("principalMoney").toString());
        return JsonResp.ok(map1);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @后台查询订单
     */
    @RequestMapping(value = "/userSelectRepaymentOrder", method = RequestMethod.GET)
    public JsonResp userSelectRepaymentOrder(Integer status, String phone, String gmtDatetime, Integer currentPage, Integer payStatus,
                                        Integer renewalStatus, Integer moneyBackStatus, String realPayTime, String limitPayTime, String name,
                                        Integer giveStatus, String extendSign) {
        log.debug("后台查询订单");
        //分页查询
        List<LoanOrder> list = loanOrderService
                .userSelectLoanOrder(status, phone, gmtDatetime, currentPage, payStatus,
                        renewalStatus, moneyBackStatus, realPayTime, limitPayTime, name, giveStatus,
                        extendSign);
        for (LoanOrder loanOrder : list) {
            int overdueCount = loanOrderService.getOverdueCount(loanOrder.getUserId());
            boolean onceOverdueSign = overdueCount > 0 ? true : false;
            loanOrder.getUser().setOnceOverdueSign(onceOverdueSign);
            loanOrder.setChannelName(userService.getChannleNameByUser(loanOrder.getUserId()));
            if (payStatus != null) {
                if (payStatus == 2) {
                    //逾期列表做特殊处理
                    EntityWrapper<OrderExtend> ewOrderEx = new EntityWrapper<>();
                    ewOrderEx.eq("order_id", loanOrder.getId());
                    ewOrderEx.eq("status", 1);
                    List<OrderExtend> orderExtends = orderExtendService.selectList(ewOrderEx);
                    if (orderExtends != null && orderExtends.size() > 0) {
                        loanOrder.setExtendSign(Boolean.TRUE);
                    } else {
                        loanOrder.setExtendSign(Boolean.FALSE);
                    }
                }
            }
        }
        //查询总数,总放款额，总分成利润
        Map<String, Object> map = loanOrderService
                .userSelectLoanOrderTotalAndMoney(status, phone, gmtDatetime, payStatus,
                        renewalStatus, moneyBackStatus, realPayTime, limitPayTime, name, giveStatus,
                        extendSign);
        Integer total = Integer.valueOf(map.get("total").toString());
        Map<String, Object> map1 = new HashMap<>();
        map1.put("pageDto", new PageDto(currentPage, 10, list, total));
        map1.put("borrowMoney", map.get("borrowMoney").toString());
        map1.put("realPayMoney", map.get("realPayMoney").toString());
        map1.put("principalMoney", map.get("principalMoney").toString());
        return JsonResp.ok(map1);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @后台查询订单
     */
    @RequestMapping(value = "/userSelectLoanOrder2", method = RequestMethod.GET)
    public JsonResp userSelectLoanOrder2(Integer status, String phone, String gmtDatetime,
                                         Integer currentPage, Integer payStatus,
                                         Integer renewalStatus, Integer moneyBackStatus, String realPayTime, String limitPayTime, String name) {
        //老用户不再算利润
        User user = userService.selectById(1053);
        LoanOrder loanOrder = loanOrderService.selectById(6820);
        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper<>();
        ewOrder.eq("order_status", 11);
        ewOrder.eq("user_id", user.getId());
        List<LoanOrder> loanOrderList = loanOrderService.selectList(ewOrder);
        if (loanOrderList.size() == 0) {
            EntityWrapper<Channel> wrapper = new EntityWrapper<>();
            wrapper.eq("id", user.getChannelId());
            Channel channel = channelService.selectOne(wrapper);
            BigDecimal bigDecimal = new BigDecimal(channel.getProportion()).multiply(loanOrder.getBorrowMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
            channel.setProfit(new BigDecimal(channel.getProfit()).add(bigDecimal).toString());
            channel.setOutMoney(new BigDecimal(channel.getOutMoney()).add(loanOrder.getRealMoney()).toString());
            channelService.updateById(channel);
            loanOrder.setChannelProfit(bigDecimal);
            loanOrderService.updateById(loanOrder);
        }
        return JsonResp.ok();
    }

    /**
     * 后台管理员申请审核订单
     *
     * @return
     */
    @RequestMapping(value = "/applyLoanAuditList", method = RequestMethod.GET)
    public JsonResp applyLoanAuditList(Integer applyNum) {
        Admin admin = userService.getThisLogin();

        //最大订单申请数量
        EntityWrapper<SystemConfig> systemConfigEntityWrapper = new EntityWrapper<>();
        systemConfigEntityWrapper.eq("config_key", "maxAuditNum");
        SystemConfig systemConfig = systemConfigService.selectOne(systemConfigEntityWrapper);

        EntityWrapper<LoanOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_status", 1);
        entityWrapper.eq("auditor_id", admin.getId());
        Integer count = loanOrderService.selectCount(entityWrapper);

        if (count + applyNum > Integer.valueOf(systemConfig.getConfigValue())) {
            return JsonResp.fa("您的申请超额！");
        }
        EntityWrapper<LoanOrder> loanOrderEntityWrapper = new EntityWrapper<>();
        loanOrderEntityWrapper.eq("order_status", 1);
        loanOrderEntityWrapper.isNull("auditor_id");
        List<LoanOrder> loanOrderList = loanOrderService.selectList(loanOrderEntityWrapper);
        if (loanOrderList.size() <= applyNum) {
            for (LoanOrder loanOrder : loanOrderList) {
                loanOrder.setAuditorId(admin.getId());
            }
            loanOrderService.updateBatchById(loanOrderList);
        } else {
            List<LoanOrder> loanOrderList1 = loanOrderList.subList(0, Integer.valueOf(systemConfig.getConfigValue()));
            for (LoanOrder loanOrder : loanOrderList1) {
                loanOrder.setAuditorId(admin.getId());
            }
            loanOrderService.updateBatchById(loanOrderList1);
        }
        return JsonResp.ok();
    }

    /**
     * 查询打款中的订单
     *
     * @return
     */
    @RequestMapping(value = "/selectPassingList", method = RequestMethod.GET)
    public JsonResp selectPassingList(String phone, String userName, Integer currentPage) {

        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("userName", userName);
        map.put("pageNo", (currentPage - 1) * 10);
        map.put("pageSize", 10);
        List<LoanOrder> loanOrderList = loanOrderService.selectPassingList(map);
        Integer count = loanOrderService.selectPassingNum(map);
        return JsonResp.ok(new PageDto(currentPage, 10, loanOrderList, count));
    }

    /**
     * 批量分配
     *
     * @return
     */
    @RequestMapping(value = "/allocationList", method = RequestMethod.POST)
    public JsonResp allocationList(@RequestBody JSONObject jsonObject) {
        String adminId = jsonObject.get("adminId").toString();
        String id = jsonObject.get("ids").toString();
        JSONArray ids = JSON.parseArray(id);
        for (int i = 0; i < ids.size(); i++) {

            LoanOrder loanOrder = loanOrderService.selectById(ids.get(i).toString());
            loanOrder.setPressMoneyMan(new Long(adminId));
            loanOrderService.updateById(loanOrder);
        }
        return JsonResp.ok();
    }

    /**
     * 查询催收列表的订单
     *
     * @return
     */
    @RequestMapping(value = "/selectMyAllPassingOrders", method = RequestMethod.GET)
    public JsonResp selectMyAllPassingOrders(Integer pageNo, String allocationStatus, String orderId, String gmtDatetime,
                                             Integer overDueDays, String phone, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("allocationStatus", allocationStatus);
        map.put("orderId", orderId);
        map.put("gmtDatetime", gmtDatetime);
        if (gmtDatetime != null && !"".equals(gmtDatetime)) {
            String time[] = gmtDatetime.split("~");
            map.put("time1", time[0]);
            map.put("time2", time[1]);
        }
        map.put("overDueDays", overDueDays);
        map.put("phone", phone);
        map.put("name", name);
        map.put("pageNo", (pageNo - 1) * 30);
        map.put("pageSize", 30);
//        map.put("beginPressDate", DateUtils.dayAdd(1, DateUtils.getTimesmorning()));
        //逾期待催收款客户
        map.put("beginPressDate", DateUtils.getTimesmorning());
        List<LoanOrder> loanOrderList = loanOrderService.selectMyAllPassingOrders(map);
        for (LoanOrder loanOrder : loanOrderList) {
            Long pressMoneyManId = loanOrder.getPressMoneyMan();
            if (pressMoneyManId != null) {
                Admin admin = adminService.selectById(pressMoneyManId);
                loanOrder.setPressMoneyManName(admin.getUserName());
            }
        }
        Map<String, Object> sumMap = loanOrderService.selectMyAllPassingOrdersNum(map);
        Long count = (Long) sumMap.get("count");
        BigDecimal totalMoney = (BigDecimal) sumMap.get("totalMoney");

        HashMap<String, Object> resultHashMap = new HashMap<>();
        resultHashMap.put("totalMoney", totalMoney);
        resultHashMap.put("totalCount", count);
        resultHashMap.put("pageDto", new PageDto(pageNo, 30, loanOrderList, count));
        return JsonResp.ok(resultHashMap);
    }

    /**
     * 贷前审核
     *
     * @return
     */
    @RequestMapping(value = "/loanPre", method = RequestMethod.GET) public JsonResp loanPre(String orderId) throws IOException {
        log.info("<-----------贷前审核----------> orderId:"+orderId);
        return loanAudit( orderId);
    }

    /**
     * 贷款申请恢复
     *
     * @return
     */
    @RequestMapping(value = "/thisRecover", method = RequestMethod.GET) public JsonResp thisRecover(
            Long id) throws Exception {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        loanOrder.setOrderStatus(1);
        loanOrderService.insertOrUpdate(loanOrder);
        return JsonResp.ok();
    }

    @SystemLog(description ="获取列表")
    @RequestMapping(value = "/selectConstraintPaymentRecordPage", method = RequestMethod.GET)
    public  JsonResp selectConstraintPaymentRecordPage(Integer pageNo,Integer pageSize,Long orderId ){
        EntityWrapper<ConstraintPaymentRecord> ew = new EntityWrapper<>();
        ew.eq("order_id", orderId);
        Page<ConstraintPaymentRecord> constraintPaymentRecordPage = constraintPaymentRecordService
                .selectPage(new Page<ConstraintPaymentRecord>(pageNo, pageSize), ew);

        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageResult", constraintPaymentRecordPage);
        resultMap.put("loanOrder", loanOrder);
        return JsonResp.ok(resultMap);
    }

    /**
     * 强制还款
     *
     * @return
     */
    @SystemLog(description = "自动扣款")
    @RequestMapping(value = "/constraintPayment", method = RequestMethod.GET)
    public JsonResp constraintPayment(Long loanId, String payment) throws Exception {
        LoanOrder loanOrder = loanOrderService.selectById(loanId);
        if (loanOrder.getNeedPayMoney().compareTo(new BigDecimal(payment.trim())) < 0) {
            return JsonResp.toFail("扣款金额大于实际需要还款金额");
        }
        //先查询一下
        EntityWrapper<ConstraintPaymentRecord> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_id", loanId);
        entityWrapper.orderBy("create_time", false);
        List<ConstraintPaymentRecord> constraintPaymentRecords = constraintPaymentRecordService.selectList(entityWrapper);
        if (constraintPaymentRecords.size() > 0) {
            ConstraintPaymentRecord constraintPaymentRecord = constraintPaymentRecords.get(0);
            if ("PROCESSING".equals(constraintPaymentRecord.getStatus())) {
                return JsonResp.toFail("该订单正在处理中,请稍后！！！");
            }
        }
        String result = constraintPaymentService.payment(loanId, payment.trim());
        if ("success".equals(result)) {
            return JsonResp.ok(result);
        } else {
            return JsonResp.toFail(result);
        }
    }

    @RequestMapping(value = "/selectstatistics", method = RequestMethod.GET)
    public JsonResp selectStatistics(Integer pageNo, Integer pageSize, String orderId) {
        EntityWrapper<ConstraintPaymentRecord> ew = new EntityWrapper<>();
        ew.eq("order_id", orderId);
        Page<ConstraintPaymentRecord> constraintPaymentRecordPage = constraintPaymentRecordService
                .selectPage(new Page<ConstraintPaymentRecord>(pageNo, pageSize), ew);

        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageResult", constraintPaymentRecordPage);
        resultMap.put("loanOrder", loanOrder);
        return JsonResp.ok(resultMap);
    }

    private JsonResp<String> loanAudit(String orderId) {
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        if (loanOrder.getOrderStatus() != 1) {
            return JsonResp.fa("不是审核中订单,不能审核");
        }
        User user = userService.selectById(loanOrder.getUserId());

        Map<String, Object> map = new HashMap<>();
        map.put("giveStatus", 2);
        map.put("userId", loanOrder.getUserId());
        map.put("pageNo", 0);
        map.put("pageSize", 1);
        List<LoanOrder> list = loanOrderService.userSelectLoanOrder(map);
        // 新用户或者老用户离上次申请日期大于30天
        if (user.getIsOld() == 0 || CollectionUtils.isEmpty(list) || DateUtils
                .dayAdd(30, list.get(0).getGiveTime()).before(new Date())) {
            // 进行风控 审核
            log.info("<--------进行风控审核------->");
            riskService.runRiskRule(orderId);
        }

        // 老用户不满30天自动审核通过
        if (user.getIsOld() == 1 && CollectionUtils.isNotEmpty(list) && DateUtils.dayAdd(30, list.get(0).getGiveTime()).after(new Date())){
            //审核通过
            loanOrder.setOrderStatus(2);
            loanOrder.setTdScore( list.get(0).getTdScore());
            loanOrderService.updateById(loanOrder);
        }else{
            if (StringUtil.isEmpty(loanOrder.getTdScore()) || loanOrder.getTdScore().equals("0")) {
                return JsonResp.fa("风控错误,请联系客服");
            }
            // 获取系统设置的分数
            EntityWrapper<SystemConfig> ew1 = new EntityWrapper();
            ew1.eq("config_key", "tongdunfenshu");
            SystemConfig systemConfig1 = sysConfigService.selectOne(ew1);
            BigDecimal score = new BigDecimal(600);
            if(systemConfig1 != null && StringUtil.isNotEmpty(systemConfig1.getConfigValue())){
                try {
                    score = new BigDecimal(systemConfig1.getConfigValue());
                }catch (Exception  e){
                    e.printStackTrace();
                }
            }
            if ((new BigDecimal(loanOrder.getTdScore())).compareTo(score)>=0) {
                //审核通过
                loanOrder.setOrderStatus(2);
                loanOrder.setPassTime(new Date());
                loanOrderService.updateById(loanOrder);
            } else {
                //审核被拒
                loanOrder.setOrderStatus(7);
                loanOrderService.updateById(loanOrder);
                EntityWrapper<UserIdentity> identityEntityWrapper = new EntityWrapper<>();
                identityEntityWrapper.eq("user_id", user.getId());
                UserIdentity userIdentity = userIdentityService.selectOne(identityEntityWrapper);
                // 拉入黑名单
                UserBlack userBlack = new UserBlack();
                userBlack.setIdCard(user.getIdNo());
                userBlack.setPhone(user.getPhone());
                userBlack.setRealName(userIdentity.getUserName());
                userBlack.setCreateTime(new Date());
                userBlackService.insert(userBlack);

                user.setStatus(2);
                user.setIsPay(0);
                userService.updateById(user);
                return JsonResp.fa("审核被拒");
            }

        }
        return JsonResp.okString("审核通过");
    }

    /**
     * 放款
     * @param id
     * @return
     */
    private JsonResp<String> giveMoney(String id){
        log.debug("打款");
        //待打款
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if(loanOrder.getOrderStatus()!=2){
            return  JsonResp.fa("不是待打款订单,不能打款");
        }
        User user = userService.selectById(loanOrder.getUserId());
        loanOrder.setOrderStatus(2);
        loanOrder.setPassTime(new Date());
        boolean bo = loanOrderService.updateById(loanOrder);
        PersonRecord pr = personRecordService.selectById(2);
        if (bo) {
            pr.setOutOrderCount(pr.getOutOrderCount() + 1);
        }
        personRecordService.updateById(pr);
        // 发送审核通过短信
        YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_EXAMINE_PASS,user.getPhone(),null);

        // 打款
        EntityWrapper<UserBank> wrapper = new EntityWrapper();
        wrapper.eq("user_id", user.getId());
        UserBank userBank = userBankService.selectOne(wrapper);
        //发起打款
        Map<String,Object> resultMap= TransferApi.transfer(userBank, loanOrder);

        if (!"0000".equals(resultMap.get("rt2_retCode"))) {
            loanOrder.setGiveStatus(0);
            return JsonResp.fa(resultMap.get("rt3_retMsg").toString());
        } else {
            // 获取打款结果
            String orderStatus = resultMap.get("rt3_retMsg").toString();
            if (StringUtil.isEmpty(orderStatus)) {
                // 打款失败
                loanOrder.setOrderStatus(9);
                loanOrder.setGiveStatus(3);
            } else if ("接收成功".equals(orderStatus)) {
                // 打款成功
                loanOrder.setGiveStatus(2);
                loanOrder.setOrderStatus(3);
                loanOrder.setGiveTime(new Date());
                //改变时间
                loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1, new Date()));
                loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays() + loanOrder.getAllowDays() - 1, new Date()));
                loanOrderService.updateById(loanOrder);
                //统计
                try {
                    if(user.getIsOld()==0){
                        everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.LENDING_NUM.getKey(),user.getChannelId()==null?0:user.getChannelId());
                        log.info("order放款--统计+1");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                EntityWrapper<PersonRecord> ew = new EntityWrapper<>();
                PersonRecord personRecord = personRecordService.selectOne(ew);
                personRecord.setOutOrderCount(personRecord.getOutOrderCount() + 1);
                personRecord.setOutMoney(personRecord.getOutMoney().add(loanOrder.getRealMoney()));
                personRecordService.updateById(personRecord);
                //有渠道商引进的，记录渠道商信息
                if (user.getChannelId() != null) {
                    //老用户不再算利润
                    EntityWrapper<LoanOrder> ewOrder = new EntityWrapper<>();
                    ewOrder.eq("order_status", 6);
                    ewOrder.eq("user_id", user.getId());
                    List<LoanOrder> loanOrderList = loanOrderService.selectList(ewOrder);
                    if (loanOrderList.size() == 0) {
                        EntityWrapper<Channel> cwrapper = new EntityWrapper<>();
                        cwrapper.eq("id", user.getChannelId());
                        Channel channel = channelService.selectOne(cwrapper);

                        BigDecimal proportion = StringUtil.isEmpty(channel.getProportion()) ? BigDecimal.ZERO : new BigDecimal(channel.getProportion());
                        BigDecimal profit = StringUtil.isEmpty(channel.getProfit()) ? BigDecimal.ZERO : new BigDecimal(channel.getProfit());
                        BigDecimal outMoney = StringUtil.isEmpty(channel.getOutMoney()) ? BigDecimal.ZERO : new BigDecimal(channel.getOutMoney());

                        BigDecimal bigDecimal = proportion.multiply(loanOrder.getBorrowMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        channel.setProfit(profit.add(bigDecimal).toString());
                        channel.setOutMoney(outMoney.add(loanOrder.getRealMoney()).toString());

                        channelService.updateById(channel);

                        loanOrder.setChannelProfit(bigDecimal);
                        loanOrderService.updateById(loanOrder);
                    }
                }
            } else {
                // 打款中
                loanOrder.setOrderStatus(9);
                loanOrder.setGiveStatus(1);
            }
            loanOrder.setLianPayNum(resultMap.get("rt5_orderId").toString());
            loanOrderService.updateById(loanOrder);
        }
        return JsonResp.okString("打款中,请注意接收");
    }
}