package com.example.devopsproject.services;

import com.example.devopsproject.exceptions.ResourceNotFoundException;
import com.example.devopsproject.models.Owner;
import com.example.devopsproject.models.Book;
import com.example.devopsproject.repositories.OwnerRepository;
import com.example.devopsproject.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    @Autowired
    BookRepository bookRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public Book createBook(Book book, Long ownerId) throws ResourceNotFoundException {

        Optional<Owner> ownerData = this.ownerRepository.findById(ownerId);
        if(ownerData.isPresent()) {
            Owner owner = ownerData.orElseThrow(()-> new ResourceNotFoundException("Owner not found"));
            book.setOwner(owner);
            Book createdBook = this.bookRepository.save(book);
            return createdBook;
        }else{
            throw new  ResourceNotFoundException("No owner found with that Id");
        }
    }

    public List<Book> getAllBooks() throws ResourceNotFoundException {
        List<Book> books = this.bookRepository.findAll();
        return books;
    }

    public Book getBookById(Long bookId) throws ResourceNotFoundException {
        Optional<Book> bookData = this.bookRepository.findById(bookId);
        if(bookData.isPresent()){
            Book book = bookData.orElseThrow(()-> new ResourceNotFoundException("owner not found"));
            return book;
        }else{
            throw new  ResourceNotFoundException("Book with this Id not found");
        }
    }

}
