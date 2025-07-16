package com.cauamattosprj.limebank.domains.auth.services;

import com.cauamattosprj.limebank.domains.auth.exceptions.EmailAlreadyExistsException;
import com.cauamattosprj.limebank.domains.auth.exceptions.InvalidCredentials;
import com.cauamattosprj.limebank.domains.user.DAO.UserDAO;
import com.cauamattosprj.limebank.domains.user.models.User;
import com.cauamattosprj.limebank.domains.user.service.UserService;
import com.cauamattosprj.limebank.utils.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

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
                    throw new EmailAlreadyExistsException();
                }
            }

            throw e;
        }

        return jwtUtil.generateToken(userDAO.getUserByEmail(request.email()));
    }

    public String login(AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            return jwtUtil.generateToken(userDAO.getUserByEmail(request.email()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentials();
        } catch (Exception e){
                System.out.println("Erro ao autenticar: " + e.getMessage());
                throw e; // ou lançar uma exception customizada
        }
    }

    public Map<String, String> refresh(Map<String, String> header) {
        try {
            String cookie = header.get("cookie");
            String currentRefreshToken = cookie.split("=")[1];
            System.out.println("currentRefreshToken = " + currentRefreshToken);
            String username = jwtUtil.extractUsername(currentRefreshToken);
            System.out.println("username = " + username);

            if (jwtUtil.isTokenValid(currentRefreshToken)) {
                Map<String, String> map = Map.of("refreshToken",jwtUtil.generateToken(userDAO.getUserByEmail(username)));
                return map;
            }
            throw new InvalidCredentials("Refresh token inválido");

        } catch (Exception e) {
            throw new InvalidCredentials("refresh token inválido ou malformado");
        }
    }
}
