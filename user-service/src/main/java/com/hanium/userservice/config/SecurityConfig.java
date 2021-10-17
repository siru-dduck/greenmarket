package com.hanium.userservice.config;

import com.hanium.userservice.jwt.JwtAuthenticationEntryPoint;
import com.hanium.userservice.jwt.JwtAuthenticationFilter;
import com.hanium.userservice.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            // enable h2-console
            .headers()
            .frameOptions()
            .sameOrigin()

            // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated()

            // jwt config
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), BasicAuthenticationFilter.class)

            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }

}