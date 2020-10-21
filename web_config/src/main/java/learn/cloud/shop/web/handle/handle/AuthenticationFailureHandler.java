package learn.cloud.shop.web.handle.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import learn.cloud.shop.web.config.properties.LoginType;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;


/**
 * @author FTSH
 */
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	protected final Log logger = LogFactory.getLog(this.getClass());
	private boolean forwardToDestination = false;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private ProjectProperties properties;

	@Autowired
	private ObjectMapper objectMapper;

	public AuthenticationFailureHandler() {
	}

	public AuthenticationFailureHandler(String defaultFailureUrl) {
		this.setDefaultFailureUrl(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		System.out.println("失败处理器："+properties.getWeb().getLoginType());

		if (LoginType.JSON.equals(properties.getWeb().getLoginType())) {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write("失败处理器："+objectMapper.writeValueAsString(exception.getMessage()));
		} else {
			response.setCharacterEncoding("UTF-8");
			//response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()+"123");
			super.onAuthenticationFailure(request,response,exception);
		}
	}
}
