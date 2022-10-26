package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    Map<Integer, User> getUsers();

    void verificationUserId(int userId);
}
