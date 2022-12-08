package com.example.devopsproject.services;

import com.example.devopsproject.exceptions.ResourceNotFoundException;
import com.example.devopsproject.models.Owner;
import com.example.devopsproject.models.Book;
import com.example.devopsproject.repositories.OwnerRepository;
import com.example.devopsproject.repositories.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class BookServiceTest {

    /**
     * Autowire in the service we want to test
     */
    @Autowired
    private BookService service;

    /**
     * Create a mock implementation of the BookRepository
     */
    @MockBean
    private BookRepository bookRepository;

    /**
     * Create a mock implementation of the OwnerRepository
     */
    @MockBean
    private OwnerRepository ownerRepository;

    @Test
    @DisplayName("Test finBookById Success")
    void testFindById() {
        // Set up our mock repository
        Book book = new Book(1l, "harry potter", "test harry potter description");
        doReturn(Optional.of(book)).when(bookRepository).findById(1l);

        // Execute the service call
        Book returnedBook = service.getBookById(1l);

        // Assert the response
        Assertions.assertSame(returnedBook, book, "The book returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test finBookById Not Found")
    void testFindByIdNotFound() {

        doReturn(Optional.empty()).when(bookRepository).findById(1l);

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Book returnedBook = service.getBookById(1l);
        });
        String expectedMessage = "Book with this Id not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    @DisplayName("Test getAllBooks")
    void testGetAllBooks() {

        // Set up our mock repository
        Book book1 = new Book(1l, "harry potter", "description of putchii test");
        Book book2 = new Book(2l, "lord of the rings", "description of lord of the rings test");
        doReturn(Arrays.asList(book1, book2)).when(bookRepository).findAll();

        // Execute the service call
        List<Book> books = service.getAllBooks();

        // Assert the response
        Assertions.assertEquals(2, books.size(), "findAll should return 2 books");
    }


    @Test
    @DisplayName("Test createBook")
    void testCreateBook() {

        Owner owner = new Owner(1l, "sofiene", 15);
        doReturn(Optional.of(owner)).when(ownerRepository).findById(1l);

        Long ownerId = 1l ;

        Book bookToSave = new Book("harry potter","harry potter description");
        Book book = new Book(1l, "harry potter", "harry potter description", owner);
        doReturn(book).when(bookRepository).save(bookToSave);

        // Execute the service call
        Book returnedBook = service.createBook(bookToSave,ownerId);

        // Assert the response
        Assertions.assertNotNull(returnedBook, "The saved book should not be null");
        Assertions.assertSame(book, returnedBook, "The returned book is not the same as expected");
        Assertions.assertSame(book.getOwner(),owner, "The book owner is not with the passed Id");
    }
}
