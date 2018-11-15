package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.mapper.PayOrderConfirmMapper;
import com.rongke.model.PayOrderConfirm;
import com.rongke.service.PayOrderConfirmService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @PayOrderConfirmController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/payOrderConfirm")
@Transactional
@CrossOrigin
public class PayOrderConfirmController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PayOrderConfirmService payOrderConfirmService;

    /**
     * @添加
     * @param payOrderConfirm
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addPayOrderConfirm(@RequestBody PayOrderConfirm payOrderConfirm){
        log.debug("添加");
        payOrderConfirmService.insert(payOrderConfirm);
        return JsonResp.ok(payOrderConfirm);
    }

    /**
     * @修改
     * @param payOrderConfirm
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updatePayOrderConfirm(@RequestBody PayOrderConfirm payOrderConfirm){
        log.debug("修改");
        payOrderConfirmService.updateById(payOrderConfirm);
        return JsonResp.ok(payOrderConfirm);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectPayOrderConfirm(Long id){
        log.debug("查找");
        PayOrderConfirm payOrderConfirm = payOrderConfirmService.selectById(id);
        return JsonResp.ok(payOrderConfirm);
    }

    /**
     * 分页查询疑似重复订单
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectByPage",method = RequestMethod.GET)
    public JsonResp selectByPage(Integer pageNo, Integer pageSize){
        EntityWrapper<PayOrderConfirm>payOrderConfirmEntityWrapper=new EntityWrapper<>();
        payOrderConfirmEntityWrapper.eq("status",0);
        Page page=payOrderConfirmService.selectPage(new Page(pageNo,pageSize),payOrderConfirmEntityWrapper);
        return  JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));

    }

    /**
     * 确认已经打过款
     * @param id
     * @return
     */
    @RequestMapping(value = "/refusePassMoneyAgain",method = RequestMethod.GET)
    public JsonResp refusePassMoneyAgain(Long id){
        PayOrderConfirm payOrderConfirm=payOrderConfirmService.selectById(id);
        payOrderConfirm.setStatus(2);
        Boolean bool=payOrderConfirmService.updateById(payOrderConfirm);
        return JsonResp.ok(bool);
    }



}
