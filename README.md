# SearchFlights

A robust Java Spring MVC web application for searching and managing flights, featuring secure user and admin interfaces, bulk data upload, and configuration management.

---

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

## 👤 Author

**Pranav Singh**
