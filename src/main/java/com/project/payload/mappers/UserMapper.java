package com.project.payload.mappers;

import com.project.entity.user.User;
import com.project.payload.request.abstracts.BaseUserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
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


    public StudentResponse mapUserToStudentResponse(User student) {

        return StudentResponse.builder()
                .userId(student.getId())
                .username(student.getUsername())
                .name(student.getName())
                .surname(student.getSurname())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .ssn(student.getSsn())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .build();
    }

    public TeacherResponse mapUserToTeacherResponse(User teacher) {

        return TeacherResponse.builder()
                .userId(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .lessonPrograms(teacher.getLessonsProgramList())
                .isAdvisorTeacher(teacher.getIsAdvisor())
                .build();

    }
}
