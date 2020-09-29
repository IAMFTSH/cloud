package learn.cloud.shop.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import learn.cloud.shop.pojo.User;
import learn.cloud.shop.service.UserService;
import learn.cloud.shop.util.JsonResult;
import learn.cloud.shop.web.vo.UserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 邝明山
 * on 2020/9/23 9:02
 */
@Controller
@ResponseBody
@RequestMapping("test")
public class Test {

    private static final Logger log = LoggerFactory.getLogger(Test.class);

    @Autowired
    UserService userService;

    @GetMapping("test")
    public JsonResult test() {
        return JsonResult.isOk();
    }

    @PostMapping("insertUser")
    @ApiOperation("添加新的用户")
    public JsonResult insertUser(@ApiParam("用户对象本身，和PO对象相同") @Valid @RequestBody UserVo userVo)
            throws IllegalAccessException, InvocationTargetException {

        log.info("insertUser");

        User user = new User(userVo.getId(),userVo.getUsername(),userVo.getBirthday(),userVo.getSex(),userVo.getAddress());


        return JsonResult.isOk(userService.save(user));
    }
}
