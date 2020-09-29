package learn.cloud.shop.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author FTSH
 */
@Configuration
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "learn.cloud")
public class ProjectProperties {

	private WebProperties web = new WebProperties();

	private ValidateCodeProperties validateCode=new ValidateCodeProperties();



}
