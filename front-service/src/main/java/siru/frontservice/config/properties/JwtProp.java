package siru.frontservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
@Getter
@Setter
public class JwtProp {

    // 해시 암호화 문자열
    private String secret;

    // jwt access token 유효기간(초 단위)
    private long accessTokenValidityInSeconds;

    // jwt refresh token 유효기간(초 단위)
    private long refreshTokenValidityInSeconds;

}