package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    @NotBlank (message = "Адрес электронной почты не может быть пустым и должен содержать символ @.")
    @Email (message = "Адрес электронной почты не может быть пустым и должен содержать символ @.")
    private String email;
    @NotBlank (message = "Логин не может быть пустым и содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent (message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
