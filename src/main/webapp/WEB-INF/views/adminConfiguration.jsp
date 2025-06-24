<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Configuration File</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            flex-direction: column;
        }

        .container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            flex-grow: 1;
        }

        h2 {
            text-align: center;
            color: #2d3748;
            font-size: 2.2rem;
            font-weight: 700;
            margin-bottom: 30px;
            letter-spacing: 1px;
        }

        .form-container {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .form-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 20px;
        }

        .form-header h3 {
            margin: 0;
            font-size: 1.3rem;
            font-weight: 600;
        }

        .form-header p {
            margin: 8px 0 0 0;
            font-size: 0.95rem;
            opacity: 0.9;
        }

        .textarea-container {
            position: relative;
            border: 2px solid #e2e8f0;
            border-radius: 10px;
            overflow: hidden;
            transition: border-color 0.3s ease;
        }

        .textarea-container:focus-within {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .textarea-header {
            background: #f7fafc;
            padding: 12px 20px;
            border-bottom: 1px solid #e2e8f0;
            font-size: 0.9rem;
            color: #4a5568;
            font-weight: 600;
        }

        textarea {
            width: 100%;
            height: 400px;
            padding: 20px;
            font-size: 1rem;
            border: none;
            resize: vertical;
            box-sizing: border-box;
            font-family: 'Courier New', monospace;
            line-height: 1.6;
            background: #fafafa;
            color: #2d3748;
            outline: none;
        }

        textarea:focus {
            background: white;
        }

        .button-container {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 20px;
        }

        .btn {
            padding: 12px 30px;
            font-size: 1.1rem;
            font-weight: 600;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            min-width: 120px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .btn-save {
            background: linear-gradient(135deg, #48bb78, #38a169);
            color: white;
        }

        .btn-save:hover {
            background: linear-gradient(135deg, #38a169, #48bb78);
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(72, 187, 120, 0.3);
        }

        .btn-save:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }

        .btn-reset {
            background: linear-gradient(135deg, #a0aec0, #718096);
            color: white;
        }

        .btn-reset:hover {
            background: linear-gradient(135deg, #718096, #a0aec0);
            transform: translateY(-2px);
        }

        .message {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
            text-align: center;
            display: none;
        }

        .message-success {
            background: #f0fff4;
            color: #22543d;
            border: 1px solid #9ae6b4;
        }

        .message-error {
            background: #fed7d7;
            color: #742a2a;
            border: 1px solid #fc8181;
        }

        .loading {
            display: none;
            text-align: center;
            padding: 20px;
            color: #666;
        }

        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #667eea;
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

        .file-info {
            background: #edf2f7;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
        }

        .file-info h4 {
            margin: 0 0 8px 0;
            color: #2d3748;
            font-size: 1.1rem;
        }

        .file-info p {
            margin: 0;
            color: #4a5568;
            font-size: 0.9rem;
        }

        @media (max-width: 768px) {
            .container {
                margin: 20px;
                padding: 20px;
            }
            
            textarea {
                height: 300px;
                padding: 15px;
            }
            
            .button-container {
                flex-direction: column;
            }
            
            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>⚙️ Configuration Manager</h2>
        
        <!-- Message Area -->
        <div id="messageArea"></div>
        
        <!-- Loading Indicator -->
        <div id="loadingArea" class="loading">
            <div class="spinner"></div>
            <p>Saving configuration...</p>
        </div>
        
        <!-- Form Header -->
        <div class="form-header">
            <h3>✏️ Edit Application Properties</h3>
            <p>Modify configuration settings for the Flight Search application</p>
        </div>
        
        <!-- Configuration Form -->
        <div class="form-container">
            <form id="configForm">
                <div class="textarea-container">
                    <div class="textarea-header">
                        💾 Configuration Content
                    </div>
                    <textarea 
                        id="configContent" 
                        name="configContent" 
                        placeholder="Enter configuration properties here..."
                        spellcheck="false">${configContent}</textarea>
                </div>
                
                <div class="button-container">
                    <button type="submit" id="saveBtn" class="btn btn-save">
                        💾 Save Configuration
                    </button>
                    <button type="button" id="resetBtn" class="btn btn-reset">
                        🔄 Reset
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            var originalContent = $('#configContent').val();
            
            // Handle form submission
            $('#configForm').submit(function(e) {
                e.preventDefault();
                saveConfiguration();
            });
            
            // Handle reset button
            $('#resetBtn').click(function() {
                if (confirm('Are you sure you want to reset all changes?')) {
                    $('#configContent').val(originalContent);
                    showMessage('success', '🔄 Configuration reset to original state');
                }
            });
            
            // Auto-save indicator (optional)
            var saveTimeout;
            $('#configContent').on('input', function() {
                clearTimeout(saveTimeout);
                // Optional: Show "unsaved changes" indicator
            });
        });
        
        function saveConfiguration() {
            var configContent = $('#configContent').val().trim();
            
            if (!configContent) {
                showMessage('error', '❌ Configuration content cannot be empty');
                return;
            }
            
            // Show loading
            $('#loadingArea').show();
            $('#saveBtn').prop('disabled', true);
            clearMessages();
            
            $.ajax({
                url: '${pageContext.request.contextPath}/admin/configuration',
                type: 'POST',
                data: {
                    configContent: configContent
                },
                success: function(response) {
                    $('#loadingArea').hide();
                    $('#saveBtn').prop('disabled', false);
                    
                    if (response.status === 'success') {
                        showMessage('success', '✅ ' + response.message);
                        // Update original content for reset functionality
                        originalContent = configContent;
                    } else {
                        showMessage('error', '❌ ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    $('#loadingArea').hide();
                    $('#saveBtn').prop('disabled', false);
                    
                    var errorMessage = 'Failed to save configuration';
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
            
            // Auto-hide success messages after 4 seconds
            if (type === 'success') {
                setTimeout(function() {
                    $('.message').slideUp();
                }, 4000);
            }
        }
        
        function clearMessages() {
            $('#messageArea').empty();
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
