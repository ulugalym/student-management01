package com.project.service.user;

import com.project.entity.enums.RoleType;
import com.project.entity.user.User;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.TeacherRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.TeacherResponse;
import com.project.repository.user.UserRepository;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;

    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        //TODO : lessonProgramController yazilinca eklenecek

        // !!! unique control
        uniquePropertyValidator.checkDuplicate(teacherRequest.getUsername(), teacherRequest.getSsn(),
                teacherRequest.getPhoneNumber(), teacherRequest.getEmail());

        // !!! DTO --> POJO
        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);
        //!!! POJO da olmasi gerekipde DTO dan gelmeyan degerleri setliyoruz
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        // TODO : LessonProgram setlenecek
        // !!! password encoder
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        // !!! isAdvisor
        if(teacherRequest.getIsAdvisorTeacher()){   //Boolean.True.equals(teacherRequest.getIsAdvisorTeacher())
            teacher.setIsAdvisor(Boolean.TRUE);
        }else teacher.setIsAdvisor(Boolean.FALSE);


        User saveTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(saveTeacher))
                .build();



    }

    // Note : updateTeacherById() ***********************************
    public ResponseMessage<TeacherResponse> updateTeacherForManagers(TeacherRequest teacherRequest, Long userId) {
        // !!! var mi yok nu control
        User user = isUserExist(userId);
        // !!! parametrede gelen id ,bir teacher a ait mi control
        if(!user.getUserRole().getRoleType().equals(RoleType.TEACHER)){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_TEACHER_MESSAGE,userId));
        }
        //TODO : LessonProgram getirilecek

        // !!! unique control
        uniquePropertyValidator.checkUniqueProperties(user,teacherRequest);
        // !!! DTO --> POJO
        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedUser(teacherRequest,userId);
        // !!! Password encoder
        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        // !!! TODO : LessonProgram eklenecek
        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        User savedTeacher = userRepository.save(updatedTeacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();


    }

    public User isUserExist(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,userId)));
    }

    // !!! Note: GetAllStudentByAdvTeacherUserName() *****************
    public List<StudentResponse> getAllStudentByAdvisorUsername(String userName) {
       User teacher = getTeacherByUsername(userName);
       //verilen username in sahibi olan user Advisor mi ??
        if(Boolean.FALSE.equals(teacher.getIsAdvisor())){
            throw new BadRequestException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME,userName));
        }

        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());

    }

    public User getTeacherByUsername(String teacherUsername){

        return userRepository.findByUsernameEquals(teacherUsername)
    }

}
