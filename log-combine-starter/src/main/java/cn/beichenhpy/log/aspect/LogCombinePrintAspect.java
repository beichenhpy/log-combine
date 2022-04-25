package cn.beichenhpy.log.aspect;

import cn.beichenhpy.log.context.LogCombineContext;
import cn.beichenhpy.log.entity.LogInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * 日志合并打印切面
 * CREATE_TIME: 2022/4/23 16:28
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Aspect
public class LogCombinePrintAspect {


    private static final Logger logger = LoggerFactory.getLogger("combine-log have generated");


    @Pointcut(value = "@annotation(cn.beichenhpy.log.annotation.LogCombine)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void preLog(JoinPoint joinPoint) {
        LogCombineContext context = LogCombineContext.getContext();
        LogInfo localLogStorage = context.getLogLocalStorage();
        if (localLogStorage == null) {
            //init
            localLogStorage = new LogInfo(new ArrayList<>(), 0);
        }
        //入栈
        localLogStorage.setNestedFloor(localLogStorage.getNestedFloor() + 1);
        context.setLogLocalStorage(localLogStorage);
    }

    @After(value = "pointCut()")
    public void logPrint(JoinPoint joinPoint) {
        String name = joinPoint.getTarget().getClass().getName();
        LogCombineContext context = LogCombineContext.getContext();
        LogInfo localLogStorage = context.getLogLocalStorage();
        try {
            //出栈
            localLogStorage.setNestedFloor(localLogStorage.getNestedFloor() - 1);
        } catch (Throwable e) {
            logger.error("error:{},{}", e.getMessage(), e);
        } finally {
            if (localLogStorage.getNestedFloor() == 0) {
                String realLog = String.join("", localLogStorage.getMessages());
                logger.info("{}", realLog);
                context.clear();
            }
        }
    }
}
