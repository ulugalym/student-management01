package com.project.controller;

import com.project.payload.request.LoginRequest;
import com.project.payload.response.AuthResponse;
import com.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
