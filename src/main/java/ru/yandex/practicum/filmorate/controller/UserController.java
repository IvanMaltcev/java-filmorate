package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.Validation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public int incrementAndGetId() {
        userId++;
        return userId;
    }

    @GetMapping
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        Validation.validationNewUser(user);
        user.setId(incrementAndGetId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        Validation.UpdateValidationUser(users, user);
        users.put(user.getId(), user);
        log.info("Информация о пользователе с id {} обновлена", user.getId());
        return user;
    }
}
