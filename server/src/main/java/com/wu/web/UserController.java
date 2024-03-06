package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.model.TUser;
import com.wu.result.R;
import com.wu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/api/userList")
    public R getUserList(@RequestParam(value = "current",required = false) Integer current) {
        if(current == null || current == 0){
            current = 1;
        }

        PageInfo<TUser> userByPage = userService.getUserByPage(current);

        return R.OK(userByPage);
    }

    @GetMapping("/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id) {
        TUser tUser = userService.getUserById(id);
        return R.OK(tUser);
    }

}
