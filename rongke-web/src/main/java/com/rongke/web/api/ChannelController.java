package com.rongke.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.mapper.IndexMapper;
import com.rongke.model.Admin;
import com.rongke.model.Channel;
import com.rongke.model.LoanOrder;
import com.rongke.model.User;
import com.rongke.service.ChannelService;
import com.rongke.service.EveryDayDataService;
import com.rongke.service.LoanOrderService;
import com.rongke.service.UserService;
import com.rongke.utils.DateUtils;
import com.rongke.utils.JSONUtils;
import com.rongke.utils.Md5;
import com.rongke.web.util.TalkingdataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version : Ver 1.0
 * @ChannelController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/channel")
@Transactional
@CrossOrigin
public class ChannelController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoanOrderService loanOrderService;
    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private EveryDayDataService everyDayDataService;

    /**
     * @param channel
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addChannel(@RequestBody Channel channel) {
        log.debug("添加");
        if (channel.getId() == null) {
            EntityWrapper<Channel> ew = new EntityWrapper();
            ew.eq("login_name", channel.getLoginName());
            Channel oldChannel = channelService.selectOne(ew);
            if (null != oldChannel) {
                return JsonResp.fa("该登录名曾经已被使用！");
            }
            channelService.insertOrUpdate(channel);
            //String linkUrl = "http://share.yironghua.cn/?channelName=" + channel.getLoginName();
            String linkUrl = "http://share.fintech-sx.com:81/?td_channelid=" + channel.getLoginName();
            channel.setLinkUrl(linkUrl);
            channel.setPassword(Md5.md5Encode(channel.getPassword()));
        } else {
            Channel channel1 = channelService.selectById(channel.getId());
            channel.setPassword(channel1.getPassword());
        }
        channel.setStatus(1);
        channelService.insertOrUpdate(channel);
        return JsonResp.ok(channel);
    }

    /**
     * @param channel
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateChannel(@RequestBody Channel channel) {
        log.debug("修改");
        channelService.updateById(channel);
        return JsonResp.ok(channel);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectChannel(Long id) {
        log.debug("查找");
        Channel channel = channelService.selectById(id);
        return JsonResp.ok(channel);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @根据id查找管理员
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JsonResp login(String userName, String password) {
        log.debug("登录");
        return JsonResp.ok(userService.channelLogin(userName, password));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @获取当前登录渠道商
     */
    @RequestMapping(value = "/getThisLogin", method = RequestMethod.GET)
    public JsonResp getThisLogin() {
        log.debug("获取当前登录渠道商");
        Channel channel = userService.getCurentChannel();
        return JsonResp.ok(channel);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @查找列表
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public JsonResp selectList(Integer pageNo, Integer pageSize, String loginName) {
        log.debug("查找列表");
        Page page = new Page(pageNo, pageSize);
        EntityWrapper<Channel> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("status", 1);
        if (!loginName.equals("")) {
            entityWrapper.like("login_name", loginName);
        }
        page = channelService.selectPage(page, entityWrapper);

        return JsonResp.ok(new PageDto(pageNo, pageSize, page.getRecords(), page.getTotal()));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @删除
     */
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public JsonResp deleteOne(Long id) {
        log.debug("删除");
        Channel channel = channelService.selectById(id);
        channel.setStatus(2);
        channelService.updateById(channel);
        return JsonResp.ok();
    }

    /**
     * @渠道商查询自己的注册会员
     * @param
     * @return 返回值JsonResp
     *//*
    @RequestMapping(value="/findMyMember", method = RequestMethod.GET)
    public JsonResp findMyMember(Integer pageNo,Integer pageSize,String phone,String userName){
        log.debug("渠道商查询自己的注册会员");
        Page page = new Page(pageNo,pageSize);
        Channel channel = userService.getCurentChannel();
        EntityWrapper<User> wrapper = new EntityWrapper();
        wrapper.eq("channel_id",channel.getId());
        if (!phone.equals("")) {
            wrapper.like("phone",phone);
        }
        if (!userName.equals("")) {
            wrapper.like("user_name",userName);
        }
        page = userService.selectPage(page,wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));
    }
*/

    /**
     * @param channelId(不为空说明后台查询指定 否则渠道商查询自己)
     * @return 返回值JsonResp
     * @渠道商查询自己的注册会员
     */
    @RequestMapping(value = "/findMyMember", method = RequestMethod.GET)
    public JsonResp findMyMember(Integer pageNo, Integer pageSize, String phone, String userName, Long channelId, String gmtGatetime) {
        log.debug("渠道商查询自己的注册会员");
        Page page = new Page(pageNo, pageSize);
        Channel channel = new Channel();
        if (channelId == null) {
            channel = userService.getCurentChannel();
        } else {
            channel = channelService.selectById(channelId);
        }
        EntityWrapper<User> wrapper = new EntityWrapper();
        wrapper.eq("channel_id", channel.getId());
        if (!phone.equals("")) {
            wrapper.like("phone", phone);
        }
        if (!userName.equals("")) {
            wrapper.like("user_name", userName);
        }
        // wrapper.eq("status",1);
        wrapper.orderBy("gmt_datetime", false);
        if (gmtGatetime != null && !"".equals(gmtGatetime)) {
            String time[] = gmtGatetime.split("~");
            wrapper.between("gmt_datetime", time[0], time[1]);
        }
        page = userService.selectPage(page, wrapper);
        return JsonResp.ok(new PageDto(pageNo, pageSize, page.getRecords(), page.getTotal()));
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @报表统计
     */
    @RequestMapping(value = "/statementSelect", method = RequestMethod.GET)
    public JsonResp statementSelect() {
        log.debug("报表统计");
        Channel channel = userService.getCurentChannel();
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        map.put("time", format.format(date));
        map.put("channelName", channel.getId());
        Integer userAddCountChannel = indexMapper.userAddCountChannel(map);
        String orderOutMoneyChannel = indexMapper.orderOutMoneyChannel(map);
        String applyOrderMoneyChannel = indexMapper.applyOrderMoneyChannel(map);
        String orderChannelProfitChannel = indexMapper.orderChannelProfitChannel(map);
        Map<String, Object> map1 = new HashMap<>();
        if (userAddCountChannel == null) {
            userAddCountChannel = 0;
        }
        if (orderOutMoneyChannel == null) {
            orderOutMoneyChannel = "0";
        }
        if (applyOrderMoneyChannel == null) {
            applyOrderMoneyChannel = "0";
        }
        if (orderChannelProfitChannel == null) {
            orderChannelProfitChannel = "0";
        }
        map1.put("userAddCountChannel", userAddCountChannel);
        map1.put("orderOutMoneyChannel", orderOutMoneyChannel);
        map1.put("applyOrderMoneyChannel", applyOrderMoneyChannel);
        map1.put("orderChannelProfitChannel", orderChannelProfitChannel);
        map1.put("channel", channel);
        return JsonResp.ok(map1);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @报表统计
     */
    @RequestMapping(value = "/findMoneyStatistics", method = RequestMethod.GET)
    public JsonResp findMoneyStatistics(String year) {
        log.debug("报表统计");
        Channel channel = userService.getCurentChannel();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("giveTime", year);
        map2.put("realPayTime", year);
        map1.put("channelName", channel.getId());
        map2.put("channelName", channel.getId());
        List<LoanOrder> list = loanOrderService.orderListChannel(map1);
        List<LoanOrder> list1 = loanOrderService.orderListChannel(map2);
        List<BigDecimal> outMoneys = new ArrayList<>();
        List<BigDecimal> backMoneys = new ArrayList<>();
        List<BigDecimal> profits = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String year1 = year + "-0" + (i + 1);
            BigDecimal outMoney = new BigDecimal("0");
            BigDecimal backMoney = new BigDecimal("0");
            BigDecimal profit = new BigDecimal("0");
            if (i >= 9) {
                year1 = year + "-" + (i + 1) + "";
            }
            for (LoanOrder loanOrder : list) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = format.format(loanOrder.getGiveTime());
                if (date.contains(year1)) {
                    outMoney = outMoney.add(loanOrder.getRealMoney());
                }
            }
            for (LoanOrder loanOrder : list1) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (loanOrder.getGiveTime() == null) {
                    int a = 1;
                }
                String date = format.format(loanOrder.getRealPayTime());
                if (date.contains(year1)) {
                    backMoney = backMoney.add(loanOrder.getRealPayMoney());
                    profit = profit.add(loanOrder.getChannelProfit());
                }
            }
            outMoneys.add(outMoney);
            backMoneys.add(backMoney);
            profits.add(profit);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("outMoneys", outMoneys);
        map.put("backMoneys", backMoneys);
        map.put("profits", profits);
        return JsonResp.ok(map);
    }

    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    @RequestMapping(value = "/getChannelStatistics1", method = RequestMethod.GET)
    public JsonResp getChannelStatistics1(
            @RequestParam(value = "startTime",defaultValue = "")String startTime,
            @RequestParam(value = "endTime",defaultValue = "")String endTime,
            @RequestParam(value = "channelId",defaultValue = "")String channelId
    ) throws Exception {

        return userService.getChannelStatistics(startTime,endTime,channelId);
    }
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    @RequestMapping(value = "/getChannelList", method = RequestMethod.GET)
    public JsonResp getChannelList() {
        JSONObject resultMap = new JSONObject();
        //获取当前登录信息，判断是否渠道商
        Admin admin = userService.getThisLogin();
        Channel selChannel = new Channel();
        selChannel.setLoginName(admin.getUserName());
        selChannel.setStatus(1);
        List<Channel> channels = channelService.selectByCondition(selChannel);
        if(channels.size()>0){

        }else{
            channels = channelService.selectByCondition(new Channel());
        }
        resultMap.put("channels",channels);
        return JsonResp.ok(resultMap);
    }


    @RequestMapping(value = "/getChannelStatistics", method = RequestMethod.GET)
    public JsonResp getChannelStatistics(String betweenDatetime) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        //获取所有channeInfo
        List<Channel> channelList =channelService.selectAllChannelInfo();

        int userRegisteCount = 0;
        int loanUserCount = 0;
        int userIntoChannelCount = 0;
        String startTime;
        String endTime;
        Integer rangeUserRegisteCount = 0;

        String channelFlag = "0";
        //获取当前登录信息，判断是否渠道商
        Admin admin = userService.getThisLogin();
        Channel selChannel = new Channel();
        selChannel.setLoginName(admin.getUserName());
        selChannel.setStatus(1);
        List<Channel> channels = channelService.selectByCondition(selChannel);

        int sum = 0;
//        JSONArray talkingdataInfoArray =  talkingdataUtil.getTalkingdataInfo();
//        if(talkingdataInfoArray == null){
//            return JsonResp.fa("系统忙请10秒后请求。");
//        }
//        for(int i=0;i<talkingdataInfoArray.size();i++){
//            JSONObject jsonObject = talkingdataInfoArray.getJSONObject(i);
//            int activeuser = jsonObject.getIntValue("activeuser");
//            sum+=activeuser;
//        }

        //查询时间内用户注册数量
        if(betweenDatetime != null && !"".equals(betweenDatetime.trim())){
            String time[] = betweenDatetime.split("~");
            startTime = time[0];
            endTime = time[1];
        }else{
            //默认
            startTime = DateUtils.dateSimple(new Date());
            endTime = DateUtils.dateSimple(DateUtils.dayAdd(1, new Date()));
        }

        if(channels.size()>0){
            log.info("渠道商用户");
            //渠道商用户
            Channel nowChannel = channels.get(0);
            for (Channel channel : channelList) {
                if(channel.getId().longValue() == nowChannel.getId().longValue()){
                    userRegisteCount=channel.getMemberCount();
                    loanUserCount=channel.getLoanUserCount();
                    rangeUserRegisteCount = userService.selectUserRegisterCountByBetweenDate(startTime, endTime, channel.getId());
                    String linkUrl = channel.getLinkUrl();
                    String nowChannelname = linkUrl.substring(linkUrl.lastIndexOf("=")+1, linkUrl.length());

//                    for(int i=0;i<talkingdataInfoArray.size();i++){
//                        JSONObject jsonObject = talkingdataInfoArray.getJSONObject(i);
//                        int activeuser = jsonObject.getIntValue("activeuser");
//                        String channelname = jsonObject.getString("channelname");
//                        if(nowChannelname.equals(channelname)){
//                            sum=activeuser;
//                        }
//                    }
                    channelFlag ="1";
                }
            }
        }else{
            log.info("非渠道商用户");
            //admin
            rangeUserRegisteCount = userService.selectUserRegisterCountByBetweenDate(startTime, endTime, null);
            loanUserCount = loanOrderService.selectPassLoanUserCount().size();
            EntityWrapper<User> ew = new EntityWrapper();
            userRegisteCount = userService.selectCount(ew);

            resultMap.put("channelInfoList",channelList);
        }
        resultMap.put("userRegisteCount",userRegisteCount);
        resultMap.put("loanUserCount",loanUserCount);
        resultMap.put("userIntoChannelCount",userIntoChannelCount);
        log.info(JSONUtils.beanToJson(resultMap));
        resultMap.put("channelFlag",channelFlag);
        resultMap.put("sum",sum);
        resultMap.put("rangeUserRegisteCount",rangeUserRegisteCount);

        return JsonResp.ok(resultMap);
    }
    @Autowired
    private  TalkingdataUtil talkingdataUtil;

    @RequestMapping(value = "/getLoginIsChannel", method = RequestMethod.GET)
    public JsonResp getLoginIsChannel() {
        //获取当前登录信息，判断是否渠道商
        Admin admin = userService.getThisLogin();
        Channel selChannel = new Channel();
        selChannel.setLoginName(admin.getUserName());
        selChannel.setStatus(1);
        List<Channel> channels = channelService.selectByCondition(selChannel);
        if(channels.size()>0){
            return JsonResp.ok("none");
        }else{
            return JsonResp.ok("show");
        }
    }
}
