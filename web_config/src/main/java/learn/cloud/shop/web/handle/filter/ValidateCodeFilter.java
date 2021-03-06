package learn.cloud.shop.web.handle.filter;


import learn.cloud.shop.response.ImageValidateCode;
import learn.cloud.shop.response.ProjectConstant;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import learn.cloud.shop.web.handle.handle.exception.ValidateCodeException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 验证码的验证逻辑，不光是拦截登录请求
 * 
 * @author jsjxy
 *
 */
@Data
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	private ProjectProperties properties;
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private Set<String> urls = new HashSet<String>();
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	private AuthenticationFailureHandler authenticationFailureHandler;

	/**
	 * 这里是设置过滤的url，下面代码设置了登录请求的url，而且还是重复的（老师说是方便人理解）
	 * @throws ServletException
	 */
	@Override
	public void afterPropertiesSet() throws ServletException {

		super.afterPropertiesSet();
		String[] urlsConfig = StringUtils.splitByWholeSeparatorPreserveAllTokens(
				properties.getValidateCode().getImageValidateCodeProcessUrls(), ",");
		System.out.println("urlsConfig = " + urlsConfig);
		for (String url : urlsConfig) {
			urls.add(url);
		}
		urls.add(ProjectConstant.LOGIN_PROCESSING_URL);
		System.out.println("urls"+urls);
	}

	/**
	 * 匹配地址，如果地址匹配成功，启动过滤
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean action = false;

		for (String url : urls) {
			if (antPathMatcher.match(url, request.getRequestURI())) {
				action = true;
			}
		}

		if (action) {

			try {
				validate(new ServletWebRequest(request, response));
			} catch (AuthenticationException e) {
				//如果匹配失败，抛出异常，调用失败处理器
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return;
			}
			//放行
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(request, response);
		}

	}

	public void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
		//取出生成验证码时，存的数字
		ImageValidateCode codeInSession = (ImageValidateCode) sessionStrategy.getAttribute(servletWebRequest,
				ProjectConstant.VALIDATE_CODE_KEY_IN_SESSION);

		String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
				ProjectConstant.VALIDATE_CODE_KEY_IN_SESSION);

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码为空，请输入验证码");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}

		if (codeInSession.isExpired()) {
			throw new ValidateCodeException("验证码已过期");
		}

		if (!StringUtils.equals(codeInRequest, codeInSession.getCode())) {
			throw new ValidateCodeException("验证码不匹配");
		}
		//删除验证码   如何让验证码过期呢
		sessionStrategy.removeAttribute(servletWebRequest, ProjectConstant.VALIDATE_CODE_KEY_IN_SESSION);
	}

}
