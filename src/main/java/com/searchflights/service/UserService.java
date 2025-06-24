package com.searchflights.service;

import com.searchflights.model.User;
import java.util.List;
/**
 * Service interface for managing user-related operations in the system.
 * <p>
 * Supports user authentication, password encoding/verification, and common
 * CRUD operations such as saving, finding, and deleting users.
 * </p>
 *
 * @see com.searchflights.model.User
 * @see com.searchflights.service.UserServiceImpl
 *
 * Author: Pranav Singh
 */

public interface UserService {
    
    // Authentication method
    User authenticateUser(String username, String password);
    
    // CRUD operations
    User saveUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
    int getTotalUserCount();
    
    // Password operations
    boolean verifyPassword(String rawPassword, String encodedPassword);
    String encodePassword(String rawPassword);
}
