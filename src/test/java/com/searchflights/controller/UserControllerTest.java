package com.searchflights.controller;

import com.searchflights.model.FlightSearchParameters;
import com.searchflights.model.User;
import com.searchflights.service.FlightService;
import com.searchflights.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
/**
 * Unit tests for {@link com.searchflights.controller.UserController}.
 * <p>
 * This class tests user-facing functionality such as registration, login, dashboard access,
 * flight search, and logout using JUnit 5 and Mockito.
 * </p>
 *
 * <p>
 * The following dependencies are mocked:
 * <ul>
 *     <li>{@link com.searchflights.service.UserService}</li>
 *     <li>{@link com.searchflights.service.FlightService}</li>
 *     <li>{@link org.springframework.ui.Model}</li>
 *     <li>{@link javax.servlet.http.HttpSession}</li>
 *     <li>{@link org.springframework.validation.BindingResult}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Covered test categories:
 * <ul>
 *     <li>User registration (validation, duplicate check, success)</li>
 *     <li>User login (valid and invalid credentials)</li>
 *     <li>Dashboard visibility based on authentication</li>
 *     <li>AJAX-based flight search with authentication enforcement</li>
 *     <li>Session-based logout functionality</li>
 * </ul>
 * </p>
 *
 * @see com.searchflights.controller.UserController
 * @see com.searchflights.model.User
 * @see com.searchflights.model.FlightSearchParameters
 * @see com.searchflights.service.UserService
 * @see com.searchflights.service.FlightService
 * 
 * @author Pranav Singh
 */

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private FlightService flightService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    private UserController userController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userController = new UserController();

        
        setField(userController, "userService", userService);
        setField(userController, "flightService", flightService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // ================= REGISTRATION TESTS =================
    @Test
    public void testRegisterUser_ValidationErrors() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.registerUser(user, bindingResult, model);
        assertEquals("register", view);
        verify(model, never()).addAttribute(eq("success"), anyString());
    }

    @Test
    public void testRegisterUser_UsernameExists() {
        User user = new User();
        user.setUsername("existing");
        when(userService.findByUsername("existing")).thenReturn(new User());

        String view = userController.registerUser(user, bindingResult, model);
        assertEquals("register", view);
        verify(model).addAttribute("error", "Username already exists");
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("newuser");
        when(userService.findByUsername(anyString())).thenReturn(null);
        when(userService.findByEmail(anyString())).thenReturn(null);

        String view = userController.registerUser(user, bindingResult, model);
        assertEquals("redirect:/login?registered=true", view);
        verify(userService).saveUser(user);
    }

    // ================= LOGIN TESTS =================
    @Test
    public void testProcessUserLogin_Success() {
        User user = new User();
        user.setUsername("valid");
        user.setPassword("valid");
        when(userService.authenticateUser("valid", "valid")).thenReturn(user);

        String view = userController.processUserLogin(user, model, session);
        assertEquals("redirect:/user/dashboard", view);
        verify(session).setAttribute("loggedInUser", user);
    }

    @Test
    public void testProcessUserLogin_InvalidCredentials() {
        when(userService.authenticateUser(anyString(), anyString())).thenReturn(null);

        String view = userController.processUserLogin(new User(), model, session);
        assertEquals("login", view);
        verify(model).addAttribute("error", "Invalid username or password");
    }

    // ================= DASHBOARD TESTS =================
    @Test
    public void testShowUserDashboard_Unauthenticated() {
        when(session.getAttribute("loggedInUser")).thenReturn(null);
        when(session.getAttribute("username")).thenReturn(null);

        String view = userController.showUserDashboard(session, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    public void testShowUserDashboard_Authenticated() {
        User user = new User();
        user.setUsername("testuser");
        when(session.getAttribute("loggedInUser")).thenReturn(user);

        String view = userController.showUserDashboard(session, model);
        assertEquals("userDashboard", view);
        verify(model).addAttribute("user", user);
    }

    // ================= FLIGHT SEARCH TESTS =================
 
    @Test
    public void testSearchFlightsAjax_Unauthenticated() {
        when(session.getAttribute("loggedInUser")).thenReturn(null); // Explicitly set to null
        
        ResponseEntity<Map<String, Object>> response = 
            userController.searchFlightsAjax(new FlightSearchParameters(), session);
        
        assertEquals(401, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Check the actual key your controller uses (might be "message" instead of "error")
        assertTrue(response.getBody().containsKey("message") || response.getBody().containsKey("error"));
    }



    // ================= LOGOUT TEST =================
    @Test
    public void testLogout() {
        when(session.getAttribute("username")).thenReturn("testuser");
        String view = userController.logout(session);
        assertEquals("logout", view);
        verify(session).invalidate();
    }
}
