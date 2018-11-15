package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.mapper.RollingNewsMapper;
import com.rongke.model.RollingNews;
import com.rongke.service.RollingNewsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @RollingNewsController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/rollingNews")
@Transactional
@CrossOrigin
public class RollingNewsController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RollingNewsService rollingNewsService;

    /**
     * @添加
     * @param rollingNews
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addRollingNews(@RequestBody RollingNews rollingNews){
        log.debug("添加");
        rollingNewsService.insert(rollingNews);
        return JsonResp.ok(rollingNews);
    }

    /**
     * @修改
     * @param rollingNews
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateRollingNews(@RequestBody RollingNews rollingNews){
        log.debug("修改");
        rollingNewsService.updateById(rollingNews);
        return JsonResp.ok(rollingNews);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectRollingNews(Long id){
        log.debug("查找");
        RollingNews rollingNews = rollingNewsService.selectById(id);
        return JsonResp.ok(rollingNews);
    }


}
