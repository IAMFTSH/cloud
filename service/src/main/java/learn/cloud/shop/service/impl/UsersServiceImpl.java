package learn.cloud.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import learn.cloud.shop.pojo.Users;
import learn.cloud.shop.mapper.UsersMapper;
import learn.cloud.shop.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import learn.cloud.shop.util.RedisUtil;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邝明山
 * @since 2020-09-24
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService, UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        QueryWrapper<Users> wrapper=new QueryWrapper();
        wrapper.eq("username",s);
        Users  user= usersMapper.selectOne(wrapper);

        log.info("用户的id为{}，在数据库中找到对应的用户为{}", user.getUsername(),
                ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));
    }
    @Override
    public UserDetails loadUserByMobile(String s) throws UsernameNotFoundException{
        QueryWrapper<Users> wrapper=new QueryWrapper();
        wrapper.eq("mobile",s);
        Users  user= usersMapper.selectOne(wrapper);

        log.info("目前不清楚会不好有记住我功能，用户的id为{}，在数据库中找到对应的用户为{}", user.getUsername(),
                ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));
    }

    @Override
    public boolean sendSMS(String mobile) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G6u7HfYLHYjLw5x3jgb", "m8EuaQohDq1XaL66g9mNGdR5isbpA1");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", mobile);
        //签名
        request.putQueryParameter("SignName", "ABC商城");
        //模板
        request.putQueryParameter("TemplateCode", "SMS_204126876");
        //构建验证码
        HashMap<String, Object> TemplateParamMap = new HashMap<>();
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            //每次随机出一个数字（0-9）
            int r = random.nextInt(10);
            //把每次随机出的数字拼在一起
            code = code + r;
        }
        TemplateParamMap.put("code", code);
        redisUtil.setString("mobile"+mobile,code);
        //验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(TemplateParamMap));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

}
