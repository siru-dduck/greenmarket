package com.hanium.userservice.mapper;

import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.UpdateUserInfoDto;
import org.mapstruct.Mapper;

/**
 * dto <-> entity domain 매퍼
 * @author siru
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UpdateUserInfoDto, User>{

}
