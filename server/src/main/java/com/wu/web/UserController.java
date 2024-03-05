package com.wu.web;

import com.wu.model.TUser;
import com.wu.result.R;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/api/login/info")
    public R loginInfo(Authentication authentication) {
        TUser tUser = (TUser)authentication.getPrincipal();
        return R.OK(tUser);
    }
}
