package com.searchflights.controller;

import com.searchflights.model.Flight;   
import com.searchflights.model.FlightSearchParameters;
import com.searchflights.model.User;
import com.searchflights.service.FlightService;
import com.searchflights.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The {@link UserController} class manages all user-facing operations in the flight search application.
 * <br>
 * This controller handles requests related to user registration, login, logout, accessing the user dashboard,
 * and searching for flights via AJAX. It coordinates user authentication, session management, and retrieval
 * of flight search results based on user preferences.
 * <br><br>
 * As a Spring MVC controller, this class is not intended for direct instantiation or use as a regular Java class.
 * The Spring framework automatically registers it as a bean and maps HTTP requests to its handler methods.
 * <br><br>
 * Only the Spring container should manage the lifecycle of this controller. Users should not create instances manually.
 * <br><br>
 * Typical user operations handled by this controller include:
 * <ul>
 *   <li>Accessing the home page</li>
 *   <li>User registration and validation</li>
 *   <li>User login and logout</li>
 *   <li>Accessing the user dashboard</li>
 *   <li>Searching for flights with sorting and validation (AJAX)</li>
 * </ul>
 *
 * @author Pranav Singh
 * @since 1.0
 */

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    // ================= HOME & REGISTRATION =================
    @GetMapping({"/", "/home"})
    public String showHomePage() {
        logger.debug("Home page accessed.");
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.debug("Registration form opened.");
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
    	logger.info("Attempting to register user: {}", user.getUsername());

    	if (result.hasErrors()) {
            logger.warn("Validation errors during registration for user: {}", user.getUsername());
            return "register";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            logger.warn("Username already exists: {}", user.getUsername());
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            logger.warn("Email already in use: {}", user.getEmail());
            model.addAttribute("error", "Email already in use");
            return "register";
        }

        user.setRole("USER");

        try {
            userService.saveUser(user);  // Should encode password here
            logger.info("User registered successfully: {}", user.getUsername());
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", user.getUsername(), e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    
 // ================= USER LOGIN VIEW =================
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "registered", required = false) String registered,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        if (registered != null) {
            model.addAttribute("success", "Registration successful! Please login.");
        }

        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully.");
        }

        model.addAttribute("user", new User());
        return "login";
    }
  
    // ================= USER LOGIN POST =================
    @PostMapping("/login")
    public String processUserLogin(@ModelAttribute("user") User loginUser, 
                                   Model model, 
                                   HttpSession session) {
        try {
            logger.info("Login attempt for username: {}", loginUser.getUsername());
            
            // Authenticate user against database
            User authenticatedUser = userService.authenticateUser(loginUser.getUsername(), loginUser.getPassword());
            
            if (authenticatedUser != null) {
                // Successful login - store user in session
                session.setAttribute("loggedInUser", authenticatedUser);
                session.setAttribute("username", authenticatedUser.getUsername());
                
                logger.info("Successful login for user: {}", authenticatedUser.getUsername());
                return "redirect:/user/dashboard";
            } else {
                // Failed login
                logger.warn("Failed login attempt for username: {}", loginUser.getUsername());
                model.addAttribute("error", "Invalid username or password");
                model.addAttribute("user", new User());
                return "login";
            }
            
        } catch (Exception e) {
            logger.error("Error during login process", e);
            model.addAttribute("error", "Login failed. Please try again.");
            model.addAttribute("user", new User());
            return "login";
        }
    }
 // ================= USER LOGOUT =================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        try {
            // Get username before invalidating session
            String username = (String) session.getAttribute("username");
            
            // Invalidate session
            session.invalidate();
            
            logger.info("User logged out: {}", username != null ? username : "unknown");
            
            // Show logout page instead of redirecting directly
            return "logout";
            
        } catch (Exception e) {
            logger.error("Error during logout", e);
            return "redirect:/login?logout=true";
        }
    }




    // ================= USER DASHBOARD & PROFILE =================
    @GetMapping("/user/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        // Check if user is logged in via session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = (String) session.getAttribute("username");
        
        if (loggedInUser == null && username == null) {
            logger.warn("No authenticated user found in session, redirecting to login");
            return "redirect:/login";
        }
        
        // Add user info to model
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            model.addAttribute("username", loggedInUser.getUsername());
            logger.info("Dashboard loaded for user: {}", loggedInUser.getUsername());
        } else {
            model.addAttribute("username", username);
            logger.info("Dashboard loaded for username: {}", username);
        }
        
        return "userDashboard";
    }



    


 // ================= AJAX FLIGHT SEARCH =================
    @PostMapping("/searchFlights")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchFlightsAjax(@RequestBody FlightSearchParameters searchParams,
                                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if user is logged in (session-based)
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                response.put("status", "error");
                response.put("message", "Please login to search flights");
                return ResponseEntity.status(401).body(response);
            }
            
            logger.info("AJAX flight search by {}: {} to {} on {}", 
                       loggedInUser.getUsername(),
                       searchParams.getDepartureLocation(), 
                       searchParams.getArrivalLocation(), 
                       searchParams.getDepartureDate());
            
            // Validate search parameters
            if (searchParams.getDepartureLocation() == null || searchParams.getDepartureLocation().trim().isEmpty() ||
                searchParams.getArrivalLocation() == null || searchParams.getArrivalLocation().trim().isEmpty() ||
                searchParams.getDepartureDate() == null) {
                response.put("status", "error");
                response.put("message", "Please fill in all required fields");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Search flights using your existing service
            List<Flight> flights = flightService.searchFlights(searchParams);
            logger.info("=== FLIGHT SEARCH RESULTS DEBUG ===");
            logger.info("Total flights found: {}", flights.size());
            for (int i = 0; i < Math.min(flights.size(), 3); i++) {
                Flight f = flights.get(i);
                logger.info("Flight {}: Number={}, Fare={}, Duration={}, Class={}", 
                           i, f.getFlightNumber(), f.getFare(), f.getDuration(), f.getFlightClass());
            }
            logger.info("=== END DEBUG ===");
            // Sort flights based on user preference
            if (searchParams.getSortPreference() == 1) {
                // Sort by duration
                flights.sort((f1, f2) -> f1.getDuration().compareTo(f2.getDuration()));
            } else {
                // Sort by fare (default)
                flights.sort((f1, f2) -> f1.getFare().compareTo(f2.getFare()));
            }
            
            response.put("status", "success");
            response.put("flights", flights);
            response.put("totalResults", flights.size());
            
            logger.info("Found {} flights for user: {}", flights.size(), loggedInUser.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error during flight search", e);
            response.put("status", "error");
            response.put("message", "Failed to search flights: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}