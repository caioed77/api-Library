package com.libraryApi.apilibrary.api.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryApi.apilibrary.api.DTO.BookDTO;
import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.exception.ApiErrors;
import com.libraryApi.apilibrary.api.exception.BusinessException;
import com.libraryApi.apilibrary.api.services.BookService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Optional;
import java.util.regex.Matcher;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";
    
    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve Cria os livros")
    public void createBookTest() throws Exception, BusinessException {

        BookDTO dto = createNewBook();
        Book savedBook = Book.builder().id(10L).author("Arthur").tittle("As aventuras").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isCreated())
                .andExpect( jsonPath("id").value(10L))
                .andExpect( jsonPath("tittle").value(dto.getTittle()))
                .andExpect( jsonPath("author").value(dto.getAuthor()))
                .andExpect( jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve obter informações de um livro")
    public void getBookDetailsTest() throws Exception {

        Long id = 1L;
        Book book = Book.builder()
                .id(id)
                .tittle(createNewBook().getTittle())
                .author(createNewBook().getAuthor())
                .isbn(createNewBook().getIsbn())
                .build();
        BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

        final var httpServletRequestBuilder = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(httpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("tittle").value(createNewBook().getTittle()))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));
    }


    private static BookDTO createNewBook() {
        return BookDTO.builder().author("Arthur").tittle("As aventuras").isbn("001").build();
    }

    @Test
    @DisplayName("Deve gerar erro de validacao quando nao houver dados corretos para gerar um livro")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request).andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));

    }


    @Test
    @DisplayName("Deve lancar erro ao tentar cadastrar um livro com isbn ja utilizado por outro.")
    public void createBookWithDuplicatedIsbn() throws Exception, BusinessException {
        BookDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String messageErro = "Isbn ja cadastrado.";
        BDDMockito.given(service
                        .save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(messageErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(messageErro));
        }

        @ExceptionHandler(BusinessException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiErrors handleBusinessException(BusinessException ex){
            return new ApiErrors(ex);
        }

    }
