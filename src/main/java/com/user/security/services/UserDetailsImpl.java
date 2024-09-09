package com.user.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.entities.Customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private int customerId;
	private String username;
	private String gender;
	@JsonIgnore
	private String password;
	private String email;
	private String phNumber;
	private String address;
	private Date dob;
	
	private Collection<? extends GrantedAuthority> authorities;
	public UserDetailsImpl(int customerId,String username, String gender,String password, String email, String phNumber,Date dob,String address) {
		this.customerId = customerId;
		this.username = username;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.phNumber = phNumber;
		this.address=address;
		this.dob=dob;
		this.authorities = authorities;
	}
	public static UserDetailsImpl build(Customer customer) {
//		List<GrantedAuthority> authorities = user.getRoles().stream()
//				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
//				.collect(Collectors.toList());
		
		 List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		return new UserDetailsImpl(
				customer.getCustomerId(),
				customer.getUsername(),
				customer.getGender(), 
				customer.getPassword(),
				customer.getEmail(),
				customer.getPhNumber(),
				customer.getDob(),
				customer.getAddress());
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public int getCustomerId() {
		return customerId;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPhNumber() {
		return phNumber;
	}
	public String getAddress() {
		return phNumber;
	}
	@Override
	public String getPassword() {
		return password;
	}
	public String getGender() {
		return gender;
	}
	public Date getDob() {
		return dob;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(customerId, user.customerId);
	}
	
	
}