<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard - SearchFlights</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .header {
            background: rgba(255, 255, 255, 0.95);
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .logo {
            font-size: 1.8rem;
            font-weight: bold;
            color: #333;
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        
        .logout-btn {
            background: #dc3545;
            color: white;
            padding: 8px 20px;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s;
        }
        
        .logout-btn:hover {
            background: #c82333;
        }
        
        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }
        
        .search-section {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .search-form {
            padding: 30px;
            border: 2px solid #ccc;
            margin: 20px;
            border-radius: 10px;
            background: #f8f9fa;
        }
        
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            align-items: center;
        }
        
        .form-row label {
            font-weight: 600;
            color: #333;
            min-width: 150px;
        }
        
        .form-control {
            flex: 1;
            padding: 8px 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
        }
        
        .checkbox-row {
            display: flex;
            gap: 30px;
            margin: 20px 0;
            align-items: center;
        }
        
        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .checkbox-group input[type="checkbox"] {
            width: 16px;
            height: 16px;
        }
        
        .button-row {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 20px;
        }
        
        .btn {
            padding: 10px 30px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-search {
            background: #28a745;
            color: white;
        }
        
        .btn-search:hover {
            background: #218838;
        }
        
        .btn-search:disabled {
            background: #6c757d;
            cursor: not-allowed;
        }
        
        .btn-cancel {
            background: #6c757d;
            color: white;
        }
        
        .btn-cancel:hover {
            background: #5a6268;
        }
        
        .results-section {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            display: none;
        }
        
        .results-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px;
            width: calc(100% - 40px);
        }
        
        .results-table th {
            background: #f8f9fa;
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
            font-weight: 600;
            color: #333;
        }
        
        .results-table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }
        
        .results-table tr:hover {
            background: #f1f3f5;
        }
        
        .flight-number {
            font-weight: 600;
            color: #333;
        }
        
        .fare {
            font-weight: 600;
            color: #28a745;
        }
        
        .duration {
            color: #666;
        }
        
        .class-cell {
            font-weight: 600;
            color: #333;
        }
        
        .loading {
            text-align: center;
            padding: 40px;
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
        
        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 8px;
            margin: 20px;
            border: 1px solid #f5c6cb;
        }
        
        .no-results {
            text-align: center;
            padding: 40px;
            color: #666;
            font-size: 1.1rem;
        }
        
        .scrollable-results {
            max-height: 300px;
            overflow-y: auto;
            border: 1px solid #ddd;
            margin: 20px;
        }
        
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
                align-items: flex-start;
            }
            
            .form-row label {
                min-width: auto;
                margin-bottom: 5px;
            }
            
            .checkbox-row {
                flex-direction: column;
                gap: 15px;
            }
            
            .container {
                padding: 0 10px;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="logo">✈️ SearchFlights</div>
        <div class="user-info">
            <span>Welcome, <strong>${sessionScope.username}</strong></span>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
        </div>
    </div>

    <div class="container">
        <!-- Search Section -->
        <div class="search-section">
            <form id="flightSearchForm" class="search-form">
                <!-- Location Row -->
                <div class="form-row">
                    <label for="departureLocation">Departure Location</label>
                    <input type="text" id="departureLocation" name="departureLocation" 
                           class="form-control" placeholder="e.g., DEL" required maxlength="3">
                    
                    <label for="arrivalLocation" style="margin-left: 20px;">Arrival Location</label>
                    <input type="text" id="arrivalLocation" name="arrivalLocation" 
                           class="form-control" placeholder="e.g., MUM" required maxlength="3">
                </div>
                
                <!-- Date and Class Row -->
                <div class="form-row">
                    <label for="departureDate">Departure Date</label>
                    <input type="date" id="departureDate" name="departureDate" 
                           class="form-control" required style="max-width: 200px;">
                    
                    <label for="flightClass" style="margin-left: 20px;">Flight Class</label>
                    <select id="flightClass" name="flightClass" class="form-control" style="max-width: 200px;">
                        <option value="">All Classes</option>
                        <option value="Economy">Economy</option>
                        <option value="Business">Business</option>
                        <option value="Premium Economy">Premium Economy</option>
                        
                        
                    </select>
                </div>
                
                <!-- Sort Options -->
                <div class="checkbox-row">
                    <div class="checkbox-group">
                        <input type="checkbox" id="sortByFare" name="sortPreference" value="0" checked>
                        <label for="sortByFare">Sort by Fare</label>
                    </div>
                    <div class="checkbox-group">
                        <input type="checkbox" id="sortByDuration" name="sortPreference" value="1">
                        <label for="sortByDuration">Sort by Duration</label>
                    </div>
                </div>
                
                <!-- Buttons -->
                <div class="button-row">
                    <button type="submit" id="searchBtn" class="btn btn-search">
                        Search
                    </button>
                    <button type="button" id="cancelBtn" class="btn btn-cancel">
                        Cancel
                    </button>
                </div>
            </form>
        </div>

        <!-- Results Section -->
        <div id="resultsSection" class="results-section">
            <div id="loadingIndicator" class="loading" style="display: none;">
                <div class="spinner"></div>
                <p>Searching flights...</p>
            </div>
            
            <div id="flightResults">
               
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            
            const today = new Date().toISOString().split('T')[0];
            $('#departureDate').attr('min', today);
            $('#departureDate').val(today);
            
            
            $('input[name="sortPreference"]').change(function() {
                if ($(this).is(':checked')) {
                    $('input[name="sortPreference"]').not(this).prop('checked', false);
                }
            });
            
          
            $('#flightSearchForm').submit(function(e) {
                e.preventDefault();
                searchFlights();
            });
            
          
            $('#cancelBtn').click(function() {
                resetForm();
            });
            
           
            $('#departureLocation, #arrivalLocation').on('input', function() {
                $(this).val($(this).val().toUpperCase());
            });
        });
        
        function searchFlights() {
            
            const searchData = {
                departureLocation: $('#departureLocation').val().trim(),
                arrivalLocation: $('#arrivalLocation').val().trim(),
                departureDate: $('#departureDate').val(),
                flightClass: $('#flightClass').val(),
                sortPreference: parseInt($('input[name="sortPreference"]:checked').val()) || 0
            };
            
           
            if (!searchData.departureLocation || !searchData.arrivalLocation || !searchData.departureDate) {
                alert('Please fill in all required fields');
                return;
            }
            
            if (searchData.departureLocation === searchData.arrivalLocation) {
                alert('Departure and arrival locations cannot be the same');
                return;
            }
            
            
            $('#resultsSection').show();
            $('#loadingIndicator').show();
            $('#flightResults').empty();
            $('#searchBtn').prop('disabled', true).text('Searching...');
            
            
            $.ajax({
                url: '${pageContext.request.contextPath}/searchFlights',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(searchData),
                success: function(response) {
                    displayResults(response);
                },
                error: function(xhr, status, error) {
                    console.error('Search failed:', error);
                    displayError('Failed to search flights. Please try again.');
                },
                complete: function() {
                    $('#loadingIndicator').hide();
                    $('#searchBtn').prop('disabled', false).text('Search');
                }
            });
        }
        
        function displayResults(response) {
            const resultsContainer = $('#flightResults');
            
            if (response.status === 'success' && response.flights && response.flights.length > 0) {
                let tableHtml = `
                    <div class="scrollable-results">
                        <table class="results-table">
                            <thead>
                                <tr>
                                    <th>Flight Number</th>
                                    <th>Fare</th>
                                    <th>Duration</th>
                                    <th>CLASS</th>
                                </tr>
                            </thead>
                            <tbody>
                `;
                
                response.flights.forEach(function(flight) {
                    
                    const flightNumber = flight.flightNumber || 'N/A';
                    const fare = flight.fare ? `₹${flight.fare}` : 'N/A';
                    const duration = flight.duration && flight.duration !== 'false' ? flight.duration : 'N/A';
                    const flightClass = flight.flightClass || 'Economy';
                    
                    tableHtml += `
                        <tr>
                            <td class="flight-number">\${flight.flightNumber}</td>
                            <td class="fare">\${flight.fare}</td>
                            <td class="duration">\${flight.duration || 'N/A'}</td>
                            <td class="class-cell">` + getClassAbbreviation(flight.flightClass) + `</td>
                        </tr>
                    `;

                });
                
                tableHtml += `
                            </tbody>
                        </table>
                    </div>
                `;
                
                resultsContainer.html(tableHtml);
                
            } else {
                resultsContainer.html(`
                    <div class="no-results">
                        <h3>No flights found</h3>
                        <p>Try adjusting your search criteria</p>
                    </div>
                `);
            }
        }

        function displayError(message) {
            $('#flightResults').html(`
                <div class="error-message">
                    <strong>Error:</strong> ${message}
                </div>
            `);
        }
        
        function getClassAbbreviation(flightClass) {
            if (!flightClass) return 'E';
            
            const classMap = {
                'Economy': 'E',
                'Business': 'B',
                'Premium Economy': 'PE',
                
            };
            
            return classMap[flightClass] || 'E';
        }
        
        function resetForm() {
            $('#flightSearchForm')[0].reset();
            $('#resultsSection').hide();
            $('#departureDate').val(new Date().toISOString().split('T')[0]);
            $('#sortByFare').prop('checked', true);
            $('#sortByDuration').prop('checked', false);
        }
    </script>
</body>
</html>
