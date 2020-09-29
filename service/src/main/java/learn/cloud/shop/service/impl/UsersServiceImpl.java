package learn.cloud.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.jdbc.Wrapper;
import learn.cloud.shop.mapper.UserMapper;
import learn.cloud.shop.pojo.Users;
import learn.cloud.shop.mapper.UsersMapper;
import learn.cloud.shop.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.Map;

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
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        QueryWrapper<Users> wrapper=new QueryWrapper();
        wrapper.eq("username","123");
        Users  user= usersMapper.selectOne(wrapper);

        log.info("用户的id为{}，在数据库中找到对应的用户为{}", user.getUsername(),
                ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));
    }

}
