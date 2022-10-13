package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;
    User user;

    @BeforeEach
    public void beforeEach() {

        userController = new UserController();

        user = new User (
                0,
                "Test@test.ru",
                "Login",
                "Name",
                LocalDate.of(1999,5,23)
        );
    }

    @Test
    void addNewUser() {

        final User newUser = userController.createUser(user);

        assertNotNull(newUser, "Пользователь не найден.");
        assertEquals(user, newUser, "Пользователи не совпадают.");

        final List<User> users = userController.findAllUsers();

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertEquals(user, users.get(0), "Пользователи не совпадают.");
    }

    @Test
    void updateNewUser() {

        final User user1 = userController.createUser(user);
        user1.setName("");
        final User updateUser = userController.updateUser(user1);
        final User newUser = userController.createUser(updateUser);

        assertEquals(user.getLogin(), newUser.getName(), "Логины пользователей не совпадают.");

        final List<User> users = userController.findAllUsers();

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(2, users.size(), "Неверное количество пользователей.");
        assertEquals(user, users.get(0), "Пользователи не совпадают.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Test.test.ru"})
    public void shouldReturnExceptionWhenIncorrectEmail(String email) {
        final User newUser = userController.createUser(user);
        newUser.setEmail(email);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    final User errorUser = userController.createUser(newUser);
                });
        assertEquals("Адрес электронной почты не может быть пустым" +
                " и должен содержать символ @.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "New login"})
    public void shouldReturnExceptionWhenIncorrectLogin(String login) {
        final User newUser = userController.createUser(user);
        newUser.setLogin(login);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    final User errorUser = userController.createUser(newUser);
                });
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenIncorrectBirthDate() {
        final User newUser = userController.createUser(user);
        newUser.setBirthday(LocalDate.of(2022,11,25));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    final User errorUser = userController.createUser(newUser);
                });
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenIdNotFound() {
        final User newUser = userController.createUser(user);
        newUser.setId(2);
        final UpdateException exception = assertThrows(
                UpdateException.class,
                () -> {
                    final User updateUser = userController.updateUser(newUser);
                });
        assertEquals("Не верный id пользователя.", exception.getMessage());
    }

}