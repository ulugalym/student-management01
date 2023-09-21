package com.project.service;

import com.project.payload.request.LoginRequest;
import com.project.payload.response.AuthResponse;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    //Note: Login() **********************************************
    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //kullaniciyi valid etmek icin authenticationManager nesnesi cagiriliyor
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        //valid edilen kullaniciyi Context'e aliyor
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //JWT Token olusturuluyor
        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails =(UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();

        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.username(userDetails.getUsername());
        authResponse.token(token.substring(7));
        authResponse.name(userDetails.getName());
        authResponse.ssn(userDetails.getSsn());

        role.ifPresent(authResponse::role);

        return ResponseEntity.ok(authResponse.build());

    }
}
