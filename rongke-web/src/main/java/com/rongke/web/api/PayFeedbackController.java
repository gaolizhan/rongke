package com.rongke.web.api;

import com.rongke.commons.JsonResp;
import com.rongke.model.PayFeedback;
import com.rongke.model.User;
import com.rongke.service.PayFeedbackService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @version : Ver 1.0
 * @PayFeedbackController
 * @Controller
 */
@RestController
@RequestMapping(value = "/api/payFeedback")
@Transactional
@CrossOrigin
public class PayFeedbackController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private PayFeedbackService payFeedbackService;
    @Autowired
    private UserService userService;

    /**
     * @param payFeedback
     * @return 返回值JsonResp
     * @添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResp addPayFeedback(@RequestBody PayFeedback payFeedback) {
        log.debug("添加");
        User user = userService.findLoginUser();
        payFeedback.setUserId(user.getId());
        payFeedbackService.insert(payFeedback);
        return JsonResp.ok(payFeedback);
    }

    /**
     * @param payFeedback
     * @return 返回值JsonResp
     * @修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResp updatePayFeedback(@RequestBody PayFeedback payFeedback) {
        log.debug("修改");
        payFeedbackService.updateById(payFeedback);
        return JsonResp.ok(payFeedback);
    }

    /**
     * @param id
     * @return 返回值JsonResp
     * @根据id查找
     */
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public JsonResp selectPayFeedback(Long id) {
        log.debug("查找");
        PayFeedback payFeedback = payFeedbackService.selectById(id);
        return JsonResp.ok(payFeedback);
    }


}
