package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.User;
import com.rmf.demoparkapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class DbService {

    @Autowired
    private UserRepository userRepository;

    public void instantiateTestDatabase() throws ParseException {

        User user = new User();
        user.setUsername("admin@apipark.com");
        user.setPassword("123456");
        user.setRole(User.Role.ROLE_ADMIN);

        userRepository.save(user);
    }
}
