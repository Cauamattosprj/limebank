package com.cauamattosprj.limebank.domains.auth.controllers;

import com.cauamattosprj.limebank.domains.user.service.UserService;
import com.cauamattosprj.limebank.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email, request.password));

        return jwtUtil.generateToken(auth.getName(), userService.loadUserByUsername(auth.getName()));
    }

    public record AuthRequest(String email, String password) {}

}

