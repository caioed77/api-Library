package com.libraryApi.apilibrary.api.services.imp;

import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.domain.repository.BookRepository;
import com.libraryApi.apilibrary.api.exception.BusinessException;
import com.libraryApi.apilibrary.api.services.BookService;

import java.util.Optional;

public class BookServiceImpl implements BookService{

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

        @Override
        public Book save(Book book) throws BusinessException {
            if (repository.existsByIsbn(book.getIsbn())){
                throw new BusinessException("Isbn ja cadastrado");
            }

            return repository.save(book);
        }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }
}
