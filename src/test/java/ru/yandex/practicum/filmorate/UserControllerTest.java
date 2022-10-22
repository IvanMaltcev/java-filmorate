package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;
    User user;
    BindingResult error;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {

        userController = new UserController();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();

        user = new User (
                0,
                "Test@test.ru",
                "Login",
                "Name",
                LocalDate.of(1999,5,23)
        );

        error = new DataBinder(user).getBindingResult();
    }

    @Test
    void addNewUser() {

        final User newUser = userController.createUser(user, error);

        assertNotNull(newUser, "Пользователь не найден.");
        assertEquals(user, newUser, "Пользователи не совпадают.");

        final List<User> users = userController.findAllUsers();

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertEquals(user, users.get(0), "Пользователи не совпадают.");
    }

    @Test
    void updateNewUser() {

        final User user1 = userController.createUser(user, error);
        user1.setName("");
        final User updateUser = userController.updateUser(user1);
        final User newUser = userController.createUser(updateUser, error);

        assertEquals(user.getLogin(), newUser.getName(), "Логины пользователей не совпадают.");

        final List<User> users = userController.findAllUsers();

        assertNotNull(users, "Пользователи не возвращаются.");
        assertEquals(2, users.size(), "Неверное количество пользователей.");
        assertEquals(user, users.get(0), "Пользователи не совпадают.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Test.test.ru"})
    public void shouldReturnExceptionWhenIncorrectEmail(String email) {
        final User newUser = userController.createUser(user, error);
        newUser.setEmail(email);
        Set<ConstraintViolation<User>> validates = validator.validate(newUser);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Адрес электронной почты не может быть пустым" +
                " и должен содержать символ @.", message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void shouldReturnExceptionWhenIncorrectLogin(String login) {
        final User newUser = userController.createUser(user, error);
        newUser.setLogin(login);
        Set<ConstraintViolation<User>> validates = validator.validate(newUser);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Логин не может быть пустым и содержать пробелы.", message);
    }

    @Test
    public void shouldReturnExceptionWhenIncorrectBirthDate() {
        final User newUser = userController.createUser(user, error);
        newUser.setBirthday(LocalDate.of(2022,11,25));
        Set<ConstraintViolation<User>> validates = validator.validate(newUser);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Дата рождения не может быть в будущем.", message);
    }

    @Test
    public void shouldReturnExceptionWhenIdNotFound() {
        final User newUser = userController.createUser(user, error);
        newUser.setId(2);
        final UpdateException exception = assertThrows(
                UpdateException.class,
                () -> {
                    final User updateUser = userController.updateUser(newUser);
                });
        assertEquals("Не верный id пользователя.", exception.getMessage());
    }

}