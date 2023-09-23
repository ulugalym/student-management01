package com.project.controller.user;

import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    //Note: Login() **********************************************
    @PostMapping("/login")  //http://localhost:8081/auth/login  +  POST  +  JSON
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest){

        return authenticationService.authenticateUser(loginRequest);
    }

    //note: findByUsername() *************************************
    @GetMapping("/user")  //http://localhost:8081/auth/user + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<UserResponse>findByUsername(HttpServletRequest request){

        String username = (String) request.getAttribute("username");
        UserResponse userResponse = authenticationService.findByUsername(username);
        return ResponseEntity.ok(userResponse); //200 kod
    }


    //Note: updatePassword() *************************************
    @PatchMapping("/updatePassword") //http://localhost:8081/auth/updatePassword
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<String>updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                HttpServletRequest request){

        authenticationService.updatePassword(updatePasswordRequest,request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE;
        return ResponseEntity.ok(response);

    }
}
