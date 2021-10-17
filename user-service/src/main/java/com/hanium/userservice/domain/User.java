package com.hanium.userservice.domain;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    private final List<RefreshToken> refreshTokenList = new ArrayList<>();

    public void setPassword(String rawPassword) {
        this.password = new BCryptPasswordEncoder().encode(rawPassword);
    }

    public boolean validatePassword(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.password);
    }
}
