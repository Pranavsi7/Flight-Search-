<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Upload History</title>
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
            max-width: 1200px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            flex-grow: 1;
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
        }

        .header h1 {
            color: #2d3748;
            font-size: 2.5rem;
            font-weight: 700;
            margin: 0;
            letter-spacing: 1px;
        }

        .tabs {
            display: flex;
            justify-content: center;
            margin-bottom: 30px;
            background: #f7fafc;
            border-radius: 10px;
            padding: 5px;
        }

        .tab {
            padding: 12px 25px;
            margin: 0 5px;
            background: transparent;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            color: #4a5568;
            transition: all 0.3s ease;
        }

        .tab.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            transform: translateY(-2px);
        }

        .history-container {
            background: #f8f9fa;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .table-container {
            overflow-x: auto;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 10px;
            overflow: hidden;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th, td {
            padding: 15px 20px;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
        }

        th {
            font-weight: 600;
            letter-spacing: 0.5px;
            text-transform: uppercase;
            font-size: 0.9rem;
        }

        tbody tr {
            transition: all 0.3s ease;
        }

        tbody tr:hover {
            background: #f7fafc;
            transform: translateY(-1px);
        }

        .file-name {
            font-weight: 600;
            color: #2d3748;
        }

        .file-type {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
        }

        .type-csv { background: #bee3f8; color: #2b6cb0; }
        .type-excel { background: #c6f6d5; color: #2f855a; }
        .type-word { background: #fed7d7; color: #c53030; }
        .type-xml { background: #fbb6ce; color: #b83280; }

        .stats {
            text-align: center;
            font-weight: 600;
        }

        .imported { color: #22543d; }
        .skipped { color: #c53030; }

        .action-btn {
            background: linear-gradient(135deg, #fc8181, #f56565);
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 0.85rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .action-btn:hover {
            background: linear-gradient(135deg, #e53e3e, #fc8181);
            transform: translateY(-1px);
        }

        .controls {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
            padding: 20px;
            background: #f7fafc;
            border-radius: 10px;
        }

        .clear-btn {
            background: linear-gradient(135deg, #a0aec0, #718096);
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .clear-btn:hover {
            background: linear-gradient(135deg, #718096, #a0aec0);
            transform: translateY(-2px);
        }

        .stats-summary {
            display: flex;
            gap: 30px;
            align-items: center;
        }

        .stat-item {
            text-align: center;
        }

        .stat-number {
            font-size: 1.5rem;
            font-weight: bold;
            color: #667eea;
        }

        .stat-label {
            font-size: 0.9rem;
            color: #4a5568;
            margin-top: 2px;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #718096;
        }

        .empty-state h3 {
            margin: 0 0 10px 0;
            font-size: 1.5rem;
        }

        .message {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
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

        @media (max-width: 768px) {
            .container {
                margin: 15px;
                padding: 20px;
            }
            
            .tabs {
                flex-wrap: wrap;
            }
            
            .controls {
                flex-direction: column;
                gap: 15px;
            }
            
            .stats-summary {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📁 Upload History</h1>
        </div>

        

        <!-- Message Area -->
        <div id="messageArea"></div>

        <!-- History Content -->
        <div class="history-container">
            <c:choose>
                <c:when test="${not empty uploadHistory}">
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>File Name</th>
                                    <th>Type</th>
                                    <th>Flights Imported</th>
                                    <th>Lines Skipped</th>
                                    <th>Upload Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id="historyTable">
                                <c:forEach var="upload" items="${uploadHistory}">
                                    <tr data-id="${upload.id}">
                                        <td class="file-name">${upload.fileName}</td>
                                        <td>
                                            <span class="file-type type-${upload.fileType.toLowerCase()}">
                                                ${upload.fileType}
                                            </span>
                                        </td>
                                        <td class="stats imported">${upload.flightsImported}</td>
                                        <td class="stats skipped">${upload.linesSkipped}</td>
                                        <td>
                                            ${upload.formattedUploadDate}

                                        </td>
                                        <td>
                                            <button class="action-btn" onclick="deleteUpload(${upload.id})">
                                                🗑️ Delete
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Controls and Summary -->
                    <div class="controls">
                        <div class="stats-summary">
                            <div class="stat-item">
                                <div class="stat-number" id="totalFiles">${uploadHistory.size()}</div>
                                <div class="stat-label">Total Files</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number" id="totalFlights">
                                    <c:set var="totalImported" value="0"/>
                                    <c:forEach var="upload" items="${uploadHistory}">
                                        <c:set var="totalImported" value="${totalImported + upload.flightsImported}"/>
                                    </c:forEach>
                                    ${totalImported}
                                </div>
                                <div class="stat-label">Total Flights</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number" id="totalSkipped">
                                    <c:set var="totalSkipped" value="0"/>
                                    <c:forEach var="upload" items="${uploadHistory}">
                                        <c:set var="totalSkipped" value="${totalSkipped + upload.linesSkipped}"/>
                                    </c:forEach>
                                    ${totalSkipped}
                                </div>
                                <div class="stat-label">Lines Skipped</div>
                            </div>
                        </div>
                        
                        <button class="clear-btn" onclick="clearHistory()">
                            🗑️ Clear All
                        </button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>📭 No Upload History</h3>
                        <p>Upload some flight files to see the history here.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script>
        function deleteUpload(uploadId) {
            if (confirm('Are you sure you want to delete this upload record?')) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/admin/history/delete/' + uploadId,
                    type: 'DELETE',
                    success: function(response) {
                        if (response.status === 'success') {
                            $('tr[data-id="' + uploadId + '"]').fadeOut(300, function() {
                                $(this).remove();
                                updateStats();
                            });
                            showMessage('success', '✅ Upload record deleted successfully');
                        } else {
                            showMessage('error', '❌ ' + response.message);
                        }
                    },
                    error: function() {
                        showMessage('error', '❌ Failed to delete upload record');
                    }
                });
            }
        }
        
        function clearHistory() {
            if (confirm('Are you sure you want to clear all upload history? This action cannot be undone.')) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/admin/history/clear',
                    type: 'DELETE',
                    success: function(response) {
                        if (response.status === 'success') {
                            location.reload();
                        } else {
                            showMessage('error', '❌ ' + response.message);
                        }
                    },
                    error: function() {
                        showMessage('error', '❌ Failed to clear history');
                    }
                });
            }
        }
        
        function updateStats() {
            var totalFiles = $('#historyTable tr').length;
            var totalFlights = 0;
            var totalSkipped = 0;
            
            $('#historyTable tr').each(function() {
                totalFlights += parseInt($(this).find('.imported').text()) || 0;
                totalSkipped += parseInt($(this).find('.skipped').text()) || 0;
            });
            
            $('#totalFiles').text(totalFiles);
            $('#totalFlights').text(totalFlights);
            $('#totalSkipped').text(totalSkipped);
            
            if (totalFiles === 0) {
                $('.history-container').html('<div class="empty-state"><h3>📭 No Upload History</h3><p>Upload some flight files to see the history here.</p></div>');
            }
        }
        
        function showMessage(type, message) {
            var messageClass = 'message message-' + type;
            var messageHtml = '<div class="' + messageClass + '">' + message + '</div>';
            
            $('#messageArea').html(messageHtml);
            $('.message').slideDown();
            
            if (type === 'success') {
                setTimeout(function() {
                    $('.message').slideUp();
                }, 3000);
            }
        }
        
        // Auto-refresh every 30 seconds to show new uploads
        setInterval(function() {
            location.reload();
        }, 30000);
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
