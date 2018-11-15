package com.rongke.thread;

import com.rongke.dto.MxDTO;
import com.rongke.service.UserPhoneService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProcssMXBillNoticeResultThread {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneService userPhoneService;

    public void processCallbackResults(MxDTO dto){
        log.info("处理通知回调结果,回调用户id:"+dto.getUser_id()+"=====================================");
        log.info(dto.toString());
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath = httpServletRequest.getServletContext().getRealPath("/");
        new Thread(new ProcssMXBillNoticeResultThread.ProcssMXBillNoticeWorker(dto,realPath)).start();
    }

    // 线程内部类，Thread或者Runnable均可
    private class ProcssMXBillNoticeWorker implements Runnable {
        private MxDTO dto;
        private String realPath;

        public ProcssMXBillNoticeWorker() {
        }

        public ProcssMXBillNoticeWorker(MxDTO dto,String realPath) {
            this.dto =dto;
            this.realPath =realPath;
        }
        @Override
        public void run() {
            try {
                userPhoneService.mxBillNoticeWorker(dto,realPath);
            }catch (Throwable t){
                log.error("魔蝎回调异步处理异常",t);
            }
        }
    }
}
