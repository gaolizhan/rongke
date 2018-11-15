package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.TbReceiverMapper;
import com.rongke.model.TbReceiver;
import com.rongke.service.TbReceiverService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @TbReceiverController
 * @淘宝收货地址Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/tbReceiver")
@Transactional
@CrossOrigin
public class TbReceiverController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TbReceiverService tbReceiverService;

    /**
     * @添加淘宝收货地址
     * @param tbReceiver
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addTbReceiver(@RequestBody TbReceiver tbReceiver){
        log.debug("添加淘宝收货地址");
        tbReceiverService.insert(tbReceiver);
        return JsonResp.ok(tbReceiver);
    }

    /**
     * @修改淘宝收货地址
     * @param tbReceiver
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateTbReceiver(@RequestBody TbReceiver tbReceiver){
        log.debug("修改淘宝收货地址");
        tbReceiverService.updateById(tbReceiver);
        return JsonResp.ok(tbReceiver);
    }
    /**
     * @根据id查找淘宝收货地址
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectTbReceiver(Long id){
        log.debug("查找淘宝收货地址");
        TbReceiver tbReceiver = tbReceiverService.selectById(id);
        return JsonResp.ok(tbReceiver);
    }


}
