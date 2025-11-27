package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import javax.sql.DataSource;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        // this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserByName() {
        // this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> dbResult = userDao.get(Integer.toString(userId.getId()));
        assertThat(dbResult).isPresent();
        assertThat(dbResult.get().getName()).isEqualTo(username);
        assertThat(dbResult.get().getId()).isEqualTo(userId);
    }

    @Test
    void successfulInsertUser() throws SQLException {
        final String username = "test321";
        final String realname = "malin";
        final String password = "password";

        UserDao userDao = new UserDao(ds);
        boolean result = userDao.insertUser(username, realname, password);

        assertThat(result).isTrue();
        Optional<User> dbResult = userDao.get("3");
        assertThat(dbResult).isPresent();
        assertThat(dbResult.get().getName()).isEqualTo(username);

    }

    @Test
    void failedInsertUser() {

        final String username = "test";
        final String realname = "malin";
        final String password = "password";

        UserDao userDao = new UserDao(ds);
        boolean result = userDao.insertUser(username, realname, password);
        assertThat(result).isFalse();
    }

    @Test
    void successfulIsNameAvailable() {
        UserDao userDao = new UserDao(ds);
        boolean result = userDao.isNameAvailable("ledigt");
        assertThat(result).isTrue();
    }

    @Test
    void unsuccesfulIsNameAvailable() {
        UserDao userDao = new UserDao(ds);
        boolean result = userDao.isNameAvailable("test");
        assertThat(result).isFalse();
    }
}
