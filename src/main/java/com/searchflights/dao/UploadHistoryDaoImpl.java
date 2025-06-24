package com.searchflights.dao;

import com.searchflights.model.UploadHistory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * The class {@link UploadHistoryDaoImpl} provides the implementation of 
 * {@link UploadHistoryDao} for performing CRUD operations on 
 * {@link com.searchflights.model.UploadHistory} entities using Hibernate.
 * <br><br>
 * This class interacts directly with the data store and should be accessed 
 * via the service layer, not directly by controllers or UI components.
 *
 * <p>Supports operations like save, fetch, delete, and clearing all history.</p>
 *
 * @author Pranav Singh
 * @see com.searchflights.service.UploadHistoryServiceImpl
 * @since 1.0
 */

@Repository
public class UploadHistoryDaoImpl implements UploadHistoryDao {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public void saveUploadHistory(UploadHistory uploadHistory) {
        sessionFactory.getCurrentSession().save(uploadHistory);
    }
    
    @Override
    public List<UploadHistory> getAllUploadHistory() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM UploadHistory ORDER BY uploadDate DESC", UploadHistory.class)
            .list();
    }
    
    @Override
    public void deleteUploadHistory(Long id) {
        UploadHistory uploadHistory = getUploadHistoryById(id);
        if (uploadHistory != null) {
            sessionFactory.getCurrentSession().delete(uploadHistory);
        }
    }
    
    @Override
    public void clearAllHistory() {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM UploadHistory")
            .executeUpdate();
    }
    
    @Override
    public UploadHistory getUploadHistoryById(Long id) {
        return sessionFactory.getCurrentSession().get(UploadHistory.class, id);
    }
}
