package com.user.security.services;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingServiceInterface bookingService;

    @Autowired
    private VehicleServiceClient vehicleService;

    @Autowired
    private RouteServiceClient routeService;

    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(customer);
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        logger.info("Creating booking for vehicle name: {}", booking.getVehicleName());

        // Retrieve vehicle details using vehicle name
        Vehicle vehicle = vehicleService.getVehicleByName(booking.getVehicleName());
        if (vehicle == null) {
            logger.error("Vehicle not found for name: {}", booking.getVehicleName());
            throw new ResourceNotFoundException("Vehicle not found for name :: " + booking.getVehicleName());
        }
        booking.setVehicleNo(vehicle.getVehicleNo());
        booking.setVehicleName(vehicle.getVehicleName());

        // Retrieve route details using route ID
        Route route = routeService.getRouteById(booking.getRouteId());
        if (route == null) {
            logger.error("Route not found for ID: {}", booking.getRouteId());
            throw new ResourceNotFoundException("Route not found for id :: " + booking.getRouteId());
        }
        booking.setSource(route.getSource());
        booking.setDestination(route.getDestination());

        // Set initial values for fields not provided
        if (booking.getBookingDate() == null) {
            booking.setBookingDate(LocalDate.now());
        }
        if (booking.getJourneyDate() == null) {
            throw new IllegalArgumentException("Journey date is required");
        }

        // Set booking status based on journey date
        booking.setBookingStatus(booking.getJourneyDate().isBefore(LocalDate.now()) ? "Completed" : "Upcoming");

        // Save booking via BookingService
        Booking savedBooking = bookingService.createBooking(booking);
        logger.info("Booking created successfully with ID: {}", savedBooking.getBookingId());
        return savedBooking;
    }

    public Booking getBookingById(Long bookingId) {
        logger.info("Fetching booking by ID: {}", bookingId);
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            logger.error("Booking not found for ID: {}", bookingId);
            throw new ResourceNotFoundException("Booking not found for this id :: " + bookingId);
        }
        return booking;
    }

    public void cancelBooking(Long bookingId) {
        logger.info("Cancelling booking with ID: {}", bookingId);
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            logger.error("Booking not found for ID: {}", bookingId);
            throw new ResourceNotFoundException("Booking not found for this id :: " + bookingId);
        }
        booking.setBookingStatus("Cancelled");
        bookingService.createBooking(booking); // Assuming createBooking updates the status if bookingId exists
        logger.info("Booking cancelled successfully for ID: {}", bookingId);
    }

    public List<BookingDto> viewAllBookings() {
        logger.info("Fetching all bookings");

        List<BookingDto> bookings = bookingService.viewAllBookings();
        if (bookings.isEmpty()) {
            logger.warn("No bookings found.");
        }

        return bookings;
    }

    public Vehicle getVehicleByNo(String vehicleNo) {
        logger.info("Fetching vehicle by number: {}", vehicleNo);
        Vehicle vehicle = vehicleService.getVehicleByNo(vehicleNo);
        if (vehicle == null) {
            logger.error("Vehicle not found for number: {}", vehicleNo);
            throw new ResourceNotFoundException("Vehicle not found for number :: " + vehicleNo);
        }
        return vehicle;
    }

    public List<Vehicle> getAllVehicles() {
        logger.info("Fetching all vehicles");
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if (vehicles.isEmpty()) {
            logger.warn("No vehicles found.");
        }
        return vehicles;
    }

    public Route getRouteById(int routeId) {
        logger.info("Fetching route details for route ID: {}", routeId);
        Route route = routeService.getRouteById(routeId);
        if (route == null) {
            logger.error("Route not found for ID: {}", routeId);
            throw new ResourceNotFoundException("Route not found for id :: " + routeId);
        }
        return route;
    }

    public List<Route> getAllRoutes() {
        logger.info("Fetching all routes");
        List<Route> routes = routeService.getAllRoutes();
        if (routes.isEmpty()) {
            logger.warn("No routes found.");
            throw new ResourceNotFoundException("No routes found");
        }
        return routes;
    }

    public String changePassword(String userName, String oldPassword, String newPassword) {
        Optional<Customer> opcust = customerRepository.findByUsername(userName);

        if (opcust.isPresent()) {
            Customer dbcust = opcust.get();
            if (bcrypt.matches(oldPassword, dbcust.getPassword())) {
                String encrypted = bcrypt.encode(newPassword);
                dbcust.setPassword(encrypted);
                customerRepository.save(dbcust);
                logger.info("Password changed successfully for user: " + userName);
                return "Password changed successfully";
            } else {
                logger.info("Incorrect password for user: " + userName);
                return "Incorrect password";
            }
        } else {
            logger.warn("User not found: " + userName);
            return "User not found";
        }
    }

    public CustomerDetailsResponse getCustomerWithBookings(int customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found for ID: " + customerId));

        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);

        return new CustomerDetailsResponse(customer.getCustomerId(), bookings);
    }
}
