package com.searchflights.model;

import javax.persistence.*; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Represents the record of a file upload operation that imports flight data.
 * <br><br>
 * Tracks details such as file name, type, size, number of flights imported,
 * lines skipped during parsing, and the upload timestamp.
 *
 * <p>This entity supports administrative auditing and history display features.</p>
 * 
 * @author Pranav Singh
 * @version 1.0
 * @since 2025-07-08
 */

@Entity
@Table(name = "upload_history")
public class UploadHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "flights_imported")
    private Integer flightsImported;
    
    @Column(name = "lines_skipped")
    private Integer linesSkipped;
    
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    // Constructors
    public UploadHistory() {}
    
    public UploadHistory(String fileName, String fileType, Integer flightsImported, 
                        Integer linesSkipped, Long fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.flightsImported = flightsImported;
        this.linesSkipped = linesSkipped;
        this.fileSize = fileSize;
        this.uploadDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public Integer getFlightsImported() { return flightsImported; }
    public void setFlightsImported(Integer flightsImported) { this.flightsImported = flightsImported; }
    
    public Integer getLinesSkipped() { return linesSkipped; }
    public void setLinesSkipped(Integer linesSkipped) { this.linesSkipped = linesSkipped; }
    
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFormattedUploadDate() {
        if (uploadDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return uploadDate.format(formatter);
        }
        return "";
    }
}
