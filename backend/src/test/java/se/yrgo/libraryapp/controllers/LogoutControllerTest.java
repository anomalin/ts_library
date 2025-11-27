package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;

import io.jooby.MockContext;
import se.yrgo.libraryapp.dao.SessionDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.UUID;

public class LogoutControllerTest {
    @Test
    void logoutNoSessionCookie() {
        SessionDao sessionDao = mock(SessionDao.class);
        LogoutController logoutController = new LogoutController(sessionDao);
        MockContext ctx = new MockContext();
        logoutController.logout(ctx, null);
        assertThat(ctx.cookieMap()).isEmpty();
        verifyNoInteractions(sessionDao);
    }

    @Test
    void logoutWithSessionCookie() {
        SessionDao sessionDao = mock(SessionDao.class);
        LogoutController logoutController = new LogoutController(sessionDao);
        MockContext ctx = new MockContext();
        String sessionId = UUID.randomUUID().toString();
        logoutController.logout(ctx, sessionId);
        verify(sessionDao).delete(UUID.fromString(sessionId));
        
    }

}