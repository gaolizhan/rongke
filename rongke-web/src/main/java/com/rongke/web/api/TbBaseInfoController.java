package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.TbBaseInfoMapper;
import com.rongke.model.TbBaseInfo;
import com.rongke.service.TbBaseInfoService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @TbBaseInfoController
 * @个人信息Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/tbBaseInfo")
@Transactional
@CrossOrigin
public class TbBaseInfoController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TbBaseInfoService tbBaseInfoService;

    /**
     * @添加个人信息
     * @param tbBaseInfo
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addTbBaseInfo(@RequestBody TbBaseInfo tbBaseInfo){
        log.debug("添加个人信息");
        tbBaseInfoService.insert(tbBaseInfo);
        return JsonResp.ok(tbBaseInfo);
    }

    /**
     * @修改个人信息
     * @param tbBaseInfo
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateTbBaseInfo(@RequestBody TbBaseInfo tbBaseInfo){
        log.debug("修改个人信息");
        tbBaseInfoService.updateById(tbBaseInfo);
        return JsonResp.ok(tbBaseInfo);
    }
    /**
     * @根据id查找个人信息
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectTbBaseInfo(Long id){
        log.debug("查找个人信息");
        TbBaseInfo tbBaseInfo = tbBaseInfoService.selectById(id);
        return JsonResp.ok(tbBaseInfo);
    }


}
