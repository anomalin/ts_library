package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.BookLoan;
import se.yrgo.libraryapp.entities.DdsClassification;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class BookDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
        BookDaoIntegrationTest.ds = ds;
    }

    @Test
    void getExcistingBook() {
        final BookId id = BookId.of(1);
        BookDao bookDao = new BookDao(ds);
        Optional<Book> result = bookDao.get(BookId.of(1));
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        Book book = result.get();
        BookEdition edition = book.getEdition();
        assertThat(edition).isNotNull();
        assertThat(edition.getIsbn()).isEqualTo("9781509302000");

    }

    @Test
    void getNonExistingBook() {
        BookDao bookDao = new BookDao(ds);
        Optional<Book> result = bookDao.get(BookId.of(300));
        assertThat(result).isNotPresent();
    }

    @Test
    void successfulFindBookByTitle() {
        BookDao bookDao = new BookDao(ds);
        Set<BookEdition> result = bookDao.find(null, "Här kommer Pippi Långstrump", null);
        assertThat(result).hasSize(1);
        BookEdition edition = result.iterator().next();
        assertThat(edition.getTitle()).isEqualTo("Här kommer Pippi Långstrump");
        assertThat(edition.getAuthor()).isEqualTo("Astrid Lindgren");

    }

    @Test
    void successfulBookEditionWithClass() {
        BookDao bookDao = new BookDao(ds);
        DdsClassification DdsClass = new DdsClassification(898, "Scandinavian");
        Set<BookEdition> result = bookDao.withClass(DdsClass);
        assertThat(result).hasSize(1);
        BookEdition edition = result.iterator().next();
        assertThat(edition.getIsbn()).isEqualTo("9789129697285");
    }

    @Test
    void successfulLoans() {
        BookDao bookDao = new BookDao(ds);
        UserId userId = UserId.of(2);
        List<BookLoan> result = bookDao.loans(userId);
        assertThat(result).hasSize(2);
        BookLoan loans = result.iterator().next();
        assertThat(loans.getOverdue()).isTrue();
    }

    @Test
    void successfulOverdueLoans() {
        BookDao bookDao = new BookDao(ds);
        List<BookLoan> result = bookDao.overdueLoans();
        assertThat(result).hasSize(2);
    }

    /* TODO:Kolla på senare
    @Test
    void successfulLend()  {
        BookDao bookDao = new BookDao(ds);
        UserId userId = UserId.of(2);
        BookId bookId = BookId.of(1);   
    }

    @Test
    void unsuccessfulLend() {
        BookDao bookDao = new BookDao(ds);
        UserId userId = UserId.of(666);
        BookId bookId = BookId.of(1523);
        boolean result = bookDao.lend(bookId, userId);
        assertThat(result).isFalse();
    }*/

    @Test
    void successfulReturnBook() {
        BookDao bookDao = new BookDao(ds);
        BookId bookId = BookId.of(4);
        boolean result = bookDao.returnBook(bookId);
        assertThat(result).isTrue();
    }

    @Test
    void unsuccessfulReturnBook() {
        BookDao bookDao = new BookDao(ds);
        BookId bookId = BookId.of(10);
        boolean result = bookDao.returnBook(bookId);
        assertThat(result).isFalse();
    }
}
