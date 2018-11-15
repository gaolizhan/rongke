package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.PressRecordsMapper;
import com.rongke.model.PressRecords;
import com.rongke.service.PressRecordsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @PressRecordsController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/pressRecords")
@Transactional
@CrossOrigin
public class PressRecordsController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PressRecordsService pressRecordsService;

    /**
     * @添加
     * @param pressRecords
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addPressRecords(@RequestBody PressRecords pressRecords){
        log.debug("添加");
        pressRecordsService.insert(pressRecords);
        return JsonResp.ok(pressRecords);
    }

    /**
     * @修改
     * @param pressRecords
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updatePressRecords(@RequestBody PressRecords pressRecords){
        log.debug("修改");
        pressRecordsService.updateById(pressRecords);
        return JsonResp.ok(pressRecords);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectPressRecords(Long id){
        log.debug("查找");
        PressRecords pressRecords = pressRecordsService.selectById(id);
        return JsonResp.ok(pressRecords);
    }


}
