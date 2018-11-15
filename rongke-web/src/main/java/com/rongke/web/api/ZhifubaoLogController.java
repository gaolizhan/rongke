package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.ZhifubaoLogMapper;
import com.rongke.model.ZhifubaoLog;
import com.rongke.service.ZhifubaoLogService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @ZhifubaoLogController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/zhifubaoLog")
@Transactional
@CrossOrigin
public class ZhifubaoLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ZhifubaoLogService zhifubaoLogService;

    /**
     * @添加
     * @param zhifubaoLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addZhifubaoLog(@RequestBody ZhifubaoLog zhifubaoLog){
        log.debug("添加");
        zhifubaoLogService.insert(zhifubaoLog);
        return JsonResp.ok(zhifubaoLog);
    }

    /**
     * @修改
     * @param zhifubaoLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateZhifubaoLog(@RequestBody ZhifubaoLog zhifubaoLog){
        log.debug("修改");
        zhifubaoLogService.updateById(zhifubaoLog);
        return JsonResp.ok(zhifubaoLog);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectZhifubaoLog(Long id){
        log.debug("查找");
        ZhifubaoLog zhifubaoLog = zhifubaoLogService.selectById(id);
        return JsonResp.ok(zhifubaoLog);
    }


}
