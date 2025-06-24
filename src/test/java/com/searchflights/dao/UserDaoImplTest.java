package com.searchflights.dao;

import com.searchflights.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link com.searchflights.dao.UserDaoImpl}.
 * <p>
 * This class validates the data access operations related to the {@link com.searchflights.model.User}
 * entity using Hibernate's {@link SessionFactory} and {@link Session}, with behavior mocked via Mockito.
 * </p>
 *
 * <p>
 * Mocked dependencies include:
 * <ul>
 *     <li>{@link SessionFactory}</li>
 *     <li>{@link Session}</li>
 *     <li>{@link Query} for {@link User} and {@link Long} results</li>
 * </ul>
 * </p>
 *
 * <p>
 * Covered test cases:
 * <ul>
 *     <li>Saving a user</li>
 *     <li>Finding users by username, email, or ID</li>
 *     <li>Fetching all users</li>
 *     <li>Deleting users that exist or do not exist</li>
 *     <li>Counting total number of users</li>
 * </ul>
 * </p>
 *
 * @see com.searchflights.dao.UserDaoImpl
 * @see com.searchflights.model.User
 * @see org.hibernate.SessionFactory
 * @see org.hibernate.Session
 * @see org.junit.jupiter.api.Test
 * 
 * @Author: Pranav Singh
 */

public class UserDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<User> userQuery;

    @Mock
    private Query<Long> countQuery;

    @InjectMocks
    private UserDaoImpl userDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        userDao.saveUser(user);
        verify(session).saveOrUpdate(user);
    }

    @Test
    public void testFindByUsername() {
        User expectedUser = new User();
        when(session.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.uniqueResult()).thenReturn(expectedUser);

        User result = userDao.findByUsername("testuser");
        assertEquals(expectedUser, result);
        verify(userQuery).setParameter("username", "testuser");
    }

    @Test
    public void testFindByEmail() {
        User expectedUser = new User();
        when(session.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.uniqueResult()).thenReturn(expectedUser);

        User result = userDao.findByEmail("test@example.com");
        assertEquals(expectedUser, result);
        verify(userQuery).setParameter("email", "test@example.com");
    }

    @Test
    public void testFindById() {
        User expectedUser = new User();
        when(session.get(User.class, 1L)).thenReturn(expectedUser);
        
        User result = userDao.findById(1L);
        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = Collections.singletonList(new User());
        when(session.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.list()).thenReturn(expectedUsers);

        List<User> result = userDao.getAllUsers();
        assertEquals(expectedUsers, result);
    }

    @Test
    public void testDeleteUser_Exists() {
        User user = new User();
        when(session.get(User.class, 1L)).thenReturn(user);
        
        userDao.deleteUser(1L);
        verify(session).delete(user);
    }

    @Test
    public void testDeleteUser_NotExists() {
        when(session.get(User.class, 1L)).thenReturn(null);
        
        userDao.deleteUser(1L);
        verify(session, never()).delete(any());
    }

    @Test
    public void testGetTotalUserCount() {
        when(session.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.uniqueResult()).thenReturn(5L);

        int result = userDao.getTotalUserCount();
        assertEquals(5, result);
    }
}
