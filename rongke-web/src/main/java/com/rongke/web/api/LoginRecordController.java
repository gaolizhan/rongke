package com.rongke.web.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.rongke.commons.JsonResp;
import com.rongke.commons.PageDto;
import com.rongke.mapper.LoginRecordMapper;
import com.rongke.model.LoginRecord;
import com.rongke.model.User;
import com.rongke.model.UserLoginLog;
import com.rongke.service.LoginRecordService;

import com.rongke.service.UserLoginLogService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @LoginRecordController
 * @Controller
 * @version : Ver 1.0
 */
@RestController
@RequestMapping(value="/api/loginRecord")
@Transactional
@CrossOrigin
public class LoginRecordController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginLogService userLoginLogService;

    /**
     * @添加
     * @param loginRecord
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public JsonResp addLoginRecord(@RequestBody LoginRecord loginRecord){
        log.debug("添加");
        loginRecordService.insert(loginRecord);
        return JsonResp.ok(loginRecord);
    }

    /**
     * @修改
     * @param loginRecord
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public JsonResp updateLoginRecord(@RequestBody LoginRecord loginRecord){
        log.debug("修改");
        loginRecordService.updateById(loginRecord);
        return JsonResp.ok(loginRecord);
    }
    /**
     * @根据id查找
     * @param id
     * @return 返回值JsonResp
     */
    @RequestMapping(value="/selectOne", method = RequestMethod.GET)
    public JsonResp selectLoginRecord(Long id){
        log.debug("查找");
        LoginRecord loginRecord = loginRecordService.selectById(id);
        return JsonResp.ok(loginRecord);
    }

    /**
     * 查询所有的后台登录日志
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectAdminLoginRecords",method = RequestMethod.GET)
    public JsonResp selectAdminLoginRecords(Integer pageNo, Integer pageSize){

        Page page=loginRecordService.selectPage(new Page(pageNo,pageSize));

        return JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));

    }

    /**
     * 根据id删除一条日志
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteOne",method = RequestMethod.GET)
    public JsonResp deleteOne(Long id){
        Boolean bool=loginRecordService.deleteById(id);
        return  JsonResp.ok(bool);
    }

    /**
     * 根据电话号码查询登陆信息
     * @param phoneNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectByPhone",method = RequestMethod.GET)
    public JsonResp selectByPhone(String phoneNumber,Integer pageNo, Integer pageSize){
        EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
        if(phoneNumber!=null&&!"".equals(phoneNumber)){
            userEntityWrapper.eq("phone",phoneNumber);
        }
        User user=userService.selectOne(userEntityWrapper);

        EntityWrapper<UserLoginLog>userLoginLogEntityWrapper=new EntityWrapper<>();
          userLoginLogEntityWrapper.eq("user_id",user.getId());
        Page page=userLoginLogService.selectPage(new Page(pageNo,pageSize),userLoginLogEntityWrapper);

        return  JsonResp.ok(new PageDto(pageNo,pageSize,page.getRecords(),page.getTotal()));

    }


}
