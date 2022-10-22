package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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
}
