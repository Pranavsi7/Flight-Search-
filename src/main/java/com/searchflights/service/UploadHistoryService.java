package com.searchflights.service;

import com.searchflights.model.UploadHistory;
import java.util.List;
/**
 * Service interface for managing flight upload history records.
 * <p>
 * Defines operations to save, retrieve, delete, and clear history of file uploads
 * performed through import operations (CSV, Excel, Word, XML).
 * </p>
 * 
 * @see com.searchflights.model.UploadHistory
 * @see com.searchflights.dao.UploadHistoryDao
 * @see UploadHistoryServiceImpl
 * 
 * @Author: Pranav singh
 */

public interface UploadHistoryService {
    void saveUploadHistory(UploadHistory uploadHistory);
    List<UploadHistory> getAllUploadHistory();
    void deleteUploadHistory(Long id);
    void clearAllHistory();
    UploadHistory getUploadHistoryById(Long id);
}
