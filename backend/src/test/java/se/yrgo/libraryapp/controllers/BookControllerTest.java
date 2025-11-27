package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;

public class BookControllerTest {
    @Test
    void successfulUserBookSearch() {
        BookDao bookDao = mock(BookDao.class);
        BookController bookController = new BookController(bookDao);
        BookEdition be = new BookEdition("Maus", "Art Spiegelman", "9780141014081");
        Set<BookEdition> expected = Set.of(be);
        when(bookDao.find(be.getIsbn(), be.getTitle(), be.getAuthor())).thenReturn(expected);
        Set<BookEdition> result = bookController.search("9780141014081", "Maus", "Art Spiegelman", null);
        assertThat(result).isEqualTo(expected);
        verify(bookDao).find(be.getIsbn(), be.getTitle(), be.getAuthor());
    }

    @Test
    void unsuccessfulUserBookSearch() {
        BookDao bookDao = mock(BookDao.class);
        BookController bookController = new BookController(bookDao);
        BookEdition be = new BookEdition("Maus", "Art Spiegelman", "9780141014081");
        when(bookDao.find(be.getIsbn(), be.getTitle(), be.getAuthor())).thenReturn(Set.of());
        Set<BookEdition> result = bookController.search("9780141014081", "Maus", "Art Spiegelman", null);
        assertThat(result).isEmpty();
        verify(bookDao).find(be.getIsbn(), be.getTitle(), be.getAuthor());
    }
}
