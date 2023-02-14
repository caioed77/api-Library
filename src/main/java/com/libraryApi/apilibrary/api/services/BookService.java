package com.libraryApi.apilibrary.api.services;

import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.exception.BusinessException;

import java.util.Optional;

public interface BookService {
    Book save(Book any) throws BusinessException;

    Optional<Book> getById(Long id);
}
