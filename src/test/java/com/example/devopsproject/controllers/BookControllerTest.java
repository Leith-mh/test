package com.example.devopsproject.controllers;
import com.example.devopsproject.models.Book;
import com.example.devopsproject.services.BookService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.devopsproject.utils.objectMapper.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @MockBean
    private BookService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /all success")
    void testGetBooksSuccess() throws Exception {

        Book book1 = new Book(1l, "harry potter", "description of harry potter test");
        Book book2 = new Book(2l, "lord of the rings", "description of lord of the rings test");
        doReturn(Lists.newArrayList(book1, book2)).when(service).getAllBooks();

        mockMvc.perform(get("/book/all"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("harry potter")))
                .andExpect(jsonPath("$[0].description", is("description of harry potter test")))
                .andExpect(jsonPath("$[1].name", is("lord of the rings")))
                .andExpect(jsonPath("$[1].description", is("description of lord of the rings test")));
    }

    @Test
    @DisplayName("GET /book/1")
    void testGetBookById() throws Exception {
        // Set up our mocked service
        Book book = new Book(1l, "harry potter", "description of harry potter test");
        doReturn(book).when(service).getBookById(1l);

        // Execute the GET request
        mockMvc.perform(get("/book/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("harry potter")))
                .andExpect(jsonPath("$.description", is("description of harry potter test")));
    }

    @Test
    @DisplayName("POST /book/create")
    void testCreateBook() throws Exception {
        // Set up our mocked service

        Long ownerId = 1l ;
        Book bookToCreate = new Book("harry potter","harry potter description" );
        Book bookToReturn = new Book(1L, "harry potter","harry potter description");
        doReturn(bookToReturn).when(service).createBook(bookToCreate,ownerId);

        // Execute the POST request
        mockMvc.perform(post("/Book/create/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(bookToCreate)))

                // Validate the response code and content type
                .andExpect(status().isNotFound());
    }

}
