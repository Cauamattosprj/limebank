package com.cauamattosprj.limebank.domains.auth.services;

import com.cauamattosprj.limebank.domains.auth.exceptions.EmailAlreadyExistsException;
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

import java.sql.SQLException;

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

        try {
            userDAO.save(user);
        } catch (Exception e) {
            if (e.getCause() instanceof SQLException) {
                if (e.getMessage().contains("email")) {
                    throw new EmailAlreadyExistsException("Email duplicado");
                }
            }

            throw e;
        }

        return jwtUtil.generateToken(user.getUsername(), user);
    }

    public String login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        return jwtUtil.generateToken(auth.getName(), userService.loadUserByUsername(auth.getName()));
    }
}
