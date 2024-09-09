package com.user.controllers;


import com.user.dtos.Booking;



import com.user.dtos.BookingDto;
import com.user.dtos.CustomerDetailsResponse;
import com.user.dtos.Route;
import com.user.dtos.Vehicle;
import com.user.feign.BookingServiceInterface;
import com.user.feign.RouteServiceClient;
import com.user.feign.VehicleServiceClient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin("*")
@RestController
@RequestMapping("/customer")

public class CustomerController {
@Autowired
private VehicleServiceClient vehicleService;
@Autowired
private RouteServiceClient routeService;
@Autowired
private BookingServiceInterface bookingService;
    @PostMapping("/addBooking")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/viewBooking/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }
    
    @GetMapping("/viewAllBookings")
    public List<BookingDto> viewAllBookings() {
        return bookingService.viewAllBookings();
    }
    @DeleteMapping("/deleteBooking/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking cancelled successfully";
    }
    @GetMapping("/ViewAllVehicles")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/ViewVehicleById/{vehicleNo}")
    public Vehicle getVehicleById(@PathVariable String vehicleNo) {
        return bookingService.getVehicleById(vehicleNo);
    }
    @GetMapping("/ViewAllRoutes")
   	public List<Route> getAllRoutes(){
   		return routeService.getAllRoutes();
   	}
    @GetMapping("/route/{routeId}")
    public Route getRouteById(@PathVariable int routeId) {
        return bookingService.getRouteById(routeId);
    }

    @GetMapping("/bookings/{customerId}")
    public CustomerDetailsResponse getBookingsByCustomerId(@PathVariable int customerId) {
        // Fetch the list of bookings for the customer
        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
        
        // Return a response containing customerId and bookings
        return new CustomerDetailsResponse(customerId, bookings);
    }
	
}
