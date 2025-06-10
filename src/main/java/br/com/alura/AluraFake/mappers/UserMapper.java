package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.dtos.users.NewUserDTO;
import br.com.alura.AluraFake.dtos.users.UserResponseDTO;
import br.com.alura.AluraFake.models.users.User;

public class UserMapper {

    public static User toEntity(NewUserDTO userDTO) {
        User user = new User(userDTO.name(), userDTO.email(), userDTO.role(), userDTO.password());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO userDTO = new UserResponseDTO(user.getName(), user.getEmail(), user.getRole());

        return userDTO;
    }
}