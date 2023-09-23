package com.project.service.user;

import com.project.entity.enums.RoleType;
import com.project.entity.user.User;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;

    //Note: saveUser() *************Teacher ve Student haric ****************
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) { // Admin, Dean, ViceDean
        // !!! unique controller
        uniquePropertyValidator.checkDuplicate(userRequest.getUsername(), userRequest.getSsn(),
                userRequest.getPhoneNumber(), userRequest.getEmail());
        // !!! DTO --> POJO
        User user = userMapper.mapUserRequestToUser(userRequest);
        // !!! Role bilgisi setlenecek
        if(userRole.equalsIgnoreCase(RoleType.ADMIN.name())){

            if(Objects.equals(userRequest.getUsername(),"Admin")){
                user.setBuilt_in(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        } else if (userRole.equalsIgnoreCase("Dean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        }else {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_USERROLE_MESSAGE,userRole));
        }

        // !!! Password encode edilecek
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // !!!isAdvisor -->
        user.setIsAdvisor(Boolean.FALSE);
        // !!! DB ye kaydediliyor
        User savedUser = userRepository.save(user);
        // !!! Response nesnesi olusturulyor

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();


    }

    // Note: getAllAdminOrDeanOrViceDean *************************************
    public Page<UserResponse> getUserByPage(int page, int size, String sort, String type, String userRole) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return userRepository.findByUserByRole(userRole,pageable).map(userMapper::mapUserToUserResponse);
    }

    //Note: getUserById() ***************************************************
    public ResponseMessage<BaseUserResponse> getUserById(Long userId) {

        BaseUserResponse baseUserResponse = null;
        // !!! id var mi ?
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,userId)));

        if(user.getUserRole().getRoleType()==RoleType.STUDENT){
            baseUserResponse = userMapper.mapUserToStudentResponse(user);
        } else if (user.getUserRole().getRoleType()==RoleType.TEACHER) {
            baseUserResponse = userMapper.mapUserToTeacherResponse(user);
        }else {
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        }

        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(baseUserResponse)
                .build();
    }


    //Note: deleteUser() *******************************************************
    public String deleteUserById(Long id, HttpServletRequest request) {
        // !!! id control,We check the user we want to delete=silinmesini istedigimiz user uzerinden kontrol yapiyoruz
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,id)));
        // !!! bu methodu tetikleyan kullaniciya ulasiyoruz
        String username = (String) request .getAttribute("username");
        User user2 = userRepository.findByUsernameEquals(username);
        // built-in control
        if(Boolean.TRUE.equals(user.getBuilt_in())){
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        } else if (user2.getUserRole().getRoleType()==RoleType.MANAGER) {
            if(!((user.getUserRole().getRoleType() == RoleType.TEACHER)||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT)||
                    (user.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER))){
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        } else if (user2.getUserRole().getRoleType()==RoleType.ASSISTANT_MANAGER) {
            if(!((user.getUserRole().getRoleType() == RoleType.TEACHER)||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT))){
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }
        userRepository.deleteById(id);
        return SuccessMessages.USER_DELETE;
    }

    //Note: updateAdminOrDeanOrViceDean() ***************************************
    public ResponseEntity<BaseUserResponse> updateUser(UserRequest userRequest, Long userId) {
        // !!! id control
        User user = isUserExist(userId);


    }

    public User isUserExist(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,userId)));
    }
}
