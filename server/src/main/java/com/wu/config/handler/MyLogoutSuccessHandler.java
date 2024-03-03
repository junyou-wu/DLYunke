package com.wu.config.handler;

import com.wu.model.TUser;
import com.wu.result.CodeEnum;
import com.wu.result.R;
import com.wu.util.JSONUtils;
import com.wu.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 退出成功处理器
 *
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //退出成功，执行该方法，在该方法中返回json给前端，就行了
        TUser tUser = (TUser)authentication.getPrincipal();

        //退出成功的统一结果
        R result = R.OK(CodeEnum.USER_LOGOUT);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
