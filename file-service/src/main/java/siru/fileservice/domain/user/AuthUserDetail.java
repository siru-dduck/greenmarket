package siru.fileservice.domain.user;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuthUserDetail {
    private long userId;
    private String email;
    private String nickname;
    private Long profileImageId;
    private String tokenId;
}