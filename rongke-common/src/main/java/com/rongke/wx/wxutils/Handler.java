package com.rongke.wx.wxutils;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Handler {

    @RequestMapping("/index_weixin.html")
    public ModelAndView getListUsersView() {
        ModelMap model = new ModelMap();
        //model.addAttribute("users", userService.getList());
        return new ModelAndView("user_list", model);
    }
}
