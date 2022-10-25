package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {


    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public int incrementAndGetId() {
        userId++;
        return userId;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user, BindingResult error) {
        if (error.hasFieldErrors()) {
            throw new ValidationException(error.getFieldErrors().get(0).getDefaultMessage());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(incrementAndGetId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Не верный id пользователя.");
        }
        users.put(user.getId(), user);
        log.info("Информация о пользователе с id {} обновлена", user.getId());
        return user;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }
}
