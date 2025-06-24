package com.searchflights.dao;

import com.searchflights.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * The class {@link UserDaoImpl} provides the implementation of {@link UserDao}
 * using Hibernate for managing {@link com.searchflights.model.User} entities.
 * <br><br>
 * This class is responsible for persisting, querying, and deleting user records in the database.
 * It should be accessed via the service layer in the application architecture.
 *
 * <p>Supports operations like user registration, lookup by ID/email/username, and user count retrieval.</p>
 *
 * @author Pranav Singh
 * @see com.searchflights.service.UserServiceImpl
 * @since 1.0
 */

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public User findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User", User.class).list();
    }

    @Override
    public void deleteUser(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
        }
    }

    @Override
    public int getTotalUserCount() {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(*) FROM User", Long.class);
        return query.uniqueResult().intValue();
    }
}
