package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.annotation.SourceAuthority;
import com.rongke.annotation.SystemLog;
import com.rongke.commons.JpushClientUtil;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.commons.constant.MsgTemplate;
import com.rongke.commons.constant.PushTemplate;
import com.rongke.enums.EveryDayDataType;
import com.rongke.enums.RiskRuleNo;
import com.rongke.enums.SourceType;
import com.rongke.mapper.UserMapper;
import com.rongke.model.*;
import com.rongke.redis.CacheUtil;
import com.rongke.service.*;
import com.rongke.utils.*;
import com.rongke.web.apix.*;
import com.rongke.web.common.SmsAPI;
import com.rongke.web.lianpay.ConfirmPaymentApi;
import com.rongke.web.lianpay.FenqiPayment;
import com.rongke.web.payutil.helipay.action.QuickPayApi;
import com.rongke.web.payutil.helipay.action.TransferApi;
import com.rongke.web.payutil.helipay.util.MessageHandle;
import com.rongke.web.payutil.helipay.vo.request.BindCardPayVo;
import com.rongke.web.payutil.helipay.vo.request.BindPayValidateCodeVo;
import com.rongke.web.payutil.helipay.vo.request.QueryOrderVo;
import com.rongke.web.util.YunpianSmsUtil;
import com.rongke.yibaoApi.RepaymentApi;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.rongke.commons.JsonResp.ok;

/**
 * @version : Ver 1.0
 * @UserController
 * @用户基本信息Controller
 */
@RestController
@RequestMapping(value = "/api/user")
@Transactional
@CrossOrigin
public class UserController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userDao;
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ParamSettingService paramSettingService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserPhoneRecordService userPhoneRecordService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    private UserBankService userBankService;
    @Autowired
    private UserPhoneService userPhoneService;
    @Autowired
    private UserTaobaoAddressService userTaobaoAddressService;
    @Autowired
    private UserJindongAddressService userJindongAddressService;
    @Autowired
    private UserTaobaoZhifubaoService userTaobaoZhifubaoService;
    @Autowired
    private PersonRecordService personRecordService;
    @Autowired
    private PayOrderConfirmService payOrderConfirmService;
    @Autowired
    private MsgModelService msgModelService;
    @Autowired
    private SystemConfigService sysConfigService;
    @Autowired
    private OrderExtendService orderExtendService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private SmsAPI smsAPI;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserZhifubaoService userZhifubaoService;

    @Autowired
    private UdInfoService udInfoService;

    @Autowired
    private CacheUtil cacheUtil;
//    @Autowired
//    private SmsUtil smsUtil;

    @Autowired
    private TextTemplateService textTemplateService;
    @Autowired
    private UserRiskService userRiskService;

    @Autowired
    private EveryDayDataService everyDayDataService;
    private final static Map keymap = new HashMap<String, Object>();
    public static String alinotify = "/api/user/alipay/succ";// 还钱回调地址
    public static String alinotify1 = "/api/user/alipay/xuqiOrder";// 续期回调地址
