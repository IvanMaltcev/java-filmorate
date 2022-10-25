package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.*;
import java.util.*;

@RestController
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film, BindingResult error) {
        return filmService.addFilm(film, error);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов не может быть меньше или равно нулю");
        }
        return filmService.findPopularFilms(count);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }
}
