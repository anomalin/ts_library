package se.yrgo.libraryapp.controllers.admin;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;

public class BookControllerTest {
    @Test
    void successfulAdminBookSearch() {
        BookDao bookDao = mock(BookDao.class);
        BookController bookController = new BookController(bookDao);
        Book book = new Book(BookId.of(100), true, mock(BookEdition.class));
        when(bookDao.get(book.getId())).thenReturn(Optional.of(book));
        Book result = bookController.search(BookId.of("100").toString());
        assertThat(result.getId()).isEqualTo(book.getId());
        verify(bookDao).get(book.getId());
    }

    @Test
    void unsuccessfulAdminBookSearch() {
        BookDao bookDao = mock(BookDao.class);
        BookController bookController = new BookController(bookDao);
        Book book = new Book(BookId.of(100), true, mock(BookEdition.class));
        when(bookDao.get(book.getId())).thenReturn(Optional.empty());
        assertThat(bookController.search("100")).isNull();
        verify(bookDao).get(BookId.of(100));
    }
}
