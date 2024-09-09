package com.user.entities;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
     private int customerId;
	 private String username;
   
     @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password must be at least 8 characters with a special character, upper case, and lower case combination")
	 @NotNull(message = "Please enter a password")
	 @NotBlank(message = "Password cannot be null")
     private String password;
     private String gender;
     @Email
     private String email;
     @Pattern(regexp="[0-9]{10}$",message="phone Number should be 10 digits")
     private String phNumber;
     private String address;
     @Temporal(TemporalType.DATE)
     @JsonFormat(pattern = "yyyy-MM-dd")
     private Date dob;
	public Customer(String username,  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password must be at least 8 characters with a special character, upper case, and lower case combination")
	 @NotNull(message = "Please enter a password")
	 @NotBlank(message = "Password cannot be null")
			 String password,
			 String gender,
			 @Email
			 String email,

		     @Pattern(regexp="[0-9]{10}$",message="phone Number should be 10 digits")
			 String phNumber,
			String address, Date dob) {
		super();
		this.username = username;
		
		this.password = password;
		this.gender = gender;
		this.email = email;
		this.phNumber = phNumber;
		this.address = address;
		this.dob=dob;
		
	}
     
     
}
