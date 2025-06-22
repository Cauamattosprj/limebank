package com.cauamattosprj.limebank.domains.auth.services;

import com.cauamattosprj.limebank.domains.user.DAO.UserDAO;
import com.cauamattosprj.limebank.domains.user.models.User;
import com.cauamattosprj.limebank.domains.user.service.UserService;
import com.cauamattosprj.limebank.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public record AuthRequest(String email, String password) {}
    private final UserDAO userDAO;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public String register(AuthRequest request) {
        User user = User.builder()
                .email(request.email)
                .password(passwordEncoder.encode(request.password))
                .role("USER")
                .build();

        userDAO.save(user);

        return jwtUtil.generateToken(user.getUsername(), user);
    }

    public String login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        return jwtUtil.generateToken(auth.getName(), userService.loadUserByUsername(auth.getName()));
    }
}
