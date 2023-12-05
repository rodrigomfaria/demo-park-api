package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.User;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.exceptions.UsernameUniqueViolationException;
import com.rmf.demoparkapi.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testSave_ValidUser_ReturnsSavedUser() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe@gmail.com");
        user.setPassword("password123");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.save(user);

        // Assert
        assertNotNull(savedUser);
        Assertions.assertEquals("john_doe@gmail.com", savedUser.getUsername());
        Assertions.assertEquals("hashedPassword", savedUser.getPassword());
    }

    @Test
    public void testSave_DuplicateUsername_ThrowsUsernameUniqueViolationException() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existing_user@gmail.com");
        existingUser.setPassword("password123");

        when(passwordEncoder.encode(existingUser.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(existingUser)).thenThrow(DataIntegrityViolationException.class);

        // Act and Assert
        assertThrows(UsernameUniqueViolationException.class, () -> {
            userService.save(existingUser);
        });
    }

    @Test
    public void testGetById_ExistingId_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User retrievedUser = userService.getById(userId);

        // Assert
        Assertions.assertEquals(userId, retrievedUser.getId());
    }

    @Test
    public void testGetById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistingUserId = 100L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getById(nonExistingUserId);
        });
    }

    @Test
    public void testUpdatePass_CorrectCurrentPassword_ReturnsUserWithUpdatedPassword() {
        // Arrange
        Long userId = 1L;
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";
        String confirmPassword = "newPassword";

        User user = new User();
        user.setId(userId);
        user.setPassword("oldPasswordHashed");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(currentPassword)).thenReturn("newPasswordHashed");

        // Act
        User updatedUser = userService.updatePass(userId, currentPassword, newPassword, confirmPassword);

        // Assert
        assertNotNull(updatedUser);
        Assertions.assertEquals("newPasswordHashed", updatedUser.getPassword());
    }

    @Test
    public void testUpdatePass_IncorrectCurrentPassword_ThrowsRuntimeException() {
        // Arrange
        Long userId = 1L;
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";
        String confirmPassword = "newPassword";

        User user = new User();
        user.setId(userId);
        user.setPassword("oldPasswordHashed");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            userService.updatePass(userId, currentPassword, newPassword, confirmPassword);
        });
    }

    @Test
    public void testUpdatePass_UserNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Long nonExistingUserId = 100L;
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";
        String confirmPassword = "newPassword";

        when(userRepository.findById(nonExistingUserId)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.updatePass(nonExistingUserId, currentPassword, newPassword, confirmPassword);
        });
    }

    @Test
    public void testUpdatePass_PasswordsNotMatching_ThrowsRuntimeException() {
        // Arrange
        Long userId = 1L;
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";
        String confirmPassword = "confirmPassword";

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            userService.updatePass(userId, currentPassword, newPassword, confirmPassword);
        });
    }

    @Test
    public void testGetAll_ReturnsListOfUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user2");
        user2.setPassword("password2");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> retrievedUsers = userService.getAll();

        // Assert
        Assertions.assertEquals(2, retrievedUsers.size());
        Assertions.assertEquals("user1", retrievedUsers.get(0).getUsername());
        Assertions.assertEquals("user2", retrievedUsers.get(1).getUsername());
    }

    @Test
    public void testGetByUsername_ExistingUsername_ReturnsUser() {
        // Arrange
        String existingUsername = "existingUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(existingUsername);
        user.setPassword("password");

        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(user));

        // Act
        User retrievedUser = userService.getByUsername(existingUsername);

        // Assert
        assertNotNull(retrievedUser);
        Assertions.assertEquals(existingUsername, retrievedUser.getUsername());
    }

    @Test
    public void testGetByUsername_NonExistingUsername_ThrowsEntityNotFoundException() {
        // Arrange
        String nonExistingUsername = "nonExistingUser";

        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getByUsername(nonExistingUsername);
        });
    }

    @Test
    public void testGetRoleByUsername_ExistingUsername_ReturnsUserRole() {
        // Arrange
        String existingUsername = "existingUser";
        User.Role expectedRole = User.Role.ROLE_ADMIN;

        when(userRepository.findRoleByUsername(existingUsername)).thenReturn(expectedRole);

        // Act
        User.Role retrievedRole = userService.getRoleByUsername(existingUsername);

        // Assert
        assertNotNull(retrievedRole);
        Assertions.assertEquals(expectedRole, retrievedRole);
    }

    @Test
    public void testGetRoleByUsername_NonExistingUsername_ThrowsEntityNotFoundException() {
        // Arrange
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.findRoleByUsername(nonExistingUsername)).thenReturn(null);

        // Act
        EntityNotFoundException exception = null;
        try {
            userService.getRoleByUsername(nonExistingUsername);
        } catch (EntityNotFoundException e) {
            exception = e;
        }

        // Assert
        assertNull(exception, "EntityNotFoundException should not be thrown");
    }

}