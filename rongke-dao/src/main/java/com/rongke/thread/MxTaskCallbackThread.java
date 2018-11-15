package com.rongke.thread;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.rongke.dto.MxDTO;
import com.rongke.enums.RiskRuleNo;
import com.rongke.model.User;
import com.rongke.model.UserAuth;
import com.rongke.model.UserPhone;
import com.rongke.service.*;
import com.rongke.utils.api.MoxieApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class MxTaskCallbackThread {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneService userPhoneService;

    public void processCallbackResults(MxDTO dto){
        log.info("处理回调结果,回调用户id:"+dto.getUser_id()+"=====================================");
        log.info(dto.toString());
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath = httpServletRequest.getServletContext().getRealPath("/");
        new Thread(new MxTaskCallbackWorker(dto,realPath)).start();
    }

    // 线程内部类，Thread或者Runnable均可
    private class MxTaskCallbackWorker implements Runnable {
        private MxDTO dto;
        private String realPath;

        public MxTaskCallbackWorker() {
        }

        public MxTaskCallbackWorker(MxDTO dto,String realPath) {
            this.dto =dto;
            this.realPath =realPath;
        }
        @Override
        public void run() {
            try {
                userPhoneService.mxCallbackRealWoker(dto,realPath);
            }catch (Throwable t){
                log.error("魔蝎回调异步处理异常",t);
            }
        }
    }
}
