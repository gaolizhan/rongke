package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.FirstCatalogue;
import com.rongke.service.FirstCatalogueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @FirstCatalogueController
 * @一级目录Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/firstCatalogue")
@Transactional
@CrossOrigin
public class FirstCatalogueController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private FirstCatalogueService firstCatalogueService;

    /**
     * @添加一级目录
     * @param firstCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addFirstCatalogue(@RequestBody FirstCatalogue firstCatalogue){
        log.debug("添加一级目录");
        firstCatalogueService.insert(firstCatalogue);
        return JsonResp.ok(firstCatalogue);
    }

    /**
     * @修改一级目录
     * @param firstCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateFirstCatalogue(@RequestBody FirstCatalogue firstCatalogue){
        log.debug("修改一级目录");
        firstCatalogueService.updateById(firstCatalogue);
        return JsonResp.ok(firstCatalogue);
    }
    /**
     * @根据id查找一级目录
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectFirstCatalogue(Long id){
        log.debug("查找一级目录");
        FirstCatalogue firstCatalogue = firstCatalogueService.selectById(id);
        return JsonResp.ok(firstCatalogue);
    }

    /**
     * @查找全部一级目录
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectAll", method = RequestMethod.GET)
    public JsonResp findAll(){
        log.debug("查找全部一级目录");
        EntityWrapper<FirstCatalogue> wrapper = new EntityWrapper<>();
        List<FirstCatalogue> list = firstCatalogueService.selectList(wrapper);
        return JsonResp.ok(list);
    }


}
