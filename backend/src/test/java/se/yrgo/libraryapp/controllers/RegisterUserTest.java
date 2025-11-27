package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import se.yrgo.libraryapp.entities.forms.RegisterUserData;
import se.yrgo.libraryapp.services.UserService;

public class RegisterUserTest {
    @Test
    void successfulRegister() {
        UserService userService = mock(UserService.class);
        RegisterUserController rUserController = new RegisterUserController(userService);
        RegisterUserData rUserData = new RegisterUserData("anomalin", "Malin", "spole73874");
        when(userService.register(rUserData.getName(), rUserData.getRealName(), rUserData.getPassword())).thenReturn(true);
        boolean result = rUserController.register(rUserData);
        assertThat(result).isTrue();
        verify(userService).register(rUserData.getName(), rUserData.getRealName(), rUserData.getPassword());
    }

    @Test
    void unsuccessfulRegister() {
        UserService userService = mock(UserService.class);
        RegisterUserController rUserController = new RegisterUserController(userService);
        RegisterUserData rUserData = new RegisterUserData("a", "Malin", "spole73874");
        boolean result = rUserController.register(rUserData);
        assertThat(result).isFalse();
    }
}
