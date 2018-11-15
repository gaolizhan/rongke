package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.EachPicture;
import com.rongke.service.EachPictureService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version : Ver 1.0
 * @EachPictureController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/eachPicture")
@Transactional
@CrossOrigin
public class EachPictureController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private EachPictureService eachPictureService;

    /**
     * @param eachPicture
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addEachPicture(@RequestBody EachPicture eachPicture) {
        log.debug("添加");
        eachPictureService.insert(eachPicture);
        return JsonResp.ok(eachPicture);
    }
//
//    /**
//     * @添加广告
//     * @param brand
//     * @return 返回值JsonResp
//     */
//    @RequestMapping(value="/add", method = RequestMethod.POST)
//    public JsonResp addBrand(@RequestBody EachPicture eachPicture){
//        log.debug("添加品牌");
//        EntityWrapper<EachPicture> ewS = new EntityWrapper<>();
//        ewS.eq("id",eachPicture.getId());
//        ShopInfo shopInfo = shopInfoService.selectOne(ewS);
//        brand.setSellerId(shopInfo.getUserId());
//        brand.setStatus(BrandStatusEnum.SQZ.getType());
//        brandService.insert(brand);
//        return JsonResp.ok(brand);
//    }


    /**
     * @param eachPicture
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateEachPicture(@RequestBody EachPicture eachPicture) {
        log.debug("修改");
        eachPictureService.updateById(eachPicture);
        return JsonResp.ok(eachPicture);
    }


    /**
     * @param page,id
     * @return 返回值JsonResp
     * @查找所有广告列表
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public JsonResp selectAll(Page page, Long id) {
        EntityWrapper<EachPicture> eachPictureEntityWrapper = new EntityWrapper<>();
        if (id != null && !"".equals(id)) {
            eachPictureEntityWrapper.eq("id", id);
        }
        List<EachPicture> eachPictureList = eachPictureService.selectList(eachPictureEntityWrapper);
        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), eachPictureList, eachPictureList.size()));
    }


    /**
     * @param 
     * @return 返回值JsonResp
     * @id查询
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectOne(Long id) {
        EachPicture eachPicture = eachPictureService.selectById(id);
        return JsonResp.ok(eachPicture);
    }

    /**
     * @param
     * @return 返回值JsonResp
     * @id查询
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public JsonResp selectList() {
        EntityWrapper<EachPicture> ew = new EntityWrapper<>();
        List<EachPicture> eachPictureList = eachPictureService.selectList(ew);
        return JsonResp.ok(eachPictureList);

    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @id删除广告
     */
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)

    public JsonResp deleteOne(Long id) {

        boolean bo = eachPictureService.deleteById(id);

        return JsonResp.ok(bo);


    }


}