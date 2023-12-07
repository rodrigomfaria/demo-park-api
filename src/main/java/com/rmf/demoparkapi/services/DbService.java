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
        user.setUsername("admin@demoparkapi.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(User.Role.ROLE_ADMIN);
        userRepository.save(user);

        User user1 = new User();
        user1.setUsername("costumer1@demoparkapi.com");
        user1.setPassword(passwordEncoder.encode("123456"));
        user1.setRole(User.Role.ROLE_COSTUMER);

        Costumer costumer1 = new Costumer();
        costumer1.setUser(userRepository.save(user1));
        costumer1.setIdentificationNumber("11111111111");
        costumer1.setName("Costumer1");
        costumerRepository.save(costumer1);

        User user2 = new User();
        user2.setUsername("costumer2@demoparkapi.com");
        user2.setPassword(passwordEncoder.encode("123456"));
        user2.setRole(User.Role.ROLE_COSTUMER);

        Costumer costumer2 = new Costumer();
        costumer2.setUser(userRepository.save(user2));
        costumer2.setIdentificationNumber("22222222222");
        costumer2.setName("Costumer2");
        costumerRepository.save(costumer2);
    }
}
