package com.wu.config.aspect;

import com.wu.commons.DataScope;
import com.wu.constant.Constants;
import com.wu.model.TUser;
import com.wu.Query.BaseQuery;
import com.wu.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class DataScopeAspect {

    //aspectJ 实现aop

    //切入点
    @Pointcut(value = "@annotation(com.wu.commons.DataScope)")
    private void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();

        //拿到方法上的注解
        DataScope dataScope = methodSignature.getMethod().getDeclaredAnnotation(DataScope.class);

        String tableAlias = dataScope.tableAlias();
        String tableField = dataScope.tableField();

        //在spring web容器中，可以拿到当前请求的request对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(Constants.TOKEN_NAME);
        //从token中解析出该用户是管理员还是普通用户
        TUser tUser = JWTUtils.parseUserFromJWT(token);

        //拿到用户的角色
        List<String> roleList = tUser.getRoleList();

        if (!roleList.contains("admin")) {//不包含admin角色，只查当前用户自己的数据，否则查所有数据
            Object params = joinPoint.getArgs()[0]; //拿方法的第一个参数,方法只有一个参数，baseQuery
            if (params instanceof BaseQuery) {
                BaseQuery query = (BaseQuery)params;

                //select * from t_user tu where tu.id = 2
                query.setFilterSQL(" and " + tableAlias + "." + tableField + " = " + tUser.getId());
            }
        }
        return joinPoint.proceed();
    }
}
