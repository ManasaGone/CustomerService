package com.user.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.user.dtos.Booking;
import com.user.dtos.BookingDto;
import com.user.dtos.Route;
import com.user.dtos.Vehicle;



@FeignClient("BOOKINGSERVICE")
public interface BookingServiceInterface {
	@PostMapping("bookings/addBooking")
    public Booking createBooking(@RequestBody Booking booking);
	 @GetMapping("bookings/viewBooking/{bookingId}")
	    public Booking getBookingById(@PathVariable Long bookingId);
	    @GetMapping("bookings/viewAllBookings")
	    public List<BookingDto> viewAllBookings();
	    @DeleteMapping("bookings/deleteBooking/{bookingId}")
	    public String cancelBooking(@PathVariable Long bookingId);
	    @GetMapping("bookings/ViewVehicleById/{vehicleNo}")
	    public Vehicle getVehicleById(@PathVariable String vehicleNo);
	    @GetMapping("bookings/route/{routeId}")
	    public Route getRouteById(@PathVariable int routeId);
	    @GetMapping("bookings/customer/{customerId}")
	    public List<Booking> getBookingsByCustomerId(@PathVariable int customerId);
}
