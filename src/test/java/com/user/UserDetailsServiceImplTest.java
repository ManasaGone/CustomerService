package com.user;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.user.dtos.Booking;
import com.user.dtos.BookingDto;
import com.user.dtos.CustomerDetailsResponse;
import com.user.dtos.Route;
import com.user.dtos.Vehicle;
import com.user.entities.Customer;
import com.user.exceptions.ResourceNotFoundException;
import com.user.feign.BookingServiceInterface;
import com.user.feign.RouteServiceClient;
import com.user.feign.VehicleServiceClient;
import com.user.repositories.CustomerRepository;
import com.user.security.services.UserDetailsServiceImpl;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookingServiceInterface bookingService;

    @Mock
    private VehicleServiceClient vehicleService;

    @Mock
    private RouteServiceClient routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(customerRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("testUser");
        });
    }

    @Test
    void testCreateBooking_VehicleNotFound() {
        Booking booking = new Booking();
        booking.setVehicleName("TestVehicle");
        booking.setRouteId(1);
        booking.setJourneyDate(LocalDate.now().plusDays(1));

        when(vehicleService.getVehicleByName("TestVehicle")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.createBooking(booking);
        });
    }

    @Test
    void testCreateBooking_RouteNotFound() {
        Booking booking = new Booking();
        booking.setVehicleName("TestVehicle");
        booking.setRouteId(1);
        booking.setJourneyDate(LocalDate.now().plusDays(1));

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo("1234");
        vehicle.setVehicleName("TestVehicle");

        when(vehicleService.getVehicleByName("TestVehicle")).thenReturn(vehicle);
        when(routeService.getRouteById(1)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.createBooking(booking);
        });
    }

    @Test
    void testGetBookingById_BookingNotFound() {
        when(bookingService.getBookingById(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.getBookingById(1L);
        });
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        when(bookingService.getBookingById(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.cancelBooking(1L);
        });
    }

    @Test
    void testChangePassword_IncorrectOldPassword() {
        Customer customer = new Customer();
        customer.setPassword("hashedPassword");

        when(customerRepository.findByUsername("testUser")).thenReturn(Optional.of(customer));

        String result = userDetailsService.changePassword("testUser", "wrongPassword", "newPassword");

        assertEquals("Incorrect password", result);
    }

    @Test
    void testChangePassword_UserNotFound() {
        when(customerRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        String result = userDetailsService.changePassword("testUser", "oldPassword", "newPassword");

        assertEquals("User not found", result);
    }

    @Test
    void testGetCustomerWithBookings_CustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userDetailsService.getCustomerWithBookings(1);
        });
    }

    @Test
    void testGetCustomerWithBookings_Success() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(bookingService.getBookingsByCustomerId(1)).thenReturn(Arrays.asList(booking));

        CustomerDetailsResponse response = userDetailsService.getCustomerWithBookings(1);

        assertNotNull(response);
        assertEquals(1, response.getBookings().size());
    }
}
