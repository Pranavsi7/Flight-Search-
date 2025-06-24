<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Edit Flight</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f7f7f7;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        
        .form-group input, .form-group select {
            width: 100%;
            padding: 10px;
            font-size: 1rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        
        .btn {
            padding: 10px 20px;
            font-size: 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 10px;
        }
        
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        
        .btn-success {
            background-color: #28a745;
            color: white;
        }
        
        .message-success {
            color: green;
            font-weight: bold;
            padding: 10px;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        
        .message-error {
            color: red;
            font-weight: bold;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        
        #editForm {
            display: none;
        }
        
        #loadingMessage {
            display: none;
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="content">
        <div class="container">
            <h2>Edit Flight</h2>
            <!-- Flash messages from server -->
<c:if test="${not empty success}">
    <div class="message-success">${success}</div>
</c:if>
<c:if test="${not empty error}">
    <div class="message-error">${error}</div>
</c:if>
            <!-- Message display area -->
            <div id="messageArea"></div>
            
            <!-- Flight selection -->
            <div class="form-group">
                <label for="flightSelect">Select Flight:</label>
                <select id="flightSelect" name="flightId">
                    <option value="" disabled selected>Select a flight</option>
                    <c:forEach var="flight" items="${allFlights}">
                        <option value="${flight.id}">${flight.flightNumber} - ${flight.departureLocation} to ${flight.arrivalLocation}</option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Loading message -->
            <div id="loadingMessage">Loading flight details...</div>
            
<div id="editForm">
    <h3>Edit Flight Details</h3>
    <form id="flightEditForm">
        <input type="hidden" id="flightId" name="id" />

        <div class="form-group">
            <label for="flightNumber">Flight Number:</label>
            <input type="text" id="flightNumber" name="flightNumber" required />
        </div>

        <div class="form-group">
            <label for="departureLocation">Departure Location:</label>
            <input type="text" id="departureLocation" name="departureLocation" required />
        </div>

        <div class="form-group">
            <label for="arrivalLocation">Arrival Location:</label>
            <input type="text" id="arrivalLocation" name="arrivalLocation" required />
        </div>

        <div class="form-group">
            <label for="fare">Fare:</label>
            <input type="number" id="fare" name="fare" step="0.01" required />
        </div>

        <div class="form-group">
            <label for="departureTime">Departure Time:</label>
            <input type="time" id="departureTime" name="departureTime" required />
        </div>

        <div class="form-group">
            <label for="duration">Duration (HH:MM):</label>
            <input type="text" id="duration" name="duration" placeholder="02:30" required />
        </div>

        <div class="form-group">
            <label for="validTill">Valid Till:</label>
            <input type="date" id="validTill" name="validTill" required />
        </div>

        <div class="form-group">
            <label for="flightClass">Select Class Type:</label>
            <select id="flightClass" name="flightClass" required>
                <option value="">Select Class Type</option>
                <option value="ECONOMY">Economy</option>
                <option value="BUSINESS">Business</option>
               <option value="PREMIUM ECONOMY">Premium economy</option>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Update Flight</button>
        <button type="button" class="btn btn-primary" onclick="resetForm()">Cancel</button>
    </form>
</div>

        </div>
    </div>

    <script>
        $(document).ready(function() {
            // Handle flight selection
            $('#flightSelect').change(function() {
                var flightId = $(this).val();
                if (flightId) {
                    loadFlightDetails(flightId);
                } else {
                    $('#editForm').hide();
                    $('#messageArea').empty();
                }
            });
            
            // Handle form submission
            $('#flightEditForm').submit(function(e) {
                e.preventDefault();
                updateFlight();
            });
        });
        
        function loadFlightDetails(flightId) {
            console.log("=== LOADING FLIGHT DETAILS ===");
            console.log("Flight ID:", flightId);
            
            $('#loadingMessage').show();
            $('#editForm').hide();
            $('#messageArea').empty();
            
            $.ajax({
                url: '<c:url value="/admin/getFlightDetails"/>',
                type: 'GET',
                data: { flightId: flightId },
                success: function(flight) {
                    console.log("=== AJAX SUCCESS ===");
                    console.log("Response received:", flight);
                    
                    if (flight && flight.id) {
                        populateForm(flight);
                        $('#loadingMessage').hide();
                        $('#editForm').show();
                        console.log("Form populated and shown");
                    } else {
                        console.error("Invalid flight data received:", flight);
                        $('#loadingMessage').hide();
                        showMessage('error', 'Invalid flight data received');
                    }
                },
                error: function(xhr, status, error) {
                    console.log("=== AJAX ERROR ===");
                    console.log("Status:", status);
                    console.log("Error:", error);
                    console.log("Response:", xhr.responseText);
                    
                    $('#loadingMessage').hide();
                    showMessage('error', 'Failed to load flight details: ' + error);
                }
            });
        }
        
        function populateForm(flight) {
            $('#flightId').val(flight.id);
            $('#flightNumber').val(flight.flightNumber);
            $('#departureLocation').val(flight.departureLocation);
            $('#arrivalLocation').val(flight.arrivalLocation);
            $('#fare').val(flight.fare);

            // Set departure time directly if it's already in HH:mm format
           if (flight.departureTime) {
    // Only use HH:mm part
    let time = flight.departureTime.substring(0, 5);
    $('#departureTime').val(time);
}


            $('#duration').val(flight.duration || '');

            // Set validTill directly if it's in yyyy-MM-dd format
            if (flight.validTill) {
                $('#validTill').val(flight.validTill);
            }

            $('#flightClass').val(flight.flightClass || '');
        }

        
        function updateFlight() {
            console.log("=== UPDATING FLIGHT ===");
            
            var formData = {
                id: $('#flightId').val(),
                flightNumber: $('#flightNumber').val(),
                departureLocation: $('#departureLocation').val(),
                arrivalLocation: $('#arrivalLocation').val(),
                departureTime: $('#departureTime').val(),
                duration: $('#duration').val(),
                fare: parseFloat($('#fare').val()),
                validTill: $('#validTill').val(),
                flightClass: $('#flightClass').val()
            };
            
            console.log("Form data being sent:", formData);
            
            $.ajax({
                url: '${pageContext.request.contextPath}/admin/updateFlight',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) {
                    console.log("Update response:", response);
                    
                    if (response.status === 'success') {
                        // Show success message
                        showMessage('success', response.message);
                        
                        
                        $('#editForm').hide();
                        $('#loadingMessage').hide();
                        $('#flightSelect').val('');
                        
                        
                        console.log("Flight updated successfully");
                    } else {
                        showMessage('error', response.message || 'Update failed');
                    }

                },
                error: function(xhr, status, error) {
                    console.error("Update failed:", xhr.responseText);
                    showMessage('error', 'Failed to update flight: ' + error);
                }
            });
        }


        function showMessage(type, message) {
            var messageClass = type === 'success' ? 'message-success' : 'message-error';
            $('#messageArea').html('<div class="' + messageClass + '">' + message + '</div>');
            
            if (type === 'success') {
                setTimeout(function() {
                    location.reload();
                }, 3000); // Wait 3 seconds, then reload
            }
        }

        
        function resetForm() {
            $('#flightSelect').val('');
            $('#editForm').hide();
            $('#messageArea').empty();
        }
        
        function formatDateTime(dateTime) {
          
            if (dateTime) {
                var date = new Date(dateTime);
                return date.toISOString().slice(0, 16);
            }
            return '';
        }
        
        function refreshFlightList() {
 
            location.reload();
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
