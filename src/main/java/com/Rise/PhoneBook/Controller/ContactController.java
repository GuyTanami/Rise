package com.Rise.PhoneBook.Controller;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Rise.PhoneBook.Exceptions.ApiRequestException;
import com.Rise.PhoneBook.Model.Contact;
import com.Rise.PhoneBook.Service.ContactService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;
	private Logger logger = Logger.getLogger(ContactController.class);

	@GetMapping
	public ResponseEntity<Page<Contact>> getContacts(@RequestParam(defaultValue = "0") @Min(0) Integer page,
			@RequestParam(defaultValue = "10") @Positive @Max(value = 10) Integer size) {
		Page<Contact> cotacts = contactService.getContacts(page, size);
		return new ResponseEntity<>(cotacts, HttpStatus.OK);

	}

	@GetMapping("/search")
	public ResponseEntity<List<Contact>> searchContacts(@RequestParam String query) {
		List<Contact> cotacts = contactService.searchContacts(query);
		return new ResponseEntity<>(cotacts, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Contact> addContact(@Valid @RequestBody Contact contact) {
		Contact savedContact = contactService.addContact(contact);
		return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Contact> editContact(@RequestBody Contact updatedContact) {
		Optional<Contact> updated = contactService.editContact(updatedContact);
		logger.info("A Contact with id : " + updatedContact.getId() + " updated");
		return updated.map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable @Positive @NotNull Long id) {

		try {
			contactService.deleteContact(id);
		} catch (ApiRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		logger.info("A Contact with id : " + id + "deleted");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
