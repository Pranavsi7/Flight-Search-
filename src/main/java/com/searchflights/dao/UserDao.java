package com.searchflights.dao;

import com.searchflights.model.User;
import java.util.List;
/**
 * The {@link UserDao} interface defines data access operations for 
 * {@link com.searchflights.model.User} entities.
 * <br><br>
 * It includes methods for creating, retrieving, deleting users, and counting total users.
 *
 * @author Pranav Singh
 * @since 1.0
 */

public interface UserDao {
    
    User saveUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
    int getTotalUserCount();
}
