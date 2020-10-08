package learn.cloud.shop.web.config;


import com.alibaba.druid.support.http.WebStatFilter;
import learn.cloud.shop.web.handle.filter.TimeFilter;
import learn.cloud.shop.web.handle.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邝明山
 * on 2020/9/23 9:15
 */
@Configuration
public class WebRegister implements WebMvcConfigurer {

    @Autowired
    private TimeInterceptor timeInterceptor;

    //过滤器设置器
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();
/*        bean.setFilter(timeFilter);
        List<String> urls = new ArrayList<String>();
        urls.add("/test/*");
        bean.setUrlPatterns(urls);*/
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor).addPathPatterns("/test/**");
    }

}