//    public static final String HELIPAY_NOTIFY_URL =  "http://api.fintech-sx.com:8080/api/money/bindCardPay";

    /**
     * @param user
     * @return 返回值JsonResp
     * @添加用户基本信息
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUser(@RequestBody User user) {
        log.debug("添加用户基本信息");
        userService.insert(user);
        return ok(user);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @用户是否设置过交易密码
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/payPwdIsExist", method = RequestMethod.GET)
    public JsonResp payPwdIsExist() {
        log.debug("用户是否设置过交易密码");
        User user = userService.findLoginUser();
        if (null == user.getPayPwd()) {
            return ok("false");
        } else {
            return ok("true");
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @设置交易密码
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/payPwdSetting", method = RequestMethod.GET)
    public JsonResp payPwdSetting(String payPwd, String payPwdConfirm) {
        log.debug("设置交易密码");
        if (payPwd.equals(payPwdConfirm) && payPwd != null && !payPwd.equals("")) {
            User user = userService.findLoginUser();
            user.setPayPwd(payPwd);
            userService.updateById(user);
            return ok();
        } else {
            return JsonResp.fa("密码不一致！");
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @旧交易密码验证
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/oldPayPwdConfirm", method = RequestMethod.GET)
    public JsonResp oldPayPwdConfirm(String oldPayPwd) {
        log.debug("旧交易密码验证");
        User user = userService.findLoginUser();
        if (user.getPayPwd().equals(oldPayPwd)) {
            return ok();
        } else {
            return JsonResp.fa("密码不正确！");
        }
    }

    /**
     * @param user
     * @return 返回值JsonResp
     * @修改用户基本信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUser(@RequestBody User user) {
        log.debug("修改用户基本信息");
        userService.updateById(user);
        return ok(user);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找用户基本信息
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUser(Long id) {
        log.debug("查找用户基本信息");
        User user = userService.selectById(id);
        return ok(user);
    }

    /**
     * @param phone    手机号
     * @param password 密码
     * @return JsonResp
     * @throws UnsupportedEncodingException
     * @登录
     */
    @RequestMapping(value = "/loginPhone", method = RequestMethod.GET)
    public JsonResp loginByPhone(String phone, String password, String phoneSign, String version,String app) throws UnsupportedEncodingException {
        log.debug("登录");
        User user = userDao.selectByPhone(phone);
        if (user == null) {
            return JsonResp.fa("该用户不存在！");
        }
//        else if(user.getStatus()!=1){
//            return JsonResp.fa("该用户已不可用！");
//        }
        JsonResp jsonResp = userService.loginByPhone(URLDecoder.decode(phone, "utf-8"), password, phoneSign);
        User data = (User) jsonResp.getData();

        String versionKey = "iosVersion";
        if(!"ios".equals(app)){
            versionKey = "androidVersion";
        }
        String currentVersion = systemConfigService.selectValueByKey(versionKey);
        if (version != null && currentVersion.equals(version)) {
            data.setLoginStatus("1");
        } else {
            data.setLoginStatus("0");
        }
        return jsonResp;
    }

    /**
     * @注销
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonResp logout() {
        log.debug("注销");
        userService.logout();
        return ok();
    }

    /**
     * @return 返回值JsonResp
     * @用户首页信息
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/userFirstPage", method = RequestMethod.GET)
    public JsonResp getUserFirstPage(BigDecimal userMoney, Integer limitDays) {
        log.debug("用户首页信息");
        User user = userService.findLoginUser();
        Map map = new HashMap();
        //用户是否有还款中订单
        if (user.getIsPay() == 0) {
            if (null == userMoney) {
                userMoney = user.getMoney();
            }
            //算出费用
            EntityWrapper<ParamSetting> ew = new EntityWrapper<>();
            if (null == limitDays) {
                limitDays = 7;
            }
            ew.eq("limit_days", limitDays);
            ew.eq("status", 1);
            ParamSetting paramSetting = paramSettingService.selectOne(ew);
            BigDecimal interestMoney = userMoney.multiply(BigDecimal.valueOf(paramSetting.getInterestPercent() / 1000));
            BigDecimal placeServeMoney = userMoney.multiply(BigDecimal.valueOf(paramSetting.getPlaceServePercent() / 100));
            BigDecimal msgAuthMoney = userMoney.multiply(BigDecimal.valueOf(paramSetting.getMsgAuthPercent() / 100));
            BigDecimal riskServePercent = userMoney.multiply(BigDecimal.valueOf(paramSetting.getRiskServePercent() / 100));
            BigDecimal riskPlanPercent = userMoney.multiply(BigDecimal.valueOf(paramSetting.getRiskPlanPercent() / 100));
            BigDecimal allWasteMoney = interestMoney.add(placeServeMoney).add(msgAuthMoney).add(riskServePercent).add(riskPlanPercent);
            BigDecimal realMoney = userMoney.subtract(allWasteMoney);

            map.put("userMoney", user.getMoney());
            map.put("realMoney", realMoney);
            map.put("interestMoney", interestMoney);
            map.put("allWasteMoney", allWasteMoney);
            map.put("placeServeMoney", placeServeMoney);
            map.put("msgAuthMoney", msgAuthMoney);
            map.put("riskServePercent", riskServePercent);
            map.put("riskPlanPercent", riskPlanPercent);
            map.put("isPay", "true");
            /*//审核前qq
            EntityWrapper<SystemConfig> ewQQ = new EntityWrapper();
            ewQQ.eq("config_key","qqOne");
            SystemConfig systemConfig = sysConfigService.selectOne(ewQQ);
            map.put("qq",systemConfig.getConfigValue());*/
        } else {
            //有还款订单时
            EntityWrapper<LoanOrder> ew = new EntityWrapper<>();
            ew.eq("user_id", user.getId());
            List<Integer> list = new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(9);
            }};
            ew.in("order_status", list);
            LoanOrder loanOrder = loanOrderService.selectOne(ew);
            BigDecimal needPayMoney = loanOrder.getNeedPayMoney();
            BigDecimal borrowMoney = loanOrder.getBorrowMoney();
            Date gmtDatetime = loanOrder.getGmtDatetime();
            Date limitPayTime = loanOrder.getLimitPayTime();
            map.put("needPayMoney", needPayMoney);
            map.put("borrowMoney", borrowMoney);
            map.put("gmtDatetime", gmtDatetime);
            map.put("limitPayTime", limitPayTime);
            map.put("isPay", "false");
            map.put("orderId", loanOrder.getId());
        }

        return ok(map);
    }

    /**
     * @注册用户
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public JsonResp register(String phone, String password, String code, String uuid, String channelId) {
        if (cacheUtil.hasKey(phone)) {
            if (code.equals(cacheUtil.get(phone))) {

                EntityWrapper<User> ew = new EntityWrapper<User>();
                ew.eq("phone", phone);
                List<User> users = userService.selectList(ew);
                if (users.size() <= 0) {
                    User user = new User();
                    user.setPhone(phone);
                    user.setPassword(Md5.md5Encode(password));
                    boolean boo = userService.insert(user);


                    //添加用户验证表
                    UserAuth userAuth = new UserAuth();
                    userAuth.setUserId(user.getId());
                    userAuthService.insert(userAuth);

                    //添加用户支付宝认证表
                    UserZhifubao userZhifubao = new UserZhifubao();
                    userZhifubao.setUserId(user.getId());
                    userZhifubaoService.insert(userZhifubao);

                    //给用户额度
                    EntityWrapper<SystemConfig> ewSys = new EntityWrapper();
                    ewSys.eq("config_key", "money");
                    SystemConfig systemConfig1 = sysConfigService.selectOne(ewSys);
                    user.setMoney(new BigDecimal(systemConfig1.getConfigValue()));
                    userService.updateById(user);
                    //新客优惠券
                    EntityWrapper<Coupon> ew4 = new EntityWrapper<>();
                    ew4.eq("type", 1);
                    ew4.eq("coupon_status", 1);
                    Coupon firstCoupon = couponService.selectOne(ew4);
                    if (firstCoupon != null) {
                        UserCoupon userCoupon = new UserCoupon();
                        userCoupon.setUserId(user.getId());
                        userCoupon.setCouponId(firstCoupon.getId());
                        userCoupon.setSaveMoney(firstCoupon.getSaveMoney());
                        userCouponService.insert(userCoupon);
                        //会员总数加一
                        EntityWrapper<PersonRecord> ewPserson = new EntityWrapper();
                        PersonRecord personRecord = personRecordService.selectOne(ewPserson);
                        personRecord.setMemberCount(personRecord.getMemberCount() + 1);
                        personRecordService.updateById(personRecord);
                        //用户优惠券总数加一
                        user.setCouponAllCount(1);
                    }

                    //邀请好友奖励
                    if (uuid != null) {
                        EntityWrapper ew2 = new EntityWrapper<>();
                        ew2.eq("uuid", uuid);
                        User shareUser = userService.selectOne(ew2);
                        if (shareUser != null && shareUser.getStatus() == 1) {
                            UserCoupon shareUserCoupon = new UserCoupon();
                            shareUserCoupon.setUserId(shareUser.getId());
                            EntityWrapper<Coupon> ew3 = new EntityWrapper<>();
                            ew3.eq("type", 2);
                            ew3.eq("coupon_status", 1);
                            Coupon sharecoupon = couponService.selectOne(ew3);
                            if (sharecoupon != null) {
                                shareUserCoupon.setCouponId(sharecoupon.getId());
                                shareUserCoupon.setSaveMoney(sharecoupon.getSaveMoney());
                                shareUserCoupon.setInviteePhone(user.getPhone());
                                userCouponService.insert(shareUserCoupon);
                                //邀请者优惠券总数加一
                                shareUser.setCouponAllCount(shareUser.getCouponAllCount() + 1);
                                userService.updateById(shareUser);
                            }
                        }
                    }
                    //是否有渠道商
                    if (channelId != null && !channelId.equals("")) {
                        EntityWrapper<Channel> ewC = new EntityWrapper();
                        ewC.eq("login_name", channelId);
                        ewC.eq("status", 1);
                        Channel channel = channelService.selectOne(ewC);
                        channel.setMemberCount(channel.getMemberCount() + 1);
                        channelService.updateById(channel);
                        user.setChannelId(channel.getId());
                        userService.updateById(user);


                        try {
                            //扣量注册
                            everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.REG_NUM.getKey(),channel.getId());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    //风控流程之用户注册
                    try {
                        String riskResult = userRiskService.dealRegisterRisk(user.getId());
                        if(RiskRuleNo.RISK_FAIL.equals(riskResult)){
                            //失败，将认证状态修改为4.认证失败
                            user.setAuthStatus(4);
                            userService.updateById(user);
                        }
                    }catch (Exception e){
                        log.error(e);
                    }
                    return ok(boo);
                } else {
                    return JsonResp.fa("该号码已被注册！");
                }
            } else {
                return JsonResp.fa("验证码不对");
            }
        } else {
            return JsonResp.fa("信息输入有误！");
        }
    }

    /**
     * @return 返回值JsonResp
     * @获取验证码
     */
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.GET)
    public JsonResp getPhoneCode(String phone) {
        log.debug("获取验证码");
        String code = null; //验证码
        code = RandomUtils.randomString(6);
        try {
            String[] arr ={code};
            YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.VERFIY,phone,arr);
        } catch (Exception e) {
            return JsonResp.fa("发送验证码失败");
        }
        if (cacheUtil.hasKey(phone)) {
            cacheUtil.delkey(phone);
        }
        cacheUtil.set(phone, code, 120L);
        return ok("获取验证码成功！");
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @忘记密码
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/updatePasswordPhone", method = RequestMethod.GET)
    public JsonResp<?> updatePasswordPhone(String phone, String code, String password) {
        log.debug("忘记密码");
        EntityWrapper<User> ew = new EntityWrapper();
        ew.eq("phone", phone);
        User user = userService.selectOne(ew);
        if (user == null) {
            return JsonResp.fa("手机号码不存在！！");
        } else {
            String phoneCode = null;
            if (cacheUtil.hasKey(phone)) {
                phoneCode = cacheUtil.get(phone);
            }
            if (phoneCode == null || code == null || !code.equals(phoneCode)) {
                return JsonResp.fa("验证码错误");
            } else {
                user.setPassword(Md5.md5Encode(password));
                userService.updateById(user);
                return ok("修改成功");
            }
        }
    }

    /**
     * @return 返回值JsonResp
     * @分享链接参数
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/shareUrlParam", method = RequestMethod.GET)
    public JsonResp shareUrlParam() {
        log.debug("分享链接参数");
        User user = userService.findLoginUser();
        return ok(user.getUuid());
    }

    /**
     * 后台信审员申请信审订单
     *
     * @return
     */
    @RequestMapping(value = "/applyAuthAuditList", method = RequestMethod.GET)
    synchronized public JsonResp applyAuthAuditList(Integer applyNum) {

        Admin admin = userService.getThisLogin();

        //最大订单申请数量
        EntityWrapper<SystemConfig> systemConfigEntityWrapper = new EntityWrapper<>();
        systemConfigEntityWrapper.eq("config_key", "maxAuditNum");
        SystemConfig systemConfig = systemConfigService.selectOne(systemConfigEntityWrapper);

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("admin_audit_id", admin.getId());
        userEntityWrapper.eq("auth_status", 0);
        Integer count = userService.selectCount(userEntityWrapper);
        if (count + applyNum > Integer.valueOf(systemConfig.getConfigValue())) {
            return JsonResp.fa("您的申请超额！");
        }
        Map<String, Object> map = new HashMap<>();

        List<User> userList = userService.selectLinquUser();
        if (applyNum >= userList.size()) {//待审核的人数少于等于用户申请审核的人数
            for (User user : userList) {

                user.setAdminAuditId(admin.getId());
            }
            userService.updateBatchById(userList);
        } else if (applyNum < userList.size()) {

            for (User user : userList) {
                if (applyNum > 0) {
                    user.setAdminAuditId(admin.getId());
                }
                applyNum--;
            }
            userService.updateBatchById(userList);
        }
        return ok();
    }

    @RequestMapping(value = "/selectLinQuInfo", method = RequestMethod.GET)
    public JsonResp selectLinQuInfo() {
        Admin admin = userService.getThisLogin();

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("admin_audit_id", admin.getId());
        userEntityWrapper.eq("auth_status", 0);
        Integer count = userService.selectCount(userEntityWrapper);

        EntityWrapper<SystemConfig> systemConfigEntityWrapper = new EntityWrapper<>();
        systemConfigEntityWrapper.eq("config_key", "maxAuditNum");
        SystemConfig systemConfig = systemConfigService.selectOne(systemConfigEntityWrapper);

        List<Integer> stringList = new ArrayList<>();
        int leftNum = Integer.valueOf(systemConfig.getConfigValue()) - count;//剩余可领取数量
        stringList.add(leftNum);

        Integer canlinqu = userService.selectCanLinquNum();
        stringList.add(canlinqu);
        return ok(stringList);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查询所有的用户列表
     */
    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    public JsonResp selectUserList(Page page, String authStatus, String phone, String userName, String status) {
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.orderBy("gmt_datetime", false);
        if (status != null && !"".equals(status)) {
            userEntityWrapper.eq("status", status);
        }

        if (authStatus != null && !"".equals(authStatus)) {
            userEntityWrapper.eq("auth_status", authStatus);
        }
        if (phone != null && !"".equals(phone)) {
            userEntityWrapper.like("phone", phone);
        }
        if (userName != null && !"".equals(userName)) {
            userEntityWrapper.like("user_name", userName);
        }



        Page<User> userPage = userService.selectPage(page, userEntityWrapper);
        for(User user :userPage.getRecords()){
            EntityWrapper<UserAuth>entityWrapper=new EntityWrapper<>();
            entityWrapper.eq("user_id",user.getId());
            UserAuth userAuth= userAuthService.selectOne(entityWrapper);
            user.setUserAuth(userAuth);

            EntityWrapper<Channel>entityWrapper2=new EntityWrapper<>();
            entityWrapper2.eq("id",user.getChannelId());
            Channel channel = channelService.selectOne(entityWrapper2);
            if(channel!=null){
                 user.setChannelName(channel.getName());
            }


        }

        return JsonResp.dataPage(userPage);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @禁用用户
     */
    @RequestMapping(value = "/GoDeleteUpdate", method = RequestMethod.GET)
    public JsonResp GoDeleteUpdate(Long id) {
        User user = new User();
        user.setId(id);
        user.setStatus(3);
        boolean boo = userService.updateById(user);

        return ok(boo);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @拉黑用户
     */
    @RequestMapping(value = "/GoDeleteUpdate1", method = RequestMethod.GET)
    public JsonResp GoDeleteUpdate1(Long id) {
        User user = userService.selectById(id);
        if (user.getStatus().equals(3)) {
            return JsonResp.fa("操作不合法");
        } else {
            user.setId(id);
            user.setStatus(2);
            boolean boo = userService.updateById(user);

            return ok(boo);
        }
    }

    /**
     * @return 返回值JsonResp
     * @用户是否能借款
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/userIsBorrow", method = RequestMethod.GET)
    public JsonResp userIsBorrow() {
        log.debug("用户是否能借款");
        User user = userService.findLoginUser();
        if (user.getAuthStatus() == 1) {
            return ok("true");
        } else {
            return JsonResp.fa("false");
        }
    }

    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/userIsRefuse", method = RequestMethod.GET)
    public JsonResp userIsRefuse() {
        User user = userService.findLoginUser();
        if (user.getStatus() == 1) {
            return ok("true");
        } else {
            return JsonResp.fa("false");
        }
    }

    /**
     * @return 返回值JsonResp
     * @用户首页的类型
     */
    //@SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/userFirstPageType", method = RequestMethod.GET)
    public JsonResp userFirstPageType() {
        log.debug("用户首页的类型");
        User user = userService.findLoginUser();
        if (user.getIsPay() == 0) {
            return ok("true");
        } else {
            EntityWrapper<LoanOrder> ew = new EntityWrapper();
            ew.eq("user_id", user.getId());
            ew.eq("order_status", 1);
            LoanOrder loanOrder = loanOrderService.selectOne(ew);
            if (loanOrder != null) {
                return ok("loading");
            } else {
                return ok("false");
            }
        }
    }

    /*   */
    /**
     * @return 返回值JsonResp
     * @运营商获取验证码
     *//*
    @RequestMapping(value = "/getSmsCodePhoneConfirm", method = RequestMethod.GET)
    public JsonResp getSmsCodePhoneConfirm(String phoneNo, String passwd) throws IOException {
        log.debug("运营商获取验证码");
        User user = userService.findLoginUser();
        if (user.getPhone().equals(phoneNo)) {
            String resp = getCapcha(phoneNo);

            JSONObject json = new JSONObject();
            json = json.parseObject(resp);
            String retCode = json.getString("errorCode");

            if (StringUtils.equals(retCode, "0")) {
                String codeType = json.getString("imgUrl");
                if (StringUtils.isBlank(codeType)) {
                    //部分手机及需要进一步操作
                    //String callback = "http://666456.tunnel.qydev.com/api/user/getReport?userId="+user.getId()+"";
                    String callback = "http://app.hzrywl.com/api/api//user/getReport?userId=" + user.getId() + "";
                    String loginResp = YYSDemo.login(phoneNo, passwd, "", callback);
                    json = new JSONObject();
                    json = json.parseObject(loginResp);
                    retCode = json.getString("errorCode");
                    Boolean isFinish = json.getBoolean("result");
                    if (isFinish) {
                        //存储user_phone表
                        //User user = userService.selectById(71);
                        EntityWrapper<UserPhone> ewPhone = new EntityWrapper();
                        ewPhone.eq("user_id", user.getId());
                        UserPhone userPhone = userPhoneService.selectOne(ewPhone);
                        if (userPhone == null || userPhone.equals("")) {
                            userPhone = new UserPhone();
                            userPhone.setPhone(phoneNo);
                            userPhone.setUserId(user.getId());
                            userPhoneService.insert(userPhone);
                        } else {
                            userPhone.setPhone(phoneNo);
                            userPhoneService.updateById(userPhone);
                        }
                        //授权完毕改变状态
                        EntityWrapper<UserAuth> ew = new EntityWrapper();
                        ew.eq("user_id", user.getId());
                        UserAuth userAuth = userAuthService.selectOne(ew);
                        userAuth.setPhoneAuth(1);
                        userAuthService.updateById(userAuth);
                        //改变user_phone表状态
                        userPhone.setStatus(1);
                        userPhoneService.updateById(userPhone);
                        return JsonResp.ok("登录不需要验证码");
                    } else if (StringUtils.equals(retCode, "0")) {
                        //发送验证码
                        String coderesp = YYSDemo.getSmsCode(phoneNo);
                        json = new JSONObject();
                        json = json.parseObject(coderesp);
                        retCode = json.getString("errorCode");
                        if (StringUtils.equals(retCode, "0")) {
                            return JsonResp.ok("第二种方式");//表示授权时走第五步
                        } else {
                            return JsonResp.fa("请求失败！ | [%s]" + json.getString("errorMsg"));
                        }
                    } else {
                        return JsonResp.fa("请求失败！ | [%s]" + json.getString("errorMsg"));
                    }
                } else if (!codeType.equals("smsCode")) {
                    return JsonResp.fa("该区域手机号暂时无法支持！");
                }
            } else {
                return JsonResp.fa("请求失败！ | [%s]" + json.getString("errorMsg"));
            }
            return JsonResp.ok();
        } else {
            return JsonResp.fa("该手机号与登录手机号不一致！");
        }

    }*/

    @Autowired
    private YYSLogService yysLogService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private MobileService mobileService;

    /**
     * @return 返回json
     * @同盾运营商接口 第一阶段
     */
    @RequestMapping(value = "/mobileAuth1", method = RequestMethod.GET)
    public JsonResp mobileAuth(String phoneNo, String passwd) {
        Map<String, String> restMap = new HashMap<>();
        YYSLog t = new YYSLog();
        try {
            User user = userService.findLoginUser();
            EntityWrapper ew = new EntityWrapper();
            ew.eq("user_id", user.getId());
            UserIdentity userIdentity = userIdentityService.selectOne(ew);
            String idcard = "";
            String userName = "";
            if (userIdentity != null) {
                idcard = userIdentity.getIdentityNum();
                userName = userIdentity.getUserName();
            }
            JSONObject createResultMap = TongdunYYSAuth.createTask(userName, idcard, phoneNo);
            Integer code = (Integer) createResultMap.get("code");
            if (createResultMap != null && code == 0) {
                t.setUserId(user.getId());
                t.setCode(createResultMap.getString("code"));
                t.setMessage(createResultMap.getString("message"));
                t.setTaskId(createResultMap.getString("task_id"));
                JSONObject dataJO = JSON.parseObject(createResultMap.getString("data"));
                t.setMobile(phoneNo);
                t.setCreatedTime(dataJO.getString("created_time"));
                yysLogService.insert(t);
                JSONObject initResultJO = TongdunYYSAuth.acquire(t.getTaskId(), phoneNo, passwd, "INIT", "submit", null);
                restMap.put("code", initResultJO.getString("code"));
                restMap.put("message", initResultJO.getString("message"));
                restMap.put("taskId", initResultJO.getString("task_id"));
                String data = initResultJO.getString("data");
                if (StringUtils.isNotEmpty(data)) {
                    JSONObject dJO = JSON.parseObject(data);
                    restMap.put("nextStage", dJO.getString("next_stage"));
                    if ("101".equals(restMap.get("code"))) {
                        restMap.put("authCode", dJO.getString("auth_code"));
                    }
                }
                EntityWrapper<UserPhone> ewPhone = new EntityWrapper();
                ewPhone.eq("user_id", user.getId());
                UserPhone userPhone = userPhoneService.selectOne(ewPhone);
                if (userPhone == null || userPhone.equals("")) {
                    userPhone = new UserPhone();
                    userPhone.setPhone(phoneNo);
                    userPhone.setUserId(user.getId());
                    userPhoneService.insert(userPhone);
                } else {
                    userPhone.setPhone(phoneNo);
                    userPhoneService.updateById(userPhone);
                }
            } else {
                restMap.put("code", createResultMap.getString("code"));
                restMap.put("message", "认证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restMap.put("code", "9999");
            restMap.put("message", "系统错误");
        } finally {
            log.info("+++++++++++++++++++++++11111111111111111111111111:" + ok(restMap));
            return ok(restMap);
        }
    }

    /**
     * @return 返回json
     * @同盾运营商接口 第二阶段
     */
    @RequestMapping(value = "/mobileAuth2", method = RequestMethod.GET)
    public JsonResp mobileAuth2(String taskId, String passwd, String nextStage, String code, String txt, String phoneNo) {
        Map<String, String> restMap = new HashMap<>();
        try {
            Map<String, String> map = new HashMap<>();
            JSONObject initResultJO = null;
            if ("100".equals(code)) {
                if (nextStage == null || nextStage == "") {
                    nextStage = "LOGIN";
                }
                initResultJO = TongdunYYSAuth.acquire(taskId, phoneNo, passwd, nextStage, "query", map);
            } else if ("101".equals(code) || "105".equals(code)) {
                if ("101".equals(code)) {
                    map.put("auth_code", txt);
                } else {
                    map.put("sms_code", txt);
                }
                initResultJO = TongdunYYSAuth.acquire(taskId, phoneNo, passwd, nextStage, "submit", map);
            }
            restMap.put("code", initResultJO.getString("code"));
            restMap.put("message", initResultJO.getString("message"));
            restMap.put("taskId", initResultJO.getString("task_id"));

            String data = initResultJO.getString("data");
            if (StringUtils.isNotEmpty(data)) {
                JSONObject dJO = JSON.parseObject(data);
                if (dJO.getString("next_stage") != null) {
                    restMap.put("nextStage", dJO.getString("next_stage"));
                } else {
                    restMap.put("nextStage", nextStage);
                }
                if ("101".equals(restMap.get("code"))) {
                    restMap.put("authCode", dJO.getString("auth_code"));
                }
            } else {
                restMap.put("nextStage", nextStage);
            }
            if ("137".equals(restMap.get("code")) || "2007".equals(restMap.get("code"))) {
                User user = userService.findLoginUser();
                //授权完毕改变状态
                EntityWrapper<UserAuth> ew3 = new EntityWrapper();
                ew3.eq("user_id", user.getId());
                UserAuth userAuth = userAuthService.selectOne(ew3);
                userAuth.setPhoneAuth(1);
                userAuthService.updateById(userAuth);
                EntityWrapper<UserPhone> ewPhone = new EntityWrapper();
                ewPhone.eq("user_id", user.getId());
                UserPhone userPhone = userPhoneService.selectOne(ewPhone);
                //改变user_phone表状态
                userPhone.setStatus(1);
                userPhone.setTaskId(taskId);
                userPhoneService.updateById(userPhone);

                //判断用户是否完成基本验证

                //用户数加一
                EntityWrapper<PersonRecord> ewPserson = new EntityWrapper();
                PersonRecord personRecord = personRecordService.selectOne(ewPserson);
                personRecord.setAllCount(personRecord.getAllCount() + 1);
                personRecordService.updateById(personRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("+++++++++++++++++++++++2222222222222222222222222:" + ok(restMap));
            return ok(restMap);
        }
    }

    /**
     * @return void
     * @同盾运营商回调接收接口
     */
    @RequestMapping(value = "/YYSCallBack", method = RequestMethod.POST)
    public String YYSCallBack(HttpServletRequest req) {
        String t = "failed";
        try {
            String notify_data = req.getParameter("notify_data");
            if (notify_data != "") {
                String taskId = JSON.parseObject(notify_data).getString("task_id");
                EntityWrapper ew = new EntityWrapper();
                ew.eq("task_id", taskId);
                YYSLog yysLog = yysLogService.selectOne(ew);
                JSONObject resultJO = TongdunYYSAuth.getAll(taskId);
                if (resultJO != null) {
                    JSONObject data = resultJO.getJSONObject("data");
                    JSONObject taskData = data.getJSONObject("task_data");
                    if (taskData != null) {
                        String callInfo = taskData.getString("call_info");
                        List<String> ciList = JSON.parseArray(callInfo, String.class);
                        for (String a : ciList) {
                            List<UserPhoneRecord> userPhoneRecords = new ArrayList<>();
                            String callRecord = JSON.parseObject(a).getString("call_record");
                            List<String> callList = JSON.parseArray(callRecord, String.class);
                            for (String b : callList) {
                                JSONObject cMap = JSON.parseObject(b);
                                UserPhoneRecord userPhoneRecord = new UserPhoneRecord();
                                userPhoneRecord.setStartTime(cMap.getString("call_start_time"));
                                userPhoneRecord.setCommPlac(cMap.getString("call_address"));
                                userPhoneRecord.setCallType(cMap.getString("call_type_name"));
                                userPhoneRecord.setPhoneNo(cMap.getString("call_other_number"));
                                userPhoneRecord.setConnTimes(cMap.getString("call_time"));
                                userPhoneRecord.setCommMode(cMap.getString("call_land_type"));
                                userPhoneRecord.setCommFee(cMap.getString("call_cost"));
                                userPhoneRecord.setUserId(yysLog.getUserId());
                                userPhoneRecords.add(userPhoneRecord);
                            }

//                            userPhoneRecordService.insertBatch(userPhoneRecords);
                            userPhoneRecordService.saveCall(userPhoneRecords);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 返回值JsonResp
     * @获取社保认证方式
     */
    @RequestMapping(value = "/getShebaoAuthType", method = RequestMethod.GET)
    public JsonResp shebaoAuth(String province_id, String city) throws IOException {
        log.debug("获取社保认证方式");
        JSONObject jsonObject = SheBaoAuth.getCityAndType(province_id);
        //JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        int i = 0;
        for (; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).get("city").toString().equals(city)) {
                break;
            }
        }
        Map map = new HashMap();
        map.put("type", jsonArray.getJSONObject(i).get("loginType"));
        map.put("uniqueKey", jsonArray.getJSONObject(i).get("uniqueKey"));
        return ok(map);
        //return JsonResp.ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @社保认证
     */
    @RequestMapping(value = "/getShebaoAuth", method = RequestMethod.GET)
    public JsonResp getShebaoAuth(String login_type, String unique_key, String loginName, String pwd) throws IOException {
        log.debug("社保认证");
        String params = loginName + "," + pwd;
        JSONObject jsonObject = SheBaoAuth.getConfirm(login_type, unique_key, params);
        //
        if (jsonObject.get("code").toString().equals("0")) {
            //认证完毕改变状态
            User user = userService.findLoginUser();
            EntityWrapper<UserAuth> ew = new EntityWrapper();
            ew.eq("user_id", user.getId());
            UserAuth userAuth = userAuthService.selectOne(ew);
            userAuth.setShebaoAuth(1);
            userAuthService.updateById(userAuth);
        }
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @京东认证
     */
    @RequestMapping(value = "/getJingdongAuth", method = RequestMethod.GET)
    public JsonResp getJingdongAuth(String success_url, String failed_url) throws IOException {
        log.debug("京东认证");
        User user = userService.findLoginUser();
        //String callback_url = "http://666456.tunnel.qydev.com/api/user/getJingdongData?userId="+user.getId()+"";
        String callback_url = "http://app.hzrywl.com/api/api/user/getJingdongData?userId=" + user.getId() + "";
        JSONObject jsonObject = JingdongAuth.getConfirm(callback_url, success_url, failed_url);
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @京东获取数据
     */
    @RequestMapping(value = "/getJingdongData", method = RequestMethod.GET)
    public JsonResp getJingdongData(String token, String status, String userId) throws IOException {
        log.debug("京东获取数据");
        if (!token.equals("") && status.equals("1")) {
            JSONObject jsonObject = JingdongAuth.getData(token);
            //认证完毕改变状态
            //User user = userService.findLoginUser();
            User user = userService.selectById(userId);
            EntityWrapper<UserAuth> ew = new EntityWrapper();
            ew.eq("user_id", user.getId());
            UserAuth userAuth = userAuthService.selectOne(ew);
            userAuth.setJindongAuth(1);
            userAuthService.updateById(userAuth);
            //存储地址信息
            String addrs = jsonObject.getString("orderAddress");
            String address = addrs.replace("[", "").replace("]", "");
            String[] addressArray = address.split(",");
            for (int i = 0; i < addressArray.length; i++) {
                UserJindongAddress userJindongAddress = new UserJindongAddress();
                userJindongAddress.setUserId(user.getId());
                userJindongAddress.setOrderAddress(addressArray[i]);
                userJindongAddressService.insert(userJindongAddress);
            }
            return ok(jsonObject);
        } else {
            return JsonResp.fa("认证未成功！");
        }
    }

    /**
     * @return 返回值JsonResp
     * @获取公积金认证方式
     */
    @RequestMapping(value = "/getGongjijinAuthType", method = RequestMethod.GET)
    public JsonResp getGongjijinAuthType(String province_id, String city) throws IOException {
        log.debug("获取公积金认证方式");
        JSONObject jsonObject = GongjijinAuth.getType(province_id);
        //JSONArray jsonArray = new JSONArray();
/*        JSONArray jsonArray = jsonObject.getJSONArray("data");
        int i=0;
        for (;i<jsonArray.size();i++){
            if(jsonArray.getJSONObject(i).get("city").toString().equals(city)){
                break;
            }
        }
        Map map = new HashMap();
        map.put("note",jsonArray.getJSONObject(i).get("note"));
        map.put("type",jsonArray.getJSONObject(i).get("loginType"));
        map.put("uniqueKey",jsonArray.getJSONObject(i).get("uniqueKey"));*/
        //return JsonResp.ok(map);
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @公积金认证
     */
    @RequestMapping(value = "/getGongjijinAuth", method = RequestMethod.GET)
    public JsonResp getGongjijinAuth(String login_type, String unique_key, String loginName, String pwd) throws IOException {
        log.debug("公积金认证");
        String params = loginName + "," + pwd;
        JSONObject jsonObject = GongjijinAuth.getConfirm(login_type, unique_key, params);
        //
/*        if(jsonObject.get("code").toString().equals("0")){
            //认证完毕改变状态
            User user = userService.findLoginUser();
            EntityWrapper<UserAuth> ew = new EntityWrapper();
            ew.eq("user_id",user.getId());
            UserAuth userAuth =userAuthService.selectOne(ew);
            userAuth.setGongjijinAuth(1);
            userAuthService.updateById(userAuth);
        }*/
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @公积金认证H5
     */
    @RequestMapping(value = "/getGongjijinUrl", method = RequestMethod.GET)
    public JsonResp getGongjijinUrl(String success_url, String failed_url) throws IOException {
        log.debug("公积金认证H5");
        String callback_url = "http://www.cctv.com";
        JSONObject jsonObject = GongjijinAuth.getH5PageUrl(callback_url, success_url, failed_url);
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @公积金获取验证码
     */
    @RequestMapping(value = "/getGongjijinCode", method = RequestMethod.GET)
    public JsonResp getGongjijinCode(String login_type, String unique_key, String login_name) throws IOException {
        log.debug("公积金获取验证码");
        JSONObject jsonObject = GongjijinAuth.getCode(login_type, unique_key, login_name);
        return ok(jsonObject);
    }

    /**
     * @return 返回值JsonResp
     * @公积金获取数据
     */
    @RequestMapping(value = "/getGongjijinData", method = RequestMethod.GET)
    public JsonResp getGongjijinData(String token) throws IOException {
        JSONObject jsonObject = GongjijinAuth.getData(token);
        log.debug("公积金获取数据");
        return ok(jsonObject);
    }

    /**
     * @param page
     * @param phoneNumber
     * @param userName
     * @return 查询黑名单
     */
    @RequestMapping(value = "/selectAllBlackList", method = RequestMethod.GET)
    public JsonResp selectAllBlackList(Page page, String phoneNumber, String userName) {

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
        map.put("pageNo", (page.getCurrent() - 1) * page.getSize());
        map.put("pageSize", page.getSize());
        if (ids.size() > 0) {
            map.put("ids", ids);
        }
        int count = loanOrderService.selectBlackListNum(map);
        List<LoanOrder> loanOrderList = loanOrderService.selectBlackList(map);
        if (!loanOrderList.isEmpty()) {
            for (LoanOrder loanOrder : loanOrderList) {
                EntityWrapper<User> userEntityWrapper1 = new EntityWrapper<>();
                userEntityWrapper1.eq("id", loanOrder.getUserId());
                User user1 = userService.selectOne(userEntityWrapper1);
                loanOrder.setUser(user1);
            }
        }
        return ok(new PageDto(page.getCurrent(), page.getSize(), loanOrderList, count));
    }

    /**
     * @param
     * @return 移出黑名单
     */
    @RequestMapping(value = "/moveOutBlackList", method = RequestMethod.GET)
    public JsonResp moveOutBlackList(Long id) {
        // EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
        User user = new User();
        if (id != null && !"".equals(id)) {
            user = userService.selectById(id);
            user.setStatus(1);
        }
        boolean bo = userService.updateById(user);
        PersonRecord pr = personRecordService.selectById(1);
        pr.setBlackCount(pr.getBlackCount() - 1);
        boolean bo1 = personRecordService.updateById(pr);
        boolean bool = false;
        if (bo && bo1) {
            bool = true;
        }

        return ok(bool);
    }

    /**
     * @return 返回值JsonResp
     * @用户是否需要还款
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/userIsNeedPay", method = RequestMethod.GET)
    public JsonResp userIsNeedPay() {
        log.debug("用户是否需要还款");
        User user = userService.findLoginUser();
        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper();
        ewOrder.eq("user_id", user.getId());
        List<Integer> list = new ArrayList<Integer>() {{
            add(3);
            add(4);
            add(5);
        }};
        ewOrder.in("order_status", list);
        LoanOrder loanOrder = loanOrderService.selectOne(ewOrder);
        if (loanOrder != null) {
            return ok();
        } else {
            return JsonResp.fa("您的订单还在审核中！");
        }
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @贷前审核认证（同盾）
     */
    @RequestMapping(value = "/borrowBeforeAuth", method = RequestMethod.GET)
    public JsonResp nameAuth(String name, String idCard, String mobile) {
        log.debug("实名认证（同盾）");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id_number", idCard);
        params.put("account_name", name);
        params.put("account_mobile", mobile);
        params.put("biz_code", "lecuidqAND");
        JSONObject jsonObject = NameAndBankAuth.invoke(params);
        Object result = jsonObject.getJSONObject("result_desc").getJSONObject("AUTHENTICATION_INFOQUERY").getJSONObject("RealNameCheck").get("realname_consistence");
        if (null == result) {
            String error = jsonObject.getJSONObject("result_desc").getJSONObject("AUTHENTICATION_INFOQUERY").getJSONObject("RealNameCheck").get("error_info").toString();
            return JsonResp.fa(error);
        } else if (result.toString().equals("0")) {
            return ok("认证成功");
        } else {
            return JsonResp.fa("认证失败！");
        }
    }

    /**
     * @param
     * @return 打款
     */
    @SystemLog(description = "线下打款")
    @RequestMapping(value = "/payOrder", method = RequestMethod.GET)
    synchronized public JsonResp payOrder(Long id) throws ParseException {
        log.debug("打款");

        LoanOrder loanOrder = loanOrderService.selectById(id);
        if (loanOrder != null && loanOrder.getOrderStatus() != 3) {
            loanOrder.setGiveStatus(2);
            loanOrder.setOrderStatus(3);
            loanOrder.setGiveTime(new Date());
            //改变时间
            loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1, new Date()));
            loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays() + loanOrder.getAllowDays() - 1, new Date()));
            loanOrderService.updateById(loanOrder);
            User user = userService.selectById(loanOrder.getUserId());
            //统计
            try {
                if(user.getIsOld()==0){
                    everyDayDataService.addEveryDayData(EveryDayDataType.TYPE.LENDING_NUM.getKey(),user.getChannelId()==null?0:user.getChannelId());
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            EntityWrapper<PersonRecord> ew = new EntityWrapper();
            PersonRecord personRecord = personRecordService.selectOne(ew);
            personRecord.setOutOrderCount(personRecord.getOutOrderCount() + 1);
            personRecord.setOutMoney(personRecord.getOutMoney().add(loanOrder.getRealMoney()));
            personRecordService.updateById(personRecord);

/*            //发送打款成功短信
            EntityWrapper<MsgModel> ewMsg = new EntityWrapper();
            ewMsg.eq("type", 1);

            User user = userService.selectById(loanOrder.getUserId());
            EntityWrapper<TextTemplate> wrapper1 = new EntityWrapper();
            wrapper1.eq("type", 3);
            wrapper1.eq("title", "还款成功");
            TextTemplate textTemplate = textTemplateService.selectOne(wrapper1);//找到短信模板*/
          /*  //将信息部分替换
//					String content = textTemplate.getText().replaceAll("<[^>]+>", "");
            String content = textTemplate.getText().replaceAll("<[^>]+>", "")
                    .replace("#userName",user.getUserName())
                    .replace("#gmtDateTime",new SimpleDateFormat("yyyy-MM-dd").format(loanOrder.getGmtDatetime()));
//                            .replace("#overDays",days.toString())
//                            .replace("#needPayMoney",evaluation.getNeedPayMoney().toString());
            smsAPI.sendsms(user.getPhone(), content);*/

            //有渠道商引进的，记录渠道商信息
            if (user.getChannelId() != null) {
                //老用户不再算利润
                EntityWrapper<LoanOrder> ewOrder = new EntityWrapper<>();
                ewOrder.eq("order_status", 6);
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
            }
        }
        return ok();
    }

    /**
     * @param
     * @return 疑似重复订单确认打款
     */
    @RequestMapping(value = "/payConfirm", method = RequestMethod.GET)
    public JsonResp payConfirmTest(Long id) {
        Admin admin = userService.getThisLogin();
        if (admin != null) {
            PayOrderConfirm payOrderConfirm = payOrderConfirmService.selectById(id);
            payOrderConfirm.setStatus(2);
            payOrderConfirmService.updateById(payOrderConfirm);
            LoanOrder loanOrder = loanOrderService.selectById(payOrderConfirm.getLoanOrderId());
            loanOrder.setOrderStatus(3);
            loanOrder.setGiveStatus(2);
            loanOrderService.updateById(loanOrder);
            String result = null;
            try {
                result = ConfirmPaymentApi.confirmPayment(payOrderConfirm.getNoOrder(), payOrderConfirm.getCode(), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.equals("success")) {
                return ok("打款申请提交成功");
            } else if (result.equals("fail")) {
                return JsonResp.fa("打款申请失败！");
            } else {
                return JsonResp.fa(result);
            }
        } else {
            return JsonResp.fa("管理员未登录！");
        }
    }

    /**
     * @return 返回值JsonResp
     * @获取确认支付信息
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/getPayMsg", method = RequestMethod.GET)
    public JsonResp getPayMsg(String payPwd) {
        log.debug("获取确认支付信息");
        User user = userService.findLoginUser();
        if (user.getPayPwd().equals(payPwd)) {
            EntityWrapper<LoanOrder> ewOrder = new EntityWrapper();
            ewOrder.eq("user_id", user.getId());
            List<Integer> list = new ArrayList<Integer>() {{
                add(3);
                add(4);
                add(5);
            }};
            ewOrder.in("order_status", list);
            LoanOrder loanOrder = loanOrderService.selectOne(ewOrder);
            EntityWrapper<UserBank> ewBank = new EntityWrapper();
            ewBank.eq("user_id", user.getId());
            UserBank userBank = userBankService.selectOne(ewBank);
            if (loanOrder != null) {
                Map map = new HashMap();
                String orderNo = "RYJHK" + OrderUtils.getOrderNo();
                map.put("no_order", orderNo);
                map.put("money", loanOrder.getNeedPayMoney());
                map.put("bankNum", userBank.getBankcardno());
                map.put("idCard", userBank.getIdcardno());
                map.put("name", user.getUserName());
                map.put("orderId", loanOrder.getId());
                map.put("userId", user.getId());
                //风控参数
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String condition = "{\"frms_ware_category\":\"2010\",\"user_info_mercht_userno\":\"" + user.getId() + "\",\"user_info_dt_register\":\"" + dataFormat.format(user.getGmtDatetime()) + "\",\"user_info_bind_phone\":\"" + user.getPhone() + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_full_name\":\"" + userBank.getName() + "\",\"user_info_id_no\":\"" + userBank.getIdcardno() + "\"}";
                map.put("risk_item", condition);
                return ok(map);
            } else {
                return JsonResp.fa("该用户当前无还款订单");
            }
        } else {
            return JsonResp.fa("支付密码不正确");
        }
    }

    /**
     * @return 返回值JsonResp
     * @获取支付界面的信息
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/getPayInterfaceMsg", method = RequestMethod.GET)
    public JsonResp getPayInterfaceMsg() {
        log.debug("获取支付界面的信息");
        User user = userService.findLoginUser();
        EntityWrapper<LoanOrder> ewOrder = new EntityWrapper();
        ewOrder.eq("user_id", user.getId());
        List<Integer> list = new ArrayList<Integer>() {{
            add(3);
            add(4);
            add(5);
        }};
        ewOrder.in("order_status", list);
        LoanOrder loanOrder = loanOrderService.selectOne(ewOrder);
        if (loanOrder != null) {
            Map map = new HashMap();
            map.put("money", loanOrder.getNeedPayMoney());
            map.put("bankNum", loanOrder.getBankCardNum());
            map.put("bankName", loanOrder.getBankName());
            map.put("orderId", loanOrder.getId());
            return ok(map);
        } else {
            return JsonResp.fa("该用户当前无还款订单");
        }
    }

    /**
     * @return 返回值JsonResp
     * @获取签约信息
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/getSignMsg", method = RequestMethod.GET)
    public JsonResp getSignMsg(String orderId) {
        log.debug("获取签约信息");
        User user = userService.findLoginUser();
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        EntityWrapper<UserBank> ewBank = new EntityWrapper();
        ewBank.eq("user_id", user.getId());
        UserBank userBank = userBankService.selectOne(ewBank);
        if (loanOrder != null) {
            Map map = new HashMap();
            String orderNo = "LYDHK" + OrderUtils.getOrderNo();
            map.put("no_order", orderNo);
            map.put("money", loanOrder.getNeedPayMoney());
            map.put("bankNum", userBank.getBankcardno());
            map.put("idCard", userBank.getIdcardno());
            map.put("name", user.getUserName());
            map.put("orderId", loanOrder.getId());
            map.put("userId", user.getUuid());
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String condition = "{\"frms_ware_category\":\"2010\",\"user_info_mercht_userno\":\"" + user.getUuid() + "\",\"user_info_dt_register\":\"" + dataFormat.format(user.getGmtDatetime()) + "\",\"user_info_bind_phone\":\"" + user.getPhone() + "\",\"user_info_identify_state\":\"1\",\"user_info_identify_type\":\"1\",\"user_info_full_name\":\"" + userBank.getName() + "\",\"user_info_id_no\":\"" + userBank.getIdcardno() + "\"}";
            map.put("risk_item", condition);
            return ok(map);
        } else {
            return JsonResp.fa("该用户当前无还款订单");
        }
    }

    /**
     * @return 返回值JsonResp
     * @签约授权
     */
    @SourceAuthority(SourceType.APP)
    @RequestMapping(value = "/getSignPass", method = RequestMethod.GET)
    public JsonResp getSignPass(String no_agree, String no_order, String user_id, String orderId) throws ParseException {
        log.debug("签约授权");
        String repayment_no = RandomUtils.randomString(7);//还款计划编号
        User user = userService.selectById(user_id);
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        //存储扣款所需信息
        loanOrder.setLianRepayNum(no_order);
        loanOrder.setNoAgree(no_agree);
        loanOrder.setRepaymentNo(repayment_no);
        loanOrderService.updateById(loanOrder);
        //授权
        String resJson = FenqiPayment.agreenoauthapply(no_agree, repayment_no, loanOrder.getNeedPayMoney().toString(), DateUtils.dateSimple(loanOrder.getLimitPayTime()).toString(), user.getId().toString());
        JSONObject jsonObject = JSONObject.parseObject(resJson);
        if (jsonObject.get("ret_code").toString().equals("0000")) {
            return ok(jsonObject.get("ret_msg").toString());
        } else {
            return JsonResp.fa(jsonObject.get("ret_msg").toString());
        }
    }

    /**
     * 查询用户是否被永续拒绝
     */
    @RequestMapping(value = "/yx", method = RequestMethod.GET)
    public JsonResp yx() {
        User user = userService.findLoginUser();
        if (user.getStatus() == 5) {
            return ok("false");
        } else {
            return ok("true");
        }
    }

    @RequestMapping(value = "/selectYYSResult", method = RequestMethod.GET)
    public JsonResp selectYYSResult(String userId) {
        EntityWrapper ew = new EntityWrapper();
        ew.eq("user_id", userId);
        YYSLog yysLog = yysLogService.selectOne(ew);
        return ok(yysLog);
    }

    @RequestMapping(value = "/getIDCardAndName", method = RequestMethod.GET)
    public JsonResp getIDCardAndName() {
        User user = userService.findLoginUser();
        EntityWrapper ew = new EntityWrapper();
        ew.eq("user_id", user.getId());
        UserIdentity userIdentity = userIdentityService.selectOne(ew);
        return ok(userIdentity);
    }

    @RequestMapping(value = "/getRefuseTime", method = RequestMethod.GET)
    public JsonResp getRefuseTime() {
        User user = userService.findLoginUser();
        return ok(user.getUptDatetime());
    }

    /**
     * @param
     * @return ebao打款
     */
    @RequestMapping(value = "/eBPayByHand", method = RequestMethod.GET)
    public JsonResp eBPayByHand(String id) throws Exception {
        return giveMoney(id);
    }

    /**
     * 绑卡支付
     *
     * @return
     */
    @RequestMapping(value = "/bindCardPay", method = RequestMethod.GET)
    public JsonResp bindCardPay(String money) throws Exception {

        return JsonResp.fa("请下载新版app！安卓下载地址：fir.im/3npq，苹果下载地址：fir.im/w3xh");
    }

    /**
     * 绑卡支付
     *
     * @return
     */
    @RequestMapping(value = "/bindCardPayNew", method = RequestMethod.GET)
    public JsonResp bindCardPayNew(String money) throws Exception {
        log.debug("绑卡支付");

        User user = userService.findLoginUser();
        EntityWrapper<UserBank> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        UserBank userBank = userBankService.selectOne(wrapper);

        BindPayValidateCodeVo bindPayValidateCodeVo  =  new BindPayValidateCodeVo();
        bindPayValidateCodeVo.setP3_bindId(userBank.getBindId());
        bindPayValidateCodeVo.setP4_userId(userBank.getUserId()+"");
        bindPayValidateCodeVo.setP5_orderId(System.currentTimeMillis() + RandomUtils.randomString(10));
        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
        bindPayValidateCodeVo.setP6_timestamp(format.format(new Date()));
        bindPayValidateCodeVo.setP7_currency("CNY");
        bindPayValidateCodeVo.setP8_orderAmount(money);
        bindPayValidateCodeVo.setP9_phone(userBank.getBankPhone());

        JSONObject result  =  QuickPayApi.bindPayValidateCode(bindPayValidateCodeVo);
        return ok(result);
    }

    /**
     * 绑卡支付短信验证
     *
     * @return
     */
    @RequestMapping(value = "/bindCardConfirm", method = RequestMethod.GET)
    public JsonResp bindCardConfirm(String code, String requestno,String orderId, Integer type,String money, String imei,String uuid) throws Exception {
        log.debug("绑卡支付短信验证");

        User user = userService.findLoginUser();
        EntityWrapper<UserBank> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        UserBank userBank = userBankService.selectOne(wrapper);

        BindCardPayVo bindCardPayVo = new BindCardPayVo();
        bindCardPayVo.setP3_bindId(userBank.getBindId());
        bindCardPayVo.setP4_userId(userBank.getUserId()+"");
        bindCardPayVo.setP5_orderId(requestno);
        SimpleDateFormat  format = new SimpleDateFormat("yyyyMMddHHmmss");
        bindCardPayVo.setP6_timestamp(format.format(new Date()));
        bindCardPayVo.setP7_currency("CNY");
        bindCardPayVo.setP8_orderAmount(money);
        bindCardPayVo.setP9_goodsName("绑卡支付");
        if(StringUtil.isNotEmpty(imei)) {
            bindCardPayVo.setP11_terminalType("IMEI");
            bindCardPayVo.setP12_terminalId(imei);
        }else{
            bindCardPayVo.setP11_terminalType("UUID");
            bindCardPayVo.setP12_terminalId(uuid);
        }
        bindCardPayVo.setP13_orderIp("127.0.0.1");
        bindCardPayVo.setP16_serverCallbackUrl(QuickPayApi.HELIPAY_NOTIFY_URL);
        bindCardPayVo.setP17_validateCode(code);
        JSONObject result  = QuickPayApi.bindCardPay(bindCardPayVo);
        if("0000".equals(result.getString("rt2_retCode"))){
            String orderStatus = result.getString("rt9_orderStatus");
            if("INIT".equals(orderStatus)||"SUCCESS".equals(orderStatus)||"DOING".equals(orderStatus)) {
                String str = orderId + "," + money + "," + type;

                LoanOrder loanOrder = loanOrderService.selectById(orderId);
                loanOrder.setType(type);
                loanOrder.setRequestNo(requestno);
                loanOrderService.updateById(loanOrder);
                return ok(result);
            }
        }else if (result.get("rt3_retMsg") != null) {
            return JsonResp.fa(result.getString("rt3_retMsg"));
        }
        return JsonResp.fa("支付异常!");
    }

    /**
     * 绑卡支付结果查询
     *
     * @return
     */
    @RequestMapping(value = "/bindCardQuery", method = RequestMethod.GET)
    public JsonResp bindCardQuery(String requestno) throws Exception {
        log.debug("绑卡支付结果查询");
        QueryOrderVo queryOrderVo = new QueryOrderVo();
        queryOrderVo.setP2_orderId(requestno);
        JSONObject resultMap =  QuickPayApi.queryOrder(queryOrderVo);
        return ok(resultMap);
    }

    /**
     * @param
     * @return 结清订单
     */
    @SystemLog(description = "结清订单")
    @RequestMapping(value = "/repayOrder", method = RequestMethod.GET)
    public JsonResp repayOrder(Long id,String money) throws ParseException {
        log.debug("结清订单");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if(money != null && !"".equals(money.trim())){
            if(loanOrder.getNeedPayMoney().compareTo(new BigDecimal(money)) <0){
                return JsonResp.toFail("输入的结清金额不能超过需要支付金额");
            }
            loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money)));
        }else{
            return JsonResp.toFail("请勿输入空值!!");
        }

        User user = userService.selectById(loanOrder.getUserId());
        if (user.getIsPay() == 0) {
            return ok("已还款!");
        }
        loanOrder.setOrderStatus(6);
        try {
            if (DateUtils.YYMMDDDate(new Date()).getTime() > loanOrder.getOverdueTime().getTime()) {
                loanOrder.setPayStatus(2);//超出容限期还款
                user.setMoney(loanOrder.getBorrowMoney());
            } else {
                loanOrder.setPayStatus(1);//正常还款
                user.setMoney(loanOrder.getBorrowMoney());
                //user.setMoney(user.getMoney().add(loanOrder.getBorrowMoney()).add(BigDecimal.valueOf(500)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //实际还款金额使用需要还款金额替代
        loanOrder.setRealPayTime(new Date());
        loanOrderService.updateById(loanOrder);

        user.setIsPay(0);
        user.setIsOld(1);
        userService.updateById(user);

        //统计
        EntityWrapper<PersonRecord> ew = new EntityWrapper();
        PersonRecord personRecord = personRecordService.selectOne(ew);
        personRecord.setOverOrderCount(personRecord.getOverOrderCount() + 1);
        personRecordService.updateById(personRecord);

        //发送还款成功短信
        EntityWrapper<TextTemplate> wrapper1 = new EntityWrapper();
        wrapper1.eq("type", 3);
        wrapper1.eq("title", "还款成功");
//        TextTemplate textTemplate = textTemplateService.selectOne(wrapper1);//找到短信模板
//        //将信息部分替换
////					String content = textTemplate.getText().replaceAll("</?[^>]+>", "");
//        String content = textTemplate.getText().replaceAll("</?[^>]+>", "")
//                .replace("#userName", user.getUserName())
//                .replace("#gmtDateTime", new SimpleDateFormat("yyyy-MM-dd").format(loanOrder.getGmtDatetime()));
////                            .replace("#overDays",days.toString())
////                            .replace("#needPayMoney",evaluation.getNeedPayMoney().toString());
//        smsAPI.sendsms(user.getPhone(), content);
        //发送账单已处理短信，和推送
        try {
            log.info("账单处理完成，发送提醒消息 " + user.getId());
            String[]  arr ={};
            YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_SETTLED,user.getPhone(),arr);
//            DhSmsUtil.sendMessage(MsgTemplate.PAYMENT_SUCCESS_SMS, user.getPhone());
            List<String> userIds = new ArrayList<>();
            userIds.add(user.getId().toString());
            int status = JpushClientUtil.sendToAliasId(userIds,
                    PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, PushTemplate.PAYMENT_SUCCESS_PUSH_TITLE, MsgTemplate.PAYMENT_SUCCESS_SMS, "");
        } catch (Exception e) {
            log.error("发送还款成功消息异常", e);
        }
        return ok("结清成功！");
    }

    /**
     * @param
     * @return 结清订单
     */
    @SystemLog(description = "部分还款（线下）")
    @RequestMapping(value = "/partRepayOrder", method = RequestMethod.GET)
    public JsonResp partRepayOrder(Long id,String money) throws ParseException {
        log.debug("部分还款（线下）");
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if(money != null && !"".equals(money.trim())){
            if(loanOrder.getNeedPayMoney().compareTo(new BigDecimal(money)) <0){
                return JsonResp.toFail("输入的部分还款金额不能超过需要支付金额");
            }
            loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(new BigDecimal(money.trim())));
            loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(new BigDecimal(money.trim())));
            loanOrder.setBelowPartPayment(loanOrder.getBelowPartPayment().add(new BigDecimal(money.trim())));
        }else{
            return JsonResp.toFail("请勿输入空值");
        }

        User user = userService.selectById(loanOrder.getUserId());
        if (user.getIsPay() == 0) {
            return ok("已还款!");
        }
        loanOrderService.updateById(loanOrder);
        return ok("部分还款成功！");
    }

    @RequestMapping(value = "/xuqiOrderByHand", method = RequestMethod.GET)
    public JsonResp xuqiOrderByHand(Long id, Integer days, BigDecimal money) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if (loanOrder.getOrderStatus() == 6) {
            return ok("订单已经续期");
        }
        OrderExtend orderExtend = new OrderExtend();
        orderExtend.setExtendMoney(money);
        orderExtend.setOrderId(Long.valueOf(id));
        orderExtend.setStatus(1);
        orderExtend.setExtendDays(days);
        orderExtendService.insert(orderExtend);
        loanOrder.setOrderStatus(3);
        loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(money));
        loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(loanOrder.getOverdueMoney()));
        loanOrder.setWateMoney(loanOrder.getWateMoney().subtract(loanOrder.getOverdueMoney()));
        //时间
        loanOrder.setLimitPayTime(DateUtils.dayAdd(days - 1 + loanOrder.getOverdueDays(), loanOrder.getLimitPayTime()));
        loanOrder.setOverdueTime(DateUtils.dayAdd(days - 1 + loanOrder.getOverdueDays(), loanOrder.getOverdueTime()));
        //清除逾期的金额天数
        loanOrder.setOverdueDays(0);
        loanOrder.setOverdueMoney(new BigDecimal(0));
        loanOrder.setAllowDays(0);
        loanOrder.setAllowMoney(new BigDecimal(0));
        loanOrderService.updateById(loanOrder);
        return ok("续期成功！");
    }

    @RequestMapping(value = "/xuqiOrder", method = RequestMethod.GET)
    public JsonResp xuqiOrder(Long id, BigDecimal money) {
        LoanOrder loanOrder = loanOrderService.selectById(id);
        if (loanOrder.getOrderStatus() == 3) {
            return ok("订单已经续期");
        }
        OrderExtend orderExtend = new OrderExtend();
        orderExtend.setExtendMoney(money);
        orderExtend.setOrderId(Long.valueOf(id));
        orderExtend.setStatus(1);
        orderExtend.setExtendDays(loanOrder.getLimitDays());
        orderExtendService.insert(orderExtend);
        loanOrder.setOrderStatus(3);
        loanOrder.setRealPayMoney(loanOrder.getRealPayMoney().add(money));
        loanOrder.setNeedPayMoney(loanOrder.getNeedPayMoney().subtract(loanOrder.getOverdueMoney()));
        loanOrder.setWateMoney(loanOrder.getWateMoney().subtract(loanOrder.getOverdueMoney()));
        //时间
        loanOrder.setLimitPayTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1 + loanOrder.getOverdueDays(), loanOrder.getLimitPayTime()));
        loanOrder.setOverdueTime(DateUtils.dayAdd(loanOrder.getLimitDays() - 1 + loanOrder.getOverdueDays(), loanOrder.getOverdueTime()));
        //清除逾期的金额天数
        loanOrder.setOverdueDays(0);
        loanOrder.setOverdueMoney(new BigDecimal(0));
        loanOrder.setAllowDays(0);
        loanOrder.setAllowMoney(new BigDecimal(0));
        loanOrderService.updateById(loanOrder);
        return ok("续期成功！");
    }

    /**
     * 后台管理获取验证码
     *
     * @return
     */
    @RequestMapping(value = "/adminGetPhoneCode", method = RequestMethod.GET)
    public JsonResp adminGetPhoneCode(String phone, String adminName) {
        log.debug("获取验证码");
        //Admin admin=userService.getThisLogin();
        String code = smsAPI.adminSendCode(phone, adminName);

        keymap.put("code", code);

        return ok("获取验证码成功！");
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JsonResp login(String userName, String password, String code) {
        log.debug("登录");

//        if (keymap.get("code") == null || code == null || !code.equals(keymap.get("code").toString())) {
//            return JsonResp.fa("验证码错误");
//        }

        return ok(userService.loginByUserName(userName, password));
    }

    @RequestMapping(value = "/selectUserlimit", method = RequestMethod.GET)
    public JsonResp selectUserlimit() {
        User user = userService.findLoginUser();
        return ok(user.getMoney());
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @易宝支付打款
     */
    @SystemLog(description = "易宝支付打款")
    @RequestMapping(value = "/payOrderByYiBao", method = RequestMethod.GET)
    public JsonResp payOrderByYiBao(Long orderId) {
        return giveMoney(orderId+"");
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @用户支付
     */
    @RequestMapping(value = "/rePayOrder", method = RequestMethod.GET)
    //还款
    public JsonResp rePayOrder(Long orderId, String money, String key, Integer type, Integer limitDays) {

        log.debug("用户支付");
        String md5key = null;
        md5key = Md5.md5Encode(orderId.toString() + money + "e10adc3949ba59abbe56e057f20f883e");
        if (!md5key.equals(key)) {
            return JsonResp.fa("非法请求!");
        }
        LoanOrder loanOrder = loanOrderService.selectById(orderId);
        if(loanOrder.getOrderStatus()==6){
            return JsonResp.fa("账单已结清!");
        }
        EntityWrapper<UserBank> ew = new EntityWrapper<>();
        ew.eq("user_id", loanOrder.getUserId());
        UserBank userBank = userBankService.selectOne(ew);
        BindCardPayVo bindCardPayVo = new BindCardPayVo();
        bindCardPayVo.setP3_bindId(userBank.getBindId());
        bindCardPayVo.setP4_userId(userBank.getUserId() + "");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        bindCardPayVo.setP6_timestamp(time);
        String p5OrderId = orderId + time;
        bindCardPayVo.setP5_orderId(p5OrderId);
        bindCardPayVo.setP7_currency("CNY");
        bindCardPayVo.setP8_orderAmount(money);
        bindCardPayVo.setP9_goodsName("绑卡支付");
        bindCardPayVo.setP11_terminalType("MAC");
        bindCardPayVo.setP12_terminalId(MessageHandle.getMACAddress());
        bindCardPayVo.setP13_orderIp("127.0.0.1");
        bindCardPayVo.setP16_serverCallbackUrl(QuickPayApi.HELIPAY_NOTIFY_URL);

        Map map = new HashMap();
        JSONObject result = QuickPayApi.bindCardPay(bindCardPayVo);
        if ("0000".equals(result.getString("rt2_retCode"))) {
            String orderStatus = result.getString("rt9_orderStatus");
            if ("INIT".equals(orderStatus) || "SUCCESS".equals(orderStatus) || "DOING".equals(orderStatus)) {
                if (type == 1) {
                    cacheUtil.set(p5OrderId, orderId + "," + money + "," + type, 120 * 120);
                    loanOrder.setLianRepayNum(p5OrderId);
                    } else if (type == 2) {
                    //续期表储存
                    OrderExtend orderExtend = new OrderExtend();
                    orderExtend.setOrderId(loanOrder.getId());
                    orderExtend.setExtendDays(limitDays);
                    orderExtend.setExtendMoney(new BigDecimal(money));
                    orderExtend.setExtendLianlianNum(p5OrderId);
                    orderExtendService.insert(orderExtend);
                    map.put("extendId", orderExtend.getId());
                    cacheUtil.set(p5OrderId, orderId + "," + money + "," + type + "," + orderExtend.getId() + ",", 120 * 120);
                }
                loanOrderService.updateById(loanOrder);
                map.put("resultMap", result);
                return ok(map);
            }
        } else if (result.get("rt3_retMsg") != null) {
            return JsonResp.fa(result.getString("rt3_retMsg"));
        }
        return JsonResp.fa("还款失败");
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @还款确认
     */
    @RequestMapping(value = "/rePayOrderConfirm", method = RequestMethod.GET)
    public synchronized JsonResp rePayOrderConfirm(Long orderId, String code, String requestno, Integer type, Long extendId) {
        log.debug("还款确认");
        Map resultMap = RepaymentApi.bindCardConfirm(code, requestno);
        log.error("还款确认====================" + resultMap);
        if (resultMap.get("status").equals("PROCESSING")) {
            //入redis中回调用
            if (type == 1) {
                } else if (type == 2) {
                cacheUtil.set(resultMap.get("requestno").toString(), orderId + "," + resultMap.get("amount").toString() + "," + type + "," + extendId + ",", 120 * 120);
            }
            return ok(resultMap);
        } else {
            return JsonResp.fa(resultMap.get("errormsg").toString());
        }
    }

//    /**
//     * @param
//     * @return 返回值JsonResp
//     * @还款轮询
//     */
//    @RequestMapping(value = "/bindCardPayQuery", method = RequestMethod.GET)
//    public JsonResp bindCardPayQuery(String requestno, String yborderid) throws Exception {
//        log.debug("绑卡支付结果查询");
//        Map<String, String> resultMap = RepaymentApi.bindCardPayQuery(requestno, yborderid);
//        return JsonResp.ok(resultMap);
//    }

    /**
     * 运营商后台报告
     *
     * @return
     */
    @RequestMapping(value = "/selectYYSMsg", method = RequestMethod.GET)
    public JsonResp selectYYSResult(Long userId) throws UnsupportedEncodingException {
        log.debug("运营商后台报告");
        //Admin admin=userService.getThisLogin();
        EntityWrapper<UserPhone> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        ew.eq("status", 1);
        UserPhone userPhone = userPhoneService.selectOne(ew);
        Map map = new HashMap();
        map.put("message", userPhone.getTaskId());
//        map.put("partner_code","qtEBjzc+a0AZOa1JpIBCaKv/AIho84B0");
        map.put("partner_code", "t+t5toAkcABBJsA8HGLLEA==");
        return ok(map);
    }

    /**
     * 运营商后台报告 魔蝎
     *
     * @return
     */
    @RequestMapping(value = "/selectYYSMsgMx", method = RequestMethod.GET)
    public JsonResp selectYYSMsgMx(Long userId) throws UnsupportedEncodingException {
        log.debug("运营商后台报告（魔蝎）");
        //Admin admin=userService.getThisLogin();
        EntityWrapper<UserPhone> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        ew.eq("status", 1);
        UserPhone userPhone = userPhoneService.selectOne(ew);
        Map map = new HashMap();
        map.put("message", userPhone.getMessage());
        return ok(map);
    }

    /**
     * ud信息报告
     *
     * @return
     */
    @RequestMapping(value = "/getUdInfo", method = RequestMethod.GET)
    public JsonResp getUdInfo(Long id) throws Exception {
        log.debug("获取有盾信息报告");
        EntityWrapper<UdInfo> ew = new EntityWrapper<>();
        ew.eq("user_id", id);
        UdInfo udInfo = udInfoService.selectOne(ew);
        if (udInfo == null) {
            log.info("开始查询有盾数据");
            udInfo = new UdInfo();
            EntityWrapper<UserIdentity> idEw = new EntityWrapper<>();
            idEw.eq("user_id", id);
            UserIdentity userIdentity = userIdentityService.selectOne(idEw);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_no", userIdentity.getIdentityNum());

            String udInfoNo = OrderUtils.getUdOrderNo();
            udInfo.setUdInfoId(udInfoNo);
            JSONObject udInfoJson;
            try {
                udInfoJson = YouDunUtil.getUdInfoJson(jsonObject, udInfoNo);
            } catch (Exception e) {
                return JsonResp.fa(e.getMessage());
            }
            log.info("有盾数据查询成功！ 开始记录有盾数据");
            udInfo = convertToUdInfo(udInfoJson, udInfo);
            udInfo.setCreateTime(new Date());
            udInfo.setUserId(id);
            udInfoService.insert(udInfo);
        }

        return ok(udInfo);
    }

    private UdInfo convertToUdInfo(JSONObject jsonObject, UdInfo udInfo) {
        log.info("有盾数据开始转换!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        JSONObject body = jsonObject.getJSONObject("body");
        //获取用户特征值
        JSONArray user_features = body.getJSONArray("user_features");
        if (user_features != null) {
            StringBuffer userFeatureBuffer = new StringBuffer();
            StringBuffer userFeatureStrBuffer = new StringBuffer();
            for (int i = 0; i < user_features.size(); i++) {
                JSONObject user_feature = user_features.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                String userFeatureType = (String) user_feature.get("user_feature_type");
                userFeatureBuffer.append(userFeatureType + ",");
                //可以直接转换为字符串存储，以后就无需多次转义
                switch (userFeatureType) {
                    case "0":
                        userFeatureStrBuffer.append("多头借贷,");
                        break;
                    case "2":
                        userFeatureStrBuffer.append("羊毛党,");
                        break;
                    case "5":
                        userFeatureStrBuffer.append("作弊软件,");
                        break;
                    case "6":
                        userFeatureStrBuffer.append("法院失信,");
                        break;
                    case "7":
                        userFeatureStrBuffer.append("网贷失信,");
                        break;
                    case "8":
                        userFeatureStrBuffer.append("关联过多,");
                        break;
                    case "10":
                        userFeatureStrBuffer.append("曾使用可疑设备,");
                        break;
                    case "11":
                        userFeatureStrBuffer.append("安装极多借贷app,");
                        break;
                    case "13":
                        userFeatureStrBuffer.append("身份信息疑似泄漏,");
                        break;
                    case "17":
                        userFeatureStrBuffer.append("活体攻击设备,");
                        break;
                    case "18":
                        userFeatureStrBuffer.append("活体攻击行为,");
                        break;
                    case "21":
                        userFeatureStrBuffer.append("疑似欺诈团伙,");
                        break;
                    case "23":
                        userFeatureStrBuffer.append("网贷不良,");
                        break;
                    case "24":
                        userFeatureStrBuffer.append("短期逾期,");
                        break;
                }
            }
            udInfo.setUserFeatureType(userFeatureBuffer.toString());
            udInfo.setUserFeatureTypeStr(userFeatureStrBuffer.toString());
        }

        //获取借款详情
        JSONObject loan_detail = body.getJSONObject("loan_detail");
        if (loan_detail != null) {
            if (loan_detail.get("actual_loan_platform_count") != null) {
                udInfo.setActualLoanPlatformCount((String) loan_detail.get("actual_loan_platform_count"));
            }
            if (loan_detail.get("actual_loan_platform_count_1m") != null) {
                udInfo.setActualLoanPlatformCount1m((String) loan_detail.get("actual_loan_platform_count_1m"));
            }
            if (loan_detail.get("actual_loan_platform_count_3m") != null) {
                udInfo.setActualLoanPlatformCount3m((String) loan_detail.get("actual_loan_platform_count_3m"));
            }
            if (loan_detail.get("actual_loan_platform_count_6m") != null) {
                udInfo.setActualLoanPlatformCount6m((String) loan_detail.get("actual_loan_platform_count_6m"));
            }
            if (loan_detail.get("loan_platform_count") != null) {
                udInfo.setLoanPlatformCount((String) loan_detail.get("loan_platform_count"));
            }
            if (loan_detail.get("loan_platform_count_1m") != null) {
                udInfo.setLoanPlatformCount1m((String) loan_detail.get("loan_platform_count_1m"));
            }
            if (loan_detail.get("loan_platform_count_3m") != null) {
                udInfo.setLoanPlatformCount3m((String) loan_detail.get("loan_platform_count_3m"));
            }
            if (loan_detail.get("loan_platform_count_6m") != null) {
                udInfo.setLoanPlatformCount6m((String) loan_detail.get("loan_platform_count_6m"));
            }
            if (loan_detail.get("repayment_times_count") != null) {
                udInfo.setRepaymentTimesCount((String) loan_detail.get("repayment_times_count"));
            }
            if (loan_detail.get("repayment_platform_count") != null) {
                udInfo.setRepaymentPlatformCount((String) loan_detail.get("repayment_platform_count"));
            }
            if (loan_detail.get("repayment_platform_count_1m") != null) {
                udInfo.setRepaymentPlatformCount1m((String) loan_detail.get("repayment_platform_count_1m"));
            }
            if (loan_detail.get("repayment_platform_count_3m") != null) {
                udInfo.setRepaymentPlatformCount3m((String) loan_detail.get("repayment_platform_count_3m"));
            }
            if (loan_detail.get("repayment_platform_count_6m") != null) {
                udInfo.setRepaymentPlatformCount6m((String) loan_detail.get("repayment_platform_count_6m"));
            }
        }

        JSONObject score_detail = body.getJSONObject("score_detail");
        if (score_detail != null) {
            if (score_detail.get("score") != null) {
                udInfo.setScore((String) score_detail.get("score"));
            }
            if (score_detail.get("risk_evaluation") != null) {
                udInfo.setRiskEvaluation((String) score_detail.get("risk_evaluation"));
            }
        }
        if (body.get("ud_order_no") != null) {
            udInfo.setUdOrderNo((String) body.get("ud_order_no"));
        }
        return udInfo;
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
            if(pr.getOutOrderCount()==  null){
                pr.setOutOrderCount(0);
            }
            pr.setOutOrderCount(pr.getOutOrderCount() + 1);
        }
        personRecordService.updateById(pr);
        // 发送审核通过短信
        YunpianSmsUtil.sendSms(YunpianSmsUtil.YTempletEnum.ORDER_EXAMINE_PASS,user.getPhone(),null);

        // 打款
        EntityWrapper<UserBank> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getId());
        UserBank userBank = userBankService.selectOne(wrapper);
        //发起请求
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
                        log.info("user放款统计+1");
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
