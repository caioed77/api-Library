package com.libraryApi.apilibrary.api.resource;

import com.libraryApi.apilibrary.api.DTO.BookDTO;
import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService bookService, ModelMapper mapper) {
        this.bookService = bookService;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto){
        Book entity = modelMapper.map( dto, Book.class);
        entity = bookService.save(entity);

        return modelMapper.map(entity, BookDTO.class);

    }

}
