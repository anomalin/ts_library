package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.jooby.MockContext;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

public class LoginControllerTest {

    @Test
    void successfulLogin() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        MockContext ctx = new MockContext();

        LoginData loginData = new LoginData("testuser", "password");
        UserId userId = UserId.of(1);
        UUID sessionId = UUID.randomUUID();

        when(userService.validate("testuser", "password")).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);

        List<Role> roles = List.of(Role.ADMIN, Role.USER);
        when(roleDao.get(userId)).thenReturn(roles);
        List<Role> result = controller.login(ctx, null, loginData);

        verify(sessionDao).create(userId);
        assertThat(result).containsExactlyElementsOf(roles);
        assertThat(ctx.getResponseCode().value()).isEqualTo(200);
    }

    @Test
    void failedLogin() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        MockContext ctx = new MockContext();

        LoginData loginData = new LoginData("fail", "wrong");

        when(userService.validate("fail", "wrong")).thenReturn(Optional.empty());
        List<Role> result = controller.login(ctx, null, loginData);
        assertThat(result).isEmpty();
        assertThat(ctx.getResponseCode().value()).isEqualTo(401);
    }

    @Test
    void successfulIsLoggedIn() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        UserId userId = UserId.of(1);
        UUID sessionId = UUID.randomUUID();
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        when(sessionDao.validate(sessionId)).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(roles);
        List<Role> result = loginController.isLoggedIn(sessionId.toString());
        assertThat(result).containsExactlyElementsOf(roles);
    }

    @Test
    void failedIsLoggedIn() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        UUID sessionId = UUID.randomUUID();

        when(sessionDao.validate(sessionId)).thenThrow(new IllegalArgumentException());

        List<Role> result = loginController.isLoggedIn(sessionId.toString());
        assertThat(result).isEmpty();
    }
}
