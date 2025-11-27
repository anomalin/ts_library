package se.yrgo.libraryapp.controllers.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

public class UserControllerTest {
    @Test
    void successfulAdminGetUser() {
        UserDao userDao = mock(UserDao.class);
        UserController userController = new UserController(userDao);
        User user = new User(UserId.of(2), "anomalin", "Malin");
        when(userDao.get("2")).thenReturn(Optional.of(user));
        User result = userController.getUser("2");
        assertThat(result).isEqualTo(user);
        verify(userDao).get("2");
    }

    @Test
    void unsuccessfulAdminGetUser() {
        UserDao userDao = mock(UserDao.class);
        UserController userController = new UserController(userDao);
        when(userDao.get("2")).thenReturn(Optional.empty());
        assertThat(userController.getUser("2")).isNull();
        verify(userDao).get("2");
    }
}
