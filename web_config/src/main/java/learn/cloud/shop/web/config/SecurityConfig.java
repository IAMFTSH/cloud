package learn.cloud.shop.web.config;

import learn.cloud.shop.response.ProjectConstant;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import learn.cloud.shop.web.handle.handle.AuthenticationFailureHandler;
import learn.cloud.shop.web.handle.handle.AuthenticationSuccessHandler;
import learn.cloud.shop.web.handle.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author FTSH
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    ProjectProperties projectProperties;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //拦截
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setProperties(projectProperties);
        validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        validateCodeFilter.afterPropertiesSet();

        // 表单登录
        httpSecurity.formLogin()
                // 没有登陆登录跳转请求
                .loginPage("/users/authentication/require")
                // 处理表单登录请求url
                .loginProcessingUrl("/login")
                .failureUrl("/errorOfPassword.html")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                //返回上一层
                .and()
                // 授权配置
                .authorizeRequests()
                // 无需认证
                .antMatchers("/errorOfPassword.html",projectProperties.getWeb().getLoginPage(), ProjectConstant.IMAGE_VALIDATE_CODE_CREATE_URL,"/css/**", "/users/authentication/require").permitAll()
                // 所有请求
                .anyRequest()
                // 都需要认证
                .authenticated()
                .and().
                //攻击防护
                csrf().
                // 配置csrf攻击
                disable();
    }

    //加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }*/
}
