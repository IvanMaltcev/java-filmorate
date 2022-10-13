package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.ValidationData;

import javax.validation.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public int incrementAndGetId() {
        filmId++;
        return filmId;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, BindingResult error) throws ValidationException {
        if (error.hasFieldErrors()) {
            throw new ValidationException(error.getFieldErrors().get(0).getDefaultMessage());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Некорректная дата релиза - {}.", film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        film.setId(incrementAndGetId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        ValidationData.UpdateValidationFilm(films, film);
        films.put(film.getId(), film);
        log.info("Информация о фильме с id {} обновлена", film.getId());
        return film;
    }
}
