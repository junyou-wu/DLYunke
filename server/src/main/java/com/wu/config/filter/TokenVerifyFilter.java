package com.wu.config.filter;

import com.wu.constant.Constants;
import com.wu.model.TUser;
import com.wu.result.CodeEnum;
import com.wu.result.R;
import com.wu.service.RedisService;
import com.wu.util.JSONUtils;
import com.wu.util.JWTUtils;
import com.wu.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.wu.constant.Constants.TOKEN_NAME;


@Component
public class TokenVerifyFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    //spring boot框架的ioc容器中已经创建好了该线程池，可以注入直接使用
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(Constants.LOGIN_URI)) { //如果是登录请求，此时还没有生成jwt，那不需要对登录请求进行jwt验证
            //验证jwt通过了 ，让Filter链继续执行，也就是继续执行下一个Filter
            filterChain.doFilter(request, response);

        } else {


            String token = request.getHeader(TOKEN_NAME);

            //验证token有没有被篡改过
            if (!JWTUtils.verifyJWT(token)) {
                //token验证未通过统一结果
                R result = R.FAIL(CodeEnum.TOKEN_IS_ERROR);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把R以json返回给前端
                ResponseUtils.write(response, resultJSON);

                return;
            }


            TUser tUser = JWTUtils.parseUserFromJWT(token);
            String redisToken = (String) redisService.getValue(Constants.REDIS_JWT_KEY + tUser.getId());
            if (!StringUtils.hasText(redisToken)) {
                //token验证未通过统一结果
                R result = R.FAIL(CodeEnum.TOKEN_IS_EXPIRED);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把R以json返回给前端
                ResponseUtils.write(response, resultJSON);

                return;
            }

            if (!token.equals(redisToken)) {
                //token验证未通过的统一结果
                R result = R.FAIL(CodeEnum.TOKEN_IS_NONE_MATCH);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把R以json返回给前端
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //jwt验证通过了，那么在spring security的上下文环境中要设置一下，设置当前这个人是登录过的，你后续不要再拦截他了
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tUser, tUser.getLoginPwd(), tUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            //异步处理（更好的方式，使用线程池去执行）
            threadPoolTaskExecutor.execute(() -> {
                //token续期
                String rememberMe = request.getHeader("rememberMe");
                if (Boolean.parseBoolean(rememberMe)) {
                    redisService.expire(Constants.REDIS_JWT_KEY + tUser.getId(), Constants.EXPIRE_TIME, TimeUnit.SECONDS);
                } else {
                    redisService.expire(Constants.REDIS_JWT_KEY + tUser.getId(), Constants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
                }
            });

            //验证jwt通过了 ，让Filter链继续执行，也就是继续执行下一个Filter
            filterChain.doFilter(request, response);
        }
    }
}
