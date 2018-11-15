package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.TbLogMapper;
import com.rongke.model.TbLog;
import com.rongke.service.TbLogService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @TbLogController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/tbLog")
@Transactional
@CrossOrigin
public class TbLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TbLogService tbLogService;

    /**
     * @添加
     * @param tbLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addTbLog(@RequestBody TbLog tbLog){
        log.debug("添加");
        tbLogService.insert(tbLog);
        return JsonResp.ok(tbLog);
    }

    /**
     * @修改
     * @param tbLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateTbLog(@RequestBody TbLog tbLog){
        log.debug("修改");
        tbLogService.updateById(tbLog);
        return JsonResp.ok(tbLog);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectTbLog(Long id){
        log.debug("查找");
        TbLog tbLog = tbLogService.selectById(id);
        return JsonResp.ok(tbLog);
    }


}
