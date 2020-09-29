package learn.cloud.shop.web.controller;


import learn.cloud.shop.service.UsersService;
import learn.cloud.shop.util.JsonResult;

import learn.cloud.shop.web.config.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邝明山
 * @since 2020-09-24
 */
@Controller
public class UsersController{

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    ProjectProperties projectProperties;
    @Autowired
    UsersService usersService;

    @GetMapping("/users/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public JsonResult requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String suffix=".html";
        String url=null;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            url = savedRequest.getRedirectUrl();
            //如果是访问页面，则重定向
            if (StringUtils.endsWithIgnoreCase(url, suffix)) {
                System.out.println("重定向了，原地址："+url);
                redirectStrategy.sendRedirect(request, response, projectProperties.getWeb().getLoginPage());
            }
        }
        return JsonResult.errorUnAuthentication("访问资源需要验证,你想访问的是"+url);
    }

    @GetMapping("/showInformation")
    @ResponseBody
    public JsonResult showInformation(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,Authentication authentication){
        Map<String,Object> map=new HashMap<>();
        map.put("tokenAuth",usernamePasswordAuthenticationToken);
        map.put("auth",authentication);
        return JsonResult.isOk(map);
    }

}

