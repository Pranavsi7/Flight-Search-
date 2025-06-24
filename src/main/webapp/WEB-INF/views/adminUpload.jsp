<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload Flights</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/admin.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .admin-container {
            max-width: 700px;
            margin: 40px auto;
            padding: 30px;
            background: #f9f9f9;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }
        
        .admin-title {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 2.2rem;
        }
        
        .admin-form {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        .form-group label {
            font-weight: 600;
            color: #555;
            margin-bottom: 8px;
            font-size: 1.1rem;
        }
        
        .form-group select, .form-group input[type="file"] {
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s;
        }
        
        .form-group select:focus, .form-group input[type="file"]:focus {
            outline: none;
            border-color: #007bff;
        }
        
        .submit-button {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
            border: none;
            padding: 15px 30px;
            font-size: 1.2rem;
            font-weight: bold;
            cursor: pointer;
            border-radius: 8px;
            transition: all 0.3s;
            margin-top: 10px;
        }
        
        .submit-button:hover {
            background: linear-gradient(135deg, #20c997, #28a745);
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(40, 167, 69, 0.3);
        }
        
        .submit-button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }
        
        .message {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
            display: none;
        }
        
        .message-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .message-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .file-info {
            background: #e9ecef;
            padding: 15px;
            border-radius: 8px;
            margin-top: 10px;
            display: none;
        }
        
        .loading {
            display: none;
            text-align: center;
            padding: 20px;
            color: #666;
        }
        
        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #28a745;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            animation: spin 1s linear infinite;
            margin: 0 auto 10px;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .file-requirements {
            background: #f8f9fa;
            border-left: 4px solid #007bff;
            padding: 15px;
            margin-top: 20px;
            border-radius: 0 8px 8px 0;
        }
        
        .file-requirements h4 {
            margin-top: 0;
            color: #007bff;
        }
        
        .file-requirements ul {
            margin-bottom: 0;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="admin-container">
        <h2 class="admin-title">📁 Upload Flights via File</h2>
        
        <!-- Message Area -->
        <div id="messageArea"></div>
        
        <!-- Loading Indicator -->
        <div id="loadingArea" class="loading">
            <div class="spinner"></div>
            <p>Processing file upload...</p>
        </div>
        
        <!-- Upload Form -->
        <form id="uploadForm" class="admin-form" enctype="multipart/form-data">
            <div class="form-group">
                <label for="fileType">Select File Type:</label>
                <select name="fileType" id="fileType" required>
                    <option value="">Choose file type...</option>
                    <option value="csv">CSV (Comma Separated Values)</option>
                    <option value="excel">Excel (.xlsx, .xls)</option>
                    <option value="word">Word (.docx, .doc)</option>
                    <option value="xml">XML</option>
                </select>
            </div>

            <div class="form-group">
                <label for="fileInput">Choose File:</label>
                <input type="file" id="fileInput" name="file" required accept=".csv,.xlsx,.xls,.docx,.doc,.xml">
            </div>
            
           <!-- File Info Display -->
<div id="fileInfo" class="file-info">
    <strong>Selected File:</strong> <span id="fileName"></span><br>
    <strong>File Size:</strong> <span id="fileSize"></span><br>
    <strong>File Type:</strong> <span id="fileTypeDisplay"></span>
</div>


            <button type="submit" id="submitBtn" class="submit-button">
                🚀 Upload File
            </button>
        </form>
        
        <!-- File Requirements Info -->
        <div class="file-requirements">
            <h4>📋 File Requirements</h4>
            <ul>
                <li><strong>CSV:</strong> Flight data with columns: flightNumber, departureLocation, arrivalLocation, fare, etc.</li>
                <li><strong>Excel:</strong> First sheet with flight data in tabular format</li>
                <li><strong>Word:</strong> Structured flight data in table format</li>
                <li><strong>XML:</strong> Valid XML with flight elements</li>
                <li><strong>Max file size:</strong> 10MB</li>
            </ul>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            // Handle file selection
            $('#fileInput').change(function() {
                var file = this.files[0];
                if (file) {
                    displayFileInfo(file);
                    validateFileType(file);
                } else {
                    hideFileInfo();
                }
            });
            
            // Handle form submission
            $('#uploadForm').submit(function(e) {
                e.preventDefault();
                uploadFile();
            });
        });
        
        function displayFileInfo(file) {
            $('#fileName').text(file.name);
            $('#fileSize').text(formatFileSize(file.size));
            $('#fileTypeDisplay').text(file.type || 'Unknown'); 
            $('#fileInfo').slideDown();
        }

        function hideFileInfo() {
            $('#fileInfo').slideUp();
        }
        
        function validateFileType(file) {
            var selectedType = $('#fileType').val();
            var fileName = file.name.toLowerCase();
            
            var validExtensions = {
                'csv': ['.csv'],
                'excel': ['.xlsx', '.xls'],
                'word': ['.docx', '.doc'],
                'xml': ['.xml']
            };
            
            if (selectedType && validExtensions[selectedType]) {
                var isValid = validExtensions[selectedType].some(ext => fileName.endsWith(ext));
                if (!isValid) {
                    showMessage('error', '❌ File type mismatch. Please select a ' + selectedType.toUpperCase() + ' file.');
                    $('#submitBtn').prop('disabled', true);
                    return;
                }
            }
            
            $('#submitBtn').prop('disabled', false);
            clearMessages();
        }
        
        function uploadFile() {
            var formData = new FormData();
            var fileInput = document.getElementById('fileInput');
            var file = fileInput.files[0];
            var fileType = $('#fileType').val();
            
            if (!file || !fileType) {
                showMessage('error', '❌ Please select both file type and file');
                return;
            }
            
            formData.append('file', file);
            formData.append('fileType', fileType);
            
            // Show loading
            $('#loadingArea').show();
            $('#submitBtn').prop('disabled', true);
            clearMessages();
            
            $.ajax({
                url: '${pageContext.request.contextPath}/admin/uploadFile',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    $('#loadingArea').hide();
                    $('#submitBtn').prop('disabled', false);
                    
                    if (response.status === 'success') {
                        showMessage('success', '✅ ' + response.message);
                        resetForm();
                    } else {
                        showMessage('error', '❌ ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    $('#loadingArea').hide();
                    $('#submitBtn').prop('disabled', false);
                    
                    var errorMessage = 'Failed to upload file';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showMessage('error', '❌ ' + errorMessage);
                }
            });
        }
        
        function showMessage(type, message) {
            var messageClass = 'message message-' + type;
            var messageHtml = '<div class="' + messageClass + '">' + message + '</div>';
            
            $('#messageArea').html(messageHtml);
            $('.message').slideDown();
            
            // Auto-hide success messages after 5 seconds
            if (type === 'success') {
                setTimeout(function() {
                    $('.message').slideUp();
                }, 5000);
            }
        }
        
        function clearMessages() {
            $('#messageArea').empty();
        }
        
        function resetForm() {
            $('#uploadForm')[0].reset();
            hideFileInfo();
        }
        
        function formatFileSize(bytes) {
            if (bytes === 0) return '0 Bytes';
            var k = 1024;
            var sizes = ['Bytes', 'KB', 'MB', 'GB'];
            var i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
