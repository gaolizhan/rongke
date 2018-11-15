package com.rongke.web.api;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.RoleThirdCatalogue;
import com.rongke.model.ThirdCatalogue;
import com.rongke.service.RoleThirdCatalogueService;
import com.rongke.service.SecondCatalogueService;
import com.rongke.service.ThirdCatalogueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ThirdCatalogueController
 * @三级目录Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/thirdCatalogue")
@Transactional
@CrossOrigin
public class ThirdCatalogueController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ThirdCatalogueService thirdCatalogueService;
    @Autowired
    private SecondCatalogueService secondCatalogueService;
    @Autowired
    private RoleThirdCatalogueService roleThirdCatalogueService;


    /**
     * @添加三级目录
     * @param thirdCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addThirdCatalogue(@RequestBody ThirdCatalogue thirdCatalogue){
        log.debug("添加三级目录");
        thirdCatalogueService.insert(thirdCatalogue);
        return JsonResp.ok(thirdCatalogue);
    }

    /**
     * @修改三级目录
     * @param thirdCatalogue
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateThirdCatalogue(@RequestBody ThirdCatalogue thirdCatalogue){
        log.debug("修改三级目录");
        thirdCatalogueService.updateById(thirdCatalogue);
        return JsonResp.ok(thirdCatalogue);
    }
    /**
     * @根据id查找三级目录
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectThirdCatalogue(Long id){
        log.debug("查找三级目录");
        ThirdCatalogue thirdCatalogue = thirdCatalogueService.selectById(id);
        return JsonResp.ok(thirdCatalogue);
    }

    /**
     * @分页查询三级目录
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findByPage", method = RequestMethod.POST)
    public JsonResp findByPage(Integer pageNo,Integer pageSize,Long roleId,@RequestBody ThirdCatalogue catalogue){
        log.debug("分页查询三级目录");

        Map<String,Object> map = new HashMap<>();
        map.put("roleId",roleId);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("firstTitle",catalogue.getSecond().getFirst().getTitle());
        map.put("secondTitle",catalogue.getSecond().getTitle());
        map.put("thirdTitle",catalogue.getTitle());
        Integer total = thirdCatalogueService.findByPageCount(map);
        List<ThirdCatalogue> list = thirdCatalogueService.findByPage(map);
        return JsonResp.ok(new PageDto(pageNo,pageSize,list,total));
    }


    /**
     * @查询三级目录
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/findAll", method = RequestMethod.GET)
    public JsonResp findAll(Long roleId){
        log.debug("查询三级目录");
        List<ThirdCatalogue> list = thirdCatalogueService.findAll();
        List<ThirdCatalogue> list1 = thirdCatalogueService.findAllSecond();
        EntityWrapper<RoleThirdCatalogue> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("role_id",roleId);
        List<RoleThirdCatalogue> list2 = roleThirdCatalogueService.selectList(entityWrapper);
        StringBuffer thirdMenu = new StringBuffer("[");
        for (int j = 0; j < list1.size(); j++) {
            if (j != 0) {
                thirdMenu.append(",");
            }
            thirdMenu.append("{\"first\":{\"title\":\""+list1.get(j).getSecond().getFirst().getTitle()+"\",\"second\":{\"title\":\""+list1.get(j).getSecond().getTitle()+"\",\"third\":[");
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list1.get(j).getSecond().getTitle().equals(list.get(i).getSecond().getTitle())) {
                    if (num != 0) {
                        thirdMenu.append(",");
                    }
                    String text="{\"title\":\"" + list.get(i).getTitle() + "\",\"data\":" + list.get(i).getId() + ",\"select\":0}";
                    for (RoleThirdCatalogue role : list2) {
                        if (role.getThirdCatalogueId().longValue() == list.get(i).getId().longValue()) {
                            text = "{\"title\":\"" + list.get(i).getTitle() + "\",\"data\":" + list.get(i).getId() + ",\"select\":1}";
                            break;
                        }
                    }
                    thirdMenu.append(text);
                    num = 1;
                }
            }
            thirdMenu.append("]}}}");
        }
        thirdMenu.append("]");
        String jsonString = thirdMenu.toString();
        JSONArray json = JSONArray.parseArray(jsonString);
        return JsonResp.ok(json);
    }

}
