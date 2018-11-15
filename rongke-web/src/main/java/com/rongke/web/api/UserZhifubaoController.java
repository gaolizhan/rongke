package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.*;
import com.rongke.service.*;
import com.rongke.utils.DateUtils;
import com.rongke.web.quartz.TimeJob;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserZhifubaoController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userZhifubao")
@Transactional
@CrossOrigin
public class UserZhifubaoController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserZhifubaoService userZhifubaoService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private ZhifubaoLogService zhifubaoLogService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private ParamSettingService paramSettingService;

    /**
     * @添加
     * @param userZhifubao
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserZhifubao(@RequestBody UserZhifubao userZhifubao){
        log.debug("添加");
        userZhifubaoService.insert(userZhifubao);
        return JsonResp.ok(userZhifubao);
    }

    /**
     * @修改
     * @param userZhifubao
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserZhifubao(@RequestBody UserZhifubao userZhifubao){
        log.debug("修改");
        userZhifubaoService.updateById(userZhifubao);
        return JsonResp.ok(userZhifubao);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserZhifubao(Long id){
        log.debug("查找");
        EntityWrapper<UserZhifubao> ew = new EntityWrapper();
        ew.eq("user_id",id);
        UserZhifubao userZhifubao = userZhifubaoService.selectOne(ew);
        return JsonResp.ok(userZhifubao);
    }

    /**
     * @支付宝认证中
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/zhifubaoLoading", method = RequestMethod.GET)
    public JsonResp zhifubaoLoading(String task_id){
        log.debug("支付宝认证中");
        User user = userService.findLoginUser();
        EntityWrapper<UserAuth> ew = new EntityWrapper();
        ew.eq("user_id",user.getId());
        UserAuth userAuth = userAuthService.selectOne(ew);
        userAuth.setZhifubaoAuth(2);
        userAuthService.updateById(userAuth);
        //支付宝日志表
        ZhifubaoLog zhifubaoLog = new ZhifubaoLog();
        zhifubaoLog.setTaskId(task_id);
        zhifubaoLog.setUserId(user.getId());
        zhifubaoLogService.insert(zhifubaoLog);
        return JsonResp.ok();
    }

    /**
     * @支付宝列表
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findList", method = RequestMethod.GET)
    public JsonResp findList(String phone,Page page,String authStatus){
        Map<String,Object>map=new HashMap<>();
        if(phone!=null&&!"".equals(phone)) {
            map.put("phone", phone);
        }
        map.put("authStatus",authStatus);
        map.put("pageSize",page.getSize());
        map.put("pageNo",(page.getCurrent()-1)*page.getSize());
        List<UserZhifubao> userZhifubaoList =  userZhifubaoService.selectAll(map);

        Integer count=userZhifubaoService.selectCountNum(map);
        if (count==null){
            count=0;
        }

        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), userZhifubaoList, count));
    }

    /**
     * 查询用户支付宝信息
     * @return
     */
    @RequestMapping(value = "/seleAliInfo",method = RequestMethod.GET)
    public JsonResp seleAliInfo(){
        User user=userService.findLoginUser();
        EntityWrapper<UserZhifubao>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",user.getId());
        UserZhifubao userZhifubao=userZhifubaoService.selectOne(entityWrapper);
        return JsonResp.ok(userZhifubao);
    }





    /**
     * @支付宝列表
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findMyList", method = RequestMethod.GET)
    public JsonResp findMyList(String phone,Page page,String authStatus){
        Admin admin=userService.getThisLogin();
        Map<String,Object>map=new HashMap<>();
        if(phone!=null&&!"".equals(phone)) {
            map.put("phone", phone);
        }
        map.put("adminAuditId",admin.getId());
        map.put("authStatus",authStatus);
        map.put("pageSize",page.getSize());
        map.put("pageNo",(page.getCurrent()-1)*page.getSize());
        List<UserZhifubao> userZhifubaoList =  userZhifubaoService.selectMyAll(map);

        Integer count=userZhifubaoService.selectMyCountNum(map);
        if (count==null){
            count=0;
        }

        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), userZhifubaoList, count));
    }


    /**
     * 支付宝i认证
     * @return
     */
    @RequestMapping(value = "/zhifubaoAuth",method = RequestMethod.GET)
    public JsonResp zhifubaoAuth(String aliNumber,String aliPwd){
        User user=userService.findLoginUser();
        EntityWrapper<UserZhifubao>userZhifubaoEntityWrapper=new EntityWrapper<>();
        userZhifubaoEntityWrapper.eq("user_id",user.getId());
        UserZhifubao userZhifubao=userZhifubaoService.selectOne(userZhifubaoEntityWrapper);
        userZhifubao.setUserId(user.getId());
        userZhifubao.setAliNumber(aliNumber);
        userZhifubao.setAliPwd(aliPwd);
        userZhifubaoService.updateById(userZhifubao);

        EntityWrapper<UserAuth>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",user.getId());
        UserAuth userAuth=userAuthService.selectOne(entityWrapper);
        userAuth.setZhifubaoAuth(1);
        Boolean bool=userAuthService.updateById(userAuth);

        return JsonResp.ok(bool);
    }


    /**
     * 后台管理添加花呗额度
     * @return
     */
    @RequestMapping(value = "/addHuabei",method = RequestMethod.GET)
    public JsonResp addHuabei(String huabei,String id){
     UserZhifubao userZhifubao=userZhifubaoService.selectById(id);
     User user=userService.selectById(userZhifubao.getUserId());

     user.setAuthStatus(1);
     user.setStatus(1);

     if (huabei!=null&&!"".equals(huabei)){

         user.setMoney(new BigDecimal(huabei));
         userService.updateById(user);
     }




     return JsonResp.ok();

    }

    /**
     * 后台拒绝认证
     * @param userId
     * @return
     */
    @RequestMapping(value = "/refuseAuth",method = RequestMethod.GET)
    public JsonResp refuseAuth(long userId){
        User user=userService.selectById(userId);
        user.setAuthStatus(0);
        user.setStatus(4);
        Boolean bool=userService.updateById(user);
        return JsonResp.ok(bool);
    }


    /**
     * 后台同意认证
     * @param userId
     * @return
     */
    @RequestMapping(value = "/agreeAuth",method = RequestMethod.GET)
    public JsonResp agreeAuth(long userId){
        User user=userService.selectById(userId);
        user.setAuthStatus(1);
        user.setStatus(1);
        user.setAuthScore(60);
        Boolean bool=userService.updateById(user);
        return JsonResp.ok(bool);
    }

    @RequestMapping(value = "/doZhiFubao",method = RequestMethod.GET)
    public JsonResp doZhiFubao(){
        EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
        List<User>userList=userService.selectList(userEntityWrapper);
        for (User user:userList) {
            EntityWrapper<UserZhifubao> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("user_id",user.getId());
            List<UserZhifubao>userZhifubaoList=userZhifubaoService.selectList(entityWrapper);
            if(userZhifubaoList.size()>1){
                int i=0;
                for (UserZhifubao userZhifubao:userZhifubaoList){

                    if (userZhifubao.getAssetsYuE()==null&&i!=5){
                        userZhifubaoService.deleteById(userZhifubao);
                        i=5;
                    }
                }
            }
        }
        return JsonResp.ok();
    }


    /**
     * 后台暂时拒绝用户
     * @return
     */
    @RequestMapping(value = "/temporaryRefuse",method = RequestMethod.POST)
    public JsonResp temporaryRefuse(@RequestBody JSONObject jsonObject){

        JSONArray status=jsonObject.getJSONArray("checkBoxArray");


        String userId=jsonObject.getString("userId");
        User user=userService.selectById(userId);
        user.setAuthStatus(2);
        userService.updateById(user);
        EntityWrapper<UserAuth>entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_id",userId);
        UserAuth userAuth=userAuthService.selectOne(entityWrapper);
        for (int i=0;i<status.size();i++){
            if (status.get(i).equals("basicMsgAuth")){
                userAuth.setBaiscAuth(0);
            }else if (status.get(i).equals("identityAuth")){
                userAuth.setIdentityAuth(0);
            }else if (status.get(i).equals("yysAuth")){
                userAuth.setPhoneAuth(0);
            }else if (status.get(i).equals("bankAuth")){
                userAuth.setBankAuth(0);
            }else if (status.get(i).equals("taobaoAuth")){
                userAuth.setTaobaoAuth(0);
            }else if (status.get(i).equals("zhifubaoAuth")){
                userAuth.setZhifubaoAuth(0);
            }
        }

        userAuthService.updateById(userAuth);
        UserMessage userMessage=new UserMessage();
        userMessage.setUserId(new Long(userId));
        userMessage.setText(jsonObject.getString("userMessage"));
        userMessage.setStatus(1);
        userMessageService.insert(userMessage);



        return JsonResp.ok();
    }

    /**
     * 后台永久拒绝用户
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/foreverRefuse",method = RequestMethod.POST)
    public JsonResp foreverRefuse(@RequestBody JSONObject jsonObject){

        String userId=jsonObject.getString("userId");
        User user=userService.selectById(userId);
        user.setAuthStatus(3);
        user.setStatus(4);
        Boolean bool=userService.updateById(user);
        UserMessage userMessage=new UserMessage();
        userMessage.setUserId(new Long(userId));
        userMessage.setText(jsonObject.getString("userMessage").toString());
        userMessage.setStatus(1);
        userMessageService.insert(userMessage);

        return JsonResp.ok(bool);
    }


    /**
     * 后台分页查询我的暂时拒绝的用户列表
     * @return
     */
    @RequestMapping(value = "/temporaryMyRefuseUserList",method = RequestMethod.GET)
    public JsonResp temporaryRefuseUserList (String phone,Page page){
            Admin admin=userService.getThisLogin();
            Map<String,Object>map=new HashMap<>();
            if(phone!=null&&!"".equals(phone)) {
                map.put("phone", phone);
            }
            map.put("adminAuditId",admin.getId());
            map.put("pageSize",page.getSize());
            map.put("pageNo",(page.getCurrent()-1)*page.getSize());
            List<UserZhifubao> userZhifubaoList =  userZhifubaoService.selectMyTemporaryAll(map);

            Integer count=userZhifubaoService.selectMyTemporaryNum(map);
            if (count==null){
                count=0;
            }

            return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), userZhifubaoList, count));
        }



    /**
     * 后台分页查询全部暂时拒绝的用户列表
     * @return
     */
    @RequestMapping(value = "/temporaryAllRefuseUserList",method = RequestMethod.GET)
    public JsonResp temporaryAllRefuseUserList (String phone,Page page){

        Map<String,Object>map=new HashMap<>();
        if(phone!=null&&!"".equals(phone)) {
            map.put("phone", phone);
        }

        map.put("pageSize",page.getSize());
        map.put("pageNo",(page.getCurrent()-1)*page.getSize());
        List<UserZhifubao> userZhifubaoList =  userZhifubaoService.selectAllTemporary(map);

        Integer count=userZhifubaoService.selectAllTemporaryNum(map);
        if (count==null){
            count=0;
        }

        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), userZhifubaoList, count));
    }





    @RequestMapping(value = "/testPrintMessage",method = RequestMethod.GET)
    public JsonResp testPrintMessage()throws ParseException{
        int i=0;
        List<LoanOrder>loanOrderList = loanOrderService.selectAllOrders();
        TimeJob timeJob=new TimeJob();
        timeJob.printMessage();
        return JsonResp.ok(loanOrderList);
    }


    public void doTimeJob()throws ParseException{

        List<LoanOrder>loanOrderList = loanOrderService.selectAllOrders();
        if (loanOrderList.size()!=0) {
            for (LoanOrder o : loanOrderList) {
                //利率
                EntityWrapper<ParamSetting> ewParam = new EntityWrapper();
                ewParam.eq("id", o.getParamSettingId());
                ewParam.eq("status", 1);
                ParamSetting paramSetting = paramSettingService.selectOne(ewParam);
                if (paramSetting.getAllowDays() == 0) {
                    //没有容限期的时候
                    if (o.getOrderStatus() == 3) {
                        //判断是否超出容限期
                        if (o.getOverdueTime().getTime() < DateUtils.YYMMDDDate(new Date()).getTime()) {
                            o.setOrderStatus(5);
                            //超出容限期一天的费用
                            BigDecimal money = o.getBorrowMoney().multiply(new BigDecimal(paramSetting.getOverduePercent() * 0.01)).setScale(1, BigDecimal.ROUND_HALF_UP);
                            o.setOverdueDays(o.getOverdueDays() + 1);
                            o.setOverdueMoney(o.getOverdueMoney().add(money));
                            o.setWateMoney(o.getWateMoney().add(money));
                            o.setNeedPayMoney(o.getNeedPayMoney().add(money));
                            loanOrderService.updateById(o);
                        }
                    } else if (o.getOrderStatus() == 5) {


                        BigDecimal money = o.getBorrowMoney().multiply(new BigDecimal(paramSetting.getOverduePercent() * 0.01)).setScale(1, BigDecimal.ROUND_HALF_UP);
                        o.setOverdueMoney(o.getOverdueMoney().add(money));
                        o.setWateMoney(o.getWateMoney().add(money));
                        o.setNeedPayMoney(o.getNeedPayMoney().add(money));
                        o.setOverdueDays(o.getOverdueDays() + 1);
                        loanOrderService.updateById(o);
                        //发送逾期短信

                    }

                }
            }


        }

    }
    }

