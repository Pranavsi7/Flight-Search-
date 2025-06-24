<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Delete Flight</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        /* Main Layout */
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            flex-direction: column;
        }

        .content {
            flex: 1 0 auto;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        /* Container Styling */
        .container {
            max-width: 600px;
            width: 100%;
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            padding: 40px;
            text-align: center;
        }

        /* Header Styling */
        .header {
            margin-bottom: 30px;
        }

        .header h2 {
            color: #333;
            font-size: 2.2rem;
            margin: 0;
            font-weight: 600;
        }

        .header p {
            color: #666;
            font-size: 1rem;
            margin: 10px 0 0 0;
        }

        /* Form Styling */
        .form-group {
            margin-bottom: 25px;
            text-align: left;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
            font-size: 1.1rem;
        }

        .form-control {
            width: 100%;
            padding: 15px;
            font-size: 1rem;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            box-sizing: border-box;
            transition: all 0.3s ease;
            background: #f8f9fa;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        /* Button Styling */
        .btn {
            padding: 15px 30px;
            font-size: 1.1rem;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
            margin: 10px;
            min-width: 120px;
        }

        .btn-danger {
            background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            color: white;
        }

        .btn-danger:hover {
            background: linear-gradient(135deg, #ee5a52, #ff6b6b);
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(238, 90, 82, 0.3);
        }

        .btn-secondary {
            background: linear-gradient(135deg, #6c757d, #5a6268);
            color: white;
        }

        .btn-secondary:hover {
            background: linear-gradient(135deg, #5a6268, #6c757d);
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(108, 117, 125, 0.3);
        }

        .btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none !important;
        }

        /* Message Styling */
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

        /* Flight Details Card */
        .flight-details {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
            display: none;
        }

        .flight-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin-bottom: 15px;
        }

        .flight-info-item {
            display: flex;
            flex-direction: column;
        }

        .flight-info-label {
            font-weight: 600;
            color: #495057;
            font-size: 0.9rem;
            margin-bottom: 5px;
        }

        .flight-info-value {
            color: #333;
            font-size: 1rem;
        }

        /* Loading Animation */
        .loading {
            display: none;
            text-align: center;
            padding: 20px;
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

        /* Responsive Design */
        @media (max-width: 768px) {
            .container {
                margin: 20px;
                padding: 30px 20px;
            }

            .flight-info {
                grid-template-columns: 1fr;
            }

            .header h2 {
                font-size: 1.8rem;
            }
        }

        /* Footer */
        footer {
            flex-shrink: 0;
            background: #333;
            color: #fff;
            text-align: center;
            padding: 15px 0;
        }
        select, option {
    color: #222 !important;
    background: #fff !important;
}
        
    </style>
</head>
<body>
    <div class="content">
        <div class="container">
            <div class="header">
                <h2>🗑️ Delete Flight</h2>
                <p>Select a flight to remove from the system</p>
            </div>
            
            <!-- Message Area -->
            <div id="messageArea"></div>
            
            <!-- Loading Indicator -->
            <div id="loadingArea" class="loading">
                <div class="spinner"></div>
                <p>Processing...</p>
            </div>
            
            <!-- Flight Selection Form -->
            <div class="form-group">
    <label for="flightSelect">Choose Flight:</label>
    <select id="flightSelect" class="form-control" name="flightId">
        <option value="" disabled selected>-- Select a flight to delete --</option>
        <c:forEach var="flight" items="${allFlights}">
            <option value="${flight.id}" 
                    data-flight-number="${flight.flightNumber}"
                    data-departure="${flight.departureLocation}"
                    data-arrival="${flight.arrivalLocation}"
                    data-fare="${flight.fare}">
                ${flight.flightNumber} - ${flight.departureLocation} → ${flight.arrivalLocation}
            </option>
        </c:forEach>
    </select>
</div>

            
            <!-- Flight Details Card -->
            <div id="flightDetails" class="flight-details">
                <h4>⚠️ Flight Details to Delete</h4>
                <div class="flight-info">
                    <div class="flight-info-item">
                        <span class="flight-info-label">Flight Number</span>
                        <span class="flight-info-value" id="detailFlightNumber">-</span>
                    </div>
                    <div class="flight-info-item">
                        <span class="flight-info-label">Route</span>
                        <span class="flight-info-value" id="detailRoute">-</span>
                    </div>
                    <div class="flight-info-item">
                        <span class="flight-info-label">Fare</span>
                        <span class="flight-info-value" id="detailFare">-</span>
                    </div>
                    <div class="flight-info-item">
                        <span class="flight-info-label">Total Seats</span>
                        <span class="flight-info-value" id="detailSeats">-</span>
                    </div>
                </div>
                <p style="color: #dc3545; font-weight: 500; margin-top: 15px;">
                    ⚠️ This action cannot be undone. Are you sure you want to delete this flight?
                </p>
            </div>
            
            <!-- Action Buttons -->
            <div style="margin-top: 30px;">
                <button type="button" id="deleteBtn" class="btn btn-danger" disabled>
                    🗑️ Delete Flight
                </button>
               
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            // Handle flight selection
            $('#flightSelect').change(function() {
                var selectedOption = $(this).find('option:selected');
                var flightId = selectedOption.val();
                
                if (flightId) {
                    showFlightDetails(selectedOption);
                    $('#deleteBtn').prop('disabled', false);
                } else {
                    hideFlightDetails();
                    $('#deleteBtn').prop('disabled', true);
                }
                clearMessages();
            });
            
            // Handle delete button click
            $('#deleteBtn').click(function() {
                var flightId = $('#flightSelect').val();
                var flightNumber = $('#flightSelect').find('option:selected').data('flight-number');
                
                if (confirm('Are you absolutely sure you want to delete flight ' + flightNumber + '?')) {
                    deleteFlight(flightId);
                }
            });
        });
        
        function showFlightDetails(selectedOption) {
            $('#detailFlightNumber').text(selectedOption.data('flight-number'));
            $('#detailRoute').text(selectedOption.data('departure') + ' → ' + selectedOption.data('arrival'));
            $('#detailFare').text('$' + selectedOption.data('fare'));
            $('#detailSeats').text(selectedOption.data('seats'));
            $('#flightDetails').slideDown();
        }
        
        function hideFlightDetails() {
            $('#flightDetails').slideUp();
        }
        
        function deleteFlight(flightId) {
            // Show loading
            $('#loadingArea').show();
            $('#deleteBtn').prop('disabled', true);
            clearMessages();
            
            $.ajax({
                url: '${pageContext.request.contextPath}/admin/deleteFlight',
                type: 'POST',
                data: { flightId: flightId },
                success: function(response) {
                    $('#loadingArea').hide();
                    
                    if (response.status === 'success') {
                        showMessage('success', '✅ ' + response.message);
                        removeFlightFromDropdown(flightId);
                        resetForm();
                    } else {
                        showMessage('error', '❌ ' + response.message);
                        $('#deleteBtn').prop('disabled', false);
                    }
                },
                error: function(xhr, status, error) {
                    $('#loadingArea').hide();
                    $('#deleteBtn').prop('disabled', false);
                    
                    var errorMessage = 'Failed to delete flight';
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
        
        function removeFlightFromDropdown(flightId) {
            $('#flightSelect option[value="' + flightId + '"]').remove();
        }
        
        function resetForm() {
            $('#flightSelect').val('');
            hideFlightDetails();
            $('#deleteBtn').prop('disabled', true);
            
            // Check if no flights remain
            if ($('#flightSelect option').length <= 1) {
                $('#flightSelect').prop('disabled', true);
                showMessage('error', 'ℹ️ No more flights available to delete');
            }
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
