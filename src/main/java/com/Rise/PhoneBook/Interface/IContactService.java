
package com.Rise.PhoneBook.Interface;

import com.Rise.PhoneBook.Model.Contact;
import com.Rise.PhoneBook.Exceptions.ApiRequestException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IContactService {
    Page<Contact> getContacts(int page, int size);
    List<Contact> searchContacts(String firstName);
    Contact addContact(Contact contact);
    Optional<Contact> editContact(Contact updatedContact) throws ApiRequestException;
    void deleteContact(Long id);
}
