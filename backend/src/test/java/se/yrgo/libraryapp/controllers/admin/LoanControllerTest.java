package se.yrgo.libraryapp.controllers.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoanData;

public class LoanControllerTest {
    @Test
    void successfulLend() {
        BookDao bookDao = mock(BookDao.class);
        LoanController loanController = new LoanController(bookDao);
        BookId bookId = BookId.of(1);
        UserId userId = UserId.of(2);
        LoanData loanData = new LoanData(bookId, userId);
        when(bookDao.lend(bookId, userId)).thenReturn(true);
        boolean result = loanController.lendBook(loanData);
        verify(bookDao).lend(bookId, userId);
        assertThat(result).isTrue();
    }

    @Test
    void unsuccessfulLend() {
        BookDao bookDao = mock(BookDao.class);
        LoanController loanController = new LoanController(bookDao);
        BookId bookId = BookId.of(1);
        UserId userId = UserId.of(2);
        LoanData loanData = new LoanData(bookId, userId);
        when(bookDao.lend(bookId, userId)).thenReturn(false);
        boolean result = loanController.lendBook(loanData);
        assertThat(result).isFalse();
    }

    @Test
    void successfulReturnBook() {
        BookDao bookDao = mock(BookDao.class);
        LoanController loanController = new LoanController(bookDao);
        BookId bookId = BookId.of(4);
        when(bookDao.returnBook(bookId)).thenReturn(true);
        boolean result = loanController.returnBook(bookId);
        verify(bookDao).returnBook(bookId);
        assertThat(result).isTrue();
    }

    @Test
    void unsuccessfulReturnBook() {
        BookDao bookDao = mock(BookDao.class);
        LoanController loanController = new LoanController(bookDao);
        BookId bookId = BookId.of(4);
        when(bookDao.returnBook(bookId)).thenReturn(false);
        boolean result = loanController.returnBook(bookId);
        assertThat(result).isFalse();
    }

    @Test
    void successfulOverdueLoans() {
        BookDao bookDao = mock(BookDao.class);
        LoanController loanController = new LoanController(bookDao);
        UserId userId = UserId.of(2);
        List<BookLoan> expectedList = List.of(new BookLoan(mock(Book.class), userId, LocalDate.now().minusDays(5)));
        when(bookDao.overdueLoans()).thenReturn(expectedList);
        List<BookLoan> result = loanController.overdue();
        assertThat(result).isEqualTo(expectedList);
        verify(bookDao).overdueLoans();
    }
}
