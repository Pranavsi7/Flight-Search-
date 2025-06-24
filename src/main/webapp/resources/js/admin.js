// admin.js
function addFlight() {
    // Get current date or let user select date
    var currentDate = new Date().toISOString().split('T')[0]; // Format: YYYY-MM-DD
    
    var formData = {
        flightNumber: $('#flightNumber').val(),
        departureLocation: $('#departureLocation').val(),
        arrivalLocation: $('#arrivalLocation').val(),
        fare: parseFloat($('#fare').val()),
        // Send time values as HH:mm format
        departureTime: $('#departureTime').val(), // Should be "23:10"
        arrivalTime: calculateArrivalTime($('#departureTime').val(), $('#duration').val()),
        flightDate: $('#flightDate').val() || currentDate, // Use selected date or current
        totalSeats: parseInt($('#totalSeats').val()),
        duration: $('#duration').val(),
        classType: $('#classType').val()
    };
    
    // Show loading
    $('#loadingArea').show();
    $('#submitBtn').prop('disabled', true);
    clearMessages();
    
    $.ajax({
        url: contextPath + '/admin/addFlight',
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
            }
            showMessage('error', '❌ ' + errorMessage);
        }
    });
}

function calculateArrivalTime(departureTime, duration) {
    if (!departureTime || !duration) return '';
    
    // Parse departure time (HH:mm)
    var depParts = departureTime.split(':');
    var depHour = parseInt(depParts[0]);
    var depMin = parseInt(depParts[1]);
    
    // Parse duration (HH:mm)
    var durParts = duration.split(':');
    var durHour = parseInt(durParts[0]);
    var durMin = parseInt(durParts[1]);
    
    // Calculate arrival time
    var arrivalMin = depMin + durMin;
    var arrivalHour = depHour + durHour;
    
    if (arrivalMin >= 60) {
        arrivalHour += Math.floor(arrivalMin / 60);
        arrivalMin = arrivalMin % 60;
    }
    
    if (arrivalHour >= 24) {
        arrivalHour = arrivalHour % 24;
    }
    
    // Format as HH:mm
    return String(arrivalHour).padStart(2, '0') + ':' + String(arrivalMin).padStart(2, '0');
}

function showMessage(type, message) {
    var messageClass = 'message message-' + type;
    var messageHtml = '<div class="' + messageClass + '">' + message + '</div>';
    
    $('#messageArea').html(messageHtml);
    $('.message').slideDown();
    
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

// Set context path for AJAX calls
var contextPath = $('meta[name="contextPath"]').attr('content') || '';
