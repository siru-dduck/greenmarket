package com.hanium.userservice.mapper;

import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.UpdateUserInfoDto;
import com.hanium.userservice.dto.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * dto <-> entity domain 매퍼
 * @author siru
 */
@Mapper(componentModel = "spring")
public interface UserInfoMapper extends GenericMapper<UserInfoDto, User>{

    @Override
    @Mapping(source = "id", target = "userId")
    UserInfoDto toDto(User user);

    @Override
    @Mapping(source = "userId", target = "id")
    User toEntity(UserInfoDto userInfoDto);

}
