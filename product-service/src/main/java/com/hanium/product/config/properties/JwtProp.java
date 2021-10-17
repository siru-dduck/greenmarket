package com.hanium.product.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
@Getter
@Setter
public class JwtProp {

    private String secret;

    private String tokenValidityInSeconds;

}
