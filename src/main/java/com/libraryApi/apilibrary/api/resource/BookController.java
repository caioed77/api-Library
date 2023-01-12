package com.libraryApi.apilibrary.api.resource;

import com.libraryApi.apilibrary.api.DTO.BookDTO;
import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto){
        Book entity = Book.builder().author(dto.getAuthor()).tittle(dto.getTittle()).isbn(dto.getIsbn()).build();
        entity = bookService.save(entity);

        return BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .tittle(entity.getTittle())
                .isbn(entity
                .getIsbn()).build();

    }

}
