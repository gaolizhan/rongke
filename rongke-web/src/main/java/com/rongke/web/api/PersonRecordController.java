package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.model.PersonRecord;
import com.rongke.service.PersonRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version : Ver 1.0
 * @PersonRecordController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/personRecord")
@Transactional
@CrossOrigin
public class PersonRecordController {
    private Logger log = Logger.getLogger(this.getClass());



}
