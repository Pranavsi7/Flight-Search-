package com.searchflights.dao;

import com.searchflights.model.UploadHistory;
import java.util.List;
/**
 * The {@link UploadHistoryDao} interface defines methods for managing 
 * {@link com.searchflights.model.UploadHistory} records in the data store.
 * <br><br>
 * Includes methods to save, retrieve, delete, and clear upload history data.
 *
 * @author Pranav Singh
 * @since 1.0
 */

public interface UploadHistoryDao {
    void saveUploadHistory(UploadHistory uploadHistory);
    List<UploadHistory> getAllUploadHistory();
    void deleteUploadHistory(Long id);
    void clearAllHistory();
    UploadHistory getUploadHistoryById(Long id);
}
