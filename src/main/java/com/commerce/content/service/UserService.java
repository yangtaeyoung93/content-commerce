package com.commerce.content.service;

import com.commerce.content.domain.Address;
import com.commerce.content.domain.User;
import com.commerce.content.dto.AddUserRequest;
import com.commerce.content.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest request){

        return userRepository.save(
                User.builder()
                .userId(request.getUserId())
                .userName(request.getUserName())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .phonNumber(request.getPhoneNumber())
                .address(new Address(request))
                .build()
                ).getId();
    }
}
