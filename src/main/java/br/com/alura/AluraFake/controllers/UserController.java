package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.dtos.users.NewUserDTO;
import br.com.alura.AluraFake.dtos.users.UserResponseDTO;
import br.com.alura.AluraFake.mappers.UserMapper;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.services.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid NewUserDTO newUser) {
        User user = UserMapper.toEntity(newUser);
        this.userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = this.userService.getAllUsers().stream().map(UserMapper::toDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

}
