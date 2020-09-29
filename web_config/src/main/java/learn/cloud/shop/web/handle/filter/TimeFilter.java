package learn.cloud.shop.web.handle.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author FTSH
 */
public class TimeFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(TimeFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("TimeFilter init in");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("时间过滤器开始");
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		long end = System.currentTimeMillis();
		log.info("时间过滤器运行时间"+(end - start));
	}

	@Override
	public void destroy() {
		log.info("时间过滤器结束");
	}

}
