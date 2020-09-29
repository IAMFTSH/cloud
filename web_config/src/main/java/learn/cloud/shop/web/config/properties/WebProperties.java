package learn.cloud.shop.web.config.properties;

import lombok.Data;

/**
 * @author FTSH
 */
@Data
public class WebProperties {
	private String loginPage = "/default-sign-in.html";
	private String loginProcessUrl = "/login";
	private LoginType loginType=LoginType.JSON;
	private int tokenValiditySeconds=3600*24*7;
}
