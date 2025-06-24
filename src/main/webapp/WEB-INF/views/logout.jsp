<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Logout - SearchFlights</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/common.css'/>">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        
        .logout-container {
            background: white;
            padding: 50px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 500px;
            width: 100%;
        }
        
        .logout-header {
            margin-bottom: 30px;
        }
        
        .logout-header h1 {
            color: #333;
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        
        .logout-message {
            color: #666;
            font-size: 1.2rem;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .logout-icon {
            font-size: 4rem;
            color: #28a745;
            margin-bottom: 20px;
        }
        
        .btn-container {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
            min-width: 120px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }
        
        .countdown {
            margin-top: 20px;
            color: #999;
            font-size: 0.9rem;
        }
        
        @media (max-width: 600px) {
            .logout-container {
                margin: 20px;
                padding: 30px 20px;
            }
            
            .btn-container {
                flex-direction: column;
                align-items: center;
            }
            
            .btn {
                width: 100%;
                max-width: 200px;
            }
        }
    </style>
</head>
<body>
    <div class="logout-container">
        <div class="logout-header">
            <div class="logout-icon">✅</div>
            <h1>Logout Successful</h1>
        </div>
        
        <div class="logout-message">
            <p>You have been successfully logged out of your account.</p>
            <p>Thank you for using SearchFlights!</p>
        </div>
        
        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                🔑 Login Again
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                🏠 Go Home
            </a>
        </div>
        
        <div class="countdown">
            <p id="redirectMessage">You will be redirected to the login page in <span id="countdown">5</span> seconds...</p>
        </div>
    </div>

    <script>
        // Auto-redirect countdown
        let seconds = 5;
        const countdownElement = document.getElementById('countdown');
        const redirectMessageElement = document.getElementById('redirectMessage');
        
        const timer = setInterval(function() {
            seconds--;
            countdownElement.textContent = seconds;
            
            if (seconds <= 0) {
                clearInterval(timer);
                redirectMessageElement.textContent = 'Redirecting...';
                window.location.href = '${pageContext.request.contextPath}/login';
            }
        }, 1000);
        
        
        document.querySelectorAll('.btn').forEach(button => {
            button.addEventListener('click', function() {
                clearInterval(timer);
            });
        });
    </script>
</body>
</html>
