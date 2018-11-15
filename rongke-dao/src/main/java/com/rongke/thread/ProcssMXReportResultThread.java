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
public class ProcssMXReportResultThread {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserPhoneService userPhoneService;

    public void processCallbackResults(MxDTO dto){
        log.info("处理报告回调结果,回调用户id:"+dto.getUser_id()+"=====================================");
        log.info(dto.toString());
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath = httpServletRequest.getServletContext().getRealPath("/");
        new Thread(new ProcssMXReportResultThread.ProcssMXReportWorker(dto,realPath)).start();
    }

    // 线程内部类，Thread或者Runnable均可
    private class ProcssMXReportWorker implements Runnable {
        private MxDTO dto;
        private String realPath;

        public ProcssMXReportWorker() {
        }

        public ProcssMXReportWorker(MxDTO dto,String realPath) {
            this.dto =dto;
            this.realPath =realPath;
        }
        @Override
        public void run() {
            try {
                userPhoneService.mxReportWorker(dto,realPath);
            }catch (Throwable t){
                log.error("魔蝎回调异步处理异常",t);
            }
        }
    }
}
