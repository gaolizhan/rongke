package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.TabaoLogMapper;
import com.rongke.model.TabaoLog;
import com.rongke.service.TabaoLogService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @TabaoLogController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/tabaoLog")
@Transactional
@CrossOrigin
public class TabaoLogController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TabaoLogService tabaoLogService;

    /**
     * @添加
     * @param tabaoLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addTabaoLog(@RequestBody TabaoLog tabaoLog){
        log.debug("添加");
        tabaoLogService.insert(tabaoLog);
        return JsonResp.ok(tabaoLog);
    }

    /**
     * @修改
     * @param tabaoLog
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateTabaoLog(@RequestBody TabaoLog tabaoLog){
        log.debug("修改");
        tabaoLogService.updateById(tabaoLog);
        return JsonResp.ok(tabaoLog);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectTabaoLog(Long id){
        log.debug("查找");
        TabaoLog tabaoLog = tabaoLogService.selectById(id);
        return JsonResp.ok(tabaoLog);
    }


}
