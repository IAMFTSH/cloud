package learn.cloud.shop.web.config;

import learn.cloud.shop.response.ProjectConstant;
import learn.cloud.shop.service.UsersService;
import learn.cloud.shop.util.RedisUtil;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import learn.cloud.shop.web.handle.authentication.SmsAuthenticationProcessingFilter;
import learn.cloud.shop.web.handle.authentication.SmsAuthenticationProvider;
import learn.cloud.shop.web.handle.filter.SmsCodeFilter;
import learn.cloud.shop.web.handle.handle.AuthenticationFailureHandler;
import learn.cloud.shop.web.handle.handle.AuthenticationSuccessHandler;
import learn.cloud.shop.web.handle.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private ProjectProperties projectProperties;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        SmsAuthenticationProcessingFilter smsAuthenticationProcessingFilter = new SmsAuthenticationProcessingFilter();
        smsAuthenticationProcessingFilter.setAuthenticationManager(super.authenticationManager());
        smsAuthenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsAuthenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        //拦截
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setProperties(projectProperties);
        validateCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setProperties(projectProperties);
        smsCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        smsCodeFilter.setRedisUtil(redisUtil);
        smsCodeFilter.afterPropertiesSet();

        // 表单登录
        httpSecurity
                // 添加短信登录的provider到springsecurity容器中
                // .authenticationProvider(smsAuthenticationProvider())
                //短信验证加入过滤链中
                .addFilterAfter(smsAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                // 验证码验证过滤器在用户名密码登录之前
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
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
                .antMatchers(ProjectConstant.SMS_LOGIN_PROCESSING_URL, projectProperties.getWeb().getLoginPage(), projectProperties.getWeb().getLoginProcessUrl(), ProjectConstant.IMAGE_VALIDATE_CODE_CREATE_URL, "/css/**", "/users/authentication/require").permitAll()
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUsersService(usersService);
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                //添加自定义的认证管理类
                .authenticationProvider(smsAuthenticationProvider);
    }

    //加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
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
