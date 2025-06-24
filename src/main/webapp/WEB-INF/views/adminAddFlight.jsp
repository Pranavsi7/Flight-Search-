<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add New Flight</title>
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <link rel="stylesheet" href="<c:url value='/resources/css/admin.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="<c:url value='/resources/js/admin.js'/>"></script>
    <style>
        .admin-container {
            max-width: 600px;
            margin: 40px auto;
            padding: 30px;
            background: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .admin-title {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 2rem;
        }
        
        .admin-form input, .admin-form select {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        
        .admin-form input:focus, .admin-form select:focus {
            outline: none;
            border-color: #007bff;
        }
        
        .submit-button {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
            border: none;
            padding: 15px 30px;
            font-size: 1.1rem;
            font-weight: bold;
            cursor: pointer;
            border-radius: 6px;
            width: 100%;
            transition: all 0.3s;
        }
        
        .submit-button:hover {
            background: linear-gradient(135deg, #0056b3, #007bff);
            transform: translateY(-2px);
        }
        
        .submit-button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }
        
        .message {
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-weight: bold;
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
        
        .loading {
            display: none;
            text-align: center;
            padding: 20px;
            color: #666;
        }
        
        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #007bff;
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
    </style>
</head>
<body>
    <div class="admin-container">
        <h2 class="admin-title">✈️ Add New Flight</h2>
        
        <!-- Message Area -->
        <div id="messageArea"></div>
        
        <!-- Loading Indicator -->
        <div id="loadingArea" class="loading">
            <div class="spinner"></div>
            <p>Adding flight...</p>
        </div>
        
        <!-- Form -->
        <form id="addFlightForm" class="admin-form">
            <input type="text" id="flightNumber" name="flightNumber" placeholder="Flight Number e.g. AI1234" required>
            <input type="text" id="departureLocation" name="departureLocation" placeholder="Departure Location e.g. DEL" required>
            <input type="text" id="arrivalLocation" name="arrivalLocation" placeholder="Arrival Location e.g. MUB" required>
            <input type="number" id="fare" name="fare" placeholder="Fare e.g. 5000" step="0.01" required>
            <input type="time" id="departureTime" name="departureTime" placeholder="Departure Time" required>
            <input type="text" id="duration" name="duration" placeholder="Duration HH:MM e.g. 02:30" pattern="[0-9]{2}:[0-9]{2}" required>
            <input type="date" id="validTill" name="validTill" required>
            <select id="classType" name="classType" required>
                <option value="">Select Class Type</option>
                <option value="Economy">Economy</option>
                <option value="Business">Business</option>
                <option value="Premium Economy">Premium Economy</option>
            </select>
            <button type="submit" id="submitBtn" class="submit-button">
                🚀 Add Flight
            </button>
        </form>
    </div>

    <script>
        $(document).ready(function() {
            $('#addFlightForm').submit(function(e) {
                e.preventDefault();
                addFlight();
            });
        });
        
        function addFlight() {
            // Show loading
            $('#loadingArea').show();
            $('#submitBtn').prop('disabled', true);
            clearMessages();
            
            // Collect form data
            var formData = {
                flightNumber: $('#flightNumber').val(),
                departureLocation: $('#departureLocation').val(),
                arrivalLocation: $('#arrivalLocation').val(),
                fare: parseFloat($('#fare').val()),
                departureTime: $('#departureTime').val(),
                duration: $('#duration').val(),
                validTill: $('#validTill').val(),
                flightClass: $('#classType').val() 
            };
            
            $.ajax({
                url: '${pageContext.request.contextPath}/admin/addFlight',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
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
                    
                    var errorMessage = 'Failed to add flight';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    } else if (xhr.responseText) {
                        errorMessage = 'Server error occurred';
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
        
        function resetForm() {
            $('#addFlightForm')[0].reset();
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
