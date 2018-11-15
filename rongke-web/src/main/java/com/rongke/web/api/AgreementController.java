package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.commons.JsonResp;
import com.rongke.model.AboutXed;
import com.rongke.model.Agreement;
import com.rongke.service.AboutXedService;
import com.rongke.service.AgreementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version : Ver 1.0
 * @AgreementController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/agreement")
@Transactional
@CrossOrigin
public class AgreementController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private AgreementService agreementService;
    @Autowired
    private AboutXedService aboutXedService;

    /**
     * @param agreement
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addAgreement(@RequestBody Agreement agreement) {
        log.debug("添加");
        agreementService.insert(agreement);
        return JsonResp.ok(agreement);
    }

    /**
     * @param agreement
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateAgreement(@RequestBody Agreement agreement) {
        log.debug("修改");
        agreementService.updateById(agreement);
        return JsonResp.ok(agreement);
    }

    /**
     * @根据类型查找
     * @param type
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOneByType", method = RequestMethod.GET)
    public JsonResp selectMsgModelByType(Integer type){
        log.debug("根据类型查找");
        EntityWrapper<Agreement> ew = new EntityWrapper();
        ew.eq("type",type);
        Agreement agreement = agreementService.selectOne(ew);
        return JsonResp.ok(agreement);
    }






}
