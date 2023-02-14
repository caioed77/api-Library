package com.libraryApi.apilibrary.api.domain.repository;

import com.libraryApi.apilibrary.api.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
}
