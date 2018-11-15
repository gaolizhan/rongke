package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.TextTemplate;
import com.rongke.service.TextTemplateService;

import com.rongke.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @TextTemplateController
 * @文本模板Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/textTemplate")
@Transactional
@CrossOrigin
public class TextTemplateController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TextTemplateService textTemplateService;

    /**
     * @添加文本模板
     * @param textTemplate
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addTextTemplate(@RequestBody TextTemplate textTemplate){
        log.debug("添加文本模板");
        textTemplateService.insert(textTemplate);
        return JsonResp.ok(textTemplate);
    }

    /**
     * @修改文本模板
     * @param textTemplate
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateTextTemplate(@RequestBody TextTemplate textTemplate){
        log.debug("修改文本模板");
        textTemplateService.updateById(textTemplate);
        return JsonResp.ok(textTemplate);
    }




    /**
     * @根据类型查找
     * @param type
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOneByType", method = RequestMethod.GET)
    public JsonResp selectMsgModelByType(Integer type , String title){
        log.debug("根据类型查找");
        EntityWrapper<TextTemplate> ew = new EntityWrapper();
        ew.eq("type",type );
        if (StringUtil.isNotEmpty(title)) {
            ew.eq("title",title );
        }
        List<TextTemplate> textTemplates = textTemplateService.selectList(ew);
        return JsonResp.ok(textTemplates);
    }
    /**
     * @根据id查找文本模板
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectTextTemplate(Long id){
        log.debug("查找文本模板");
        TextTemplate textTemplate = textTemplateService.selectById(id);
        return JsonResp.ok(textTemplate);
    }


}
