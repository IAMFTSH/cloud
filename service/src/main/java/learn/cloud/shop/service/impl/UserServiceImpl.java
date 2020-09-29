package learn.cloud.shop.service.impl;

import learn.cloud.shop.pojo.User;
import learn.cloud.shop.mapper.UserMapper;
import learn.cloud.shop.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邝明山
 * @since 2020-09-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
