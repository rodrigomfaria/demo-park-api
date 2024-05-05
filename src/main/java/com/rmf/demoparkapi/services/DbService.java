package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.entities.User;
import com.rmf.demoparkapi.repositories.CostumerRepository;
import com.rmf.demoparkapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@RequiredArgsConstructor
@Service
public class DbService {

    private final UserRepository userRepository;

    private final CostumerRepository costumerRepository;

    private final PasswordEncoder passwordEncoder;

    public void instantiateTestDatabase() throws ParseException {

        User user = new User();
        user.setUsername("demopark.admin@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(User.Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}
