package com.rongke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rongke.commons.FailException;
import com.rongke.commons.JsonCodeEnum;
import com.rongke.commons.JsonResp;
import com.rongke.enums.EveryDayDataType;
import com.rongke.mapper.ChannelMapper;
import com.rongke.mapper.EveryDayDataMapper;
import com.rongke.mapper.PersonRecordMapper;
import com.rongke.mapper.UserMapper;
import com.rongke.model.*;
import com.rongke.service.AdminService;
import com.rongke.service.ChannelService;
import com.rongke.service.LoginRecordService;
import com.rongke.service.UserService;
import com.rongke.utils.DateUtils;
import com.rongke.utils.Md5;
import com.rongke.utils.RequestUtil;
import com.rongke.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version : Ver 1.0
 * @UserServiceImpl
 * @用户基本信息ServiceImpl
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userDao;

    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private AdminService adminService;


    @Autowired
    private LoginRecordService loginRecordService;

    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private EveryDayDataMapper everyDayDataMapper;

    /**
     * 登录
     *
     * @param userName 账号
     * @param password 密码
     * @return JsonResp
     */
    public JsonResp loginByUserName(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, Md5.md5Encode(password));
        try {
            //踢除用户
            this.kickOutUser(token);
        } catch (Exception e) {
            String message = e.getMessage();
            if (!message.contains("There is no session with id [")) {
                e.printStackTrace();
            }
        } finally {
            this.kickOutUser(token);
        }
        try {
            subject.login(token);
            Session session = subject.getSession();
            Serializable sessionId = session.getId();
            System.out.println("sessionId:" + sessionId);
            System.out.println("sessionHost:" + session.getHost());
            System.out.println("sessionTimeout:" + session.getTimeout());
            System.out.println(userName + "登录成功");

            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setIp(session.getHost());
            loginRecord.setUserName(userName);
            loginRecord.setLoginTime(new Date());
            loginRecordService.insert(loginRecord);



            EntityWrapper<Admin> wrapper = new EntityWrapper<>();
            wrapper.eq("user_name", userName);
            wrapper.eq("status", 1);
            Admin admin = adminService.selectOne(wrapper);
            admin.setToken(sessionId.toString());
            adminService.updateById(admin);
            return JsonResp.ok(admin);
        } catch (Exception e) {
            String msg = "用户名或密码错误!";
            throw new FailException(msg);
        }
    }

    /**
     * 登录
     *
     * @param phone    手机
     * @param password 密码
     * @return JsonResp
     */
    public JsonResp loginByPhone(String phone, String password, String phoneSign) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(phone, Md5.md5Encode(password));
        try {
            //踢除用户
            this.kickOutUser(token);
        } catch (Exception e) {
            String message = e.getMessage();
            if (!message.contains("There is no session with id [")) {
                e.printStackTrace();
            }
        } finally {
            this.kickOutUser(token);
        }
        try {
            subject.login(token);
            Session session = subject.getSession();
            Serializable sessionId = session.getId();
            System.out.println("sessionId:" + sessionId);
            System.out.println("sessionHost:" + session.getHost());
            System.out.println("sessionTimeout:" + session.getTimeout());
            System.out.println(phone + "登录成功");

           /* LoginRecord loginRecord=new LoginRecord();
            loginRecord.setIp(session.getHost());
            loginRecord.setPhone(phone);
            EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
            userEntityWrapper.eq("phoone",phone);
            User user1=userService.selectOne(userEntityWrapper);
            loginRecord.setUserName(user1.getUserName());
            loginRecord.setLoginTime(new Date());
            loginRecord.setType(2);
            loginRecordService.insert(loginRecord);*/

            //User user = userDao.selectByPhone(phone);
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("phone", phone);
            User user = userService.selectOne(entityWrapper);

            user.setToken(sessionId.toString());
            user.setPhoneSign(phoneSign);
            userService.updateById(user);
            return JsonResp.ok(user);
        } catch (Exception e) {
            String msg = "用户名或密码错误!";
            throw new FailException(msg);
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            subject.logout();
        }
    }

    /**
     * 踢除用户
     * http://www.ithao123.cn/content-7174367.html
     */
    private void kickOutUser(UsernamePasswordToken token) {
        String loginName = token.getUsername();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            if (session.getTimeout() == 0) {
                sessionDAO.delete(session);
            }
            if (loginName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                //设置session立即失效，即将其踢出系统
                session.setTimeout(0);
                logout();
            }
        }
    }

    /**
     * 获取当前登陆用户
     *
     * @return JsonResp
     */
    public User findLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection collection = subject.getPrincipals();
        User user = null;
        System.out.println("----------------------------- collection : " + (collection == null));

        if (null != collection && !collection.isEmpty()) {//web 端
            System.out.println("----------------------------- web");
            String userName = (String) collection.iterator().next();
            user = userDao.selectByUserName(userName);
            if (user == null) throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
            if (user != null) {
                if (user.getStatus() == 3) throw new FailException(JsonCodeEnum.STOP.getMessage());
                return user;
            }
        }
        if (null == collection) {//手机端
            System.out.println("----------------------------- 手机端");
            HttpServletRequest request = RequestUtil.getRequest();
            //请求方发送的token
            String token = request.getHeader("x-client-token");
            System.out.println("----------------------------- token : " + token);

            if (StringUtil.isNotEmpty(token)) user = userDao.selectByToken(token);
            if (user != null) {
                if (user.getStatus() == 3) throw new FailException(JsonCodeEnum.STOP.getMessage());
                return user;
            }

            if (user != null && user.getStatus() == 4) {
                Date date = user.getRefuseRemoveTime();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String DATE1 = df.format(new Date());
                String DATE2 = df.format(date);

                try {
                    Date dt1 = df.parse(DATE1);
                    Date dt2 = df.parse(DATE2);
                    if (dt1.getTime() <= dt2.getTime()) {
                        user.setStatus(1);
                        userDao.updateById(user);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
    }

    /**
     * 获取当前登陆管理员
     *
     * @return JsonResp
     */
    public Admin getThisLogin() {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection collection = subject.getPrincipals();
        Admin admin = null;
        if (null != collection && !collection.isEmpty()) {//web 端
            String userName = (String) collection.iterator().next();
            EntityWrapper<Admin> wrapper = new EntityWrapper<>();
            wrapper.eq("user_name", userName);
            admin = adminService.selectOne(wrapper);
            if (admin == null) throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
            return admin;
        }
        throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
    }

    //渠道商

    /**
     * 获取当前登陆渠道商
     *
     * @return JsonResp
     */
    public Channel getCurentChannel() {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection collection = subject.getPrincipals();
        Channel channel = null;
        if (null != collection && !collection.isEmpty()) {//web 端
            String userName = (String) collection.iterator().next();
            EntityWrapper<Channel> wrapper = new EntityWrapper<>();
            wrapper.eq("login_name", userName);
            wrapper.eq("status", 1);
            channel = channelService.selectOne(wrapper);
            if (channel == null) throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
            return channel;
        }
        throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
    }

    /**
     * 渠道商登录
     *
     * @param userName 账号
     * @param password 密码
     * @return JsonResp
     */
    public JsonResp channelLogin(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, Md5.md5Encode(password));
        try {
            //踢除用户
            this.kickOutUser(token);
        } catch (Exception e) {
            String message = e.getMessage();
            if (!message.contains("There is no session with id [")) {
                e.printStackTrace();
            }
        } finally {
            this.kickOutUser(token);
        }
        try {
            subject.login(token);
            Session session = subject.getSession();
            Serializable sessionId = session.getId();
            System.out.println("sessionId:" + sessionId);
            System.out.println("sessionHost:" + session.getHost());
            System.out.println("sessionTimeout:" + session.getTimeout());
            System.out.println(userName + "登录成功");

            EntityWrapper<Channel> wrapper = new EntityWrapper<>();
            wrapper.eq("login_name", userName);
            Channel channel = channelService.selectOne(wrapper);
            channel.setToken(sessionId.toString());
            channelService.updateById(channel);
            return JsonResp.ok(channel);
        } catch (Exception e) {
            String msg = "用户名或密码错误!";
            throw new FailException(msg);
        }
    }

    public Boolean updateUserList(Map<String, Object> map) {
        return userMapper.updateUserList(map);
    }

    public Integer selectCanLinquNum() {
        return userMapper.selectCanLinquNum();
    }

    public List<User> selectLinquUser() {
        return userMapper.selectLinquUser();
    }

    @Override
    public String getChannleNameByUser(Long id) {
        return userMapper.getChannleNameByUser(id);
    }

    @Override
    public List<User> getBlackUserList() {
        return userMapper.getBlackUserList();
    }

    @Override
    public List<String> getAllPhone() {
        return userMapper.getAllPhone();
    }

    @Override
    public Integer selectUserRegisterCountByBetweenDate(String startTime, String endTime,Long channelId) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("startTime",startTime);
        paramMap.put("endTime",endTime);
        paramMap.put("channelId",channelId);
        return userMapper.selectUserRegisterCountByBetweenDate(paramMap);
    }

    @Override
    public JsonResp getChannelStatistics(String startTime, String endTime, String channelId) {

        JSONObject resultMap = new JSONObject();
        JSONObject parameter = new JSONObject();
        List<JSONObject> list=new ArrayList<>();
        //是否渠道商用户
        boolean isChannel=true;
        if(StringUtils.isNotEmpty(startTime)){
            parameter.put("startTime",startTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            parameter.put("endTime",endTime);
        }

        //获取当前登录信息，判断是否渠道商
        Admin admin = userService.getThisLogin();
        Channel selChannel = new Channel();
        selChannel.setLoginName(admin.getUserName());
        //selChannel.setStatus(1);

        List<Channel> channels = channelService.selectByCondition(selChannel);
        if(channels.size()==1){
            parameter.put("channelId",channels.get(0).getId());

        }
        else{
            isChannel=false;
            Channel channel= new Channel();
            if(StringUtils.isNotEmpty(channelId)){
                parameter.put("channelId",channelId);
                channel.setId(Long.valueOf(channelId));
            }

            //获取所有channeInfo
            channels= channelMapper.selectByCondition(channel);
        }

        //渠道商显示
        if(isChannel){
            resultMap.put("userTotalRegisteCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(new JSONObject(){{
                put("type", EveryDayDataType.TYPE.REG_NUM.getKey());
                put("channelId",channelId);
            }})));

            parameter.put("type", EveryDayDataType.TYPE.REG_NUM.getKey());
            resultMap.put("userRegisteCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));


            //已申请用户数
            parameter.put("type", EveryDayDataType.TYPE.APPLY_NUM.getKey());
            resultMap.put("todayApplyNumCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

            //已放贷用户数量
            parameter.put("type", EveryDayDataType.TYPE.LENDING_NUM.getKey());
            resultMap.put("todayLendingNumCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

        }
        else{
            JSONObject userTotalParameter=new JSONObject();
            if(StringUtils.isNotEmpty(channelId)){
                userTotalParameter.put("channelId",channelId);
            }
            //总注册人数
            resultMap.put("userTotalRegisteCount", userMapper.getCountUserData(userTotalParameter));


            //当日注册人数
            resultMap.put("userRegisteCount",userMapper.getCountUserData(parameter));


            //已申请用户数
            parameter.put("type", EveryDayDataType.TYPE.APPLY_NUM.getKey());
            resultMap.put("todayApplyNumCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

            //已放贷用户数量
            parameter.put("type", EveryDayDataType.TYPE.LENDING_NUM.getKey());
            resultMap.put("todayLendingNumCount", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

            for (Channel c :channels){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("channelId",c.getId());
                jsonObject.put("channelNmae",c.getName());

                parameter.put("channelId",c.getId());

                //渠道注册人数
                jsonObject.put("userRegisteCount",userMapper.getCountUserData(parameter));

                //已申请用户数
                parameter.put("type", EveryDayDataType.TYPE.APPLY_NUM.getKey());
                jsonObject.put("applyNum", channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

                //已放贷用户数量
                parameter.put("type", EveryDayDataType.TYPE.LENDING_NUM.getKey());
                jsonObject.put("lendingNum",channelDecrementNumShow(isChannel,everyDayDataMapper.getTimeEveryDayData(parameter)));

                list.add(jsonObject);
            }
            resultMap.put("list",list);
        }

        return JsonResp.ok(resultMap);
    }

    /**
     * 是否显示渠道商扣量数据
     * @param isChannel
     * @param

     * @return
     */
    private int channelDecrementNumShow(boolean isChannel,JSONObject jsonObject){
        int num=0;
        if(isChannel && jsonObject!=null){
            num=jsonObject.getIntValue("decrementNum");
        }
        else if(!isChannel && jsonObject!=null){
            num=jsonObject.getIntValue("num");
        }
        return num;
    }


}
