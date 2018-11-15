package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.ParamSetting;
import com.rongke.service.ParamSettingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ParamSettingController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/paramSetting")
@Transactional
@CrossOrigin
public class ParamSettingController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ParamSettingService paramSettingService;

    /**
     * @添加
     * @param paramSetting
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addParamSetting(@RequestBody ParamSetting paramSetting){
        log.debug("添加");
        EntityWrapper<ParamSetting>paramSettingEntityWrapper=new EntityWrapper<>();

        paramSettingEntityWrapper.eq("limit_days",paramSetting.getLimitDays());
        paramSettingEntityWrapper.in("status",new ArrayList<Integer>(){{add(1);add(2);}});
        List<ParamSetting> paramSettingList=paramSettingService.selectList(paramSettingEntityWrapper);
        if(paramSettingList.size()>0){
            return JsonResp.fa("这种贷款天数您已经发布过了哦！");
        }else {
            paramSettingService.insert(paramSetting);
            return JsonResp.ok(paramSetting);
        }
    }

    /**
     * @修改
     * @param paramSetting
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateParamSetting(@RequestBody ParamSetting paramSetting){
        log.debug("修改");
        paramSettingService.updateById(paramSetting);
        return JsonResp.ok(paramSetting);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectParamSetting(Long id){
        log.debug("查找");
        ParamSetting paramSetting = paramSettingService.selectById(id);
        return JsonResp.ok(paramSetting);
    }
    /**
     * @查询贷款类型列表
     * @param
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectAllType",method = RequestMethod.GET)
    public JsonResp selectParamSettingList(Page page,String status){

        EntityWrapper<ParamSetting> paramSettingEntityWrapper = new EntityWrapper<>();
        if(status!=null&&!"".equals(status)) {
            paramSettingEntityWrapper.eq("status",status);
        }

        paramSettingEntityWrapper.in("status",new ArrayList<Integer>(){{add(1);add(2);}});
        List<ParamSetting>paramSettingList=paramSettingService.selectList(paramSettingEntityWrapper);


        return JsonResp.ok(new PageDto(page.getCurrent(), page.getSize(), paramSettingList, paramSettingList.size()));

    }



    /**
     * @根据id删除
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value = "/deleteById",method = RequestMethod.GET)
    public JsonResp deleteById(Long id){

        boolean boo=paramSettingService.deleteById(id);

        return JsonResp.ok(boo);
    }
    /**
     * @param id
     * @return 返回值JsonResp
     * @删除贷款项目
     */
    @RequestMapping(value="/GoDeleteUpdate", method = RequestMethod.GET)
    public JsonResp GoDeleteUpdate(Long id){
       ParamSetting paramSetting=new ParamSetting();
         paramSetting.setId(id);
         paramSetting.setStatus(3);

         Boolean boo=paramSettingService.updateById(paramSetting);

         return JsonResp.ok(boo);
    }


    /**
     * @param id
     * @return 返回值JsonResp
     * @禁用贷款项目
     */
    @RequestMapping(value="/stopUsing", method = RequestMethod.GET)
    public JsonResp GoDeleteUpdate1(Long id){
        ParamSetting paramSetting=paramSettingService.selectById(id);
        if(paramSetting.getStatus().equals(3)) {
           return JsonResp.fa("操作不合法");
        }else {
            paramSetting.setId(id);
            paramSetting.setStatus(2);
            Boolean boo = paramSettingService.updateById(paramSetting);
            return JsonResp.ok(boo);
        }
    }

    /**
     * @查询所有列表
     * @param
     * @return 返回值JsonResp
     */
     @RequestMapping(value="/selectList",method = RequestMethod.GET)
    public JsonResp selectList() {
         EntityWrapper<ParamSetting> paramSettingEntityWrapper = new EntityWrapper<>();
         List<ParamSetting> paramSettingList = paramSettingService.selectList(paramSettingEntityWrapper);
         return JsonResp.ok(paramSettingList);
     }





}
