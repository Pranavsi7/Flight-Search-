package com.searchflights.service;

import com.searchflights.dao.UserDao;
import com.searchflights.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link com.searchflights.service.UserServiceImpl}.
 * <p>
 * This test class validates the behavior of the user-related service logic such as
 * authentication, registration, password handling, and basic CRUD operations.
 * </p>
 *
 * <p>
 * Dependencies are mocked using Mockito:
 * <ul>
 *     <li>{@link com.searchflights.dao.UserDao}</li>
 *     <li>{@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Covered test scenarios include:
 * <ul>
 *     <li>User authentication with both plaintext and BCrypt-encoded passwords</li>
 *     <li>User registration with automatic password encoding</li>
 *     <li>Password verification utility behavior</li>
 *     <liRetrieving, deleting users, and total user count checks</li>
 * </ul>
 * </p>
 *
 * @see com.searchflights.service.UserServiceImpl
 * @see com.searchflights.model.User
 * @see com.searchflights.dao.UserDao
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @see org.junit.jupiter.api.Test
 * 
 * @Author: Pranav Singh
 */

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("testpass");
        testUser.setEmail("test@example.com");
        testUser.setEnabled(true);
    }

    // ========== Authentication Tests ==========
    @Test
    void authenticateUser_SuccessWithPlainText() {
        when(userDao.findByUsername("testuser")).thenReturn(testUser);
        
        User result = userService.authenticateUser("testuser", "testpass");
        assertEquals(testUser, result);
        verify(userDao).findByUsername("testuser");
    }

    @Test
    void authenticateUser_SuccessWithEncodedPassword() {
        testUser.setPassword("$2a$10$encoded");
        when(userDao.findByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("testpass", "$2a$10$encoded")).thenReturn(true);

        User result = userService.authenticateUser("testuser", "testpass");
        assertEquals(testUser, result);
    }

    @Test
    void authenticateUser_WrongPassword() {
        when(userDao.findByUsername("testuser")).thenReturn(testUser);
        
        User result = userService.authenticateUser("testuser", "wrongpass");
        assertNull(result);
    }

    @Test
    void authenticateUser_UserDisabled() {
        testUser.setEnabled(false);
        when(userDao.findByUsername("testuser")).thenReturn(testUser);
        
        User result = userService.authenticateUser("testuser", "testpass");
        assertNull(result);
    }

    // ========== User Management Tests ==========
    @Test
    void saveUser_EncodesPlainTextPassword() {
        when(passwordEncoder.encode("newpass")).thenReturn("$2a$10$encoded");
        User newUser = new User();
        newUser.setPassword("newpass");

        userService.saveUser(newUser);
        
        assertEquals("$2a$10$encoded", newUser.getPassword());
        assertTrue(newUser.isEnabled());
        verify(userDao).saveUser(newUser);
    }

    @Test
    void saveUser_PreservesEncodedPassword() {
        User existingUser = new User();
        existingUser.setPassword("$2a$10$existing");
        
        userService.saveUser(existingUser);
        
        assertEquals("$2a$10$existing", existingUser.getPassword());
        verify(passwordEncoder, never()).encode(any());
    }

    // ========== Password Verification Tests ==========
    @Test
    void verifyPassword_EncodedMatch() {
        when(passwordEncoder.matches("testpass", "$2a$10$encoded")).thenReturn(true);
        assertTrue(userService.verifyPassword("testpass", "$2a$10$encoded"));
    }

    @Test
    void verifyPassword_PlainTextMatch() {
        assertTrue(userService.verifyPassword("testpass", "testpass"));
    }

    // ========== CRUD Operation Tests ==========
    @Test
    void findByUsername_ReturnsUser() {
        when(userDao.findByUsername("testuser")).thenReturn(testUser);
        User result = userService.findByUsername("testuser");
        assertEquals(testUser, result);
    }

    @Test
    void getAllUsers_ReturnsList() {
        when(userDao.getAllUsers()).thenReturn(Collections.singletonList(testUser));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void deleteUser_CallsDao() {
        userService.deleteUser(1L);
        verify(userDao).deleteUser(1L);
    }

    @Test
    void getTotalUserCount_ReturnsCount() {
        when(userDao.getTotalUserCount()).thenReturn(5);
        assertEquals(5, userService.getTotalUserCount());
    }
}
