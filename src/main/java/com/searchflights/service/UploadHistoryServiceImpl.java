package com.searchflights.service;

import com.searchflights.dao.UploadHistoryDao;
import com.searchflights.model.UploadHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Implementation of the {@link UploadHistoryService} interface.
 * <p>
 * Provides transactional methods for persisting and managing upload history data.
 * Used to log metadata about each import file such as filename, type, size, and results.
 * </p>
 * 
 * <p>
 * Relies on {@link com.searchflights.dao.UploadHistoryDao} for data access operations
 * and logs relevant events using SLF4J.
 * </p>
 * 
 * @see com.searchflights.model.UploadHistory
 * @see com.searchflights.dao.UploadHistoryDao
 * @see UploadHistoryService
 * 
 * @Author: Pranav Singh
 */

@Service
@Transactional
public class UploadHistoryServiceImpl implements UploadHistoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(UploadHistoryServiceImpl.class);
    
    @Autowired
    private UploadHistoryDao uploadHistoryDao;
    
    @Override
    public void saveUploadHistory(UploadHistory uploadHistory) {
        uploadHistoryDao.saveUploadHistory(uploadHistory);
        logger.info("Upload history saved: {}", uploadHistory.getFileName());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UploadHistory> getAllUploadHistory() {
        return uploadHistoryDao.getAllUploadHistory();
    }
    
    @Override
    public void deleteUploadHistory(Long id) {
        uploadHistoryDao.deleteUploadHistory(id);
        logger.info("Upload history deleted: {}", id);
    }
    
    @Override
    public void clearAllHistory() {
        uploadHistoryDao.clearAllHistory();
        logger.info("All upload history cleared");
    }
    
    @Override
    @Transactional(readOnly = true)
    public UploadHistory getUploadHistoryById(Long id) {
        return uploadHistoryDao.getUploadHistoryById(id);
    }
}
