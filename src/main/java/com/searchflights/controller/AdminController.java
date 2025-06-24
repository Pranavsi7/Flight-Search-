package com.searchflights.controller;

import com.searchflights.model.Flight; 
import com.searchflights.model.User;
import com.searchflights.service.FlightService;
import com.searchflights.model.UploadHistory;
import com.searchflights.service.UserService;
import com.searchflights.service.UploadHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * The {@link AdminController} class manages all administrative operations for the flight search application.
 * <br>
 * This controller handles requests related to admin authentication, dashboard display, flight management
 * (adding, editing, deleting), file uploads for bulk flight data import, configuration file management,
 * and viewing or clearing upload history.
 * <br><br>
 * As a Spring MVC controller, this class is not intended for direct instantiation or use as a regular Java class.
 * The Spring framework automatically registers it as a bean and maps HTTP requests to its handler methods.
 * <br><br>
 * Only the Spring container should manage the lifecycle of this controller. Users should not create instances manually.
 * <br><br>
 * Typical admin operations handled by this controller include:
 * <ul>
 *   <li>Admin login and logout</li>
 *   <li>Viewing dashboard statistics</li>
 *   <li>Uploading flight data files (CSV, Excel, Word, XML)</li>
 *   <li>Adding, editing, and deleting flights</li>
 *   <li>Viewing and clearing upload history</li>
 *   <li>Managing application configuration files</li>
 * </ul>
 *
 * @author Pranav Singh
 * @since 1.0
 */

@Controller

