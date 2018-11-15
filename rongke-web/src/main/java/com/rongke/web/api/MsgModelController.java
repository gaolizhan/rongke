package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.MsgModel;
import com.rongke.service.MsgModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @MsgModelController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/msgModel")
@Transactional
@CrossOrigin
public class MsgModelController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private MsgModelService msgModelService;

    /**
     * @添加
     * @param msgModel
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addMsgModel(@RequestBody MsgModel msgModel){
        log.debug("添加");
        msgModelService.insert(msgModel);
        return JsonResp.ok(msgModel);
    }

    /**
     * @修改
     * @param msgModel
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateMsgModel(@RequestBody MsgModel msgModel){
        log.debug("修改");
        msgModelService.updateById(msgModel);
        return JsonResp.ok(msgModel);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectMsgModel(Long id){
        log.debug("查找");
        MsgModel msgModel = msgModelService.selectById(id);
        return JsonResp.ok(msgModel);
    }

    /**
     * @根据类型查找
     * @param type
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOneByType", method = RequestMethod.GET)
    public JsonResp selectMsgModelByType(Integer type){
        log.debug("根据类型查找");
        EntityWrapper<MsgModel> wrapper = new EntityWrapper<>();
        wrapper.eq("type",type);
        MsgModel msgModel = msgModelService.selectOne(wrapper);
        return JsonResp.ok(msgModel);
    }


}
