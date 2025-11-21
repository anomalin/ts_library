package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoSettings;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    void failedLoginWithWrongPassword() {
        final String username = "testuser";
        final String password = "wrong";
        final String passwordHash = "password";
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final LoginInfo info = new LoginInfo(id, passwordHash);
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username, password)).isEmpty();
    }

    @Test
    void failedLoginWithUnknownUser() {
        final String username = "unknownuser";
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        when(userDao.getLoginInfo(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.validate(username, "password")).isEmpty();
    }

    @Test
    @SuppressWarnings("deprecated")
    void correctRegister() {
        String realname = "Bosse Stenberg";
        String password = "password";
        String username = "bman";
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        String expectedRealname = realname.replace("'", "\\'");
        String expectedPassword = encoder.encode(password);

        when(userDao.insertUser(username, expectedRealname, expectedPassword))
                .thenReturn(true);

        UserService userService = new UserService(userDao, encoder);

        boolean result = userService.register(username, realname, password);

        assertThat(result).isTrue();
    }

    @Test
    @SuppressWarnings("deprecated")
    void failedRegisterWhenDaoReturnsFalse() {
        String realname = "Bosse Stenberg";
        String password = "password";
        String username = "bman";
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        String expectedRealname = realname.replace("'", "\\'");
        String expectedPassword = encoder.encode(password);

        when(userDao.insertUser(username, expectedRealname, expectedPassword)).thenReturn(false);
        UserService userService = new UserService(userDao, encoder);
        boolean result = userService.register(username, expectedRealname, expectedPassword);
        assertThat(result).isFalse();
    }

    @Test
    void correctIsNameAvailable() {
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        String name = "mali nn";
        String expectedName = name.trim();
        when(userDao.isNameAvailable(expectedName))
                .thenReturn(true);
        boolean result = userService.isNameAvailable(name);
        assertThat(result).isTrue();
    }

    @Test
    void nameIsNotAvailable() {
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        String name = "taken";
        String trimmed = name.trim();
        when(userDao.isNameAvailable(trimmed)).thenReturn(false);
        boolean result = userService.isNameAvailable(name);
        assertThat(result).isFalse();
    }

    @Test
    void nameIsNullOrEmpty() {
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.isNameAvailable(null)).isFalse();
        assertThat(userService.isNameAvailable("   ")).isFalse();
    }
}
