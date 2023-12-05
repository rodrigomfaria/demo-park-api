package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.User;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.exceptions.UsernameUniqueViolationException;
import com.rmf.demoparkapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username %s already registered", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User id=%s not found!!!", id))
        );
    }

    @Transactional
    public User updatePass(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords don't match!!!");
        }

        User user = getById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect Password!!!");
        }

        user.setPassword(passwordEncoder.encode(currentPassword));

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("User %s not found!!!", username))
        );
    }

    @Transactional(readOnly = true)
    public User.Role getRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
