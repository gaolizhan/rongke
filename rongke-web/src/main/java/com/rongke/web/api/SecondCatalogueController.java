package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.FirstCatalogue;
import com.rongke.model.SecondCatalogue;
import com.rongke.service.FirstCatalogueService;
import com.rongke.service.SecondCatalogueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @SecondCatalogueController
 * @二级目录Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/secondCatalogue")
@Transactional
@CrossOrigin
public class SecondCatalogueController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SecondCatalogueService secondCatalogueService;
    @Autowired
    private FirstCatalogueService firstCatalogueService;

    /**
     * @添加二级目录
     * @param secondCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addSecondCatalogue(@RequestBody SecondCatalogue secondCatalogue){
        log.debug("添加二级目录");
        secondCatalogueService.insert(secondCatalogue);
        return JsonResp.ok(secondCatalogue);
    }

    /**
     * @修改二级目录
     * @param secondCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateSecondCatalogue(@RequestBody SecondCatalogue secondCatalogue){
        log.debug("修改二级目录");
        secondCatalogueService.updateById(secondCatalogue);
        return JsonResp.ok(secondCatalogue);
    }
    /**
     * @根据id查找二级目录
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectSecondCatalogue(Long id){
        log.debug("查找二级目录");
        SecondCatalogue secondCatalogue = secondCatalogueService.selectById(id);
        return JsonResp.ok(secondCatalogue);
    }


    /**
     * @根据一级查找全部二级目录
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectAll", method = RequestMethod.GET)
    public JsonResp findAll(Long firstId,String name){
        log.debug("根据一级查找全部二级目录");
        if (null != name) {
            EntityWrapper<FirstCatalogue> wrapper = new EntityWrapper<>();
            wrapper.eq("title",name);
            FirstCatalogue firstCatalogue = firstCatalogueService.selectOne(wrapper);
            firstId = firstCatalogue.getId();
        }
        EntityWrapper<SecondCatalogue> wrapper = new EntityWrapper<>();
        wrapper.eq("first_id",firstId);
        List<SecondCatalogue> list = secondCatalogueService.selectList(wrapper);
        return JsonResp.ok(list);
    }
}
