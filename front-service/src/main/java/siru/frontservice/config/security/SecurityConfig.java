package siru.frontservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import siru.frontservice.config.security.jwt.JwtAuthenticationFilter;
import siru.frontservice.config.security.jwt.JwtProvider;

/**
 * @author siru
 */
@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtProvider jwtProvider;

    /**
     * 시큐리티 무시 설정 matcher
     * @return
     */
    private NegatedServerWebExchangeMatcher negatedServerWebExchangeMatcher() {
        return new NegatedServerWebExchangeMatcher(
                ServerWebExchangeMatchers.pathMatchers(
                        "/h2-console/**"
                        ,"/swagger-ui/**"
                        ,"/swagger-resources/**"
                        ,"/v2/api-docs"
                        ,"/favicon.ico"
                        ,"/error"
                )
        );
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {
        return http
                .securityMatcher(negatedServerWebExchangeMatcher())
                .headers()
                .frameOptions()
                .mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)

                .and()
                .authorizeExchange()
                    .anyExchange().authenticated()
                .and()

                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .addFilterAt(new JwtAuthenticationFilter(jwtProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

}