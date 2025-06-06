package br.com.alura.AluraFake.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import br.com.alura.AluraFake.utils.PasswordGeneration;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    // Por questões didáticas, a senha será armazenada em texto plano.
    private String password;

    public User() {}

    public User(String name, String email, Role role, String password) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password == null ? PasswordGeneration.generatePassword() : password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isInstructor() {
        return Role.INSTRUCTOR.equals(this.role);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
