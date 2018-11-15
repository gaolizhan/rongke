package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.model.UserTongdunfen;
import com.rongke.service.UserTongdunfenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @UserTongdunfenController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/userTongdunfen")
@Transactional
@CrossOrigin
public class UserTongdunfenController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserTongdunfenService userTongdunfenService;

    /**
     * @添加
     * @param userTongdunfen
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addUserTongdunfen(@RequestBody UserTongdunfen userTongdunfen){
        log.debug("添加");
        userTongdunfenService.insert(userTongdunfen);
        return JsonResp.ok(userTongdunfen);
    }

    /**
     * @修改
     * @param userTongdunfen
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateUserTongdunfen(@RequestBody UserTongdunfen userTongdunfen){
        log.debug("修改");
        userTongdunfenService.updateById(userTongdunfen);
        return JsonResp.ok(userTongdunfen);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectUserTongdunfen(Long id){
        log.debug("查找");
        UserTongdunfen userTongdunfen = userTongdunfenService.selectById(id);
        return JsonResp.ok(userTongdunfen);
    }


    @RequestMapping(value="/findByUserPage", method = RequestMethod.GET)
    public JsonResp findByUserPage(Long userId,Integer pageNo,Integer pageSize ){
        log.debug("查询贷前审核记录");
        EntityWrapper<UserTongdunfen> entityWrapper = new EntityWrapper<>();
        Page page = new Page(pageNo,pageSize);
        entityWrapper.eq("user_id",userId);
        entityWrapper.orderBy("gmt_datetime",false);
        Page page1 = userTongdunfenService.selectPage(page,entityWrapper);

        return JsonResp.ok(new PageDto(pageNo,pageSize,page1.getRecords(),page1.getTotal()));
    }

}