public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;
    
    @Autowired
    private UploadHistoryService uploadHistoryService;

    // ===== Admin Login =====
    @GetMapping("/adminLogin")
    public String showAdminLoginForm(@RequestParam(value = "error", required = false) String error,
                                     @RequestParam(value = "logout", required = false) String logout,
                                     Model model) {
        if (error != null) {
            logger.warn("Admin login error occurred.");
            model.addAttribute("error", "Invalid admin credentials");
        }
        if (logout != null) {
            logger.info("Admin logged out.");
            model.addAttribute("message", "Admin logged out successfully");
        }
        return "adminLogin";
    }
    @PostMapping("/adminLogin")
    public String processAdminLogin(@ModelAttribute("user") User user, BindingResult result, Model model) {
        // Authentication logic here
        if ("pranav".equals(user.getUsername()) && "pranav123".equals(user.getPassword())) {
            return "redirect:/admin/dashboard"; // ✅ updated redirect path
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "adminLogin";
        }
    }
 
    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        try {
            // Get admin username before invalidating session
            String username = (String) session.getAttribute("username");
            
            // Invalidate session
            session.invalidate();
            
            logger.info("Admin logged out: {}", username != null ? username : "unknown");
            
            // Show admin logout page
            return "adminLogout";
            
        } catch (Exception e) {
            logger.error("Error during admin logout", e);
            return "redirect:/adminLogin?logout=true";
        }
    }

    // ===== Admin Dashboard =====
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
    	String adminUsername = "anonymous";
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
    	    adminUsername = auth.getName();
    	} else {
    	    logger.warn("Unauthenticated access attempt to admin dashboard.");
    	}

        try {
            logger.info("Loading admin dashboard for user: {}", adminUsername);
            User currentAdmin = userService.findByUsername(adminUsername);

            if (currentAdmin != null) {
                model.addAttribute("adminName", currentAdmin.getFirstName() + " " + currentAdmin.getLastName());
                model.addAttribute("adminUsername", adminUsername);
            }

            model.addAttribute("totalUsers", userService.getTotalUserCount());
            model.addAttribute("totalFlights", flightService.getAllFlights().size());
        } catch (Exception e) {
            logger.error("Error loading dashboard for admin: {}", adminUsername, e);
            model.addAttribute("adminUsername", adminUsername);
            model.addAttribute("totalUsers", 0);
            model.addAttribute("totalFlights", 0);
        }

        return "adminDashboard";
    }

    // ===== File Upload =====
    @GetMapping("/admin/upload")
    public String showUploadPage(Model model) {
        logger.debug("Opening upload page.");
        model.addAttribute("selectedTab", "upload");
        return "adminUpload";
    }

    @PostMapping("/admin/uploadFile")
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleFileUploadAjax(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType) {
        
        Map<String, String> response = new HashMap<>();
        
        if (file.isEmpty()) {
            logger.warn("No file selected for upload.");
            response.put("status", "error");
            response.put("message", "Please select a file to upload");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            logger.info("Uploading {} file: {}", fileType, file.getOriginalFilename());
            
            // Validate file type
            if (!isValidFileType(file, fileType)) {
                response.put("status", "error");
                response.put("message", "Invalid file type. Expected " + fileType + " file.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Process file based on type
            int recordsProcessed = processFileByType(file, fileType);
            
            response.put("status", "success");
            response.put("message", "File uploaded successfully! Processed " + recordsProcessed + " flight records.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Upload failed: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private boolean isValidFileType(MultipartFile file, String expectedType) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) return false;
        
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        
        switch (expectedType.toLowerCase()) {
            case "csv":
                return extension.equals("csv");
            case "excel":
                return extension.equals("xlsx") || extension.equals("xls");
            case "word":
                return extension.equals("docx") || extension.equals("doc");
            case "xml":
                return extension.equals("xml");
            default:
                return false;
        }
    }

    private int processFileByType(MultipartFile file, String fileType) throws Exception {
        switch (fileType.toLowerCase()) {
            case "csv":
                return flightService.importFromCSV(file);
            case "excel":
                return flightService.importFromExcel(file);
            case "word":
                return flightService.importFromWord(file);
            case "xml":
                return flightService.importFromXML(file);
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    // ===== Flight: Add =====
    @GetMapping("/admin/addFlight")
    public String showAddFlightForm(Model model) {
        logger.debug("Opening add flight form.");
        model.addAttribute("flight", new Flight());
        model.addAttribute("selectedTab", "manually");
        return "adminAddFlight";
    }

    @PostMapping("/admin/addFlight")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addFlightAjax(@RequestBody Flight flight) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Basic validation
            if (flight.getFlightNumber() == null || flight.getFlightNumber().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Flight number is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if flight already exists
            Flight existingFlight = flightService.getFlightByNumber(flight.getFlightNumber());
            if (existingFlight != null) {
                response.put("status", "error");
                response.put("message", "Flight with number " + flight.getFlightNumber() + " already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Save flight
            flightService.saveFlight(flight);
            logger.info("New flight added via AJAX: {}", flight.getFlightNumber());
            
            response.put("status", "success");
            response.put("message", "Flight " + flight.getFlightNumber() + " added successfully!");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to add flight via AJAX", e);
            response.put("status", "error");
            response.put("message", "Failed to add flight: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

   
    // ===== Flight: Edit =====
    @GetMapping("/admin/editFlight")
    public String showEditFlightForm(@RequestParam(value = "flightId", required = false) Long flightId,
                                     Model model) {
        logger.debug("Opening edit flight form for flightId: {}", flightId);
        if (flightId != null) {
            Flight flight = flightService.getFlightById(flightId);
            model.addAttribute("flight", flight != null ? flight : new Flight());
            if (flight == null) model.addAttribute("error", "Flight not found");
        } else {
            model.addAttribute("flight", new Flight());
        }

        model.addAttribute("allFlights", flightService.getAllFlights());
        return "adminEditFlight";
    }

    @PostMapping("/admin/editFlight")
    public String editFlight(@Valid @ModelAttribute("flight") Flight flight,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while editing flight: {}", result);
            return "adminEditFlight";
        }

        try {
            flightService.updateFlight(flight);
            logger.info("Flight updated: {}", flight.getFlightNumber());
            redirectAttributes.addFlashAttribute("success", "Flight updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update flight", e);
            redirectAttributes.addFlashAttribute("error", "Failed to update flight: " + e.getMessage());
        }

        return "redirect:/admin/editFlight";
    }
    
    
    @GetMapping("/admin/getFlightDetails")
    @ResponseBody
    public ResponseEntity<Flight> getFlightDetails(@RequestParam Long flightId) {
        try {
            logger.info("=== FLIGHT DETAILS REQUEST ===");
            logger.info("Requested flightId: {}", flightId);
            
            Flight flight = flightService.getFlightById(flightId);
            
            if (flight != null) {
                logger.info("Flight found: {}", flight.getFlightNumber());
                logger.info("Flight details: {}", flight);
                return ResponseEntity.ok(flight);
            } else {
                logger.warn("Flight not found for ID: {}", flightId);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error getting flight details for ID: {}", flightId, e);
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/admin/updateFlight")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateFlightAjax(@Valid @RequestBody Flight flight) {
        Map<String, String> response = new HashMap<>();
        try {
            flightService.updateFlight(flight);
            response.put("status", "success");
            response.put("message", "Flight updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to update flight: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }



    // ===== Flight: Delete =====
    @GetMapping("/admin/deleteFlight")
    public String showDeleteFlightForm(@RequestParam(value = "flightId", required = false) Long flightId,
                                       Model model) {
        logger.debug("Opening delete flight form for flightId: {}", flightId);
        if (flightId != null) {
            Flight flight = flightService.getFlightById(flightId);
            model.addAttribute("flight", flight != null ? flight : new Flight());
            if (flight == null) model.addAttribute("error", "Flight not found");
        } else {
            model.addAttribute("flight", new Flight());
        }

        model.addAttribute("allFlights", flightService.getAllFlights());
        return "adminDeleteFlight";
    }

    @PostMapping("/admin/deleteFlight")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteFlight(@RequestParam Long flightId) {
        Map<String, String> response = new HashMap<>();
        
        try {
            Flight flight = flightService.getFlightById(flightId);
            if (flight == null) {
                response.put("status", "error");
                response.put("message", "Flight not found");
                return ResponseEntity.notFound().build();
            }
            
            String flightNumber = flight.getFlightNumber();
            flightService.deleteFlight(flightId);
            
            logger.info("Flight deleted: {}", flightNumber);
            response.put("status", "success");
            response.put("message", "Flight " + flightNumber + " deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to delete flight", e);
            response.put("status", "error");
            response.put("message", "Failed to delete flight: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
  
    @GetMapping("/admin/history")
    public String showHistory(Model model) {
        logger.debug("Opening admin history page.");
        List<UploadHistory> uploadHistory = uploadHistoryService.getAllUploadHistory();
        model.addAttribute("uploadHistory", uploadHistory);
        model.addAttribute("selectedTab", "history");
        return "adminHistory";
    }

    @DeleteMapping("/admin/history/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteUploadHistory(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            uploadHistoryService.deleteUploadHistory(id);
            response.put("status", "success");
            response.put("message", "Upload record deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to delete upload record");
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/admin/history/clear")
    @ResponseBody
    public ResponseEntity<Map<String, String>> clearHistory() {
        Map<String, String> response = new HashMap<>();
        try {
            uploadHistoryService.clearAllHistory();
            response.put("status", "success");
            response.put("message", "All upload history cleared");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to clear history");
            return ResponseEntity.status(500).body(response);
        }
    }


    // ===== Configuration File =====
    @GetMapping("/admin/configuration")
    public String showConfigurationFile(Model model) {
        String configFilePath = "C:\\FlightSearch\\property_file";
        logger.debug("Opening configuration file: {}", configFilePath);
        try {
            String configContent = new String(Files.readAllBytes(Paths.get(configFilePath)));
            model.addAttribute("configContent", configContent);
        } catch (IOException e) {
            logger.error("Could not load configuration file: {}", configFilePath, e);
            model.addAttribute("configContent", "");
            model.addAttribute("error", "Could not load configuration file: " + e.getMessage());
        }
        model.addAttribute("selectedTab", "configure");
        return "adminConfiguration";
    }

    @PostMapping("/admin/configuration")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveConfigurationFileAjax(
            @RequestParam("configContent") String configContent) {
        
        Map<String, String> response = new HashMap<>();
        String configFilePath = "C:\\FlightSearch\\property_file";
        
        try {
            if (configContent == null || configContent.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Configuration content cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate properties format (optional)
            try {
                Properties props = new Properties();
                props.load(new StringReader(configContent));
            } catch (IOException e) {
                response.put("status", "error");
                response.put("message", "Invalid properties format: " + e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
            
            Files.write(Paths.get(configFilePath), configContent.getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            logger.info("Configuration file updated successfully");
            response.put("status", "success");
            response.put("message", "Configuration file updated successfully!");
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            logger.error("Failed to update configuration file: {}", configFilePath, e);
            response.put("status", "error");
            response.put("message", "Failed to update configuration file: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
