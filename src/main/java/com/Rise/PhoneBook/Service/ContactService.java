package com.Rise.PhoneBook.Service;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.Rise.PhoneBook.Exceptions.ApiRequestException;
import com.Rise.PhoneBook.Interface.IContactService;
import com.Rise.PhoneBook.Model.Contact;
import com.Rise.PhoneBook.Repository.ContactRepository;

@Service
public class ContactService implements IContactService {

	@Autowired
	private ContactRepository contactRepository;
	private Logger logger = Logger.getLogger(ContactService.class);

	@Cacheable(value = "contacts", key = "#page + '-' + #size")
	public Page<Contact> getContacts(int page, int size) {
		Page<Contact> contacts = contactRepository.findAll(PageRequest.of(page, size));
		logger.info("Get Contacts api׳s call with page number : " + page);
		return contacts;
	}

	@CacheEvict(value = "contacts", allEntries = true)
	public List<Contact> searchContacts(String firstName) {
		logger.info("Search Contacts api׳s call with name : " + firstName);
		return contactRepository.findContactsByFirstName(firstName);
	}

	@CacheEvict(value = "contacts", allEntries = true)
	public Contact addContact(Contact contact) {
		Contact addedContact = contactRepository.save(contact);
		logger.info("A new contact with id : " + contact.getId() + " added");
		return addedContact;
	}

	@CacheEvict(value = "contacts", allEntries = true)
	public Optional<Contact> editContact(Contact updatedContact) throws ApiRequestException {
		Long contactId = updatedContact.getId();
		Optional<Contact> existContact = contactRepository.findById(contactId);
		if (!existContact.isPresent()) {
			throw new ApiRequestException(ApiRequestException.NotFoundException(contactId.toString()));
		}
		return contactRepository.findById(updatedContact.getId()).map(contact -> {
			contact.setFirstName(updatedContact.getFirstName());
			contact.setLastName(updatedContact.getLastName());
			contact.setPhone(updatedContact.getPhone());
			contact.setAddress(updatedContact.getAddress());
			return contactRepository.save(contact);
		});
	}

	@CacheEvict(value = "contacts", allEntries = true)
	public void deleteContact(Long id) {
		contactRepository.deleteById(id);
	}

}
