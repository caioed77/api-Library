package com.libraryApi.apilibrary.service;

import com.libraryApi.apilibrary.api.domain.Book;
import com.libraryApi.apilibrary.api.domain.repository.BookRepository;
import com.libraryApi.apilibrary.api.exception.BusinessException;
import com.libraryApi.apilibrary.api.services.BookService;
import com.libraryApi.apilibrary.api.services.imp.BookServiceImpl;
import org.assertj.core.api.Assertions;
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
    public void saveBookTest() throws BusinessException {

        Book book = createBook(Book.builder());
        Mockito.when( repository.save(book)).thenReturn(
                createBook(Book.builder()
                        .id(1L)));

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTittle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private static Book createBook(Book.BookBuilder builder) {
        return builder.isbn("123").author("Fulano").tittle("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn dupilcado" )
    public void shouldNotSaveABookWithDuplicatedISBN(){
        Book book = createBook(Book.builder());
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() ->  service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn ja cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

}
