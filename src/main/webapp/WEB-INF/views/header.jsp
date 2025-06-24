
<!DOCTYPE html>

<html>
<head>
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500,700&display=swap" rel="stylesheet">
    <style>
        body { margin: 0; padding: 0; font-family: 'Roboto', Arial, sans-serif; }
        .sf-header {
            background: linear-gradient(90deg, #445878 0%, #92CDCF 100%);
            color: #fff;
            padding: 0;
            box-shadow: 0 2px 8px #e0e0e0;
        }
        .sf-header-inner {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 32px;
            height: 70px;
        }
        .sf-logo {
            font-size: 2em;
            font-weight: 700;
            letter-spacing: 1px;
            color: #fff;
            text-decoration: none;
            transition: color 0.2s;
        }
        .sf-nav {
            display: flex;
            gap: 24px;
        }
        .sf-nav a {
            color: #fff;
            text-decoration: none;
            font-size: 1.1em;
            font-weight: 500;
            padding: 8px 18px;
            border-radius: 4px;
            transition: background 0.2s, color 0.2s;
        }
        .sf-nav a:hover, .sf-nav a.active {
            background: #fff;
            color: #445878;
        }
        @media (max-width: 700px) {
            .sf-header-inner { flex-direction: column; height: auto; padding: 10px 8px; }
            .sf-nav { flex-direction: column; gap: 8px; width: 100%; align-items: flex-start; }
            .sf-logo { margin-bottom: 8px; }
        }
    </style>
</head>
<body>
<div class="sf-header">
    <div class="sf-header-inner">
        <a href="/" class="sf-logo">SearchFlights</a>
        <nav class="sf-nav">
            <a href="/adminLogin" <c:if test="${pageContext.request.requestURI == '/adminLogin'}">class="active"</c:if>>Admin Login</a>
            <a href="/login" <c:if test="${pageContext.request.requestURI == '/login'}">class="active"</c:if>>Login</a>
            <a href="/register" <c:if test="${pageContext.request.requestURI == '/register'}">class="active"</c:if>>Register</a>
        </nav>
    </div>
</div>
</body>
</html>
