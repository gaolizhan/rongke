package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.AddressLogMapper;
import com.rongke.model.AddressLog;
import com.rongke.service.AddressLogService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @version : Ver 1.0
 * @AddressLogController
 * @地址信息Controller
 */
@RestController
@RequestMapping(value = "/api/addressLog")
@Transactional
@CrossOrigin
public class AddressLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AddressLogService addressLogService;

    /**
     * @param addressLog
     * @return 返回值JsonResp
     * @添加地址信息
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAddressLog(@RequestBody AddressLog addressLog) {
        log.debug("添加地址信息");
        addressLogService.insert(addressLog);
        return JsonResp.ok(addressLog);
    }

    /**
     * @param addressLog
     * @return 返回值JsonResp
     * @修改地址信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAddressLog(@RequestBody AddressLog addressLog) {
        log.debug("修改地址信息");
        addressLogService.updateById(addressLog);
        return JsonResp.ok(addressLog);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找地址信息
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAddressLog(Long id) {
        log.debug("查找地址信息");
        AddressLog addressLog = addressLogService.selectById(id);
        return JsonResp.ok(addressLog);
    }


}
