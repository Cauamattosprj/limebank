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

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userDAO.findByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
