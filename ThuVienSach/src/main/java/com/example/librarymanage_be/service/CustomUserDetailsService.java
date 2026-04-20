package com.example.librarymanage_be.service;

import com.example.librarymanage_be.entity.Users;
import com.example.librarymanage_be.entity.UserPrincipal;
import com.example.librarymanage_be.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return UserPrincipal.build(user);
    }
}
