package com.Rise.PhoneBook;

import com.Rise.PhoneBook.Exceptions.ApiRequestException;
import com.Rise.PhoneBook.Model.Contact;
import com.Rise.PhoneBook.Repository.ContactRepository;
import com.Rise.PhoneBook.Service.ContactService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("contacts", "contactSearch");
    }

    @Test
    void getContacts() {
        int page = 0;
        int size = 2;
        List<Contact> contacts = Arrays.asList(new Contact(), new Contact());
        Page<Contact> contactPage = new PageImpl<>(contacts);

        when(contactRepository.findAll(any(Pageable.class))).thenReturn(contactPage);

        Page<Contact> result = contactService.getContacts(page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(contactRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void searchContacts() {
        String query = "John";
        List<Contact> contacts = Arrays.asList(new Contact());

        when(contactRepository.findContactsByFirstName(query)).thenReturn(contacts);

        List<Contact> result = contactService.searchContacts(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contactRepository, times(1)).findContactsByFirstName(query);
    }

    @Test
    void addContact() {
        Contact contact = new Contact();
        contact.setId(1L);

        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = contactService.addContact(contact);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void editContact() {
        Contact existingContact = new Contact();
        existingContact.setId(1L);
        existingContact.setFirstName("John");

        Contact updatedContact = new Contact();
        updatedContact.setId(1L);
        updatedContact.setFirstName("Johnny");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(updatedContact);

        Optional<Contact> result = contactService.editContact(updatedContact);

        assertTrue(result.isPresent());
        assertEquals("Johnny", result.get().getFirstName());
 
    }

    @Test
    void editContact_NotFound() {
        Contact updatedContact = new Contact();
        updatedContact.setId(1L);

        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        @SuppressWarnings("unused")
		ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            contactService.editContact(updatedContact);
        });

    }

    @Test
    void deleteContact() {
        Long contactId = 1L;

        doNothing().when(contactRepository).deleteById(contactId);

        contactService.deleteContact(contactId);

        verify(contactRepository, times(1)).deleteById(contactId);
    }
}
