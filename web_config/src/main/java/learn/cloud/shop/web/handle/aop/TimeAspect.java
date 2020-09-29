package learn.cloud.shop.web.handle.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author FTSH
 */
@Component
@Aspect
public class TimeAspect {

	private static final Logger log = LoggerFactory.getLogger(TimeAspect.class);
	@Pointcut("execution(* learn.cloud.shop..*.controller.Test.*(..))")
	public void timePointCut() {

	}

	@Around("timePointCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {

		log.info("AOP的Aspect开始");
		long start = System.currentTimeMillis();

		Object result = pjp.proceed();

		long end = System.currentTimeMillis();
		log.info("AOP的Aspect运行"+(end - start));
		return result;
	}
	@After("timePointCut()")
	public void After() {
		log.info("AOP的Aspect最后通知");
	}
}
