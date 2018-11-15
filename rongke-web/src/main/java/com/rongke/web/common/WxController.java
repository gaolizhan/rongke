package com.rongke.web.common;

import com.alibaba.fastjson.JSONObject;
import com.gson.util.SHA1;
import com.rongke.service.UserService;
import com.rongke.utils.EmojiFilter;
import com.rongke.wx.message.TextMessage;
import com.rongke.wx.wxutils.MessageUtil;
import com.rongke.wx.wxutils.WeixinToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/04/24.
 */
@RestController
@RequestMapping(value = "/api/wx")
@Transactional
@CrossOrigin
public class WxController {
    @Autowired
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static Map<String, String> TIME_OPENID_MAP = new HashMap<String, String>();

    /**
     * POST  /menus -> Create weixin menus.
     */
    //http://localhost:8080/api/wechat/gateway?signature=123432&timestamp=12343&nonce=1234&echostr=1223434
    @RequestMapping(value = "/gateway", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String weChatValidate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("enter weChatValidate!");
        String _token = "zxcvbnm";
        String outPut = "error";
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        TIME_OPENID_MAP.put(timestamp, "");
        String nonce = request.getParameter("nonce");// 随机数 1413789908
        String echostr = request.getParameter("echostr");//
        System.out.println("in get nonce: " + nonce);
        String[] str = {_token, timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2]; // SHA1加密
        String digest = SHA1.encode(bigStr);
        if (digest.equals(signature)) {
            outPut = echostr;
            System.out.println("check success!");
        }
        System.out.println("write response:" + outPut);
        PrintWriter writer = response.getWriter();
        writer.write(outPut);
        writer.flush();
        writer.close();
        return null;
    }

    @RequestMapping(value = "/gateway", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void handleWechatMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("received wechat event!");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String nonce = request.getParameter("nonce");
        System.out.println("in post nonce: " + nonce);
        request.getSession().setAttribute("nonce", nonce);
        // 调用核心业务类接收消息、处理消息
        String respMessage = processRequest(request, response);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
    }

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            request.setAttribute("openId", fromUserName);
            HttpSession session = request.getSession();
            session.setAttribute("openId", fromUserName);

            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 带参，二维码
            String eventKey = requestMap.get("EventKey");
            // 消息类型
            String msgType = requestMap.get("MsgType").toLowerCase();
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                respContent = "您发送的是文本消息！";
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！" + requestMap.get("EventKey");
            }
            // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是音频消息！";
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event").toLowerCase();
                // 自动获取地理位置
                if (eventType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                   /* Example example = userService.getExample();
                    example.createCriteria().andEqualTo("openid",fromUserName).andEqualTo("status",1);
                    User user = userService.selectFirst(example);

                    if(user != null){
                        //删除之前旧数据
                        Position position = new Position();
                        position.setUserId(user.getId());
                        positionService.delete(position);

                        position.setServerType(user.getServerType());
                        position.setLng(Float.valueOf(requestMap.get("Longitude")));
                        position.setLat(Float.valueOf(requestMap.get("Latitude")));
                        positionService.add(position);
                    }*/

                    respContent = "地理位置！";
                }
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                    String access_token = WeixinToken.getRecentToken();

                    String urlStr = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + fromUserName;
                    URL url = new URL(urlStr);
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(new BufferedInputStream(url.openStream()), "utf-8"));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = bufr.readLine()) != null) {
                        sb.append(line);
                    }
                    bufr.close();
                    JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                    if (jsonObject.get("errcode") != null) {
                        throw new Exception(jsonObject.getString("errmsg"));
                    }


                    String nick = String.valueOf(jsonObject.get("nickname"));
                    nick = EmojiFilter.filterEmoji(nick);
                    String sex = String.valueOf(jsonObject.get("sex"));
                    if (sex.equals("1")) {
                        sex = "男";
                    } else if (sex.equals("2")) {
                        sex = "女";
                    } else if (sex.equals("0")) {
                        sex = "未知";
                    }
                    String city = String.valueOf(jsonObject.get("city"));
                    String province = String.valueOf(jsonObject.get("province"));
                    String country = String.valueOf(jsonObject.get("country"));
                    String headimgurl = String.valueOf(jsonObject.get("headimgurl"));

                    /*//用户信息更新或者添加
                    Example example = userService.getExample();
                    example.createCriteria().andEqualTo("openid",fromUserName);
                    User resultUser = userService.selectFirst(example);
                    if (resultUser == null){
                        resultUser = new User();
                    }

                    resultUser.setOpenid(fromUserName);
                    resultUser.setNickName(nick);
                    resultUser.setSex(sex);
                    resultUser.setCity(city);
                    resultUser.setProvince(province);
                    resultUser.setCountry(country);
                    resultUser.setHeadImgurl(headimgurl);
                    if(resultUser.getId() != null){
                        //被封号的不能关注
                        if(resultUser.getStatus().equals(3)){
                            log.debug("关注成功");
                            return "关注失败！";
                        }
                        resultUser.setStatus(1);
                        userService.updateByPrimaryKey(resultUser);
                    }else{
                        resultUser.setUserType(1);  //用户
                        resultUser.setStatus(1);
                        resultUser.setAdminId(1L); //默认总公司
                        userService.add(resultUser);

                        //若参数中包含qrscene_,则获取当前用户,设置分销
                        if(eventKey.indexOf("qrscene_") != -1){
                            resultUser = userService.selectFirst(example);
                            userService.setTeamUser(resultUser.getId(),Long.parseLong(eventKey.split("qrscene_")[1]));
                        }

                        User reurnUser = userService.selectFirst(example);
                        //创建用户账户信息
                        UserAccount userAccount = new UserAccount();
                        userAccount.setUserId(reurnUser.getId());
                        userAccount.setAllMoney(new BigDecimal(0));
                        userAccount.setBlockMoney(new BigDecimal(0));
                        userAccount.setAllCredit(0);
                        userAccount.setBlockCredit(0);
                        userAccount.setAccountLevel(1);
                        userAccount.setAccountStatus(1);
                        userAccountService.add(userAccount);
                    }*/
                    respContent = "谢谢您的关注！";
                    log.debug("关注成功");

                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                   /* Example example = userService.getExample();
                    example.createCriteria().andEqualTo("openid",fromUserName);
                    User resultUser = userService.selectFirst(example);
                    resultUser.setStatus(2);
                    userService.updateByPrimaryKeySelectiveJson(resultUser);*/
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    // TODO 自定义菜单权没有开放，暂不处理该类消息
                    String key = requestMap.get("EventKey");
                    if (key.equals("MAINTAIN")) {
                        respContent = "";
                    }
                    if (key.equals("INFO")) {
                        respContent = "";
                    }
                    System.out.println("event key:" + key);
                }
                // 自定义view界面
                else if (eventType.equals(MessageUtil.EVENT_TYPE_VIEW)) {
                    String url = requestMap.get("EventKey");
                    System.out.println("url:" + url);

                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            }
            return respContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;

    }

    /**
     * 获取openId
     *
     * @param wxCode
     * @return
     */
    @RequestMapping(value = "/wxcode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getWeiXinOpenId(@PathVariable String wxCode) {
        System.out.println("wxCode--》》》：" + wxCode);
        if ("undefined".equals(wxCode)) {
            return null;
        }
        String wxOpenId = "";
        try {
            wxOpenId = WeixinToken.getWeChatId(wxCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxOpenId;
    }

}

