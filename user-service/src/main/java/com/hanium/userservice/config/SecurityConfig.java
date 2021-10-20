package com.hanium.userservice.config;

import com.hanium.userservice.jwt.JwtAuthenticationEntryPoint;
import com.hanium.userservice.jwt.JwtAuthenticationFilter;
import com.hanium.userservice.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author siru
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtProvider jwtProvider;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/swagger-ui/**"
                        ,"/swagger-resources/**"
                        ,"/v2/api-docs"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            .headers()
            .frameOptions()
            .sameOrigin()

            // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/users/join").permitAll()
                .antMatchers("/users/email/exist").permitAll()
                .anyRequest().authenticated()

            // jwt config
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), BasicAuthenticationFilter.class)

            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }

}