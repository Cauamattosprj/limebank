package com.cauamattosprj.limebank.domains.auth.controllers;

import com.cauamattosprj.limebank.domains.auth.services.AuthService;
import com.cauamattosprj.limebank.domains.common.dtos.ApiResponse;
import com.cauamattosprj.limebank.domains.user.service.UserService;
import com.cauamattosprj.limebank.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody AuthService.AuthRequest request) {
        String result = authService.register(request);

        return ResponseEntity.ok(ApiResponse.ofSuccess(result,200));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody AuthService.AuthRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(authService.login(request), 200));
    }
}

