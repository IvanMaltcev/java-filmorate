package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class UserController {

    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public int incrementAndGetId() {
        userId++;
        return userId;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Некорректный адрес электронной почты - {}", user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым" +
                    " и должен содержать символ @.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Некорректный логин - {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Некорректная дата рождения - {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setId(incrementAndGetId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с id {} не найден", user.getId());
            throw new ValidationException("Не верный id пользователя.");
        }
        users.put(user.getId(), user);
        log.info("Информация о пользователе с id {} обновлена", user.getId());
        return user;
    }
}
