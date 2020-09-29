package learn.cloud.shop.service;

import learn.cloud.shop.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邝明山
 * @since 2020-09-24
 */
public interface UsersService extends IService<Users>{
    public UserDetails loadUserByUsername(String s);
}
