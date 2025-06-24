package com.searchflights.service;

import com.searchflights.dao.UserDao;
import com.searchflights.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Implementation of the {@link UserService} interface.
 * <p>
 * Provides transactional methods for managing user authentication and persistence.
 * Uses {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
 * for secure password encoding and verification.
 * </p>
 *
 * <p>
 * Handles plain text passwords during development/testing but encourages
 * proper encoding for production environments.
 * </p>
 *
 * @see com.searchflights.dao.UserDao
 * @see com.searchflights.model.User
 * @see UserService
 *
 * @Author: Pranav Singh
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User authenticateUser(String username, String password) {
        try {
            User user = userDao.findByUsername(username);
            if (user != null && user.isEnabled()) {
                
                if (password.equals(user.getPassword()) || 
                    passwordEncoder.matches(password, user.getPassword())) {
                    logger.info("Authentication successful for user: {}", username);
                    return user;
                }
            }
            logger.warn("Authentication failed for user: {}", username);
            return null;
        } catch (Exception e) {
            logger.error("Error during authentication for user: {}", username, e);
            return null;
        }
    }

    @Override
    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setEnabled(true);
        return userDao.saveUser(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public int getTotalUserCount() {
        return userDao.getTotalUserCount();
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (encodedPassword != null && encodedPassword.startsWith("$2a$")) {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } else {
            return rawPassword.equals(encodedPassword);
        }
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
