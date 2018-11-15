package com.rongke.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lianpay.api.util.ApiConfProperty;
import com.lianpay.api.util.TraderRSAUtil;
import com.rongke.commons.JsonResp;
import com.rongke.model.CommOrder;
import com.rongke.model.User;
import com.rongke.pojo.CommodityDetail;
import com.rongke.service.CommOrderService;
import com.rongke.service.UserService;
import com.rongke.utils.CreateCommOrderId;
import com.rongke.utils.JSONUtils;
import com.rongke.web.payutil.alipay.util.httpClient.HttpRequest;
import com.rongke.yibaoModel.AuthBindCardBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2018/8/15.
 */
@Controller
@RequestMapping(value="/api/commOrder")
@Transactional
@CrossOrigin
public class CommOrderController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private CommOrderService commOrderService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value="/selectPage", method = RequestMethod.GET)
    public JsonResp getCommOrder(Integer pageNo, Integer pageSize,String status){
        User user = userService.findLoginUser();

        EntityWrapper<CommOrder> commOrderEntityWrapper=new EntityWrapper<>();
        if(status != null && status != ""){
            commOrderEntityWrapper.eq("status",status);
        }
        commOrderEntityWrapper.eq("user_id",user.getId());
        Page page = commOrderService.selectPage(new Page(pageNo,pageSize,"create_time"), commOrderEntityWrapper);
        List<CommOrder> records = page.getRecords();
        for (CommOrder commOrder : records) {
            String commInfoJson = commOrder.getCommInfoJson();
            try {
                List<CommodityDetail> commodityDetails = JSONArray.parseArray(commInfoJson, CommodityDetail.class);
                commOrder.setCommodityDetails(commodityDetails);
                commOrder.setCommInfoJson("");
            }catch (Exception e){
                return  new JsonResp().failRes("商品详情转换错误");
            }
            //

        }
        return  new JsonResp().ok(page);
    }

    @ResponseBody
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addCommOrder(@RequestBody Map<String, Object> map) throws IOException {
        CommOrder commOrder = new CommOrder();

        String commInfoJson = "";
        try {
            List<Map<String,Object>> commInfoList = ( List<Map<String,Object>>)map.get("commInfoJson");
            commInfoJson = JSONUtils.beanToJson(commInfoList);
        }catch (Exception e){
            log.error("commInfoJson转换异常",e);
            return new JsonResp().failRes("commInfoJson转换异常");
        }

        try {
            commOrder.setReceiverName((String)map.get("receiverName"));
            commOrder.setReceiverPhone((String)map.get("receiverPhone"));
            commOrder.setReceiverAddress((String)map.get("receiverAddress"));
        }catch (Exception e){
            log.error("用户地址异常",e);
            return new JsonResp().failRes("用户地址异常");
        }


        commOrder.setCommInfoJson(commInfoJson);
        BigDecimal postAmount =new BigDecimal("0.00");
        if(map.get("postAmount") != null){
            postAmount = new BigDecimal(map.get("postAmount").toString());
            commOrder.setPostAmount(postAmount);
        }

        BigDecimal commTotalAmount =new BigDecimal("0.00");
        if(map.get("commTotalAmount") != null){
            commTotalAmount = new BigDecimal(map.get("commTotalAmount").toString());
            commOrder.setCommTotalAmount(commTotalAmount);
        }

        String commOrderId = CreateCommOrderId.getOrderId();
        commOrder.setCommOrderId(commOrderId);

        //计算付款总金额金额
        commOrder.setNeedPayAmount(commTotalAmount.add(postAmount));

        User user = userService.findLoginUser();
        commOrder.setCreateTime(new Date());
        commOrder.setUserId(user.getId());
        commOrder.setStatus("notPay");
        commOrderService.insertOrUpdate(commOrder);
        return  new JsonResp().ok(commOrder);
    }

    @RequestMapping(value="/pay", method = RequestMethod.GET)
    public ModelAndView payCommOrder(String commOrderId, HttpServletRequest request){
        CommOrder commOrder = commOrderService.selectById(commOrderId);

        commOrder.setCommOrderId(commOrderId);
        commOrder.setStatus("payErr");//支付失败
        commOrder.setPaymentTime(new Date());
        commOrder.setUpdateTime(new Date());
        commOrder.setRealPayAmount(new BigDecimal("0.00"));
        boolean b = commOrderService.insertOrUpdate(commOrder);

        Random random = new Random();
        int time = random.nextInt(1000)+500;
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        request.getSession();
        return  new ModelAndView("redirect:/payErr.html");
    }
}
