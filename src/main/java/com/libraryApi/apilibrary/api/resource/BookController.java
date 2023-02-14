package com.libraryApi.apilibrary.api.resource;

import com.libraryApi.apilibrary.api.DTO.BookDTO;
import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.exception.ApiErrors;
import com.libraryApi.apilibrary.api.exception.BusinessException;
import com.libraryApi.apilibrary.api.services.BookService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BookDTO create(@RequestBody @Validated BookDTO dto) throws BusinessException {
        Book entity = modelMapper.map( dto, Book.class);
        entity = bookService.save(entity);

        return modelMapper.map(entity, BookDTO.class);

    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id){
        Book book = bookService.getById(id).get();
        return modelMapper.map(book, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(@NotNull MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        return new ApiErrors(bindingResult);


    }






}
