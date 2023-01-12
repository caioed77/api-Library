package com.libraryApi.apilibrary.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryApi.apilibrary.api.DTO.BookDTO;
import com.libraryApi.apilibrary.api.domain.Book;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



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
    public void createBookTest() throws Exception{

        BookDTO dto = BookDTO.builder().author("Arthur").tittle("As aventuras").isbn("001").build();
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
    @DisplayName("Deve gerar erro de validacao quando nao houver dados corretos para gerar um livro")
    public void createInvalidBookTest(){

    }

}
