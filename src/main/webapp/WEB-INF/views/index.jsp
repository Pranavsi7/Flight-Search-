<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SearchFlights | Find the Best Flights</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background: 
                linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.7)),
                url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1474&q=80');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            color: #fff;
        }

        header {
            padding: 20px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: rgba(0, 0, 0, 0.7);
            border-bottom: 2px solid #0288d1;
        }

        .logo {
            font-size: 1.8rem;
            font-weight: 700;
            color: #4fc3f7;
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
        }

        .logo i {
            color: #00c853;
        }

        main {
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 0 20px;
        }

        .hero {
            max-width: 800px;
            padding: 40px;
            background: rgba(0, 0, 0, 0.6);
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            margin: 20px;
            backdrop-filter: blur(5px);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .hero h1 {
            font-size: 3.5rem;
            margin-bottom: 20px;
            color: #4fc3f7;
            text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
        }

        .hero p {
            font-size: 1.3rem;
            margin-bottom: 40px;
            line-height: 1.6;
            color: #e0f7fa;
        }

        .cta-buttons {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 30px;
        }

        .btn {
            padding: 14px 35px;
            font-size: 1.1rem;
            border: none;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            min-width: 200px;
            justify-content: center;
        }

        .btn-login {
            background: #0288d1;
            color: white;
        }

        .btn-register {
            background: #00c853;
            color: white;
        }

        .btn-admin {
            background: #7b1fa2;
            color: white;
        }

        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        .btn:active {
            transform: translateY(1px);
        }

        footer {
            background: rgba(0, 0, 0, 0.7);
            padding: 30px 5%;
            color: #ccc;
            text-align: center;
            border-top: 2px solid #0288d1;
        }

        .footer-contact p {
            margin: 5px 0;
            font-size: 0.95rem;
        }

        .footer-bottom {
            margin-top: 10px;
            font-size: 0.85rem;
            color: #aaa;
        }

        @media (max-width: 768px) {
            .hero h1 {
                font-size: 2.5rem;
            }

            .cta-buttons {
                flex-direction: column;
                align-items: center;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <header>
        <a href="#" class="logo">
            <i class="fas fa-plane-departure"></i>
            <span>SearchFlights</span>
        </a>
    </header>

    <main>
        <!-- Hero Section -->
        <section class="hero">
            <h1>Welcome to SearchFlights</h1>
            <p>Find the best flights, compare fares, and travel smarter. Our advanced search technology helps you discover the perfect flight options at the most competitive prices.</p>
            
            <div class="cta-buttons">
                <a href="adminLogin" class="btn btn-admin">
                    <i class="fas fa-user-shield"></i>Admin Login
                </a>
                <a href="login" class="btn btn-login">
                    <i class="fas fa-sign-in-alt"></i>Login
                </a>
                <a href="register" class="btn btn-register">
                    <i class="fas fa-user-plus"></i>Register
                </a>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <footer>
        <div class="footer-contact">
            <p><i class="fas fa-envelope"></i> pranav.singh071204@gmail.com</p>
            <p><i class="fas fa-phone-alt"></i> +91 9205757614</p>
        </div>
        <div class="footer-bottom">
            &copy; 2025 SearchFlights. All rights reserved.
        </div>
    </footer>

    <script>
        // Button hover effect enhancement
        document.querySelectorAll('.btn').forEach(button => {
            button.addEventListener('mouseenter', () => {
                button.style.transform = 'translateY(-5px)';
                button.style.boxShadow = '0 8px 20px rgba(0, 0, 0, 0.4)';
            });

            button.addEventListener('mouseleave', () => {
                button.style.transform = 'translateY(0)';
                button.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.3)';
            });
        });
    </script>
</body>
</html>
