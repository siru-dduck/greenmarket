package com.hanium.userservice.domain;

import com.hanium.userservice.config.properties.JwtProp;
import com.hanium.userservice.dto.JoinDto;
import io.jsonwebtoken.*;
import lombok.*;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String address1;

    @Column(length = 30, nullable = false)
    private String address2;

    @Column(length = 30, nullable = false)
    private String nickname;

    private Long profileFileId;

    @Column(nullable = false)
    @Enumerated
    private UserStatus status;

    @Column(nullable = false)
    private LocalDateTime creatDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<RefreshToken> refreshTokenList = new ArrayList<>();

    @Transient
    private JwtProp jwtProp;

    public static User createUser(JoinDto joinDto) {
        return User.builder()
                .email(joinDto.getEmail())
                .password(new BCryptPasswordEncoder().encode(joinDto.getPassword()))
                .address1(joinDto.getAddress1())
                .address2(joinDto.getAddress2())
                .nickname(joinDto.getNickname())
                .status(UserStatus.NORMAL)
                .creatDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public Authentication createAuthentication() {
        String jti = UUID.randomUUID().toString();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(getEmail(), null, new ArrayList<>());
        UserDetail userDetail = UserDetail.builder()
                .userId(getId())
                .email(getEmail())
                .profileImageId(getProfileFileId())
                .tokenId(jti)
                .build();
        authentication.setDetails(userDetail);
        return authentication;
    }

    public boolean validatePassword(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.password);
    }

    public void setRefreshToken(Jws<Claims> jws, String jwtString) {
        LocalDateTime expireDateTime =  Instant.ofEpochMilli(jws.getBody()
                .getExpiration()
                .getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(jws.getBody().getId())
                .user(this)
                .token(jwtString)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .expireDate(expireDateTime)
                .build();

        getRefreshTokenList().add(refreshToken);
    }

}
