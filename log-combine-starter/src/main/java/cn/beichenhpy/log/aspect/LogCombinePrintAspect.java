package cn.beichenhpy.log.aspect;

import cn.beichenhpy.log.context.LogCombineContext;
import cn.beichenhpy.log.entity.LogInfo;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @After(value = "pointCut()")
    public void logPrint() {
        LogCombineContext context = LogCombineContext.getContext();
        LogInfo localLogStorage = context.getLogLocalStorage();
        try {
            logger.info(localLogStorage.getFormat(), localLogStorage.getParams());
        } catch (Throwable e) {
            logger.error("error:{},{}", e.getMessage(), e);
        } finally {
            context.clear();
        }
    }
}
