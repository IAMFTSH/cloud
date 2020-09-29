package learn.cloud.shop.web.handle.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learn.cloud.shop.web.config.properties.LoginType;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ProjectProperties properties;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		System.out.println(LoginType.JSON);
		System.out.println(properties.getWeb().getLoginType());
		if (LoginType.JSON.equals(properties.getWeb().getLoginType())) {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write("成功处理器："+objectMapper.writeValueAsString(authentication));
		} else if (LoginType.REDIREACT.equals(properties.getWeb().getLoginType())){
			response.setCharacterEncoding("UTF-8");
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}
