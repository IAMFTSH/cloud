package learn.cloud.shop.web.config.properties;

import lombok.Data;

@Data
public class WebProperties {
	private String loginPage = "/default-sign-in.html";
	private String loginProcessUrl = "/authentication/form";
	private LoginType loginType=LoginType.JSON;
}
