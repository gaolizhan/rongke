package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.AboutXed;
import com.rongke.service.AboutXedService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version : Ver 1.0
 * @AboutXedController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/aboutXed")
@Transactional
@CrossOrigin
public class AboutXedController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AboutXedService aboutXedService;

    /**
     * @param aboutXed
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAboutXed(@RequestBody AboutXed aboutXed) {
        log.debug("添加");
        aboutXedService.insert(aboutXed);
        return JsonResp.ok(aboutXed);
    }

    /**
     * @param aboutXed
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAboutXed(@RequestBody AboutXed aboutXed) {
        log.debug("修改");
        aboutXedService.updateById(aboutXed);
        return JsonResp.ok(aboutXed);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectAboutXed(Long id) {
        log.debug("查找");
        AboutXed aboutXed = aboutXedService.selectById(id);
        return JsonResp.ok(aboutXed);
    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @关于小额贷
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public JsonResp selectAll(Long id) {
        log.debug("关于小额贷");
        EntityWrapper<AboutXed> ew = new EntityWrapper();
        AboutXed aboutXed = aboutXedService.selectOne(ew);
        return JsonResp.ok(aboutXed);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @关于我们详情
     */
    @RequestMapping(value = "/selectAboutUs", method = RequestMethod.GET)
    public JsonResp selectAboutUs(Long id) {
        log.debug("关于我们详情");
        EntityWrapper<AboutXed> ew = new EntityWrapper();
        AboutXed aboutXed = aboutXedService.selectOne(ew);
        return JsonResp.ok(aboutXed.getAboutUs());
    }


    /**
     * @根据类型查找
     * @param type
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOneByType", method = RequestMethod.GET)
    public JsonResp selectMsgModelByType(Integer type){
        log.debug("根据类型查找");
        AboutXed aboutXed=aboutXedService.selectById(1);
        return JsonResp.ok(aboutXed);
    }

}
