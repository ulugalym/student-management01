package com.project.controller.user;

import com.project.payload.request.user.UserRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Note: saveUser() *************Teacher ve Student haric ****************
    @PostMapping("/save/{userRole}")  //http://localhost:8081/user/save/Admin + POST
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<UserResponse>>saveUser(@PathVariable String userRole,
                                                                 @RequestBody @Valid UserRequest userRequest){
        return  ResponseEntity.ok( userService.saveUser(userRequest,userRole));
    }

    // Note: getAllAdminOrDeanOrViceDean *************************************
    @GetMapping("/getAllUserByPAge/{userRole}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<Page<UserResponse>> getUserByPage(
            @PathVariable String userRole,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "name") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type
    ){
        Page<UserResponse> adminsOsDeansOrViceDeans = userService.getUserByPage(page,size,sort,type,userRole);
        return new ResponseEntity<>(adminsOsDeansOrViceDeans, HttpStatus.OK);
    }

    //Note: getUserById() ***************************************************
    @GetMapping("/getUserById/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BaseUserResponse>getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    //Note: deleteUser() *******************************************************
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<String>deleteUserById(@PathVariable Long id,
                                                HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(userService.deleteUserById(id,httpServletRequest));
    }

    //Note: updateAdminOrDeanOrViceDean() ***************************************
    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BaseUserResponse>updateAdminOrDeanOrViceDean(@RequestBody @Valid UserRequest userRequest,
                                                                       @PathVariable Long userId){
        return userService.updateUser(userRequest,userId);
    }
}
