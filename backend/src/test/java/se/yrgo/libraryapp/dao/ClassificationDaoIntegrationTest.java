package se.yrgo.libraryapp.dao;

import java.util.List;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.DdsClassification;

@Tag("integration")
@H2
public class ClassificationDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        ClassificationDaoIntegrationTest.ds = ds;
    }

    @Test
    void successfulGetAvailable() {
        ClassificationDao classificationDao = new ClassificationDao(ds);
        List<DdsClassification> result = classificationDao.getAvailable();
        assertThat(result).hasSize(7);
        DdsClassification classification = result.iterator().next();
        assertThat(classification.getCode()).isEqualTo(0);
    }
}
