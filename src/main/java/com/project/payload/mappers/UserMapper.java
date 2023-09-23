package com.project.payload.mappers;

import com.project.entity.user.User;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();
    }

    public User mapUserRequestToUser(BaseUserRequest baseUserRequest){

        return User.builder()
                .username(baseUserRequest.getUsername())
                .name(baseUserRequest.getName())
                .surname(baseUserRequest.getSurname())
                .password(baseUserRequest.getPassword())
                .ssn(baseUserRequest.getSsn())
                .birthDay(baseUserRequest.getBirthDay())
                .birthPlace(baseUserRequest.getBirthPlace())
                .phoneNumber(baseUserRequest.getPhoneNumber())
                .gender(baseUserRequest.getGender())
                .email(baseUserRequest.getEmail())
                .built_in(baseUserRequest.getBuiltIn())
                .build();
    }
}
