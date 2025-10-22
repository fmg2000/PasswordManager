package com.example.PasswordManager.security.userdetails;

import com.example.PasswordManager.user.model.User;
import com.example.PasswordManager.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementarea standard a UserDetailsService:
 * - primește username
 * - caută utilizatorul în DB
 * - mapează entitatea User -> UserDetails (UserPrincipal)
 */

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }

        return new UserPrincipal(user);
    }
}
