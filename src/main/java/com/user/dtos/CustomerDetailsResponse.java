package com.user.dtos;

import java.util.List;

public class CustomerDetailsResponse {
    private int customerId ;
    private List<Booking> bookings;
    
	public CustomerDetailsResponse(int customerId, List<Booking> bookings) {
		super();
		this.customerId = customerId;
		this.bookings = bookings;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public List<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

    
}