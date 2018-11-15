package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.model.UserRiskRecord;
import com.rongke.service.UserRiskRecordService;
import com.rongke.service.UserRiskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/userRiskRecord")
@CrossOrigin
public class UserRiskRecordController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private UserRiskRecordService userRiskRecordService;

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    @RequestMapping(value="/selectPage", method = RequestMethod.GET)
    public JsonResp selectPage(Integer pageNo, Integer pageSize,String userId){
        EntityWrapper<UserRiskRecord> ew = new EntityWrapper<>();
        ew.eq("user_id",userId);
        Page<UserRiskRecord> userRiskRecordPage = userRiskRecordService.selectPage(new Page<UserRiskRecord>(pageNo, pageSize), ew);
        return JsonResp.ok(userRiskRecordPage);
    }

}
