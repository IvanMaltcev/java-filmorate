package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    public User createUser(User user) {
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
        verificationUserId(user.getId());
        users.put(user.getId(), user);
        log.info("Информация о пользователе с id {} обновлена", user.getId());
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public void verificationUserId(int userId) {
        if (!users.containsKey(userId)) {
            log.info("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Не верный id пользователя.");
        }
    }
}
