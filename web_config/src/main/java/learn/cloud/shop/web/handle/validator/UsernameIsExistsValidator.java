package learn.cloud.shop.web.handle.validator;


import learn.cloud.shop.mapper.UserMapper;
import learn.cloud.shop.pojo.User;
import learn.cloud.shop.web.handle.validator.annotation.UsernameIsExists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsernameIsExistsValidator implements ConstraintValidator<UsernameIsExists, String> {

	@Autowired
	private UserMapper userMapper;

	private static final Logger log = LoggerFactory.getLogger(UsernameIsExistsValidator.class);

	@Override
	public void initialize(UsernameIsExists constraintAnnotation) {
		log.info("UsernameIsExistsValidator 验证器初始化");
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("user :{} 注解中的messsge为    f:{}", value, context.getDefaultConstraintMessageTemplate());
		User user = new User();
		user.setUsername(value);
		Map<String,Object> map=new HashMap<>();
		map.put("username","mt");
		List<User> maps = userMapper.selectByMap(map);
		if(maps!=null) {
			log.info("用户名重复");
			return false;
		}
		return true;
	}

}
