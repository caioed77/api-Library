package com.libraryApi.apilibrary.service;

import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.domain.repository.BookRepository;
import com.libraryApi.apilibrary.api.services.BookService;
import com.libraryApi.apilibrary.api.services.imp.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl( repository );
     }


    @Test
    @DisplayName("Deve salvar o livro")
    public void saveBookTest(){

        Book book = Book.builder().isbn("123").author("Fulano").tittle("As aventuras").build();
        Mockito.when( repository.save(book)).thenReturn(
                Book.builder()
                        .id(1L)
                        .isbn("123")
                        .author("Fulano")
                        .tittle("As aventuras").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTittle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

}
