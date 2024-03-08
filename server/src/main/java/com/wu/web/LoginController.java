package com.wu.web;

import com.wu.model.TUser;
import com.wu.result.R;
import com.wu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Resource
    UserService userService;
    @GetMapping("/api/login/info")
    public R loginInfo(Authentication authentication) {
        TUser tUser = (TUser)authentication.getPrincipal();
        userService.updateLoginTime(tUser.getId());
        return R.OK(tUser);
    }

    @GetMapping("/api/logout")
    public R logOut(Authentication authentication) {
        TUser tUser = (TUser)authentication.getPrincipal();
        return R.OK(tUser);
    }

    @GetMapping("/api/login/free")
    public R loginFree() {
        return R.OK();
    }
}
