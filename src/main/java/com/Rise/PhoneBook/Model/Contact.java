package com.Rise.PhoneBook.Model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contacts", indexes = { @Index(name = "idx_first_name", columnList = "firstName"),
		@Index(name = "idx_last_name", columnList = "lastName") })
@Getter
@Setter
@ToString
public class Contact implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Valid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "First Name is mandatory")
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	@NotNull(message = "Last Name is mandatory")
	@NotBlank(message = "Last Name is mandatory")
	private String lastName;
	@Pattern(message = "Phone must be consist of 10 digits", regexp = "(^$|[0-9]{10})")
	private String phone;
	@NotNull(message = "Address Name is mandatory")
	@NotBlank(message = "Address Name is mandatory")
	private String address;


}
