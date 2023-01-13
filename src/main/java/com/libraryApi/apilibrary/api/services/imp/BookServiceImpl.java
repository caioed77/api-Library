package com.libraryApi.apilibrary.api.services.imp;

import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.domain.repository.BookRepository;
import com.libraryApi.apilibrary.api.services.BookService;

public class BookServiceImpl implements BookService{

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

        @Override
        public Book save(Book book) {
            return repository.save(book);
        }
    }
