package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class FilmController {

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public int incrementAndGetId() {
        filmId++;
        return filmId;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
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
        film.setId(incrementAndGetId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с id {} не найден", film.getId());
            throw new ValidationException("Не верный id фильма.");
        }
        films.put(film.getId(), film);
        log.info("Информация о фильме с id {} обновлена", film.getId());
        return film;
    }
}
