package com.rongke.web.aspect;

import com.rongke.annotation.SystemLog;
import com.rongke.commons.FailException;
import com.rongke.commons.JsonCodeEnum;
import com.rongke.model.Admin;
import com.rongke.model.AdminOperationLog;
import com.rongke.service.AdminOperationLogService;
import com.rongke.service.UserService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 日志记录AOP实现
 * Created by Administrator on 2017/3/26.
 */
@Aspect
@Component
public class AdminOperationLogAspect {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private AdminOperationLogService adminOperationLogService;

    @Pointcut("@annotation(com.rongke.annotation.SystemLog)")
    public void operationLogAspect(){}

    @Before("operationLogAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String description = getControllerMethodDescription(joinPoint);

            String url = "http://" + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort()           //端口号
                    + request.getContextPath()      //项目名称
                    + request.getServletPath();

            String params = "";
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < joinPoint.getArgs().length; i++) {
                    params += joinPoint.getArgs()[i] + ";";
                }
            }
            Admin admin = userService.getThisLogin();
            if(admin != null){
                AdminOperationLog adminOperationLog = new AdminOperationLog();
                adminOperationLog.setAdminId(admin.getId());
                adminOperationLog.setCreateTime(new Date());
                adminOperationLog.setDescription(description);
                adminOperationLog.setParams(params);
                adminOperationLog.setRequestUrl(url);
                adminOperationLogService.insert(adminOperationLog);
            }
        } catch (Exception e) {
            logger.error(e);
            throw new FailException(JsonCodeEnum.OVERTIME.getMessage());
        }
    }

    private static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}
