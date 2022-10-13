package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;


@Slf4j
public class Validation {

    public static void validationNewFilm (Film film) {

        if (film.getName().isBlank()) {
            log.info("Некорректное название фильма - {}.", film.getName());
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.info("Превышено максимальное число символов - 200(текущее {}).", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Некорректная дата релиза - {}.", film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.info("Введено отрицательное число - {}.", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    public static void validationNewUser (User user) {
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
    }

    public static void UpdateValidationFilm (Map<Integer, Film> films, Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с id {} не найден", film.getId());
            throw new UpdateException("Не верный id фильма.");
        }
    }

    public static void UpdateValidationUser (Map<Integer, User> films, User user) {
        if (!films.containsKey(user.getId())) {
            log.info("Пользователь с id {} не найден", user.getId());
            throw new UpdateException("Не верный id пользователя.");
        }
    }
}
