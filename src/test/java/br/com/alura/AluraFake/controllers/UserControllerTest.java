package br.com.alura.AluraFake.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.alura.AluraFake.dtos.users.NewUserDTO;
import br.com.alura.AluraFake.exceptions.EmailAlreadyRegisteredException;
import br.com.alura.AluraFake.models.users.Role;
import br.com.alura.AluraFake.models.users.User;
import br.com.alura.AluraFake.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(br.com.alura.AluraFake.infra.security.SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private br.com.alura.AluraFake.infra.security.CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    void newUser__should_return_bad_request_when_email_is_blank() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO("Caio Bugorin", "", Role.STUDENT, "123456");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").isNotEmpty());
    }

    @Test
    @WithAnonymousUser
    void newUser__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO("Caio Bugorin", "caio", Role.STUDENT, "123456");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").isNotEmpty());
    }

    @Test
    @WithAnonymousUser
    void newUser__should_return_conflict_when_email_already_exists() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO("Caio Bugorin", "caio.bugorin@alura.com.br", Role.STUDENT, "123456");

        doThrow(new EmailAlreadyRegisteredException("This email address is already registered.")).when(userService)
                .createUser(any(User.class));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("This email address is already registered."));
    }

    @Test
    @WithAnonymousUser
    void newUser__should_return_created_when_user_request_is_valid() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO("Caio Bugorin", "caio.bugorin@alura.com.br", Role.STUDENT, "123456");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listAllUsers__should_list_all_users_when_authenticated() throws Exception {
        User user1 = new User("User 1", "user1@alura.com", Role.STUDENT, "encodedPassword1");
        User user2 = new User("User 2", "user2@alura.com", Role.STUDENT, "encodedPassword2");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    @WithAnonymousUser
    void listAllUsers__should_return_unauthorized_when_not_authenticated() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}