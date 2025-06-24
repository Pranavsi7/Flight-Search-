package com.searchflights.controller;

import com.searchflights.model.Flight; 
import com.searchflights.model.User;
import com.searchflights.model.UploadHistory;
import com.searchflights.service.FlightService;
import com.searchflights.service.UserService;
import com.searchflights.service.UploadHistoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
/**
 * Unit tests for {@link com.searchflights.controller.AdminController}.
 * <p>
 * This test class uses JUnit 5 and Mockito to validate the behavior of 
 * the admin-related features such as login, dashboard rendering, file uploads,
 * manual flight addition, and upload history viewing.
 * </p>
 *
 * <p>
 * Dependencies like {@link UserService}, {@link FlightService}, and
 * {@link UploadHistoryService} are mocked to isolate the controller logic.
 * Model and redirect-related interactions are also mocked.
 * </p>
 *
 * <p>
 * Key test scenarios include:
 * <ul>
 *     <li>Admin login success and failure paths</li>
 *     <li>Dashboard data population</li>
 *     <li>Upload page rendering</li>
 *     <li>File upload validation (empty/invalid)</li>
 *     <li>Manual flight addition validation and success</li>
 *     <li>Upload history display</li>
 * </ul>
 * </p>
 *
 * @author Pranav Singh
 */

public class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private FlightService flightService;

    @Mock
    private UploadHistoryService uploadHistoryService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private AdminController adminController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController();

        
        setField(adminController, "userService", userService);
        setField(adminController, "flightService", flightService);
        setField(adminController, "uploadHistoryService", uploadHistoryService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testShowAdminLoginForm_ErrorCase() {
        String view = adminController.showAdminLoginForm("true", null, model);
        assertEquals("adminLogin", view);
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    public void testShowAdminLoginForm_LogoutCase() {
        String view = adminController.showAdminLoginForm(null, "true", model);
        assertEquals("adminLogin", view);
        verify(model).addAttribute(eq("message"), anyString());
    }

    @Test
    public void testProcessAdminLogin_Success() {
        User user = new User();
        user.setUsername("pranav");
        user.setPassword("pranav123");
        String view = adminController.processAdminLogin(user, null, model);
        assertEquals("redirect:/admin/dashboard", view);
    }

    @Test
    public void testProcessAdminLogin_Failure() {
        User user = new User();
        user.setUsername("wrong");
        user.setPassword("wrong");
        String view = adminController.processAdminLogin(user, null, model);
        assertEquals("adminLogin", view);
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    public void testShowAdminDashboard_Success() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Admin");
        when(userService.findByUsername(anyString())).thenReturn(user);
        when(userService.getTotalUserCount()).thenReturn(10);
        when(flightService.getAllFlights()).thenReturn(Collections.emptyList());

        String view = adminController.showAdminDashboard(model);
        assertEquals("adminDashboard", view);
        verify(model).addAttribute(eq("adminName"), anyString());
        verify(model).addAttribute(eq("adminUsername"), anyString());
        verify(model).addAttribute(eq("totalUsers"), eq(10));
        verify(model).addAttribute(eq("totalFlights"), eq(0));
    }

    @Test
    public void testShowUploadPage() {
        String view = adminController.showUploadPage(model);
        assertEquals("adminUpload", view);
        verify(model).addAttribute("selectedTab", "upload");
    }

    @Test
    public void testHandleFileUploadAjax_EmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        ResponseEntity<Map<String, String>> response = adminController.handleFileUploadAjax(file, "csv");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
    }

    @Test
    public void testHandleFileUploadAjax_InvalidType() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        ResponseEntity<Map<String, String>> response = adminController.handleFileUploadAjax(file, "csv");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
    }

    @Test
    public void testShowAddFlightForm() {
        String view = adminController.showAddFlightForm(model);
        assertEquals("adminAddFlight", view);
        verify(model).addAttribute(eq("flight"), any(Flight.class));
        verify(model).addAttribute(eq("selectedTab"), eq("manually"));
    }

    @Test
    public void testAddFlightAjax_FlightNumberMissing() {
        Flight flight = new Flight();
        ResponseEntity<Map<String, String>> response = adminController.addFlightAjax(flight);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
    }

    @Test
    public void testAddFlightAjax_FlightAlreadyExists() {
        Flight flight = new Flight();
        flight.setFlightNumber("123");
        when(flightService.getFlightByNumber("123")).thenReturn(new Flight());
        ResponseEntity<Map<String, String>> response = adminController.addFlightAjax(flight);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody().get("status"));
    }

    @Test
    public void testAddFlightAjax_Success() {
        Flight flight = new Flight();
        flight.setFlightNumber("123");
        when(flightService.getFlightByNumber("123")).thenReturn(null);
        ResponseEntity<Map<String, String>> response = adminController.addFlightAjax(flight);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    public void testShowHistory() {
        List<UploadHistory> historyList = Collections.emptyList();
        when(uploadHistoryService.getAllUploadHistory()).thenReturn(historyList);
        String view = adminController.showHistory(model);
        assertEquals("adminHistory", view);
        verify(model).addAttribute("uploadHistory", historyList);
        verify(model).addAttribute("selectedTab", "history");
    }
}
