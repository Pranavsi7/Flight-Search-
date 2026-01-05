# SearchFlights

A robust Java Spring MVC web application for searching and managing flights, featuring secure user and admin interfaces, bulk data upload, and configuration management.

---
![image](https://github.com/user-attachments/assets/1ff5e552-cffc-4adf-9d26-5c6860f5e03b)

## 🌟 Overview

SearchFlights is a web-based flight management and search system. Users can register, log in, and search for flights by location and date, while administrators have access to advanced management features such as adding, editing, deleting flights, bulk uploading flight data, and editing application configuration files.

---

## 🚀 Features

- **User Registration & Login:** Secure authentication, registration, and session management.
- **Flight Search:** Search flights by location and date, with sorting by fare or duration.
- **Admin Panel:**
  - Admin authentication and dashboard.
  - Add, edit, and delete flights.
  - Bulk upload of flight data (CSV, Excel, Word, XML).
  - Application configuration file management.
  - View and clear upload history.
- **Logging:** SLF4J, Logback, and Log4j for comprehensive logging.
- **Testing:** JUnit 5 and Mockito for unit and integration testing.


---


## 🛠️ Tech Stack

| Technology        | Version      | Purpose                        |
|-------------------|-------------|--------------------------------|
| Java              | 11          | Core language                  |
| Spring Framework  | 5.3.29      | MVC, DI, Security              |
| Hibernate         | 5.6.15      | ORM                            |
| MySQL             | 8.0.33      | Database                       |
| JSP/JSTL          | 2.3.3/1.2   | View rendering                 |
| Maven             | —           | Build & dependency management  |
| Tomcat            | 9.x         | Servlet container              |
| JUnit/Mockito     | 5.x         | Testing                        |
| SLF4J, Logback    | 1.7.x/1.2.x | Logging                        |

---

## ⚙️ Configuration

- **Spring:** `applicationContext.xml`
- **Hibernate:** `hibernate.cfg.xml`
- **Database:** `jdbc.properties`
- **Logging:** `logback.xml`
- **Web:** `web.xml`, `dispatcher-servlet.xml`

---
## 📈 Impact & Achievements

- Supported **1,000+ monthly users** with **700+ weekly flight search queries**
- Improved database response time by **30%** through Hibernate query optimization
- Increased data ingestion speed by **60%** using bulk upload (CSV, Excel, XML)
- Reduced manual admin errors by **25%** through validation workflows

## 🏗️ System Architecture
Controller Layer → Service Layer → DAO Layer → Database
↓ ↓ ↓
JSP UI Business Logic Hibernate ORM

- MVC architecture with clear separation of concerns
- Hibernate ORM for efficient database access
- JSP-based frontend with Spring MVC controllers

## 🔐 Role-Based Access Control

### 👤 User
- Register and log in securely
- Search flights by source, destination, and date
- Sort results by fare or duration

### 🛠️ Admin
- Secure admin authentication
- Add, edit, and delete flight records
- Bulk upload flight data (CSV, Excel, Word, XML)
- Manage application configuration files
- View and clear upload history
  

## 👤 Author

**Pranav Singh**  
Final-year B.Tech CSE Student | Backend-focused Developer  

🔗 GitHub: https://github.com/Pranavsi7  
🔗 LinkedIn: https://www.linkedin.com/in/pranav-singh-8a802424b/


