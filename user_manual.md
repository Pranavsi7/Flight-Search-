# Flight Search Web Application – User Manual

## Introduction

This web application enables users to search for flights , while administrators can manage flights, import data, and configure the system. The backend is built using Spring MVC and Hibernate ORM, with a MySQL database.

---

## Getting Started

- **Deployment:** Deploy the application on a Java web server (e.g., Tomcat).
- **Database:** Ensure MySQL is running and the `flightdb` schema exists.
- **Configuration:** Update database credentials in `src/main/resources/jdbc.properties` if needed.

---

## User Features

### Registration

- Go to `/register` or click "Register" on the homepage.
- Fill in your details and submit to create an account.

### Login & Logout

- Go to `/login` and enter your credentials.
- After successful login, you are redirected to your dashboard.
- To logout, click "Logout" or visit `/logout`.

### Search Flights

- Use the search form on your dashboard.
- Enter departure and arrival locations, date, and class.
- Results are displayed with sorting options (by fare or duration).

---

## Admin Features

### Admin Login & Logout

- Go to `/adminLogin` and enter admin credentials.
- Default credentials:  
  - Username: `pranav`  
  - Password: `pranav123`
- To logout, go to `/admin/logout`.

### Admin Dashboard

- Access `/admin/dashboard` after login.
- View statistics: total users, total flights, and quick links to admin operations.

### Flight Management

#### Add Flight

- Go to `/admin/addFlight`.
- Fill in flight details and submit to add a new flight.

#### Edit Flight

- Go to `/admin/editFlight`.
- Select a flight, update details, and save changes.

#### Delete Flight

- Go to `/admin/deleteFlight`.
- Select a flight and confirm deletion.

### Bulk Flight Import

- Go to `/admin/upload`.
- Select and upload a file (CSV, Excel, Word, XML) with flight data.
- Imported records are logged in the upload history.

### Upload History

- Go to `/admin/history`.
- View or delete import records.

### Configuration Management

- Go to `/admin/configuration`.
- View and edit the application’s configuration file.

---

## Project Structure Overview

| Directory/File                   | Purpose                                       |
|----------------------------------|-----------------------------------------------|
| `src/main/java`                  | Java source code (controllers, services, etc.)|
| `src/main/resources`             | Configuration files (Spring, Hibernate, logs) |
| `src/main/webapp/WEB-INF/views`  | JSP pages (user/admin interfaces)             |
| `src/main/webapp/resources`      | Static resources (CSS, JS, images)            |
| `logs/`                          | Application log files                         |
| `pom.xml`                        | Maven build configuration                     |
| `README.md`                      | Project documentation                         |

---

## Troubleshooting & FAQ

- **Cannot login?**
  - Check credentials. For admin, use the default unless changed.
  - Verify database connection in `jdbc.properties`.
- **File import fails?**
  - Ensure the file format matches the selected type.
  - Check log files in `logs/` for details.
- **Database errors?**
  - Ensure MySQL is running and schema matches model entities.
  - Check configuration files for correct settings.
- **UI not loading?**
  - Ensure server is running and JSPs are properly deployed.

---

## Contact

For further assistance, contact @author : Pranav Singh or refer to code comments for more documentation.

---

*This manual was generated for the Flight Search Web Application (Spring MVC + Hibernate) as of June 2025.*
