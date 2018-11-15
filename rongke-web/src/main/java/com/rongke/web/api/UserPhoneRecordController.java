package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.UserPhoneRecord;
import com.rongke.service.UserPhoneRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version : Ver 1.0
 * @UserPhoneRecordController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/userPhoneRecord")
@Transactional
@CrossOrigin
public class UserPhoneRecordController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneRecordService userPhoneRecordService;

    /**
     * @param userPhoneRecord
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addUserPhoneRecord(@RequestBody UserPhoneRecord userPhoneRecord) {
        log.debug("添加");
        userPhoneRecordService.insert(userPhoneRecord);
        return JsonResp.ok(userPhoneRecord);
    }

    /**
     * @param userPhoneRecord
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updateUserPhoneRecord(@RequestBody UserPhoneRecord userPhoneRecord) {
        log.debug("修改");
        userPhoneRecordService.updateById(userPhoneRecord);
        return JsonResp.ok(userPhoneRecord);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserPhoneRecord(Long id) {
        log.debug("查找");
        UserPhoneRecord userPhoneRecord = userPhoneRecordService.selectById(id);
        return JsonResp.ok(userPhoneRecord);
    }


    /**
     * @param
     * @return 返回值JsonResp
     * @分页查询通话记录
     */
    @RequestMapping(value = "/findByUserIdPage", method = RequestMethod.GET)
    public JsonResp findByUserIdPage(Integer pageNo, Integer pageSize,Long userId,String phoneNo){
        log.debug("分页查询通话记录");
        pageSize=50;
        Page page = new Page(pageNo,pageSize);
        EntityWrapper<UserPhoneRecord> wrapper = new EntityWrapper();
        if(userId!=null&&!"".equals(userId)) {
            wrapper.eq("user_id", userId);
        }
        wrapper.orderBy("conn_times",false);
        if(phoneNo!=null&&!"".equals(phoneNo)) {
            wrapper.like("phone_no", phoneNo);
        }
        wrapper.orderBy("start_time",false);
       Page page1 = userPhoneRecordService.selectPage(page,wrapper);
        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }



}
