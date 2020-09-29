package learn.cloud.shop.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author FTSH
 */
@SpringBootApplication
@ComponentScan(basePackages = {"learn.cloud.shop"})
//@ComponentScan("learn.cloud.shop.service")
//@ComponentScan("learn.cloud.shop.mapper")
//@ComponentScan("learn.cloud.shop.web.config")
//@MapperScan("learn.cloud.shop.mapper")
public class ShopApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShopApplication.class, args);
	}
}
