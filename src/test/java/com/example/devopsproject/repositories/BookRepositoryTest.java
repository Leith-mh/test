package com.example.devopsproject.repositories;

import com.example.devopsproject.models.Book;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BookRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("books.yml")
    void testGetAllPets() {
        List<Book> books = Lists.newArrayList(repository.findAll());
        Assertions.assertEquals(2, books.size(), "Expected 2 books in the database");
    }

    @Test
    @DataSet("books.yml")
    void testGetByIdSuccess() {
        Optional<Book> book = repository.findById(1L);
        Assertions.assertTrue(book.isPresent(), "We should find an book with ID 1");

        Book b = book.get();
        Assertions.assertEquals(1, b.getId(), "The book ID should be 1");
        Assertions.assertEquals("book 1", b.getName(), "Incorrect book name");
        Assertions.assertEquals("the description of book 1", b.getDescription(), "Incorrect book age");
    }

    @Test
    @DataSet("books.yml")
    void testGetByIdNotFound() {
        Optional<Book> book = repository.findById(3L);
        Assertions.assertFalse(book.isPresent(), "A book with ID 3 should not be found");
    }
}
