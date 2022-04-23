package cn.beichenhpy.log.aspect;

import cn.beichenhpy.log.context.LogCombineContext;
import cn.beichenhpy.log.entity.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 日志合并打印切面
 * CREATE_TIME: 2022/4/23 16:28
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Aspect
@Slf4j
public class LogCombinePrintAspect {


    @Pointcut(value = "@annotation(cn.beichenhpy.log.annotation.LogCombine)")
    public void pointCut() {

    }

    @After(value = "pointCut()")
    public void logPrint() {
        LogCombineContext context = LogCombineContext.getContext();
        try {
            LogInfo localLogStorage = context.getLogLocalStorage();
            log.info("[combine-log have generate]" + localLogStorage.getFormat(), localLogStorage.getParams());
        } catch (Throwable e) {
            log.error("error:{},{}", e.getMessage(), e);
        } finally {
            context.clear();
        }

    }
}
