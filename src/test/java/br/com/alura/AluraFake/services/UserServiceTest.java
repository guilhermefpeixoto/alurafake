package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.exceptions.EmailAlreadyRegisteredException;
import br.com.alura.AluraFake.models.Role;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_should_throw_exception_when_email_exists() {
        User user = new User("Caio", "caio@alura.com.br", Role.STUDENT);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        EmailAlreadyRegisteredException exception = assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> userService.createUser(user));

        assertEquals("This email address is already registered.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void createUser_should_save_user_when_email_is_unique() throws Exception {
        User user = new User("Caio", "caio@alura.com.br", Role.STUDENT);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_should_return_empty_list_when_no_users_exist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers__should_return_user_list_when_users_exist() {
        List<User> expectedUsers = List.of(
                new User("User 1", "user1@test.com", Role.STUDENT),
                new User("User 2", "user2@test.com", Role.STUDENT));

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userRepository, times(1)).findAll();
    }
}