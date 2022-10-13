package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.Validation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        Validation.validationNewFilm(film);
        film.setId(incrementAndGetId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Validation.UpdateValidationFilm(films, film);
        films.put(film.getId(), film);
        log.info("Информация о фильме с id {} обновлена", film.getId());
        return film;
    }
}
