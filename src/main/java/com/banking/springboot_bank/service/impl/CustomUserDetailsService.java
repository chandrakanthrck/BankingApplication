package com.banking.springboot_bank.service.impl;

import com.banking.springboot_bank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByEmail(username).orElseThrow(()
//                -> new UsernameNotFoundException(username + " not found"));
        return userRepository.findByEmail(username).orElseThrow(()
                -> new UsernameNotFoundException(username + " not found"));
    }
}
