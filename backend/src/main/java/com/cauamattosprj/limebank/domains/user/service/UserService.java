package com.cauamattosprj.limebank.domains.user.service;

import com.cauamattosprj.limebank.domains.user.DAO.UserDAO;
import com.cauamattosprj.limebank.domains.user.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserByEmail(String email) {
        try {
            return userDAO.getUserByEmail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado no banco");
        }
    }

    @Override
    public UserDetails loadUserByUsername (String email) {
        try {
            return userDAO.getUserByEmail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
