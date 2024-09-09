package com.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.user.controllers.CustomerController;
import com.user.dtos.Booking;
import com.user.dtos.BookingDto;
import com.user.dtos.CustomerDetailsResponse;
import com.user.dtos.Route;
import com.user.dtos.Vehicle;
import com.user.feign.BookingServiceInterface;
import com.user.feign.RouteServiceClient;
import com.user.feign.VehicleServiceClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    @Mock
    private VehicleServiceClient vehicleService;

    @Mock
    private RouteServiceClient routeService;

    @Mock
    private BookingServiceInterface bookingService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testCreateBooking() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        // Set other fields as necessary

        doReturn(booking).when(bookingService).createBooking(any(Booking.class));

        mockMvc.perform(post("/customer/addBooking")
                .contentType("application/json")
                .content("{ \"bookingId\": 1 }")) // Provide actual JSON content
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1));
    }

    @Test
    void testGetBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        // Set other fields as necessary

        doReturn(booking).when(bookingService).getBookingById(anyLong());

        mockMvc.perform(get("/customer/viewBooking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1));
    }

    @Test
    void testViewAllBookings() throws Exception {
        List<BookingDto> bookings = new ArrayList<>();
        BookingDto bookingDto = new BookingDto();
        // Set properties as necessary
        bookings.add(bookingDto);

        doReturn(bookings).when(bookingService).viewAllBookings();

        mockMvc.perform(get("/customer/viewAllBookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists()); // Adjust based on properties
    }

 

    @Test
    void testGetAllVehicles() throws Exception {
        List<Vehicle> vehicles = new ArrayList<>();
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo("ABC123");
        // Set other fields as necessary
        vehicles.add(vehicle);

        doReturn(vehicles).when(vehicleService).getAllVehicles();

        mockMvc.perform(get("/customer/ViewAllVehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleNo").value("ABC123"));
    }

    @Test
    void testGetVehicleById() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo("ABC123");
        // Set other fields as necessary

        doReturn(vehicle).when(bookingService).getVehicleById(any());

        mockMvc.perform(get("/customer/ViewVehicleById/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNo").value("ABC123"));
    }

    @Test
    void testGetAllRoutes() throws Exception {
        List<Route> routes = new ArrayList<>();
        Route route = new Route();
        route.setRouteId(1);
        // Set other fields as necessary
        routes.add(route);

        doReturn(routes).when(routeService).getAllRoutes();

        mockMvc.perform(get("/customer/ViewAllRoutes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].routeId").value(1));
    }

    @Test
    void testGetRouteById() throws Exception {
        Route route = new Route();
        route.setRouteId(1);
        // Set other fields as necessary

        doReturn(route).when(bookingService).getRouteById(anyInt());

        mockMvc.perform(get("/customer/route/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeId").value(1));
    }

    @Test
    void testGetBookingsByCustomerId() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        booking.setBookingId(1L);
        // Set other fields as necessary
        bookings.add(booking);

        CustomerDetailsResponse response = new CustomerDetailsResponse(1, bookings);

        doReturn(bookings).when(bookingService).getBookingsByCustomerId(anyInt());

        mockMvc.perform(get("/customer/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.bookings[0].bookingId").value(1));
    }
}
