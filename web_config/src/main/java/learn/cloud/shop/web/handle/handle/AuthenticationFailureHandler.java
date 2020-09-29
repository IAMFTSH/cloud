package learn.cloud.shop.web.handle.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learn.cloud.shop.web.config.properties.LoginType;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private ProjectProperties properties;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		System.out.println("失败处理器："+properties.getWeb().getLoginType());

		if (LoginType.JSON.equals(properties.getWeb().getLoginType())) {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write("失败处理器："+objectMapper.writeValueAsString(exception));

		} else {
			response.setCharacterEncoding("UTF-8");
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}
