package se.yrgo.libraryapp.dao;

import java.util.UUID;

import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.CredentialsException;

import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class SessionDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void init() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        SessionDaoIntegrationTest.ds = ds;
    }
    
    @Test
    void createAndValidateSession() {
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = UserId.of(1);
        UUID session = sessionDao.create(userId);
        UserId result = sessionDao.validate(session);
        assertThat(result).isEqualTo(userId);
    }

    @Test
    void deletedSessionCannotBeValidated() {
        SessionDao sessionDao = new SessionDao(ds);
        UserId userId = UserId.of(1);
        UUID session = sessionDao.create(userId);
        sessionDao.delete(session);

        assertThatThrownBy(() -> sessionDao.validate(session)).isInstanceOf(CredentialsException.class).hasMessageContaining("Unable to validate session");
    }
}
