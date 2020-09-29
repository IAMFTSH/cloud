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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //拦截
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setProperties(projectProperties);
        validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        validateCodeFilter.afterPropertiesSet();

        // 表单登录
        httpSecurity.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                // 没有登陆登录跳转请求
                .loginPage(projectProperties.getWeb().getLoginPage())
                // 处理表单登录请求url
                .loginProcessingUrl(projectProperties.getWeb().getLoginProcessUrl())
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()// 返回http对象
                // 后面都是对记住我功能的详细配置
                .rememberMe()
                // 使用mysql维持用户登录状态
                .tokenRepository(persistentTokenRepository())
                // token有效期
                .tokenValiditySeconds(projectProperties.getWeb().getTokenValiditySeconds())
                // userid使用什么做为登录的逻辑
                .userDetailsService(userDetailsService)
                //返回上一层
                .and()
                // 授权配置
                .authorizeRequests()
                // 无需认证
                .antMatchers(projectProperties.getWeb().getLoginPage(),projectProperties.getWeb().getLoginProcessUrl(), ProjectConstant.IMAGE_VALIDATE_CODE_CREATE_URL,"/css/**", "/users/authentication/require").permitAll()
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

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		//tokenRepositoryImpl.setCreateTableOnStartup(true);
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;

    }

}
