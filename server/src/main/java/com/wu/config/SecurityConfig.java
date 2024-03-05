package com.wu.config;
import com.wu.config.filter.TokenVerifyFilter;
import com.wu.config.handler.MyAccessDeniedHandler;
import com.wu.config.handler.MyAuthenticationFailureHandler;
import com.wu.config.handler.MyAuthenticationSuccessHandler;
import com.wu.config.handler.MyLogoutSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Resource
    private TokenVerifyFilter tokenVerifyFilter;

    @Resource
    MyLogoutSuccessHandler myLogoutSuccessHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        //禁用跨站请求伪造
        return httpSecurity
                .formLogin( (formLogin) -> {
                    formLogin.loginProcessingUrl("/api/login")
                            .usernameParameter("loginAct")
                            .passwordParameter("loginPwd")
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler);
                })

                .authorizeHttpRequests( (authorize) -> {
                    authorize.requestMatchers("/api/login").permitAll()
                            .anyRequest().authenticated(); //其它任何请求都需要登录后才能访问
                })
                //方法引用，禁用跨站请求伪造
                .csrf(AbstractHttpConfigurer :: disable)

                //支持跨域请求
                .cors( (cors) -> {
                    cors.configurationSource(configurationSource);
                })


                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.accessDeniedHandler(myAccessDeniedHandler);
                })

                .sessionManagement( (session) -> {
                    //session创建策略
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 无session状态，也就是禁用session
                })

                //添加自定义的Filter
                .addFilterBefore(tokenVerifyFilter,LogoutFilter.class)

                .build();
    }

    //注册组件
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); //允许任何来源，http://localhost:8089
        configuration.setAllowedMethods(Arrays.asList("*")); //允许任何请求方法
        configuration.setAllowedHeaders(Arrays.asList("*")); //允许任何的请求头
        //实现接口
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
