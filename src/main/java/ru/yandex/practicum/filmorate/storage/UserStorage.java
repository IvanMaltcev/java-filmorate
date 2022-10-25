package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user, BindingResult error);

    User updateUser(User user);

    Map<Integer, User> getUsers();
}
